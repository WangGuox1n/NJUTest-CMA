package com.example.cma.ui.self_inspection;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.cma.R;
import com.example.cma.adapter.self_inspection.SelfInspectionFileAdapter;
import com.example.cma.model.self_inspection.SelfInspection;
import com.example.cma.model.self_inspection.SelfInspection_File;
import com.example.cma.ui.external_review.ExternalReviewDocument_Add;
import com.example.cma.utils.AddressUtil;
import com.example.cma.utils.DownloadHelper;
import com.example.cma.utils.FileUtil;
import com.example.cma.utils.HttpUtil;
import com.example.cma.utils.ToastUtil;
import com.example.cma.utils.ViewUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SelfInspection_FileList extends AppCompatActivity implements SearchView.OnQueryTextListener, View.OnClickListener {

    private static final String TAG = "SelfInspection_FileList";
    //data
    private SelfInspection selfInspection;
    private List<SelfInspection_File> list = new ArrayList<>();

    //View
    private RecyclerView recyclerView;
    private SelfInspectionFileAdapter adapter;
    private ProgressDialog progressDialog;

    private File file; //需要添加和修改文件时从文件管理器获取的file
    EditText name_text;
    TextView file_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.self_inspection_file_list);
        initView();
        selfInspection = (SelfInspection) getIntent().getSerializableExtra("SelfInspection");
        adapter = new SelfInspectionFileAdapter(list);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDataFromServer();
    }

    //初始化所有控件
    public void initView() {
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        Toolbar toolbar = findViewById(R.id.toolbar);
        ViewUtil.getInstance().setSupportActionBar(this, toolbar);

        SearchView searchView = findViewById(R.id.searchview);
        searchView.setFocusable(false);
        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(false);

        findViewById(R.id.add_button).setOnClickListener(this);
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.filter(newText);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        adapter.filter(query);
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_button:
                showAddDialog();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FileUtil.GET_FILE:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    file = FileUtil.getInstance().getFile(this, uri);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            file_text.setText(file.getName());
                        }
                    });
                }
                break;
            default:
                break;
        }
    }

    public void showAddDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(SelfInspection_FileList.this);
        final View dialogView = LayoutInflater.from(SelfInspection_FileList.this)
                .inflate(R.layout.self_inspection_file_add, null);
        builder.setTitle("新增自查文档");
        builder.setView(dialogView);
        builder.setCancelable(false);
        final AlertDialog editDialog = builder.create();
        editDialog.show();

        //取得 dialog中的值
        Button selectButton = dialogView.findViewById(R.id.select_button);
        Button submitButton = dialogView.findViewById(R.id.submit_button);
        Button cancelButton = dialogView.findViewById(R.id.cancel_button);
        name_text = dialogView.findViewById(R.id.fileName_text);
        file_text = dialogView.findViewById(R.id.file_text);


        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileUtil.getInstance().localStorage(SelfInspection_FileList.this);
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name_text.getText().toString().isEmpty()) {
                    ToastUtil.showShort(SelfInspection_FileList.this, "请填写文件名");
                } else if (file_text.getText().toString().isEmpty()) {
                    ToastUtil.showShort(SelfInspection_FileList.this, "请选择文件");
                } else {
                    postSave(name_text.getText().toString());
                    editDialog.dismiss();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!name_text.getText().toString().isEmpty() || !file_text.getText().toString().isEmpty()) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(SelfInspection_FileList.this);
                    dialog.setTitle("内容尚未保存");
                    dialog.setMessage("是否退出？");
                    dialog.setCancelable(true);
                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            editDialog.dismiss();
                        }
                    });
                    dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    dialog.show();
                } else {
                    editDialog.dismiss();
                }
            }
        });
    }

    public void showModifyDialog(final int index) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(SelfInspection_FileList.this);
        final View dialogView = LayoutInflater.from(SelfInspection_FileList.this)
                .inflate(R.layout.self_inspection_file_add, null);
        builder.setTitle("编辑自查文档");
        builder.setView(dialogView);
        builder.setCancelable(false);
        final AlertDialog editDialog = builder.create();
        editDialog.show();

        //取得 dialog中的值
        Button selectButton = dialogView.findViewById(R.id.select_button);
        Button submitButton = dialogView.findViewById(R.id.submit_button);
        Button cancelButton = dialogView.findViewById(R.id.cancel_button);
        name_text = dialogView.findViewById(R.id.fileName_text);
        file_text = dialogView.findViewById(R.id.file_text);
        name_text.setText(list.get(index).getFileName());
        file_text.setText(list.get(index).getFile());

        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileUtil.getInstance().localStorage(SelfInspection_FileList.this);
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name_text.getText().toString().isEmpty()) {
                    ToastUtil.showShort(SelfInspection_FileList.this, "请填写文档名称");
                } else if (file_text.getText().toString().isEmpty()) {
                    ToastUtil.showShort(SelfInspection_FileList.this, "请选择文件");
                } else {
                    postModify(index, name_text.getText().toString());
                    editDialog.dismiss();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDialog.dismiss();
            }
        });
    }

    public void postSave(String fileName) {
        String address = AddressUtil.getAddress(AddressUtil.SelfInspection_addOneFile);
        RequestBody fileBody = RequestBody.create(MediaType.parse("*/*"), file);

        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        requestBody.addFormDataPart("id", selfInspection.getId() + "")
                .addFormDataPart("fileName", fileName)
                .addFormDataPart("file", file.getName(), fileBody);

        progressDialog = new ProgressDialog(SelfInspection_FileList.this);
        progressDialog.setTitle("正在上传中");
        progressDialog.setMessage("请稍等......");
        progressDialog.setCancelable(true);
        progressDialog.show();
        HttpUtil.sendOkHttpWithMultipartBody(address, requestBody.build(), new okhttp3.Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                Log.d(TAG, responseData);
                int code = 0;
                String msg = "";
                try {
                    JSONObject object = new JSONObject(responseData);
                    code = object.getInt("code");
                    msg = object.getString("msg");
                } catch (JSONException e) {
                    ToastUtil.showShort(SelfInspection_FileList.this, "文档名称重复，添加失败");
                    progressDialog.dismiss();
                    e.printStackTrace();
                }
                if (code == 200 && msg.equals("成功")) {
                    ToastUtil.showShort(SelfInspection_FileList.this, "添加成功");
                    progressDialog.dismiss();
                    getDataFromServer();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showShort(SelfInspection_FileList.this, "添加失败");
                progressDialog.dismiss();
            }
        });
    }

    public void postModify(int index, String fileName) {
        String address = AddressUtil.getAddress(AddressUtil.SelfInspection_modifyOneFile);
        RequestBody fileBody = RequestBody.create(MediaType.parse("*/*"), file);

        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        requestBody.addFormDataPart("fileId", list.get(index).getId() + "")
                .addFormDataPart("fileName", fileName)
                .addFormDataPart("file", file.getName(), fileBody);

        HttpUtil.sendOkHttpWithMultipartBody(address, requestBody.build(), new okhttp3.Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                Log.d(TAG, responseData);
                int code = 0;
                String msg = "";
                try {
                    JSONObject object = new JSONObject(responseData);
                    code = object.getInt("code");
                    msg = object.getString("msg");
                } catch (JSONException e) {
                    ToastUtil.showShort(SelfInspection_FileList.this, "修改失败");
                    e.printStackTrace();
                }
                if (code == 200 && msg.equals("成功")) {
                    getDataFromServer();
                    ToastUtil.showShort(SelfInspection_FileList.this, "修改成功");
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showShort(SelfInspection_FileList.this, "修改失败");
            }
        });
    }

    public void downloadFile(final int index) {
        Log.d(TAG, list.get(index).getFileName() + " " + list.get(index).getFile());
        String[] spilt = list.get(index).getFile().split(".");
        String fileSaveName = list.get(index).getFileName();
        if (spilt.length > 1) {
            fileSaveName = fileSaveName + "." + spilt[1];
        }
        String address = AddressUtil.getAddress(AddressUtil.SelfInspection_downloadFile);
        address = address + "?fileId=" + list.get(index).getId();

        final ProgressDialog loadingDialog = new ProgressDialog(SelfInspection_FileList.this);
        loadingDialog.setTitle("正在下载中");
        loadingDialog.setMessage("请稍等......");
        loadingDialog.setCancelable(true);
        loadingDialog.show();
        DownloadHelper.Builder builder = new DownloadHelper.Builder(SelfInspection_FileList.this)
                .title(list.get(index).getFile())
                .description("自查文档下载")
                .downloadUrl(address)
                .fileSaveName(list.get(index).getFile())
                .fileSavePath("CMA/自查管理/" + selfInspection.getName())
                .notifyVisible(true)
                .fileType(DownloadHelper.FileType.NORMAL)
                .onProgressListener(new DownloadHelper.OnDownloadProgressListener() {
                    @Override
                    public void onProgress(int downloadedSize, int totalSize) {
                        if(downloadedSize==totalSize)
                            loadingDialog.dismiss();
                        /*final int progress = (int) ((downloadedSize * 1.0f / totalSize) * 100);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setProgress(progress);
                            }
                        });*/
                    }

                    @Override
                    public void onSuccess(Uri fileUri) {
                        loadingDialog.dismiss();
                        String msg = list.get(index).getFile() + "\n下载成功";
                        ToastUtil.showShort(SelfInspection_FileList.this, msg);
                    }

                    @Override
                    public void onFail() {
                        loadingDialog.dismiss();
                        ToastUtil.showShort(SelfInspection_FileList.this, "下载失败");
                    }

                    @Override
                    public void fileAlreadyExits(File file) {
                        loadingDialog.dismiss();
                        String msg = list.get(index).getFile() + "\n已在 CMA/SelfInspection/" + selfInspection.getName() + " 文件夹中";
                        ToastUtil.showLong(SelfInspection_FileList.this, msg);
                    }
                });
        DownloadHelper downloadHelper = builder.build();
        downloadHelper.start();
    }

    public void getDataFromServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String address = AddressUtil.getAddress(AddressUtil.SelfInspection_getAllFile) + "?id=" + selfInspection.getId();

                HttpUtil.sendOkHttpRequest(address, new okhttp3.Callback() {
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseData = response.body().string();
                        Log.d(TAG, responseData);
                        parseJSONWithGSON(responseData);
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        ToastUtil.showShort(SelfInspection_FileList.this, "请求数据失败");
                    }
                });
            }
        }).start();
    }

    private void parseJSONWithGSON(String jsonData) {
        JSONArray array = new JSONArray();
        try {
            JSONObject object = new JSONObject(jsonData);
            array = object.getJSONArray("data");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (array.length() == 0) {
            ToastUtil.showShort(SelfInspection_FileList.this, "文档集为空");
        }
        Gson gson = new Gson();
        List<SelfInspection_File> newList = gson.fromJson(array.toString(), new TypeToken<List<SelfInspection_File>>() {
        }.getType());
        list.clear();
        list.addAll(newList);
        Collections.reverse(list);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.myNotifyDataSetChanged();
            }
        });
    }

}

package com.example.cma.ui.staff_management;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cma.R;
import com.example.cma.model.staff_management.StaffManagement;
import com.example.cma.ui.self_inspection.SelfInspection_FileList;
import com.example.cma.utils.HttpUtil;
import com.example.cma.utils.PhotoUtil;
import com.example.cma.utils.ToastUtil;
import com.example.cma.utils.ViewUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;


public class StaffFile_Add extends AppCompatActivity implements View.OnClickListener, Spinner.OnItemSelectedListener {
    private static final String TAG = "StaffFile_Add";

    private List<StaffManagement> wholeList = new ArrayList<>();
    private StaffManagement staffManagement;
    private Spinner spinner;
    private List<StaffManagement> noFile_staffList = new ArrayList<>();
    private List<String> data_list = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    private TextView name_text;
    private TextView department_text;
    private TextView position_text;
    private EditText id_text;
    private EditText location_text;
    private ProgressDialog loadingDialog;

    private ImageView fileImage;
    private File outputImage;
    private boolean isPhotoSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_file_add);
        initView();
        getAll();
        getDataFromServer();
    }

    public void initView() {
        spinner = findViewById(R.id.spinner);
        name_text = findViewById(R.id.name_text);
        department_text = findViewById(R.id.department_text);
        position_text = findViewById(R.id.position_text);
        id_text = findViewById(R.id.id_text);
        location_text = findViewById(R.id.location_text);
        fileImage = findViewById(R.id.file_image);
        fileImage.setOnClickListener(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        ViewUtil.getInstance().setSupportActionBar(this, toolbar);
        ViewUtil.ShowCursor(id_text);
        ViewUtil.ShowCursor(location_text);
        //设置监听
        findViewById(R.id.submit_button).setOnClickListener(this);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_button:
                onSave();
                break;
            case R.id.file_image:
                PhotoUtil.getInstance().showPopupWindow(this);
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        int i = 1;
        for (StaffManagement staff : noFile_staffList) {
            String selectedItem = arg0.getSelectedItem().toString();
            if (selectedItem.equals(i + ". " + staff.getName())) {
                for (final StaffManagement info : wholeList) {
                    if (info.getId() == staff.getId()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                name_text.setText(info.getName());
                                department_text.setText(info.getDepartment());
                                position_text.setText(info.getPosition());
                            }
                        });
                    }
                }
                staffManagement = staff;
                break;
            }
            i++;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void initDataList() {
        data_list.clear();
        int i = 1;
        for (StaffManagement staff : noFile_staffList) {
            data_list.add(i + ". " + staff.getName());
            i++;
        }
        Log.d(TAG, "" + i + noFile_staffList.size());
    }

    //向后端发送请求，返回所有没有档案的人员
    public void getDataFromServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpUtil.sendOkHttpRequest("http://119.23.38.100:8080/cma/StaffManagement/getNoFile", new okhttp3.Callback() {
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseData = response.body().string();
                        Log.d(TAG, responseData);
                        parseJSONWithGSON(responseData);
                        initDataList();
                        showResponse();
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(StaffFile_Add.this, "请求数据失败！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        }).start();
    }

    private void parseJSONWithGSON(String jsonData) {
        JSONArray array = new JSONArray();
        try {
            JSONObject object = new JSONObject(jsonData);//最外层的JSONObject对象
            array = object.getJSONArray("data");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (array.length() == 0) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(StaffFile_Add.this, "无可添加档案的人员", Toast.LENGTH_LONG).show();
                }
            });
        }
        Gson gson = new Gson();
        List<StaffManagement> newList = gson.fromJson(array.toString(), new TypeToken<List<StaffManagement>>() {
        }.getType());
        noFile_staffList.clear();
        noFile_staffList.addAll(newList);
    }

    private void showResponse() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //适配器
                adapter = new ArrayAdapter<>(StaffFile_Add.this, android.R.layout.simple_spinner_item, data_list);
                //设置样式
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //加载适配器
                spinner.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });
    }

    //监听返回按钮的点击事件，比如可以返回上级Activity
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackConfirm();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!id_text.getText().toString().isEmpty() ||
                !location_text.getText().toString().isEmpty() || isPhotoSelect) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(StaffFile_Add.this);
            dialog.setTitle("内容尚未保存");
            dialog.setMessage("是否退出？");
            dialog.setCancelable(true);
            dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            dialog.show();
        } else
            super.onBackPressed();
    }

    public void getAll() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpUtil.sendOkHttpRequest("http://119.23.38.100:8080/cma/StaffManagement/getAll", new okhttp3.Callback() {
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseData = response.body().string();
                        Log.d(TAG, responseData);
                        parseAll(responseData);
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(StaffFile_Add.this, "请求数据失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        }).start();
    }

    private void parseAll(String jsonData) {
        JSONArray array = new JSONArray();
        try {
            JSONObject object = new JSONObject(jsonData);//最外层的JSONObject对象
            array = object.getJSONArray("data");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (array.length() == 0) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(StaffFile_Add.this, "无可添加档案的人员", Toast.LENGTH_LONG).show();
                }
            });
        }
        wholeList = new Gson().fromJson(array.toString(), new TypeToken<List<StaffManagement>>() {
        }.getType());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PhotoUtil.TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    outputImage = PhotoUtil.getInstance().getOutputImage();
                    Bitmap bitmap = BitmapFactory.decodeFile(outputImage.getPath());
                    fileImage.setImageBitmap(bitmap);
                    isPhotoSelect = true;
                }
                break;
            case PhotoUtil.CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    if (PhotoUtil.getInstance().selectPicture(data)) {
                        Bitmap bitmap = PhotoUtil.getInstance().getBitmap();
                        fileImage.setImageBitmap(bitmap);
                        outputImage = PhotoUtil.getInstance().getOutputImage();
                        isPhotoSelect = true;
                    }
                }
                break;
            default:
                break;
        }
    }

    public void onSave() {
        if (noFile_staffList.size() == 0) {
            ToastUtil.showShort(StaffFile_Add.this, "没有可添加档案的人员");
            return;
        }
        if (id_text.getText().toString().isEmpty() ||
                location_text.getText().toString().isEmpty()) {
            Toast.makeText(StaffFile_Add.this, "请填写完整", Toast.LENGTH_LONG).show();
            return;
        }
        if (outputImage == null) {
            Toast.makeText(StaffFile_Add.this, "请选择档案图片", Toast.LENGTH_LONG).show();
            return;
        }

        AlertDialog.Builder dialog = new AlertDialog.Builder(StaffFile_Add.this);
        dialog.setMessage("确定提交？");
        dialog.setCancelable(false);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                /*final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(StaffFile_Add.this);
                final View dialogView = LayoutInflater.from(StaffFile_Add.this)
                        .inflate(R.layout.post_file_loading, null);
                builder.setTitle("记录上传中");
                builder.setView(dialogView);
                builder.setCancelable(false);
                loadingDialog = builder.create();
                loadingDialog.show();*/
                loadingDialog = new ProgressDialog(StaffFile_Add.this);
                loadingDialog.setTitle("正在上传中");
                loadingDialog.setMessage("请稍等......");
                loadingDialog.setCancelable(true);
                loadingDialog.show();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        postDataToServer();
                    }
                }).start();
            }
        });
        dialog.show();
    }

    public void postDataToServer() {
        String address = "\thttp://119.23.38.100:8080/cma/StaffFile/addOne";
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        requestBody.addFormDataPart("id", "" + staffManagement.getId());
        requestBody.addFormDataPart("fileId", id_text.getText().toString());
        requestBody.addFormDataPart("fileLocation", location_text.getText().toString());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        final String now = simpleDateFormat.format(new Date());
        RequestBody body = RequestBody.create(MediaType.parse("image/*"), outputImage);
        requestBody.addFormDataPart("fileImage", staffManagement.getName()+"_档案扫描件_"+now+".jpg", body);

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
                    loadingDialog.dismiss();
                    e.printStackTrace();
                }
                if (code == 200 && msg.equals("成功")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(StaffFile_Add.this, "添加成功", Toast.LENGTH_SHORT).show();
                            loadingDialog.dismiss();
                        }
                    });
                    finish();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(StaffFile_Add.this, "添加失败", Toast.LENGTH_SHORT).show();
                        loadingDialog.dismiss();
                    }
                });
            }
        });
    }

    public void onBackConfirm() {
        if (!id_text.getText().toString().isEmpty() ||
                !location_text.getText().toString().isEmpty() || isPhotoSelect) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(StaffFile_Add.this);
            dialog.setTitle("内容尚未保存");
            dialog.setMessage("是否退出？");
            dialog.setCancelable(true);
            dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            dialog.show();
        } else
            finish();
    }
}

package com.example.cma.ui.staff_management;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.StringSignature;
import com.example.cma.R;
import com.example.cma.model.staff_management.StaffFile;
import com.example.cma.utils.HttpUtil;
import com.example.cma.utils.ViewUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class StaffFile_Info extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "StaffFile_Info";
    private StaffFile staffFile;
    private TextView name_text;
    private TextView id_text;
    private TextView location_text;
    private ImageView file_image;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_file_info);

        initView();
        Intent intent = getIntent();
        staffFile = (StaffFile) intent.getSerializableExtra("StaffFile");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (staffFile == null) {
            //ToastUtil.showShort(StaffFile_Info.this, "数据传送失败");
            return;
        }
        getStaffFile();  //重新获取是为了从编辑页面返回后刷新
        getImage();
    }

    public void initView() {
        name_text = findViewById(R.id.name_text);
        id_text = findViewById(R.id.id_text);
        location_text = findViewById(R.id.location_text);
        file_image = findViewById(R.id.file_image);
        progressBar = findViewById(R.id.progressBar);

        Toolbar toolbar = findViewById(R.id.toolbar);
        ViewUtil.getInstance().setSupportActionBar(this, toolbar);

        (findViewById(R.id.edit_button)).setOnClickListener(this);
        (findViewById(R.id.delete_button)).setOnClickListener(this);
    }

    public void setText() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                name_text.setText(staffFile.getName());
                id_text.setText(staffFile.getFileId());
                location_text.setText(staffFile.getFileLocation());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_button:
                Intent intent = new Intent(StaffFile_Info.this, StaffFile_Modify.class);
                intent.putExtra("StaffFile", staffFile);
                startActivityForResult(intent, 0);
                break;
            case R.id.delete_button:  //点击删除，弹出弹窗
                onDeleteConfirm();
                break;
            default:
                break;
        }
    }

    /*
    * 确认删除的对话框
    * */
    public void onDeleteConfirm() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(StaffFile_Info.this);
        dialog.setMessage("确定删除？");
        dialog.setCancelable(true);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onDelete();
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Do nothing
            }
        });
        dialog.show();
    }

    public void onDelete() {
        String address = "http://119.23.38.100:8080/cma/StaffFile/deleteOne";
        RequestBody requestBody = new FormBody.Builder().add("id", "" + staffFile.getId()).build();
        HttpUtil.sendOkHttpWithRequestBody(address, requestBody, new okhttp3.Callback() {
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
                    e.printStackTrace();
                }
                if (code == 200 && msg.equals("成功")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(StaffFile_Info.this, "删除成功！", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(StaffFile_Info.this, "删除失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    /*
    * 监听返回按钮的点击事件，比如可以返回上级Activity
    * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
    * 由于需要取得新的文件名（其中包含了文件的修改时间）
    * 作为Glide缓存的signature
    * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0: {
                if (resultCode == RESULT_OK) {
                    String newFileName = data.getStringExtra("newFileName");
                    staffFile.setFileImage(newFileName);
                }
                break;
            }
            default:
                break;
        }
    }

    public void getStaffFile() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String address = "http://119.23.38.100:8080/cma/StaffFile/getOne?id=" + staffFile.getId();
                HttpUtil.sendOkHttpRequest(address, new okhttp3.Callback() {
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseData = response.body().string();
                        parseJSONWithGSON(responseData);
                        setText();
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(StaffFile_Info.this, "获取数据失败！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        }).start();
    }

    public void parseJSONWithGSON(String jsonData) {
        String staffData = "";
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            staffData = jsonObject.getString("data");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (staffData.equals("null")) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(StaffFile_Info.this, "获取数据失败", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            List<StaffFile> list = new Gson().fromJson(staffData, new TypeToken<List<StaffFile>>() {
            }.getType());
            if (list.size() == 1)
                staffFile = list.get(0);
        }
    }

    public void getImage() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
                file_image.setVisibility(View.GONE);
            }
        });
        Log.d(TAG,staffFile.getFileImage());
        Glide.with(this)
                .load("http://119.23.38.100:8080/cma/StaffFile/getImage?id=" + staffFile.getId())
                .error(R.drawable.invalid_image)
                .dontAnimate()
                .signature(new StringSignature(staffFile.getFileImage()))
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        /*runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showLong(StaffFile_Info.this,"档案扫描件加载失败！");
                            }
                        });*/
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                file_image.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                            }
                        });

                        return false;
                    }
                })
                .into(file_image);
    }
}


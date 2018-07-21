package com.example.cma.ui.manage_review;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cma.R;
import com.example.cma.utils.FileUtil;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ManageReviewOne_Modify extends AppCompatActivity  implements View.OnClickListener {

    String id;
    String filename;
    String year;
    Toolbar toolbar;
    TextView attachment_text;
    EditText editText1;
    File file;
    Boolean flag=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_review_one__modify);
        Intent intent=getIntent();
        id=(String) intent.getStringExtra("fileId");
        String filename=(String)intent.getStringExtra("fileName");
        year=(String)intent.getStringExtra("year");
        Log.d("编号",id);
        Log.d("年份",year);
        toolbar = (Toolbar) findViewById(R.id.mToolbar4);
        setSupportActionBar(toolbar);

        //设置Toolbar左边显示一个返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        editText1=(EditText) findViewById(R.id.edit_text1);
        editText1.setText(filename);

        ShowCursor(editText1);

        attachment_text=(TextView)findViewById(R.id.attachment_text);
        attachment_text.setOnClickListener(this);
        Button button2=(Button)findViewById(R.id.save_button);
        button2.setOnClickListener(this);

    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.attachment_text:
                FileUtil.getInstance().localStorage(this);
                break;
            case R.id.save_button:
                OnSave();
                break;

        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FileUtil.GET_FILE:
                if (resultCode == RESULT_OK) {
                    addAttachment(data);
                }
                break;
            default:
                break;
        }
    }
    public void addAttachment(Intent data) {
        Uri uri = data.getData();
        file = FileUtil.getInstance().getFile(this, uri);
        flag=true;
        if (!file.exists()) {

            Log.d("失败！！！", "!!!");
            return;
        }

    }
    public void OnSave()
    {

        final String s1=editText1.getText().toString();


        if((s1==null||s1.equals("")))
            Toast.makeText(ManageReviewOne_Modify.this,"请全部填满",Toast.LENGTH_SHORT).show();
        else
        {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    postSave(s1);
                }
            }).start();
        }

        finish();
    }

    private void postDelete(){

        OkHttpClient okHttpClient=new OkHttpClient();
        RequestBody requestBody1=new FormBody.Builder()
                .add("fileId",id).build();


        Request request = new Request.Builder()
                .url("http://119.23.38.100:8080/cma/ManagementReview/deleteOneFile")//url的地址
                .post(requestBody1)
                .build();

        //同步上传
        //异步post
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ManageReviewOne_Modify.this, "删除失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ManageReviewOne_Modify.this, "删除成功！", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }
    private void postSave(String name){
        //String result="{\"name\":\""+preName+"\",\"staffFile\":"+jsonStr+"}";
        OkHttpClient okHttpClient=new OkHttpClient();
        Log.d("文件名是",name);
        if(flag==false) {
            Log.d("修改","没有修改文件");
            Log.d("编号",id);
            Log.d("年份",year);
            RequestBody requestBody1 = new FormBody.Builder()
                    .add("fileId",id)
                    .add("year", year)
                    .add("fileName", name)
                    .build();
            Request request = new Request.Builder()
                    .url("http://119.23.38.100:8080/cma/ManagementReview/modifyOneFile")//url的地址
                    .post(requestBody1)
                    .build();
            //同步上传
            //异步post
            okHttpClient.newCall(request).enqueue(new Callback() {


                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("失败了","!!!!!!1");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ManageReviewOne_Modify.this, "保存失败！", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String c=response.toString();
                            Log.d("查看返回信息",c);
                            Toast.makeText(ManageReviewOne_Modify.this, "保存成功！", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            });
        }
        else {
            Log.d("修改了文件","修改！！1");
            MultipartBody.Builder builder = new MultipartBody.Builder();
            //设置类型
            builder.setType(MultipartBody.FORM);
            //追加参数
            Log.d("文件名称",name);
            Log.d("年份",year);
            builder.addFormDataPart("fileId", id);
            builder.addFormDataPart("year", year);
            builder.addFormDataPart("fileName",name);
            builder.addFormDataPart("file", file.getName(), RequestBody.create(null, file));


            //创建RequestBody
            RequestBody body = builder.build();

            final Request request = new Request.Builder().url("http://119.23.38.100:8080/cma/ManagementReview/modifyOneFile").post(body).build();

            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override

                public void onResponse(Call call, Response response)throws IOException {
                    String responseData = response.body().string();
                    Log.d("onSave:",responseData);
                    int code = 0;
                    String msg = "";
                    try {
                        JSONObject object = new JSONObject(responseData);
                        code = object.getInt("code");
                        msg = object.getString("msg");
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    if(code == 200 && msg.equals("成功")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ManageReviewOne_Modify.this, "提交成功！", Toast.LENGTH_SHORT).show();
                            }
                        });
                        finish();
                    }
                }
                @Override
                public void onFailure(Call call,IOException e){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ManageReviewOne_Modify.this, "提交失败！", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }

    }
    private void ShowCursor(final EditText editText){
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setCursorVisible(true);
            }
        });
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

}


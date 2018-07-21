package com.example.cma.ui.staff_management;


import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cma.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class StaffTraining_result_add extends AppCompatActivity {
    String id;
    String trainingId;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_training_result_add);
        Intent intent=getIntent();
        id=(String) intent.getSerializableExtra("id");
         Log.d("传递的ID是！！！！！！！！！",id);
         trainingId=(String)intent.getStringExtra("trainingId");
        //在Activity代码中使用Toolbar对象替换ActionBar
        toolbar = (Toolbar) findViewById(R.id.mToolbar4);
        setSupportActionBar(toolbar);
        ShowCursor((EditText)findViewById(R.id.edit_text1));
        //设置Toolbar左边显示一个返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }



        Button button=(Button)findViewById(R.id.submit_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean isfull=true;
                EditText editText1=(EditText)findViewById(R.id.edit_text1);
                final String s1=editText1.getText().toString();
                if((s1==null||s1.equals("")) )
                    isfull=false;
                if(isfull==true){
                    AlertDialog.Builder dialog=new AlertDialog.Builder(StaffTraining_result_add.this);
                    dialog.setTitle("确定提交吗？");
                    dialog.setMessage("确保信息准确！");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //将信息提交到数据库 TODO
                            //传递json数据和图片流
                            new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    postJson(s1);
                                }
                            }).start();


                            finish();
                        }
                    });
                    dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(StaffTraining_result_add.this,"你仍旧可以修改！",Toast.LENGTH_SHORT).show();
                        }
                    });
                    dialog.show();
                }
                else{
                    if(isfull==false)
                        Toast.makeText(StaffTraining_result_add.this,"请全部填满",Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(StaffTraining_result_add.this,"请上传扫描件",Toast.LENGTH_SHORT).show();

                }

            }
        });

    }
    private void ShowCursor(final EditText editText){
        editText.setOnTouchListener(new View.OnTouchListener() {
            int touch_flag=0;
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                touch_flag++;
                if(touch_flag==2){
                    touch_flag=0;
                    editText.setCursorVisible(true);
                }
                return false;
            }
        });

    }

    private void postJson(String s1){
        // String name,String department,String position,String id,String location
        Log.i("StaffFile_Add"," Here here11111");


        OkHttpClient okHttpClient=new OkHttpClient();
        //创建一个请求对象
        MediaType JSON=MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody1= new FormBody.Builder()
                .add("id",id)
                .add("trainingId",trainingId)
                .add("result",s1)

                .build();
        Request request = new Request.Builder()
                .url("http://119.23.38.100:8080/cma/StaffTraining/addTrainingResult")//url的地址
                .post(requestBody1)
                .build();
        Log.d("StaffFile_Add"," Here here2222");

        //同步上传
        //异步上传
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("androixx.cn", "失败！！！！");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(StaffTraining_result_add.this, "上传失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.d("androixx.cn", result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(StaffTraining_result_add.this, "上传成功！", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }
    //监听返回按钮的点击事件，比如可以返回上级Activity
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

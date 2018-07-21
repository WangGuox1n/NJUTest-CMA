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
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cma.R;
import com.example.cma.model.staff_management.Result;
import com.example.cma.model.staff_management.StaffTraining;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class StaffTraining_See extends AppCompatActivity {
    Toolbar toolbar;
    StaffTraining staff;
    static public List<StaffTraining> dangAns=new ArrayList<StaffTraining>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_training__see);

        Intent intent = getIntent();
        staff = (StaffTraining) intent.getSerializableExtra("chuandi");

        ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);

        //在Activity代码中使用Toolbar对象替换ActionBar
        toolbar = (Toolbar) findViewById(R.id.mToolbar3);
        setSupportActionBar(toolbar);
        Button modify_button=(Button) findViewById(R.id.save_button);
        modify_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(StaffTraining_See.this,StaffTraining_modify.class);
                intent.putExtra("chuandi",staff);
                startActivity(intent);
            }
        });

        //设置Toolbar左边显示一个返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        final TextView text1 = (TextView) findViewById(R.id.text1);
        text1.setText(staff.getProgram());

        final TextView text2 = (TextView) findViewById(R.id.text2);
        text2.setText(staff.getTrainingDate());

        final TextView text3 = (TextView) findViewById(R.id.text3);
        text3.setText(staff.getPlace());

        final TextView text4 = (TextView) findViewById(R.id.text4);
        text4.setText(staff.getPresenter());

        final TextView text5 = (TextView) findViewById(R.id.text5);
        text5.setText(staff.getContent());

        final TextView text6 = (TextView) findViewById(R.id.text6);
        text6.setText(staff.getNote());


        Button button2 = (Button) findViewById(R.id.check_button);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(StaffTraining_See.this, StaffTraining_staff_main.class);
                String d = String.valueOf(staff.getTrainingId());
                Log.d("选中的ID", d);
                intent.putExtra("chuandi", d);
                intent.putExtra("programName", staff);
                startActivity(intent);
                refresh();

            }
        });

        Button delete_button=(Button)findViewById(R.id.delete_button);
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog=new AlertDialog.Builder(StaffTraining_See.this);
                dialog.setTitle("确定删除此年度培训计划吗？");
                dialog.setCancelable(false);
                dialog.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //将信息提交到数据库
                    }
                });
                dialog.setNegativeButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //从数据库删除这个人的档案 TODO
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                postDelete(staff);
                            }
                        }).start();


                        finish();
                    }
                });
                dialog.show();
            }
        });


    }


    private void refresh(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("点击获取的","here is json11");
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            // 指定访问的服务器地址，后续在这里修改
                            //.url("http://10.0.2.2:3000/stars")
                            .url("http://119.23.38.100:8080/cma/StaffTraining/getAll")
                            .build();
                    Response response = client.newCall(request).execute();
                    Log.d("点击获取的","here is json22");
                    String responseData = response.body().string();
                    Log.d("点击获取的","here is json44");
                    Log.d("获得的数据:",responseData);
                    parseJSONWithGSON(responseData);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void parseJSONWithGSON(String jsonData) {

        System.out.print(jsonData);
        Gson gson = new Gson();
        //Log.d("点击获取得数据","jsonData");
        //dangAns = gson.fromJson(jsonData, new TypeToken<List<StaffTraining>>() {}.getType());
        Log.d("更新！！！！！！！！！！！","！！！！！");
        Result<List<StaffTraining>> userListResult = gson.fromJson(jsonData,new TypeToken<Result<List<StaffTraining>>>(){}.getType());
        //Result<Array<StaffTraining>> userListResult = gson.fromJson(jsonData,new TypeToken<Result<List<StaffTraining>>>(){}.getType());
       for(StaffTraining dangAns:userListResult.data)
       {
           if(dangAns.getTrainingId()==staff.getTrainingId())
               showResponse(dangAns);
       }


    }

    private void showResponse(final StaffTraining dangAns) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                final TextView text1 = (TextView) findViewById(R.id.text1);
                text1.setText(dangAns.getProgram());

                final TextView text2 = (TextView) findViewById(R.id.text2);
                text2.setText(dangAns.getTrainingDate());

                final TextView text3 = (TextView) findViewById(R.id.text3);
                text3.setText(dangAns.getPlace());

                final TextView text4 = (TextView) findViewById(R.id.text4);
                text4.setText(dangAns.getPresenter());

                final TextView text5 = (TextView) findViewById(R.id.text5);
                text5.setText(dangAns.getContent());

                final TextView text6 = (TextView) findViewById(R.id.text6);
                text6.setText(dangAns.getNote());


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
    private void postDelete(StaffTraining temp){
        Gson gson=new Gson();
        OkHttpClient okHttpClient=new OkHttpClient();


        //或者传一个name
        RequestBody requestBody1=new FormBody.Builder().add("trainingId",String.valueOf(temp.getTrainingId())).build();

        Request request = new Request.Builder()
                .url("http://119.23.38.100:8080/cma/StaffTraining/deleteOne")//url的地址
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
                        Toast.makeText(StaffTraining_See.this, "删除失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(StaffTraining_See.this, "删除成功！", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }
    protected void onResume(){
        super.onResume();
        refresh();
    }
}


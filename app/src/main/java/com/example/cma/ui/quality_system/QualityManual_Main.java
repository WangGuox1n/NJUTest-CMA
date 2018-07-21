package com.example.cma.ui.quality_system;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.support.v7.widget.Toolbar;


import com.example.cma.R;
import com.example.cma.model.quality_system.QualityManual;
import com.example.cma.model.staff_management.Result;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class QualityManual_Main extends AppCompatActivity implements View.OnClickListener {
    Button b3;
    Button b2;
    Button b1;
    Boolean flag;
    Toolbar toolbar;
    String function;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quality_manual__main);
        Intent intent=getIntent();
        function=intent.getStringExtra("function");
        toolbar = (Toolbar) findViewById(R.id.mToolbar);
        setSupportActionBar(toolbar);
        if(function.equals("QualityManual"))
           toolbar.setTitle("质量手册");
        else if(function.equals("ProgramFile"))
            toolbar.setTitle("程序文件");
        else
            toolbar.setTitle("作业指导书");
        b3 = (Button) findViewById(R.id.button3);
        b3.setOnClickListener(this);
        b2 = (Button) findViewById(R.id.button2);
        b2.setOnClickListener(this);
        b1 = (Button) findViewById(R.id.button1);
        b1.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.button3:
                Intent intent = new Intent(QualityManual_Main.this, QualityManualChange_Main.class);
                intent.putExtra("function",function);
                startActivity(intent);
                break;
            case R.id.button2:
                Log.d("获得2222222222", "!!!!!!!!!");
                Intent intent2 = new Intent(QualityManual_Main.this, QualityManualHistory_Main.class);
                intent2.putExtra("function",function);
                startActivity(intent2);
                break;
            case R.id.button1:
                Log.d("获得", "!!!!!!!!!");
                 ifCurrentExists();

                break;

            default:
                break;
        }
    }

    public void ifCurrentExists() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String path = "http://119.23.38.100:8080/cma/"+function+"/getCurrent";
                    Log.d("地址！！！！！！！",path);
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            // 指定访问的服务器地址，后续在这里修改
                            .url(path)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Gson gson = new Gson();
                    Result<QualityManual> userListResult = gson.fromJson(responseData, new TypeToken<Result<QualityManual>>() {
                    }.getType());
                    //Result<Array<AnnualPlan>> userListResult = gson.fromJson(jsonData,new TypeToken<Result<List<AnnualPlan>>>(){}.getType());
                    if (userListResult.data == null) {
                        Log.d("没有", "记录！！！！！！");

                        flag = false;
                    } else
                        flag = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(flag==true) {
                    Intent intent3 = new Intent(QualityManual_Main.this, QualityManualCurrent_Main.class);
                    intent3.putExtra("function",function);
                    startActivity(intent3);
                }
                else
                {
                    Looper.prepare();
                    AlertDialog.Builder builder = new AlertDialog.Builder(QualityManual_Main.this);
                    builder.setTitle("没有最新版本");
                    builder.setPositiveButton("上传", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            Intent intent = new Intent(QualityManual_Main.this, QualityManual_Add.class);
                            intent.putExtra("version","1");
                            intent.putExtra("function",function);
                            intent.putExtra("state","2");
                            startActivity(intent);
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {

                        }
                    });
                    builder.show();
                    Looper.loop();

                }
            }

        }).start();
    }
}


package com.example.cma.ui.staff_management;


import com.example.cma.R;
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
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cma.model.staff_management.StaffTraining;
import com.google.gson.Gson;

import org.feezu.liuli.timeselector.TimeSelector;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class StaffTraining_modify extends AppCompatActivity {
    Toolbar toolbar;
    StaffTraining staff;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_training_modify);

        Intent intent=getIntent();
        staff=(StaffTraining) intent.getSerializableExtra("chuandi");

        ScrollView scrollView=(ScrollView)findViewById(R.id.scrollView);

        //在Activity代码中使用Toolbar对象替换ActionBar
        toolbar = (Toolbar) findViewById(R.id.mToolbar3);
        setSupportActionBar(toolbar);

        //设置Toolbar左边显示一个返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        final Long trainingId=staff.getTrainingId();

        final EditText editText1=(EditText) findViewById(R.id.edit_text3_1);
        editText1.setText(staff.getProgram());

        final TextView editText2=(TextView)findViewById(R.id.edit_text3_2);
        editText2.setText(staff.getTrainingDate());

        final EditText editText3=(EditText)findViewById(R.id.edit_text3_3);
        editText3.setText(staff.getPlace());

        final EditText editText4=(EditText)findViewById(R.id.edit_text3_4);
        editText4.setText(staff.getPresenter());

        final EditText editText5=(EditText)findViewById(R.id.edit_text3_5);
        editText5.setText(staff.getContent());

        final EditText editText6=(EditText)findViewById(R.id.edit_text3_6);
        editText6.setText(staff.getNote());

        ShowCursor(editText1);
        ShowCursor(editText3);
        ShowCursor(editText4);
        ShowCursor(editText5);
        ShowCursor(editText6);


        editText2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
                final String now=simpleDateFormat.format(new Date());
                //editText.setText();
                TimeSelector timeSelector = new TimeSelector(StaffTraining_modify.this,new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        // Toast.makeText(StaffTraining_Add.this, time, Toast.LENGTH_SHORT).show();
                        editText2.setText(time.split(" ")[0]);
                    }
                }, "2000-01-01 00:00", now);
                timeSelector.setIsLoop(false);//设置不循环,true循环
                timeSelector.setTitle("请选择日期");
                //        timeSelector.setMode(TimeSelector.MODE.YMDHM);//显示 年月日时分（默认）
                timeSelector.setMode(TimeSelector.MODE.YMD);//只显示 年月日
                timeSelector.show();
            }
        });

        toolbar.findViewById(R.id.save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(StaffTraining_modify.this, "保存成功！", Toast.LENGTH_SHORT).show();
                //需要保存数据到数据库 TODO

                final String s1=editText1.getText().toString();
                final String s2=editText2.getText().toString();
                final  String s3=editText3.getText().toString();
                final String s4=editText4.getText().toString();
                final String s5=editText5.getText().toString();
                final String s6=editText6.getText().toString();
                if((s1==null||s1.equals("")) ||(s2==null||s2.equals("")) || (s3==null||s3.equals("")) || (s4==null||s4.equals("")) ||(s5==null||s5.equals(""))||(s6==null||s6.equals("")))
                    Toast.makeText(StaffTraining_modify.this,"请全部填满",Toast.LENGTH_SHORT).show();
                else
                {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            postSave(trainingId,s1,s2,s3,s4,s5,s6);
                        }
                    }).start();
                }

                finish();
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
    private void postSave(Long id,String s1,String s2,String s3,String s4,String s5,String s6){
        Gson gson=new Gson();
        Log.d("备注",s6);
        StaffTraining temp=new StaffTraining(id,s1,s2,s3,s4,s5,s6);
        String jsonStr=gson.toJson(temp,StaffTraining.class);
        //String result="{\"trainingId\":\""+String.valueOf(id)+"\",\"program\":\""+s1+"\",\"trainingDate\":\""+s2+"\",\"place\":\""+s3+"\",\"presenter\":\""+s4+"\",\"content\":\"" + s5 + "\",\"note\":\"" + s6 + "\"}";
        Log.d("传递的数据",jsonStr);
        //String result="{\"name\":\""+preName+"\",\"staffFile\":"+jsonStr+"}";
        OkHttpClient okHttpClient=new OkHttpClient();
        //创建一个请求对象
        MediaType JSON=MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody=RequestBody.create(JSON,jsonStr);
        RequestBody requestBody1= new FormBody.Builder().add("trainingId",String.valueOf(id))
                .add("program",s1)
                .add("trainingDate",s2)
                .add("place",s3)
                .add("presenter",s4)
                .add("content",s5)
                .add("note",s6)
                .build();

        Request request = new Request.Builder()
                .url("http://119.23.38.100:8080/cma/StaffTraining/modifyOne")//url的地址
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
                        Toast.makeText(StaffTraining_modify.this, "保存失败！", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(StaffTraining_modify.this, "保存成功！", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }



    private void ShowCursor(final EditText editText){
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setCursorVisible(true);
            }
        });
    }
}

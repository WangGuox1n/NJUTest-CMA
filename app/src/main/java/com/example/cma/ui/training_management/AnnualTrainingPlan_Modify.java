package com.example.cma.ui.training_management;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.cma.R;
import com.example.cma.model.training_management.AnnualTrainingPlan;

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

public class AnnualTrainingPlan_Modify extends AppCompatActivity {

    Toolbar toolbar;
    AnnualTrainingPlan staff;
    int gender;
    AnnualTrainingPlan prestaff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.annual_training_plan__modify);
        Intent intent=getIntent();
        staff=(AnnualTrainingPlan) intent.getSerializableExtra("chuandi");
        prestaff=(AnnualTrainingPlan)intent.getSerializableExtra("chuandi");

        // Bundle bundle=intent.getExtras();
        ScrollView scrollView=(ScrollView)findViewById(R.id.scrollView);

        //在Activity代码中使用Toolbar对象替换ActionBar
        toolbar = (Toolbar) findViewById(R.id.mToolbar4);
        setSupportActionBar(toolbar);

        //设置Toolbar左边显示一个返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }



        final EditText editText1=(EditText) findViewById(R.id.edit_text1);
        editText1.setText(String.valueOf(staff.getPlanId()));

        final EditText editText2=(EditText)findViewById(R.id.edit_text2);
        editText2.setText(staff.getTrainProject());


        final EditText editText3=(EditText)findViewById(R.id.edit_text3);
        editText3.setText(staff.getPeople());

        final EditText editText4=(EditText)findViewById(R.id.edit_text4);
        editText4.setText(staff.getMethod());

        final EditText editText5=(EditText)findViewById(R.id.edit_text5);
        editText5.setText(String.valueOf(staff.getTrainingTime()));

        final EditText editText6=(EditText)findViewById(R.id.edit_text6);
        editText6.setText(staff.getStartTime());

        final EditText editText7=(EditText)findViewById(R.id.edit_text7);
        editText7.setText(staff.getEndTime());

        final EditText editText8=(EditText)findViewById(R.id.edit_text8);
        editText8.setText(staff.getNote());



        //PreName=staff.getName();



        //在xml的时候，光标是不可见的，但是当用户编辑的时候，光标要可见。
        ShowCursor(editText1);
        ShowCursor(editText2);
        ShowCursor(editText3);
        ShowCursor(editText4);
        ShowCursor(editText5);
        ShowCursor(editText6);
        ShowCursor(editText7);
        ShowCursor(editText8);




        //对 保存 按钮监听
        findViewById(R.id.save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean isfull=true;
                EditText editText1=(EditText)findViewById(R.id.edit_text1);
                final String s1=editText1.getText().toString();

                TextView editText2=(TextView) findViewById(R.id.edit_text2);
                final String s2=editText2.getText().toString();

                EditText editText3=(EditText)findViewById(R.id.edit_text3);
                final String s3=editText3.getText().toString();

                EditText editText4=(EditText)findViewById(R.id.edit_text4);
                final String s4=editText4.getText().toString();

                EditText editText5=(EditText)findViewById(R.id.edit_text5);
                final String s5=editText5.getText().toString();

                EditText editText6=(EditText)findViewById(R.id.edit_text6);
                final String s6=editText6.getText().toString();

                EditText editText7=(EditText)findViewById(R.id.edit_text7);
                final String s7=editText7.getText().toString();

                EditText editText8=(EditText)findViewById(R.id.edit_text8);
                final String s8=editText8.getText().toString();


                if((s1==null||s1.equals("")) ||(gender==99) || (s3==null||s3.equals("")) || (s4==null||s4.equals("")) ||(s5==null||s5.equals("")) || (s6==null||s6.equals(""))||(s7==null||s7.equals("")) ||(s8==null||s8.equals(""))   )
                    isfull=false;
                if(isfull==true){
                    AlertDialog.Builder dialog=new AlertDialog.Builder(AnnualTrainingPlan_Modify.this);
                    dialog.setTitle("确定提交吗？");
                    dialog.setMessage("确保信息准确！");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    postJson(s1,s2,s3,s4,s5,s6,s7,s8);
                                }
                            }).start();

                            finish();
                        }
                    });
                    dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(AnnualTrainingPlan_Modify.this,"你仍旧可以修改！",Toast.LENGTH_SHORT).show();
                        }
                    });
                    dialog.show();
                }
                else{
                    Toast.makeText(AnnualTrainingPlan_Modify.this,"请全部填满",Toast.LENGTH_SHORT).show();

                }

            }
        });




    }




    //设置光标可见
    private void ShowCursor(final EditText editText){
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setCursorVisible(true);
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




    private void postJson(String s1,String s2,String s3,String s4,String s5,String s6,String s7,String s8){

        OkHttpClient okHttpClient=new OkHttpClient();
        //创建一个请求对象

        RequestBody requestBody=new FormBody.Builder()
                .add("planId",s1)
                .add("trainProject",s2)
                .add("people",s3)
                .add("method",s4)
                .add("trainingTime",s5)
                .add("startTime",s6)
                .add("endTime",s7)
                .add("note",s8)
                .build();

        Request request = new Request.Builder()
                .url("http://119.23.38.100:8080/cma/AnnualTrainingPlan/modifyOne")//url的地址
                .post(requestBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("androixx.cn", "失败！！！！");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AnnualTrainingPlan_Modify.this, "保存失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("androixx.cn", "成功！！！！");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AnnualTrainingPlan_Modify.this, "保存成功！", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }
}




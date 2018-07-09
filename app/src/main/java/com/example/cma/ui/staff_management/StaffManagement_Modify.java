package com.example.cma.ui.staff_management;

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
import com.example.cma.model.staff_management.StaffFile;
import com.example.cma.model.staff_management.StaffManagement;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.feezu.liuli.timeselector.TimeSelector;
import org.json.JSONObject;

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

public class StaffManagement_Modify extends AppCompatActivity {

    Toolbar toolbar;
    StaffManagement staff;
    int gender;
    StaffManagement prestaff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_management__modify);
        Intent intent=getIntent();
        staff=(StaffManagement) intent.getSerializableExtra("chuandi");
        prestaff=(StaffManagement)intent.getSerializableExtra("chuandi");

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
        editText1.setText(staff.getName());

        RadioGroup rg=(RadioGroup)findViewById(R.id.rg);
        if(staff.getGender()==0)
        {
            rg.check(R.id.male);
        }else
        {
            rg.check(R.id.femle);
        }

        gender=staff.getGender();

        final EditText editText3=(EditText)findViewById(R.id.edit_text3);
        editText3.setText(staff.getDepartment());

        final EditText editText4=(EditText)findViewById(R.id.edit_text4);
        editText4.setText(staff.getPosition());

        final EditText editText5=(EditText)findViewById(R.id.edit_text5);
        editText5.setText(staff.getTitle());

        final EditText editText6=(EditText)findViewById(R.id.edit_text6);
        editText6.setText(staff.getDegree());

        final EditText editText7=(EditText)findViewById(R.id.edit_text7);
        editText7.setText(staff.getGraduationSchool());

        final EditText editText8=(EditText)findViewById(R.id.edit_text8);
        editText8.setText(staff.getGraduationMajor());

        final TextView textView9=(TextView)findViewById(R.id.select_time9);
        textView9.setText(staff.getGraduationDate());

        final EditText editText10=(EditText)findViewById(R.id.edit_text10);
        editText10.setText(Integer.toString(staff.getWorkingYears()));



        //PreName=staff.getName();



        //在xml的时候，光标是不可见的，但是当用户编辑的时候，光标要可见。
        ShowCursor(editText1);
        // ShowCursor(editText2);
        ShowCursor(editText3);
        ShowCursor(editText4);
        ShowCursor(editText5);
        ShowCursor(editText6);
        ShowCursor(editText7);
        ShowCursor(editText8);
        ShowCursor(editText10);

        LinearLayout linearLayout=(LinearLayout)findViewById(R.id.top9);
        final TextView editText=(TextView) findViewById(R.id.select_time9);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
                final String now=simpleDateFormat.format(new Date());
                //editText.setText();
                TimeSelector timeSelector = new TimeSelector(StaffManagement_Modify.this,new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        // Toast.makeText(StaffTraining_Add.this, time, Toast.LENGTH_SHORT).show();
                        editText.setText(time.split(" ")[0]);
                    }
                }, "2000-01-01 00:00", now);
                timeSelector.setIsLoop(false);//设置不循环,true循环
                timeSelector.setTitle("请选择日期");
                //        timeSelector.setMode(TimeSelector.MODE.YMDHM);//显示 年月日时分（默认）
                timeSelector.setMode(TimeSelector.MODE.YMD);//只显示 年月日
                timeSelector.show();
            }
        });

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
                final String now=simpleDateFormat.format(new Date());
                //editText.setText();
                TimeSelector timeSelector = new TimeSelector(StaffManagement_Modify.this,new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        // Toast.makeText(StaffTraining_Add.this, time, Toast.LENGTH_SHORT).show();
                        editText.setText(time.split(" ")[0]);
                    }
                }, "2000-01-01 00:00", now);
                timeSelector.setIsLoop(false);//设置不循环,true循环

                //        timeSelector.setMode(TimeSelector.MODE.YMDHM);//显示 年月日时分（默认）
                timeSelector.setMode(TimeSelector.MODE.YMD);//只显示 年月日
                timeSelector.show();
            }
        });

        rg=(RadioGroup)findViewById(R.id.rg);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.male)
                {
                    gender=0;
                }
                else
                    gender=1;
            }
        });





        //对 保存 按钮监听
        findViewById(R.id.save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean isfull=true;
                EditText editText1=(EditText)findViewById(R.id.edit_text1);
                final String s1=editText1.getText().toString();
                //  TextView editText2=(TextView) findViewById(R.id.edit_text2);
                // final String s2=editText2.getText().toString();
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
                TextView textView9=(TextView)findViewById(R.id.select_time9);
                final String s9=textView9.getText().toString();
                EditText editText10=(EditText)findViewById(R.id.edit_text10);
                final String s10=editText10.getText().toString();

                if((s1==null||s1.equals("")) ||(gender==99) || (s3==null||s3.equals("")) || (s4==null||s4.equals("")) ||(s5==null||s5.equals("")) || (s6==null||s6.equals(""))||(s7==null||s7.equals("")) ||(s8==null||s8.equals("")) || (s9==null||s9.equals("")) || (s10==null||s10.equals(""))  )
                    isfull=false;
                if(isfull==true){
                    AlertDialog.Builder dialog=new AlertDialog.Builder(StaffManagement_Modify.this);
                    dialog.setTitle("确定提交吗？");
                    dialog.setMessage("确保信息准确！");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    postJson(s1,gender,s3,s4,s5,s6,s7,s8,s9,Integer.parseInt(s10));
                                }
                            }).start();

                            finish();
                        }
                    });
                    dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(StaffManagement_Modify.this,"你仍旧可以修改！",Toast.LENGTH_SHORT).show();
                        }
                    });
                    dialog.show();
                }
                else{
                    Toast.makeText(StaffManagement_Modify.this,"请全部填满",Toast.LENGTH_SHORT).show();

                }

            }
        });




    }




    //设置光标可见
    private void ShowCursor(final EditText editText){
        /* editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setCursorVisible(true);
            }
        });*/
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




    private void postJson(String s1,int s2,String s3,String s4,String s5,String s6,String s7,String s8,String s9,int s10){

        OkHttpClient okHttpClient=new OkHttpClient();
        //创建一个请求对象
        MediaType JSON=MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody=new FormBody.Builder().add("id",Long.toString(staff.getId()))
                .add("name",s1)
                .add("gender",Integer.toString(s2))
                .add("department",s3)
                .add("position",s4)
                .add("title",s5)
                .add("degree",s6)
                .add("graduationSchool",s7)
                .add("graduationMajor",s8)
                .add("graduationDate",s9)
                .add("workingYears",Integer.toString(s10))
                .build();

        Request request = new Request.Builder()
                .url("http://119.23.38.100:8080/cma/StaffManagement/modifyOne")//url的地址
                .post(requestBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("androixx.cn", "失败！！！！");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(StaffManagement_Modify.this, "保存失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("androixx.cn", "成功！！！！");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(StaffManagement_Modify.this, "保存成功！", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }
}




package com.example.cma.ui.period_check;

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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cma.R;
import com.example.cma.model.period_check.IntermediateChecksPlan;
import com.example.cma.model.period_check.IntermediateChecksRecord;
import com.example.cma.utils.ViewUtil;

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

public class PeriodCheckRecord_Modify extends AppCompatActivity {

    Toolbar toolbar;
    IntermediateChecksRecord checksRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.period_check_record__modify);
        Intent intent=getIntent();
        checksRecord=(IntermediateChecksRecord) intent.getSerializableExtra("chuandi");

        ScrollView scrollView=(ScrollView)findViewById(R.id.scrollView);

        //在Activity代码中使用Toolbar对象替换ActionBar
        toolbar = (Toolbar) findViewById(R.id.mToolbar4);
        setSupportActionBar(toolbar);

        //设置Toolbar左边显示一个返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        final TextView check_date=(TextView)findViewById(R.id.check_date);
        check_date.setText(checksRecord.getCheckDate());

        final EditText process_record=(EditText) findViewById(R.id.process_record);
        process_record.setText(checksRecord.getProcessRecord());

        final EditText process_person=(EditText)findViewById(R.id.process_person);
        process_person.setText(checksRecord.getProcessRecordPerson());

        final TextView process_date=(TextView)findViewById(R.id.process_date);
        process_date.setText(checksRecord.getProcessRecordDate());

        final EditText result_record=(EditText)findViewById(R.id.result_record);
        result_record.setText(checksRecord.getResultRecord());

        final EditText result_person=(EditText)findViewById(R.id.result_person);
        result_person.setText(checksRecord.getResultRecordPerson());

        final TextView result_date=(TextView)findViewById(R.id.result_date);
        result_date.setText(checksRecord.getResultRecordDate());

        final EditText note=(EditText)findViewById(R.id.note_text);
        note.setText(checksRecord.getNote());

        //在xml的时候，光标是不可见的，但是当用户编辑的时候，光标要可见。
        ViewUtil.ShowCursor(process_record);
        ViewUtil.ShowCursor(process_person);
        ViewUtil.ShowCursor(result_record);
        ViewUtil.ShowCursor(result_person);
        ViewUtil.ShowCursor(note);


        LinearLayout linearLayout=(LinearLayout)findViewById(R.id.top2);
        final TextView editText=(TextView) findViewById(R.id.check_date);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
                final String now=simpleDateFormat.format(new Date());
                //editText.setText();
                TimeSelector timeSelector = new TimeSelector(PeriodCheckRecord_Modify.this,new TimeSelector.ResultHandler() {
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
                TimeSelector timeSelector = new TimeSelector(PeriodCheckRecord_Modify.this,new TimeSelector.ResultHandler() {
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


        LinearLayout linearLayout2=(LinearLayout)findViewById(R.id.top5);
        final TextView editText2=(TextView) findViewById(R.id.process_date);
        editText2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
                final String now=simpleDateFormat.format(new Date());
                //editText.setText();
                TimeSelector timeSelector = new TimeSelector(PeriodCheckRecord_Modify.this,new TimeSelector.ResultHandler() {
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

        linearLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
                final String now=simpleDateFormat.format(new Date());
                //editText.setText();
                TimeSelector timeSelector = new TimeSelector(PeriodCheckRecord_Modify.this,new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        // Toast.makeText(StaffTraining_Add.this, time, Toast.LENGTH_SHORT).show();
                        editText2.setText(time.split(" ")[0]);
                    }
                }, "2000-01-01 00:00", now);
                timeSelector.setIsLoop(false);//设置不循环,true循环

                //        timeSelector.setMode(TimeSelector.MODE.YMDHM);//显示 年月日时分（默认）
                timeSelector.setMode(TimeSelector.MODE.YMD);//只显示 年月日
                timeSelector.show();
            }
        });

        LinearLayout linearLayout3=(LinearLayout)findViewById(R.id.top8);
        final TextView editText3=(TextView) findViewById(R.id.result_date);
        editText3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
                final String now=simpleDateFormat.format(new Date());
                //editText.setText();
                TimeSelector timeSelector = new TimeSelector(PeriodCheckRecord_Modify.this,new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        // Toast.makeText(StaffTraining_Add.this, time, Toast.LENGTH_SHORT).show();
                        editText3.setText(time.split(" ")[0]);
                    }
                }, "2000-01-01 00:00", now);
                timeSelector.setIsLoop(false);//设置不循环,true循环
                timeSelector.setTitle("请选择日期");
                //        timeSelector.setMode(TimeSelector.MODE.YMDHM);//显示 年月日时分（默认）
                timeSelector.setMode(TimeSelector.MODE.YMD);//只显示 年月日
                timeSelector.show();
            }
        });

        linearLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
                final String now=simpleDateFormat.format(new Date());
                //editText.setText();
                TimeSelector timeSelector = new TimeSelector(PeriodCheckRecord_Modify.this,new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        // Toast.makeText(StaffTraining_Add.this, time, Toast.LENGTH_SHORT).show();
                        editText3.setText(time.split(" ")[0]);
                    }
                }, "2000-01-01 00:00", now);
                timeSelector.setIsLoop(false);//设置不循环,true循环

                //        timeSelector.setMode(TimeSelector.MODE.YMDHM);//显示 年月日时分（默认）
                timeSelector.setMode(TimeSelector.MODE.YMD);//只显示 年月日
                timeSelector.show();
            }
        });





        //对 保存 按钮监听
        findViewById(R.id.save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean isfull=true;
                TextView check_date=(TextView)findViewById(R.id.check_date);
                final String s1=check_date.getText().toString();
                EditText process_record=(EditText)findViewById(R.id.process_record);
                final String s2=process_record.getText().toString();
                EditText process_person=(EditText)findViewById(R.id.process_person);
                final String s3=process_person.getText().toString();
                TextView process_date=(TextView)findViewById(R.id.process_date);
                final String s4=process_date.getText().toString();
                EditText result_record=(EditText)findViewById(R.id.result_record);
                final String s5=result_record.getText().toString();
                EditText result_person=(EditText)findViewById(R.id.result_person);
                final String s6=result_person.getText().toString();
                TextView result_date=(TextView)findViewById(R.id.result_date);
                final String s7=result_date.getText().toString();
                EditText note_text=(EditText)findViewById(R.id.note_text);
                final String s8=note_text.getText().toString();


                if((s1==null||s1.equals("")) || (s2==null||s2.equals(""))||(s3==null||s3.equals("")) ||(s4==null||s4.equals("")) || (s5==null||s5.equals(""))||(s6==null||s6.equals("")) ||(s7==null||s7.equals("")) ||(s8==null||s8.equals("")))
                    isfull=false;
                if(isfull==true){
                    AlertDialog.Builder dialog=new AlertDialog.Builder(PeriodCheckRecord_Modify.this);
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
                            Toast.makeText(PeriodCheckRecord_Modify.this,"你仍旧可以修改！",Toast.LENGTH_SHORT).show();
                        }
                    });
                    dialog.show();
                }
                else{
                    Toast.makeText(PeriodCheckRecord_Modify.this,"请全部填满",Toast.LENGTH_SHORT).show();

                }

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
        RequestBody requestBody=new FormBody.Builder()
                .add("recordId",Long.toString(checksRecord.getRecordId()))
                .add("checkDate",s1)
                .add("processRecord",s2)
                .add("processRecordPerson",s3)
                .add("processRecordDate",s4)
                .add("resultRecord",s5)
                .add("resultRecordPerson",s6)
                .add("resultRecordDate",s7)
                .add("note",s8)
                .build();
        Request request = new Request.Builder()
                .url("http://119.23.38.100:8080/cma/IntermediateChecksRecord/modifyOne")//url的地址
                .post(requestBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("androixx.cn", "失败！！！！");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(PeriodCheckRecord_Modify.this, "上传失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.d("androixx.cn", result);
                Log.d("androixx.cn", "成功！！！！");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(PeriodCheckRecord_Modify.this, "上传成功！", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }




}




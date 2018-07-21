package com.example.cma.ui.sample_management;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cma.R;

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

public class SampleReceive_Add extends AppCompatActivity {
    Toolbar toolbar;
    int state = 99;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_receive__add);
        //在Activity代码中使用Toolbar对象替换ActionBar
        toolbar = (Toolbar) findViewById(R.id.mToolbar4);
        setSupportActionBar(toolbar);

        //设置Toolbar左边显示一个返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        ShowCursor((EditText) findViewById(R.id.edit_text1));
        ShowCursor((EditText) findViewById(R.id.edit_text2));
        ShowCursor((EditText) findViewById(R.id.edit_text3));
        //ShowCursor((EditText) findViewById(R.id.edit_text4));
        ShowCursor((EditText) findViewById(R.id.edit_text5));
        ShowCursor((EditText) findViewById(R.id.edit_text6));
        ShowCursor((EditText) findViewById(R.id.edit_text7));
        ShowCursor((EditText) findViewById(R.id.edit_text8));
        ShowCursor((EditText) findViewById(R.id.edit_text9));

        LinearLayout linearLayout1 = (LinearLayout) findViewById(R.id.top7);
        final TextView editText1 = (TextView) findViewById(R.id.edit_text7);
        editText1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
                final String now = simpleDateFormat.format(new Date());
                //editText.setText();
                TimeSelector timeSelector = new TimeSelector(SampleReceive_Add.this, new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        editText1.setText(time.split(" ")[0]);
                    }
                }, "2000-01-01 00:00", now);
                timeSelector.setIsLoop(true);//设置不循环,true循环
                timeSelector.setTitle("请选择日期");
                //        timeSelector.setMode(TimeSelector.MODE.YMDHM);//显示 年月日时分（默认）
                timeSelector.setMode(TimeSelector.MODE.YMD);//只显示 年月日
                timeSelector.show();
            }
        });

        linearLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
                final String now = simpleDateFormat.format(new Date());
                //editText.setText();
                TimeSelector timeSelector = new TimeSelector(SampleReceive_Add.this, new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        editText1.setText(time.split(" ")[0]);
                    }
                }, "2000-01-01 00:00", now);
                timeSelector.setIsLoop(true);//设置不循环,true循环

                //        timeSelector.setMode(TimeSelector.MODE.YMDHM);//显示 年月日时分（默认）
                timeSelector.setMode(TimeSelector.MODE.YMD);//只显示 年月日
                timeSelector.show();
            }
        });


        LinearLayout linearLayout2 = (LinearLayout) findViewById(R.id.top9);
        final TextView editText2 = (TextView) findViewById(R.id.edit_text9);
        editText2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
                final String now = simpleDateFormat.format(new Date());
                //editText.setText();
                TimeSelector timeSelector = new TimeSelector(SampleReceive_Add.this, new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        editText2.setText(time.split(" ")[0]);
                    }
                }, "2000-01-01 00:00", now);
                timeSelector.setIsLoop(true);//设置不循环,true循环
                timeSelector.setTitle("请选择日期");
                //        timeSelector.setMode(TimeSelector.MODE.YMDHM);//显示 年月日时分（默认）
                timeSelector.setMode(TimeSelector.MODE.YMD);//只显示 年月日
                timeSelector.show();
            }
        });

        linearLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
                final String now = simpleDateFormat.format(new Date());
                //editText.setText();
                TimeSelector timeSelector = new TimeSelector(SampleReceive_Add.this, new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        editText2.setText(time.split(" ")[0]);
                    }
                }, "2000-01-01 00:00", now);
                timeSelector.setIsLoop(true);//设置不循环,true循环

                //        timeSelector.setMode(TimeSelector.MODE.YMDHM);//显示 年月日时分（默认）
                timeSelector.setMode(TimeSelector.MODE.YMD);//只显示 年月日
                timeSelector.show();
            }
        });

        RadioGroup rg=(RadioGroup)findViewById(R.id.rg);
        rg.clearCheck();
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.state0)
                {
                    state=0;
                }
                else if(i==R.id.state1)
                {
                    state=1;
                }
                else if(i==R.id.state2)
                {
                    state=2;
                }
                else if(i==R.id.state3)
                {
                    state=3;
                }
            }
        });

        Button button = (Button) findViewById(R.id.submit_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean isfull = true;
                EditText editText1 = (EditText) findViewById(R.id.edit_text1);
                final String s1 = editText1.getText().toString();
                EditText editText2 = (EditText) findViewById(R.id.edit_text2);
                final String s2 = editText2.getText().toString();
                EditText editText3 = (EditText) findViewById(R.id.edit_text3);
                final String s3 = editText3.getText().toString();
                //EditText editText4 = (EditText) findViewById(R.id.edit_text4);
                //final String s4 = editText4.getText().toString();
                EditText editText5 = (EditText) findViewById(R.id.edit_text5);
                final String s5 = editText5.getText().toString();
                EditText editText6 = (EditText) findViewById(R.id.edit_text6);
                final String s6 = editText6.getText().toString();
                EditText editText7 = (EditText) findViewById(R.id.edit_text7);
                final String s7 = editText7.getText().toString();
                EditText editText8 = (EditText) findViewById(R.id.edit_text8);
                final String s8 = editText8.getText().toString();
                EditText editText9 = (EditText) findViewById(R.id.edit_text9);
                final String s9 = editText9.getText().toString();
                if ((s1 == null || s1.equals("")) || (s2 == null || s2.equals(""))|| (s3 == null || s3.equals("")) || (state==99) || (s5 == null || s5.equals(""))
                        || (s6 == null || s6.equals("")) || (s7 == null || s7.equals(""))|| (s8 == null || s8.equals(""))|| (s9 == null || s9.equals("")))
                    isfull = false;
                if (isfull == true) {
                    //Log.d("select_time:",s9);
                    //Log.d("选择的性别：",Integer.toString(gender));
                    AlertDialog.Builder dialog = new AlertDialog.Builder(SampleReceive_Add.this);
                    dialog.setTitle("确定提交吗？");
                    dialog.setMessage("确保信息准确！");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    postJson(s1, s2, Integer.parseInt(s3), state, s5, s6, s7,s8,s9);
                                }
                            }).start();
                            //finish();
                        }
                    });
                    dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(SampleReceive_Add.this, "你仍旧可以修改！", Toast.LENGTH_SHORT).show();
                        }
                    });
                    dialog.show();
                } else {
                    Toast.makeText(SampleReceive_Add.this, "请全部填满", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void postJson(String s1,String s2,int s3,int s4,String s5,String s6,String s7,String s8,String s9){

        OkHttpClient okHttpClient=new OkHttpClient();
        //创建一个请求对象
        MediaType JSON=MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody=new FormBody.Builder()
                .add("sampleNumber",s1)
                .add("sampleName",s2)
                .add("sampleAmount",String.valueOf(s3))
                .add("sampleState",String.valueOf(s4))
                .add("requester",s5)
                .add("receiver",s6)
                .add("receiveDate",s7)
                .add("obtainer",s8)
                .add("obtainDate",s9)
                .build();

        Request request = new Request.Builder()
                .url("http://119.23.38.100:8080/cma/SampleReceive/addOne")//url的地址
                .post(requestBody)
                .build();
        //  Log.d("Add"," Here here133");
        //异步上传
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("androixx.cn", "失败！！！！");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SampleReceive_Add.this, "上传失败！", Toast.LENGTH_SHORT).show();
                        finish();
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
                        Toast.makeText(SampleReceive_Add.this, "上传成功！", Toast.LENGTH_SHORT).show();
                        finish();
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


    //设置光标可见
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



}
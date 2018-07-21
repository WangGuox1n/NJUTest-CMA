package com.example.cma.ui.sample_management;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cma.R;
import com.example.cma.model.sample_management.SampleReceipt;

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

public class SampleReceipt_Modify extends AppCompatActivity {

    int state[]=new int[10];
    SampleReceipt sampleReceipt;
    Toolbar toolbar;
    private Long sampleid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_receipt__modify);
        Intent intent=getIntent();
        sampleReceipt=(SampleReceipt) intent.getSerializableExtra("samplereceipt");
        sampleid=sampleReceipt.getSampleId();
        Log.d("sampleID",sampleid+"");

        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

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
        editText1.setText(sampleReceipt.getApplicationUnit());
        final EditText editText3=(EditText)findViewById(R.id.edit_text3);
        editText3.setText(sampleReceipt.getVersion());
        final EditText editText4=(EditText)findViewById(R.id.edit_text4);
        editText4.setText(sampleReceipt.getContractId());
        final EditText editText6=(EditText)findViewById(R.id.edit_text6);
        editText6.setText(sampleReceipt.getElectronicMedia());
        final EditText editText15=(EditText)findViewById(R.id.edit_text15);
        editText15.setText(sampleReceipt.getOther());
        final EditText editText17=(EditText)findViewById(R.id.edit_text17);
        editText17.setText(sampleReceipt.getReceiveUnit());
        final EditText editText18=(EditText)findViewById(R.id.edit_text18);
        editText18.setText(sampleReceipt.getReceiveDate());
        final EditText editText19=(EditText)findViewById(R.id.edit_text19);
        editText19.setText(sampleReceipt.getSender());
        final EditText editText20=(EditText)findViewById(R.id.edit_text20);
        editText20.setText(sampleReceipt.getReceiver());

        RadioGroup rg0=(RadioGroup)findViewById(R.id.testtype_rg);
        RadioGroup rg1=(RadioGroup)findViewById(R.id.readme_rg);
        RadioGroup rg2=(RadioGroup)findViewById(R.id.application_rg);
        RadioGroup rg3=(RadioGroup)findViewById(R.id.materialreceipt_rg);
        RadioGroup rg4=(RadioGroup)findViewById(R.id.function_rg);
        RadioGroup rg5=(RadioGroup)findViewById(R.id.confirmations_rg);
        RadioGroup rg6=(RadioGroup)findViewById(R.id.introduction_rg);
        RadioGroup rg7=(RadioGroup)findViewById(R.id.guarantee_rg);
        RadioGroup rg8=(RadioGroup)findViewById(R.id.software_rg);
        RadioGroup rg9=(RadioGroup)findViewById(R.id.softwaretype_rg);

        if(sampleReceipt.getTestType()==0){
            rg0.check(R.id.state0_0);
        }
        else if(sampleReceipt.getTestType()==1){
            rg0.check(R.id.state0_1);
        }
        else if(sampleReceipt.getTestType()==2){
            rg0.check(R.id.state0_2);
        }


        if(sampleReceipt.getReadMe()==0){
            rg1.check(R.id.state1_0);
        }
        else if(sampleReceipt.getReadMe()==1){
            rg1.check(R.id.state1_1);
        }
        else if(sampleReceipt.getReadMe()==2){
            rg1.check(R.id.state1_2);
        }
        else if(sampleReceipt.getReadMe()==3){
            rg1.check(R.id.state1_3);
        }


        if(sampleReceipt.getApplication()==0){
            rg2.check(R.id.state2_0);
        }
        else if(sampleReceipt.getApplication()==1){
            rg2.check(R.id.state2_1);
        }
        else if(sampleReceipt.getApplication()==2){
            rg2.check(R.id.state2_2);
        }
        else if(sampleReceipt.getApplication()==3){
            rg2.check(R.id.state2_3);
        }


        if(sampleReceipt.getMaterialReceipt()==0){
            rg3.check(R.id.state3_0);
        }
        else if(sampleReceipt.getMaterialReceipt()==1){
            rg3.check(R.id.state3_1);
        }
        else if(sampleReceipt.getMaterialReceipt()==2){
            rg3.check(R.id.state3_2);
        }
        else if(sampleReceipt.getMaterialReceipt()==3){
            rg3.check(R.id.state3_3);
        }

        if(sampleReceipt.getFunctions()==0){
            rg4.check(R.id.state4_0);
        }
        else if(sampleReceipt.getFunctions()==1){
            rg4.check(R.id.state4_1);
        }
        else if(sampleReceipt.getFunctions()==2){
            rg4.check(R.id.state4_2);
        }
        else if(sampleReceipt.getFunctions()==3){
            rg4.check(R.id.state4_3);
        }

        if(sampleReceipt.getConfirmations()==0){
            rg5.check(R.id.state5_0);
        }
        else if(sampleReceipt.getConfirmations()==1){
            rg5.check(R.id.state5_1);
        }
        else if(sampleReceipt.getConfirmations()==2){
            rg5.check(R.id.state5_2);
        }
        else if(sampleReceipt.getConfirmations()==3){
            rg5.check(R.id.state5_3);
        }

        if(sampleReceipt.getIntroduction()==0){
            rg6.check(R.id.state6_0);
        }
        else if(sampleReceipt.getIntroduction()==1){
            rg6.check(R.id.state6_1);
        }
        else if(sampleReceipt.getIntroduction()==2){
            rg6.check(R.id.state6_2);
        }
        else if(sampleReceipt.getIntroduction()==3){
            rg6.check(R.id.state6_3);
        }

        if(sampleReceipt.getGuarantee()==0){
            rg7.check(R.id.state7_0);
        }
        else if(sampleReceipt.getGuarantee()==1){
            rg7.check(R.id.state7_1);
        }
        else if(sampleReceipt.getGuarantee()==2){
            rg7.check(R.id.state7_2);
        }
        else if(sampleReceipt.getGuarantee()==3){
            rg7.check(R.id.state7_3);
        }

        if(sampleReceipt.getSoftwareSample()==0){
            rg8.check(R.id.state8_0);
        }
        else if(sampleReceipt.getSoftwareSample()==1){
            rg8.check(R.id.state8_1);
        }


        if(sampleReceipt.getSoftwareType()==0){
            rg9.check(R.id.state9_0);
        }
        else if(sampleReceipt.getSoftwareType()==1){
            rg9.check(R.id.state9_1);
        }
        else if(sampleReceipt.getSoftwareType()==2){
            rg9.check(R.id.state9_2);
        }
        else if(sampleReceipt.getSoftwareType()==3){
            rg9.check(R.id.state9_3);
        }

        state[0]=sampleReceipt.getTestType();
        state[1]=sampleReceipt.getReadMe();
        state[2]=sampleReceipt.getApplication();
        state[3]=sampleReceipt.getMaterialReceipt();
        state[4]=sampleReceipt.getFunctions();
        state[5]=sampleReceipt.getConfirmations();
        state[6]=sampleReceipt.getIntroduction();
        state[7]=sampleReceipt.getGuarantee();
        state[8]=sampleReceipt.getSoftwareSample();
        state[9]=sampleReceipt.getSoftwareType();

        LinearLayout linearLayout1 = (LinearLayout) findViewById(R.id.top18);
        final TextView editText = (TextView) findViewById(R.id.edit_text18);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
                final String now = simpleDateFormat.format(new Date());
                //editText.setText();
                TimeSelector timeSelector = new TimeSelector(SampleReceipt_Modify.this, new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        editText.setText(time.split(" ")[0]);
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
                TimeSelector timeSelector = new TimeSelector(SampleReceipt_Modify.this, new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        editText.setText(time.split(" ")[0]);
                    }
                }, "2000-01-01 00:00", now);
                timeSelector.setIsLoop(true);//设置不循环,true循环

                //        timeSelector.setMode(TimeSelector.MODE.YMDHM);//显示 年月日时分（默认）
                timeSelector.setMode(TimeSelector.MODE.YMD);//只显示 年月日
                timeSelector.show();
            }
        });

        ShowCursor((EditText) findViewById(R.id.edit_text1));
        ShowCursor((EditText) findViewById(R.id.edit_text3));
        ShowCursor((EditText) findViewById(R.id.edit_text4));
        ShowCursor((EditText) findViewById(R.id.edit_text6));
        ShowCursor((EditText) findViewById(R.id.edit_text15));
        ShowCursor((EditText) findViewById(R.id.edit_text17));
        ShowCursor((EditText) findViewById(R.id.edit_text18));
        ShowCursor((EditText) findViewById(R.id.edit_text19));
        ShowCursor((EditText) findViewById(R.id.edit_text20));



        rg0.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.state0_0)
                {
                    state[0]=0;
                }
                else if(i==R.id.state0_1)
                {
                    state[0]=1;
                }
                else if(i==R.id.state0_2)
                {
                    state[0]=2;
                }
            }
        });

        rg1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.state1_0)
                {
                    state[1]=0;
                }
                else if(i==R.id.state1_1)
                {
                    state[1]=1;
                }
                else if(i==R.id.state1_2)
                {
                    state[1]=2;
                }
                else if(i==R.id.state1_3)
                {
                    state[1]=3;
                }
            }
        });

        rg2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.state2_0)
                {
                    state[2]=0;
                }
                else if(i==R.id.state2_1)
                {
                    state[2]=1;
                }
                else if(i==R.id.state2_2)
                {
                    state[2]=2;
                }
                else if(i==R.id.state2_3)
                {
                    state[2]=3;
                }
            }
        });

        rg3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.state3_0)
                {
                    state[3]=0;
                }
                else if(i==R.id.state3_1)
                {
                    state[3]=1;
                }
                else if(i==R.id.state3_2)
                {
                    state[3]=2;
                }
                else if(i==R.id.state3_3)
                {
                    state[3]=3;
                }
            }
        });

        rg4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.state4_0)
                {
                    state[4]=0;
                }
                else if(i==R.id.state4_1)
                {
                    state[4]=1;
                }
                else if(i==R.id.state4_2)
                {
                    state[4]=2;
                }
                else if(i==R.id.state4_3)
                {
                    state[4]=3;
                }
            }
        });

        rg5.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.state5_0)
                {
                    state[5]=0;
                }
                else if(i==R.id.state5_1)
                {
                    state[5]=1;
                }
                else if(i==R.id.state5_2)
                {
                    state[5]=2;
                }
                else if(i==R.id.state5_3)
                {
                    state[5]=3;
                }
            }
        });

        rg6.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.state6_0)
                {
                    state[6]=0;
                }
                else if(i==R.id.state6_1)
                {
                    state[6]=1;
                }
                else if(i==R.id.state6_2)
                {
                    state[6]=2;
                }
                else if(i==R.id.state6_3)
                {
                    state[6]=3;
                }
            }
        });

        rg7.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.state7_0)
                {
                    state[7]=0;
                }
                else if(i==R.id.state7_1)
                {
                    state[7]=1;
                }
                else if(i==R.id.state7_2)
                {
                    state[7]=2;
                }
                else if(i==R.id.state7_3)
                {
                    state[7]=3;
                }
            }
        });

        rg8.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.state8_0)
                {
                    state[8]=0;
                }
                else if(i==R.id.state8_1)
                {
                    state[8]=1;
                }
            }
        });

        rg9.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.state9_0)
                {
                    state[9]=0;
                }
                else if(i==R.id.state9_1)
                {
                    state[9]=1;
                }
                else if(i==R.id.state9_2)
                {
                    state[9]=2;
                }
                else if(i==R.id.state9_3)
                {
                    state[9]=3;
                }
            }
        });

        TextView text2 = (TextView) findViewById(R.id.text2);
        text2.setText(sampleReceipt.getSampleName());

        findViewById(R.id.save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean isfull = true;

                EditText editText1 = (EditText) findViewById(R.id.edit_text1);
                final String s1 = editText1.getText().toString();
                TextView text2 = (TextView) findViewById(R.id.text2);
                final String s2 = text2.getText().toString();
                EditText editText3 = (EditText) findViewById(R.id.edit_text3);
                final String s3 = editText3.getText().toString();
                EditText editText4 = (EditText) findViewById(R.id.edit_text4);
                final String s4 = editText4.getText().toString();
                EditText editText6 = (EditText) findViewById(R.id.edit_text6);
                final String s6 = editText6.getText().toString();
                EditText editText15 = (EditText) findViewById(R.id.edit_text15);
                final String s15 = editText15.getText().toString();
                EditText editText17 = (EditText) findViewById(R.id.edit_text17);
                final String s17 = editText17.getText().toString();
                EditText editText18 = (EditText) findViewById(R.id.edit_text18);
                final String s18 = editText18.getText().toString();
                EditText editText19 = (EditText) findViewById(R.id.edit_text19);
                final String s19 = editText19.getText().toString();
                EditText editText20 = (EditText) findViewById(R.id.edit_text20);
                final String s20 = editText20.getText().toString();

                if ((s1 == null || s1.equals("")) || (s2 == null || s2.equals(""))|| (s3 == null || s3.equals(""))  || (s4 == null || s4.equals(""))
                        || (s6 == null || s6.equals("")) || (s15 == null || s15.equals(""))|| (s17 == null || s17.equals(""))|| (s18 == null || s18.equals(""))
                        || (s19 == null || s19.equals(""))|| (s20 == null || s20.equals("")))
                    isfull = false;
                if (isfull == true) {
                    //Log.d("select_time:",s9);
                    //Log.d("选择的性别：",Integer.toString(gender));
                    AlertDialog.Builder dialog = new AlertDialog.Builder(SampleReceipt_Modify.this);
                    dialog.setTitle("确定提交吗？");
                    dialog.setMessage("确保信息准确！");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {


                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    postJson(sampleid,s1, s2, s3, s4,s6,s15,s17,s18,s19,s20,state[0],state[1],state[2],state[3],state[4],state[5],state[6],state[7],state[8],state[9]);
                                }
                            }).start();

                            //finish();
                        }
                    });
                    dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(SampleReceipt_Modify.this, "你仍旧可以修改！", Toast.LENGTH_SHORT).show();
                        }
                    });
                    dialog.show();
                } else {
                    Toast.makeText(SampleReceipt_Modify.this, "请全部填满", Toast.LENGTH_SHORT).show();

                }

            }
        });


    }
    private void postJson(Long id,String s1,String s2,String s3,String s4,String s6,String s15,String s17,String s18,String s19,String s20,
                          int st0,int st1,int st2,int st3,int st4,int st5,int st6,int st7,int st8,int st9){

        OkHttpClient okHttpClient=new OkHttpClient();
        //创建一个请求对象
        MediaType JSON=MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody=new FormBody.Builder()
                .add("sampleId",String.valueOf(id))
                .add("applicationUnit",s1)
                .add("sampleName",s2)
                .add("version",s3)
                .add("contractId",s4)
                .add("testType",String.valueOf(st0))
                .add("electronicMedia",s6)
                .add("readMe",String.valueOf(st1))
                .add("application",String.valueOf(st2))
                .add("materialReceipt",String.valueOf(st3))
                .add("functions",String.valueOf(st4))
                .add("confirmations",String.valueOf(st5))
                .add("introduction",String.valueOf(st6))
                .add("guarantee",String.valueOf(st7))
                .add("softwareSample",String.valueOf(st8))
                .add("other",s15)
                .add("softwareType",String.valueOf(st9))
                .add("receiveUnit",s17)
                .add("receiveDate",s18)
                .add("sender",s19)
                .add("receiver",s20)
                .build();
        Log.d("777",st1+""+st2+""+st3+""+st4+""+st5+""+st6+""+st7+"");

        Request request = new Request.Builder()
                .url("http://119.23.38.100:8080/cma/SampleReceipt/modifyOne")//url的地址
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
                        Toast.makeText(SampleReceipt_Modify.this, "上传失败！", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(SampleReceipt_Modify.this, "上传成功！", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

            }
        });
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

package com.example.cma.ui.testing_institution;

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
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.cma.R;
import com.example.cma.model.testing_institution.TestingInstitutionInformation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TestingInstitutionInformation_Modify extends AppCompatActivity {
    Toolbar toolbar;
    TestingInstitutionInformation testingInstitutionInformation;
    int state[] = new int[2];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testing_institution_information__modify);
        Intent intent=getIntent();
        testingInstitutionInformation=(TestingInstitutionInformation) intent.getSerializableExtra("testinfo");

        ScrollView scrollView=(ScrollView)findViewById(R.id.scrollView);

        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //在Activity代码中使用Toolbar对象替换ActionBar
        toolbar = (Toolbar) findViewById(R.id.mToolbar4);
        setSupportActionBar(toolbar);

        //设置Toolbar左边显示一个返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        final EditText editText1=(EditText) findViewById(R.id.text1);
        final EditText editText2=(EditText) findViewById(R.id.text2);
        final EditText editText3=(EditText) findViewById(R.id.text3);
        final EditText editText4=(EditText) findViewById(R.id.text4);
        final EditText editText5=(EditText) findViewById(R.id.text5);
        final EditText editText6=(EditText) findViewById(R.id.text6);
        final EditText editText7=(EditText) findViewById(R.id.text7);
        final EditText editText8=(EditText) findViewById(R.id.text8);
        final EditText editText9=(EditText) findViewById(R.id.text9);
        final EditText editText10=(EditText) findViewById(R.id.text10);
        final EditText editText11=(EditText) findViewById(R.id.text11);
        final EditText editText12=(EditText) findViewById(R.id.text12);
        final EditText editText13=(EditText) findViewById(R.id.text13);
        final EditText editText14=(EditText) findViewById(R.id.text14);
        final EditText editText15=(EditText) findViewById(R.id.text15);
        final EditText editText16=(EditText) findViewById(R.id.text16);
        final EditText editText17=(EditText) findViewById(R.id.text17);
        final EditText editText18=(EditText) findViewById(R.id.text18);
        final EditText editText19=(EditText) findViewById(R.id.text19);
        final EditText editText20=(EditText) findViewById(R.id.text20);
        final EditText editText21=(EditText) findViewById(R.id.text21);
        final EditText editText22=(EditText) findViewById(R.id.text22);
        final EditText editText23=(EditText) findViewById(R.id.text23);
        final EditText editText24=(EditText) findViewById(R.id.text24);
        final EditText editText25=(EditText) findViewById(R.id.text25);
        RadioGroup rg0=(RadioGroup)findViewById(R.id.characteristic_rg);
        RadioGroup rg1=(RadioGroup)findViewById(R.id.legalentity_rg);

        editText1.setText(testingInstitutionInformation.getTestingInstitutionName());
        editText2.setText(testingInstitutionInformation.getTestingInstitutionAddress());
        editText3.setText(testingInstitutionInformation.getPostcode());
        editText4.setText(testingInstitutionInformation.getFax());
        editText5.setText(testingInstitutionInformation.getEmail());
        editText6.setText(testingInstitutionInformation.getTiPeopelInCharge());
        editText7.setText(testingInstitutionInformation.getTiPicPosition());
        editText8.setText(testingInstitutionInformation.getTiPicTelephone());
        editText9.setText(testingInstitutionInformation.getTiPicMobilephone());
        editText10.setText(testingInstitutionInformation.getLiaison());
        editText11.setText(testingInstitutionInformation.getLiaisonPosition());
        editText12.setText(testingInstitutionInformation.getLiaisonTelephone());
        editText13.setText(testingInstitutionInformation.getLiaisonMobilephone());
        editText14.setText(testingInstitutionInformation.getSocialCreditCode());

        editText15.setText(testingInstitutionInformation.getLegalEntityBelongedName());
        editText16.setText(testingInstitutionInformation.getLegalEntityBelongedAddress());
        editText17.setText(testingInstitutionInformation.getLebPeopelInCharge());
        editText18.setText(testingInstitutionInformation.getLebPicPosition());
        editText19.setText(testingInstitutionInformation.getLebPicTelephone());
        editText20.setText(testingInstitutionInformation.getLebSocialCreditCode());

        editText21.setText(testingInstitutionInformation.getCompetentDepartmentName());
        editText22.setText(testingInstitutionInformation.getCompetentDepartmentAddress());
        editText23.setText(testingInstitutionInformation.getCdPeopelInCharge());
        editText24.setText(testingInstitutionInformation.getCdPicPosition());
        editText25.setText(testingInstitutionInformation.getCdPicTelephone());

        if(testingInstitutionInformation.getCharacteristic()==0){
            rg0.check(R.id.state0_0);
        }
        else if(testingInstitutionInformation.getCharacteristic()==1){
            rg0.check(R.id.state0_1);
        }
        else if(testingInstitutionInformation.getCharacteristic()==2){
            rg0.check(R.id.state0_2);
        }
        else if(testingInstitutionInformation.getCharacteristic()==3){
            rg0.check(R.id.state0_3);
        }

        if(testingInstitutionInformation.getLegalEntity()==0){
            rg1.check(R.id.state1_0);
        }
        else if(testingInstitutionInformation.getLegalEntity()==1){
            rg1.check(R.id.state1_1);
        }
        else if(testingInstitutionInformation.getLegalEntity()==2){
            rg1.check(R.id.state1_2);
        }
        else if(testingInstitutionInformation.getLegalEntity()==3){
            rg1.check(R.id.state1_3);
        }
        else if(testingInstitutionInformation.getLegalEntity()==4){
            rg1.check(R.id.state1_4);
        }
        else if(testingInstitutionInformation.getLegalEntity()==5){
            rg1.check(R.id.state1_5);
        }
        else if(testingInstitutionInformation.getLegalEntity()==6){
            rg1.check(R.id.state1_6);
        }
        else if(testingInstitutionInformation.getLegalEntity()==7){
            rg1.check(R.id.state1_7);
        }
        else if(testingInstitutionInformation.getLegalEntity()==8){
            rg1.check(R.id.state1_8);
        }
        else if(testingInstitutionInformation.getLegalEntity()==9){
            rg1.check(R.id.state1_9);
        }

        ShowCursor(editText1);
        ShowCursor(editText2);
        ShowCursor(editText3);
        ShowCursor(editText4);
        ShowCursor(editText5);
        ShowCursor(editText6);
        ShowCursor(editText7);
        ShowCursor(editText8);
        ShowCursor(editText9);
        ShowCursor(editText10);
        ShowCursor(editText11);
        ShowCursor(editText12);
        ShowCursor(editText13);
        ShowCursor(editText14);
        ShowCursor(editText15);
        ShowCursor(editText16);
        ShowCursor(editText17);
        ShowCursor(editText18);
        ShowCursor(editText19);
        ShowCursor(editText20);
        ShowCursor(editText21);
        ShowCursor(editText22);
        ShowCursor(editText23);
        ShowCursor(editText24);
        ShowCursor(editText25);


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
                else if(i==R.id.state0_3)
                {
                    state[0]=3;
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
                else if(i==R.id.state1_4)
                {
                    state[1]=4;
                }
                else if(i==R.id.state1_5)
                {
                    state[1]=5;
                }
                else if(i==R.id.state1_6)
                {
                    state[1]=6;
                }
                else if(i==R.id.state1_7)
                {
                    state[1]=7;
                }
                else if(i==R.id.state1_8)
                {
                    state[1]=8;
                }
                else if(i==R.id.state1_9)
                {
                    state[1]=9;
                }
            }
        });

        findViewById(R.id.save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean isfull = true;

                final String s1=editText1.getText().toString();
                final String s2=editText2.getText().toString();
                final String s3=editText3.getText().toString();
                final String s4=editText4.getText().toString();
                final String s5=editText5.getText().toString();
                final String s6=editText6.getText().toString();
                final String s7=editText7.getText().toString();
                final String s8=editText8.getText().toString();
                final String s9=editText9.getText().toString();
                final String s10=editText10.getText().toString();
                final String s11=editText11.getText().toString();
                final String s12=editText12.getText().toString();
                final String s13=editText13.getText().toString();
                final String s14=editText14.getText().toString();
                final String s15=editText15.getText().toString();
                final String s16=editText16.getText().toString();
                final String s17=editText17.getText().toString();
                final String s18=editText18.getText().toString();
                final String s19=editText19.getText().toString();
                final String s20=editText20.getText().toString();
                final String s21=editText21.getText().toString();
                final String s22=editText22.getText().toString();
                final String s23=editText23.getText().toString();
                final String s24=editText24.getText().toString();
                final String s25=editText25.getText().toString();

                if ((s1 == null || s1.equals("")) || (s2 == null || s2.equals(""))|| (s3 == null || s3.equals(""))  || (s4 == null || s4.equals(""))|| (s5 == null || s5.equals(""))
                        || (s6 == null || s6.equals("")) || (s7 == null || s7.equals(""))|| (s8 == null || s8.equals(""))|| (s9 == null || s9.equals(""))|| (s10 == null || s10.equals(""))
                        || (s11 == null || s11.equals(""))|| (s12 == null || s12.equals(""))|| (s13 == null || s13.equals(""))|| (s14 == null || s14.equals(""))|| (s15 == null || s15.equals(""))
                        || (s16 == null || s16.equals(""))|| (s17 == null || s17.equals(""))|| (s18 == null || s18.equals(""))|| (s19 == null || s19.equals(""))|| (s20 == null || s20.equals(""))
                        || (s21 == null || s21.equals(""))|| (s22 == null || s22.equals(""))|| (s23 == null || s23.equals(""))|| (s24 == null || s24.equals(""))|| (s25 == null || s25.equals("")))
                    isfull = false;
                if (isfull == true) {
                    //Log.d("select_time:",s9);
                    //Log.d("选择的性别：",Integer.toString(gender));
                    AlertDialog.Builder dialog = new AlertDialog.Builder(TestingInstitutionInformation_Modify.this);
                    dialog.setTitle("确定提交吗？");
                    dialog.setMessage("确保信息准确！");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {


                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    postJson(s1, s2, s3, s4,s5,s6,s7,s8,s9,s10,s11,s12,s13,s14,s15,s16,s17,s18,s19,s20,s21,s22,s23,s24,s25,state[0],state[1]);
                                }
                            }).start();

                            finish();
                        }
                    });
                    dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(TestingInstitutionInformation_Modify.this, "你仍旧可以修改！", Toast.LENGTH_SHORT).show();
                        }
                    });
                    dialog.show();
                } else {
                    Toast.makeText(TestingInstitutionInformation_Modify.this, "请全部填满", Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

    private void postJson(String s1,String s2,String s3,String s4,String s5,String s6,String s7,String s8,String s9,String s10,String s11,String s12,String s13,String s14,String s15,
                          String s16,String s17,String s18,String s19,String s20, String s21,String s22,String s23,String s24,String s25,int st0,int st1){

        OkHttpClient okHttpClient=new OkHttpClient();
        //创建一个请求对象
        MediaType JSON=MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody=new FormBody.Builder()
                .add("testingInstitutionName",s1)
                .add("testingInstitutionAddress",s2)
                .add("postcode",s3)
                .add("fax",s4)
                .add("email",s5)
                .add("tiPeopelInCharge",s6)
                .add("tiPicPosition",s7)
                .add("tiPicTelephone",s8)
                .add("tiPicMobilephone",s9)
                .add("liaison",s10)
                .add("liaisonPosition",s11)
                .add("liaisonTelephone",s12)
                .add("liaisonMobilephone",s13)
                .add("socialCreditCode",s14)

                .add("legalEntityBelongedName",s15)
                .add("legalEntityBelongedAddress",s16)
                .add("lebPeopelInCharge",s17)
                .add("lebPicPosition",s18)
                .add("lebPicTelephone",s19)
                .add("lebSocialCreditCode",s20)
                .add("competentDepartmentName",s21)
                .add("competentDepartmentAddress",s22)
                .add("cdPeopelInCharge",s23)
                .add("cdPicPosition",s24)
                .add("cdPicTelephone",s25)
                .add("characteristic",String.valueOf(st0))
                .add("legalEntity",String.valueOf(st1))
                .build();
        //Log.d("777",st1+""+st2+""+st3+""+st4+""+st5+""+st6+""+st7+"");

        Request request = new Request.Builder()
                .url("http://119.23.38.100:8080/cma/TestingInstitutionInformation/modify")//url的地址
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
                        Toast.makeText(TestingInstitutionInformation_Modify.this, "上传失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                JSONObject object = new JSONObject();
                int code = 0;
                String msg = "";
                try {
                    object = new JSONObject(result);
                    code = object.getInt("code");
                    msg = object.getString("msg");
                }catch (JSONException e){
                    e.printStackTrace();
                }
                Log.d("androixx.cn", result);
                //Log.d("androixx.cn", "成功！！！！");
                if(code==200) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(TestingInstitutionInformation_Modify.this, "修改成功！", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(TestingInstitutionInformation_Modify.this, "数据填写错误，请按格式填写！", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                finish();
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
                onBackConfirm();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackConfirm(){
        AlertDialog.Builder dialog=new AlertDialog.Builder(TestingInstitutionInformation_Modify.this);
        dialog.setTitle("内容尚未保存");
        dialog.setMessage("是否退出？");
        dialog.setCancelable(true);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        dialog.show();
    }
}

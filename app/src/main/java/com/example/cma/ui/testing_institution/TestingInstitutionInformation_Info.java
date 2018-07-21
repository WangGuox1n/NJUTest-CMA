package com.example.cma.ui.testing_institution;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.cma.R;
import com.example.cma.model.testing_institution.TestingInstitutionInformation;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TestingInstitutionInformation_Info extends AppCompatActivity {
    Toolbar toolbar;
    TestingInstitutionInformation testingInstitutionInformation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testing_institution_information__info);

        init();

        ScrollView scrollView=(ScrollView)findViewById(R.id.scrollView);

        //在Activity代码中使用Toolbar对象替换ActionBar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //设置Toolbar左边显示一个返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //对 编辑 按钮监听
        Button button2=(Button)findViewById(R.id.edit_button);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(TestingInstitutionInformation_Info.this,TestingInstitutionInformation_Modify.class);
                intent.putExtra("testinfo",testingInstitutionInformation);
                startActivity(intent);
            }
        });
    }

    public String trans1(int state){
        String s="";
        if(state==0) {
            s= "固定";
        }
        else if(state==1) {
            s= "临时";
        }
        else if(state==2) {
            s= "可移动";
        }
        else if(state==3) {
            s= "多场所";
        }
        return s;
    }
    public String trans2(int state){
        String s="";
        if(state==0) {
            s= "独立法人检测机构-社团法人";
        }
        else if(state==1) {
            s= "独立法人检测机构-事业法人";
        }
        else if(state==2) {
            s= "独立法人检测机构-企业法人";
        }
        else if(state==3) {
            s= "独立法人检测机构-机关法人";
        }
        else if(state==4) {
            s= "独立法人检测机构-其他";
        }
        else if(state==5) {
            s= "检测检验机构所属法人-社团法人";
        }
        else if(state==6) {
            s= "检测检验机构所属法人-事业法人";
        }
        else if(state==7) {
            s= "检测检验机构所属法人-企业法人";
        }
        else if(state==8) {
            s= "检测检验机构所属法人-机关法人";
        }
        else if(state==9) {
            s= "检测检验机构所属法人-其他";
        }
        return s;
    }

    public void initView(){
        TextView textView1=(TextView) findViewById(R.id.text1);
        TextView textView2=(TextView) findViewById(R.id.text2);
        TextView textView3=(TextView) findViewById(R.id.text3);
        TextView textView4=(TextView) findViewById(R.id.text4);
        TextView textView5=(TextView) findViewById(R.id.text5);
        TextView textView6=(TextView) findViewById(R.id.text6);
        TextView textView7=(TextView) findViewById(R.id.text7);
        TextView textView8=(TextView) findViewById(R.id.text8);
        TextView textView9=(TextView) findViewById(R.id.text9);
        TextView textView10=(TextView) findViewById(R.id.text10);
        TextView textView11=(TextView) findViewById(R.id.text11);
        TextView textView12=(TextView) findViewById(R.id.text12);
        TextView textView13=(TextView) findViewById(R.id.text13);
        TextView textView14=(TextView) findViewById(R.id.text14);
        TextView textView15=(TextView) findViewById(R.id.text15);
        TextView textView16=(TextView) findViewById(R.id.text16);
        TextView textView17=(TextView) findViewById(R.id.text17);
        TextView textView18=(TextView) findViewById(R.id.text18);
        TextView textView19=(TextView) findViewById(R.id.text19);
        TextView textView20=(TextView) findViewById(R.id.text20);
        TextView textView21=(TextView) findViewById(R.id.text21);;
        TextView textView22=(TextView) findViewById(R.id.text22);
        TextView textView23=(TextView) findViewById(R.id.text23);
        TextView textView24=(TextView) findViewById(R.id.text24);
        TextView textView25=(TextView) findViewById(R.id.text25);
        TextView textView26=(TextView) findViewById(R.id.text26);
        TextView textView27=(TextView) findViewById(R.id.text27);

        textView1.setText(testingInstitutionInformation.getTestingInstitutionName());
        textView2.setText(testingInstitutionInformation.getTestingInstitutionAddress());
        textView3.setText(testingInstitutionInformation.getPostcode());
        textView4.setText(testingInstitutionInformation.getFax());
        textView5.setText(testingInstitutionInformation.getEmail());
        textView6.setText(testingInstitutionInformation.getTiPeopelInCharge());
        textView7.setText(testingInstitutionInformation.getTiPicPosition());
        textView8.setText(testingInstitutionInformation.getTiPicTelephone());
        textView9.setText(testingInstitutionInformation.getTiPicMobilephone());
        textView10.setText(testingInstitutionInformation.getLiaison());
        textView11.setText(testingInstitutionInformation.getLiaisonPosition());
        textView12.setText(testingInstitutionInformation.getLiaisonTelephone());
        textView13.setText(testingInstitutionInformation.getLiaisonMobilephone());
        textView14.setText(testingInstitutionInformation.getSocialCreditCode());
        textView15.setText(testingInstitutionInformation.getLegalEntityBelongedName());
        textView16.setText(testingInstitutionInformation.getLegalEntityBelongedAddress());
        textView17.setText(testingInstitutionInformation.getLebPeopelInCharge());
        textView18.setText(testingInstitutionInformation.getLebPicPosition());
        textView19.setText(testingInstitutionInformation.getLebPicTelephone());
        textView20.setText(testingInstitutionInformation.getLebSocialCreditCode());
        textView21.setText(testingInstitutionInformation.getCompetentDepartmentName());
        textView22.setText(testingInstitutionInformation.getCompetentDepartmentAddress());
        textView23.setText(testingInstitutionInformation.getCdPeopelInCharge());
        textView24.setText(testingInstitutionInformation.getCdPicPosition());
        textView25.setText(testingInstitutionInformation.getCdPicTelephone());
        textView26.setText(trans1(testingInstitutionInformation.getCharacteristic()));
        textView27.setText(trans2(testingInstitutionInformation.getLegalEntity()));

    }

    public void init(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String temp="http://119.23.38.100:8080/cma/TestingInstitutionInformation/get";
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            // 指定访问的服务器地址，后续在这里修改
                            .url(temp)
                            .build();
                    Response response = client.newCall(request).execute();;
                    String responseData = response.body().string();
                    //Log.d("请求回复：",responseData);
                    parseJSONWithGSON2(responseData);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // 在这里进行UI操作，将结果显示到界面上
                            initView();
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
    private void  parseJSONWithGSON2(String responseData){
        // JSONArray array=new JSONArray();
        try{
            //Log.d("responseData:",responseData);
            JSONObject object=new JSONObject(responseData);
            String array=object.getString("data");
            Log.d("请求array：",array);
            if(array.equals("null"))
            {
                Log.d("null","array null");

            }else
            {
                Gson gson=new Gson();
                testingInstitutionInformation=gson.fromJson(array,new TypeToken<TestingInstitutionInformation>(){}.getType());
            }

        }catch (Exception e){
            e.printStackTrace();
        }


    }
    @Override
    protected void onResume() {
        super.onResume();
        init();
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

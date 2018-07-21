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
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cma.R;
import com.example.cma.model.sample_management.SampleReceipt;
import com.example.cma.model.sample_management.SampleReceive;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SampleReceipt_Info extends AppCompatActivity {
    Toolbar toolbar;
    SampleReceipt sampleReceipt;
    SampleReceipt sampleReceipttemp;
    SampleReceive sampleReceive;
    Long id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_receipt__info);
        Intent intent=getIntent();
        sampleReceive=(SampleReceive) intent.getSerializableExtra("samplereceivetemp");
        id=sampleReceive.getSampleId();
        init(id);

        ScrollView scrollView=(ScrollView)findViewById(R.id.scrollView);
        //在Activity代码中使用Toolbar对象替换ActionBar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //设置Toolbar左边显示一个返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Button button=(Button)findViewById(R.id.delete_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog=new AlertDialog.Builder(SampleReceipt_Info.this);
                dialog.setTitle("确定删除此样品接收单吗？");
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
                                postDelete(sampleReceive.getSampleId());
                            }
                        }).start();


                        finish();
                    }
                });
                dialog.show();
            }
        });

        //对 编辑 按钮监听
        toolbar.findViewById(R.id.edit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SampleReceipt_Info.this,SampleReceipt_Modify.class);
                intent.putExtra("samplereceipt",sampleReceipt);
                startActivity(intent);
            }
        });
    }

    public String trans1(int state){
        String s="";
        if(state==0) {
            s= "登记检测";
        }
        else if(state==1) {
            s= "确认检测";
        }
        else if(state==2) {
            s= "验收检测";
        }
        return s;
    }
    public String trans2(int state){
        String s="";
        if(state==0) {
            s= "没有该材料";
        }
        else if(state==1) {
            s= "只有电子文档";
        }
        else if(state==2) {
            s= "只有书面文档";
        }
        else if(state==3) {
            s= "两种文档都有";
        }
        return s;
    }
    public String trans3(int state){
        String s="";
        if(state==0) {
            s= "无";
        }
        else if(state==1) {
            s= "有电子档";
        }

        return s;
    }
    public String trans4(int state){
        String s="";
        if(state==0) {
            s= "系统软件";
        }
        else if(state==1) {
            s= "支持软件";
        }
        else if(state==2) {
            s= "应用软件";
        }
        else if(state==3) {
            s= "其他软件";
        }
        return s;
    }


    public void initView(){
        TextView textView1=(TextView) findViewById(R.id.text1);
        textView1.setText(sampleReceipt.getApplicationUnit());

        TextView textView2=(TextView) findViewById(R.id.text2);
        textView2.setText(sampleReceipt.getSampleName());

        TextView textView3=(TextView) findViewById(R.id.text3);
        textView3.setText(sampleReceipt.getVersion());

        TextView textView4=(TextView) findViewById(R.id.text4);
        textView4.setText(sampleReceipt.getContractId());

        TextView textView5=(TextView) findViewById(R.id.text5);
        textView5.setText(trans1(sampleReceipt.getTestType()));

        TextView textView6=(TextView) findViewById(R.id.text6);
        textView6.setText(sampleReceipt.getElectronicMedia());

        TextView textView7=(TextView) findViewById(R.id.text7);
        textView7.setText(trans2(sampleReceipt.getReadMe()));

        TextView textView8=(TextView) findViewById(R.id.text8);
        textView8.setText(trans2(sampleReceipt.getApplication()));

        TextView textView9=(TextView) findViewById(R.id.text9);
        textView9.setText(trans2(sampleReceipt.getMaterialReceipt()));

        TextView textView10=(TextView) findViewById(R.id.text10);
        textView10.setText(trans2(sampleReceipt.getFunctions()));

        TextView textView11=(TextView) findViewById(R.id.text11);
        textView11.setText(trans2(sampleReceipt.getConfirmations()));

        TextView textView12=(TextView) findViewById(R.id.text12);
        textView12.setText(trans2(sampleReceipt.getIntroduction()));

        TextView textView13=(TextView) findViewById(R.id.text13);
        textView13.setText(trans2(sampleReceipt.getGuarantee()));

        TextView textView14=(TextView) findViewById(R.id.text14);
        textView14.setText(trans3(sampleReceipt.getSoftwareSample()));

        TextView textView15=(TextView) findViewById(R.id.text15);
        textView15.setText(sampleReceipt.getOther());

        TextView textView16=(TextView) findViewById(R.id.text16);
        textView16.setText(trans4(sampleReceipt.getSoftwareType()));

        TextView textView17=(TextView) findViewById(R.id.text17);
        textView17.setText(sampleReceipt.getReceiveUnit());

        TextView textView18=(TextView) findViewById(R.id.text18);
        textView18.setText(sampleReceipt.getReceiveDate());

        TextView textView19=(TextView) findViewById(R.id.text19);
        textView19.setText(sampleReceipt.getSender());

        TextView textView20=(TextView) findViewById(R.id.text20);
        textView20.setText(sampleReceipt.getReceiver());
    }

    private void postDelete(long id){
        OkHttpClient okHttpClient=new OkHttpClient();
        RequestBody requestBody=new FormBody.Builder()
                .add("sampleId",Long.toString(id))
                .build();
        Request request = new Request.Builder()
                .url("http://119.23.38.100:8080/cma/SampleReceipt/deleteOne")//url的地址
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SampleReceipt_Info.this, "删除失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.d("androixx.cn", result);

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        Toast.makeText(SampleReceipt_Info.this, "删除成功！", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    public void init(final long id){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String temp="http://119.23.38.100:8080/cma/SampleReceipt/getOne?sampleId="+id;
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
                sampleReceipt=gson.fromJson(array,new TypeToken<SampleReceipt>(){}.getType());
                Log.d("sampleIO_name:",sampleReceipt.getSampleName());
            }

        }catch (Exception e){
            e.printStackTrace();
        }


    }
    @Override
    protected void onResume() {
        super.onResume();
        init(id);
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

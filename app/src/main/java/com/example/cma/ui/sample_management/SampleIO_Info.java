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
import com.example.cma.model.sample_management.SampleIO;
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

public class SampleIO_Info extends AppCompatActivity {
    Toolbar toolbar;
    SampleIO sampleIO;
    SampleIO sampleIOtemp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_io__info);
        Intent intent=getIntent();
        sampleIOtemp=(SampleIO) intent.getSerializableExtra("sampleIO");
        init(sampleIOtemp.getSampleIoId());

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
                AlertDialog.Builder dialog=new AlertDialog.Builder(SampleIO_Info.this);
                dialog.setTitle("确定删除此样品进出登记表吗？");
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
                                postDelete(sampleIOtemp.getSampleIoId());
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
                Intent intent=new Intent(SampleIO_Info.this,SampleIO_Modify.class);
                intent.putExtra("sampleIO",sampleIO);
                startActivity(intent);
            }
        });
    }

    public void initView(){
        TextView textView1=(TextView) findViewById(R.id.text_view1_1);
        textView1.setText(sampleIO.getSampleNumber());

        TextView textView2=(TextView) findViewById(R.id.text_view2_1);
        textView2.setText(sampleIO.getSampleName());

        TextView textView3=(TextView) findViewById(R.id.text_view3_1);
        textView3.setText(String.valueOf(sampleIO.getSampleAmount()));

        TextView textView4=(TextView) findViewById(R.id.text_view4_1);
        textView4.setText(sampleIO.StateToString());

        TextView textView5=(TextView) findViewById(R.id.text_view5_1);
        textView5.setText(sampleIO.getSender());

        TextView textView6=(TextView) findViewById(R.id.text_view6_1);
        textView6.setText(sampleIO.getReceiver());

        TextView textView7=(TextView) findViewById(R.id.text_view7_1);
        textView7.setText(sampleIO.getSendDate());

        TextView textView8=(TextView) findViewById(R.id.text_view8_1);
        textView8.setText(sampleIO.getObtainer());

        TextView textView9=(TextView) findViewById(R.id.text_view9_1);
        textView9.setText(sampleIO.getObtainDate());

        TextView textView10=(TextView) findViewById(R.id.text_view10_1);
        textView10.setText(sampleIO.getNote());
    }

    private void postDelete(long id){
        OkHttpClient okHttpClient=new OkHttpClient();
        RequestBody requestBody=new FormBody.Builder()
                .add("sampleIoId",Long.toString(id))
                .build();
        Request request = new Request.Builder()
                .url("http://119.23.38.100:8080/cma/SampleIo/deleteOne")//url的地址
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SampleIO_Info.this, "删除失败！", Toast.LENGTH_SHORT).show();
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

                        Toast.makeText(SampleIO_Info.this, "删除成功！", Toast.LENGTH_SHORT).show();
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
                    String temp="http://119.23.38.100:8080/cma/SampleIo/getOne?sampleIoId="+id;
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
                sampleIO=gson.fromJson(array,new TypeToken<SampleIO>(){}.getType());
                Log.d("sampleIO_name:",sampleIO.getSampleName());
            }

        }catch (Exception e){
            e.printStackTrace();
        }


    }
    @Override
    protected void onResume() {
        super.onResume();
        init(sampleIOtemp.getSampleIoId());
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

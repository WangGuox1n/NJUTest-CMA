package com.example.cma.ui.capacity_verification;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cma.R;
import com.example.cma.model.capacity_verification.CapacityVerificationPlan;
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

public class CapacityVerificationPlan_Info extends AppCompatActivity {

    CapacityVerificationPlan capacityVerificationPlan;
    Toolbar toolbar;
    EditText nameText;
    EditText organizerText;
    EditText yearText;
    EditText noteText;
    Button editButton;
    Button deleteButton;
    TextView projectSee;
    LinearLayout downloadLayout;//表示存在分析报告，可以下载和更新
    TextView downloadAnalysis;
    LinearLayout uploadLayout;//表示不存在分析报告，需要上传
    TextView uploadAnalysis;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.capacity_verification_plan__info);

        toolbar = (Toolbar) findViewById(R.id.mToolbar);
        setSupportActionBar(toolbar);
        //设置Toolbar左边显示一个返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent=getIntent();
        capacityVerificationPlan=(CapacityVerificationPlan)intent.getSerializableExtra("chuandi");

        nameText=(EditText)findViewById(R.id.name_text);
        organizerText=(EditText)findViewById(R.id.organizer_text);
        yearText=(EditText)findViewById(R.id.year_text);
        noteText=(EditText)findViewById(R.id.note_text);
        editButton=(Button)findViewById(R.id.edit_button);
        deleteButton=(Button)findViewById(R.id.delete_button);
        projectSee=(TextView)findViewById(R.id.project_see);

        downloadLayout=(LinearLayout)findViewById(R.id.download_layout);
        downloadAnalysis=(TextView)findViewById(R.id.download_analysis);
        uploadLayout=(LinearLayout)findViewById(R.id.upload_layout);
        uploadAnalysis=(TextView)findViewById(R.id.upload_analysis);


        nameText.setKeyListener(null);
        organizerText.setKeyListener(null);
        yearText.setKeyListener(null);
        noteText.setKeyListener(null);

        initViews();

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CapacityVerificationPlan_Info.this,CapacityVericationPlan_Modify.class);
                intent.putExtra("chuandi",capacityVerificationPlan);
                startActivity(intent);
            }
        });

        projectSee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CapacityVerificationPlan_Info.this,CapacityVerificationProject_Main.class);
                intent.putExtra("chuandi",capacityVerificationPlan);
                startActivity(intent);
            }
        });

        uploadAnalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CapacityVerificationPlan_Info.this,CapacityVericationFile_Add.class);
                intent.putExtra("chuandi",capacityVerificationPlan);
                startActivity(intent);
            }
        });

        downloadAnalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CapacityVerificationPlan_Info.this,CapacityVericationFile_Info.class);
                intent.putExtra("chuandi",capacityVerificationPlan);
                startActivity(intent);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postDelete();
            }
        });
    }

    public void postDelete(){
        OkHttpClient okHttpClient=new OkHttpClient();
        RequestBody requestBody=new FormBody.Builder().add("id",Long.toString(capacityVerificationPlan.getPlanId())).build();
        Request request = new Request.Builder()
                .url("http://119.23.38.100:8080/cma/CapacityVerification/deleteOne")//url的地址
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CapacityVerificationPlan_Info.this, "删除失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            //Log.d("??response:",response.body().string());
                           // Log.d("???response:",response.toString());
                            String s=response.body().string();
                            String []a=s.split(":");
                            String []aa=a[2].split(",");
                            if(aa[0].length()>10)
                                Toast.makeText(CapacityVerificationPlan_Info.this,"该计划存在执行的项目，不可删除！",Toast.LENGTH_SHORT).show();
                            else
                            {
                                Toast.makeText(CapacityVerificationPlan_Info.this, "删除成功！", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }catch (Exception e)
                        {

                        };
                    }
                });

            }
        });


    }


    public void initData(final long id){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String temp="http://119.23.38.100:8080/cma/CapacityVerification/getOne?id="+id;
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            // 指定访问的服务器地址，后续在这里修改
                            .url(temp)
                            .build();
                    Response response = client.newCall(request).execute();;
                    String responseData = response.body().string();
                    parseJSONWithGSON2(responseData);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // 在这里进行UI操作，将结果显示到界面上
                            initViews();
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private void  parseJSONWithGSON2(String responseData){
        try{
            JSONObject object=new JSONObject(responseData);
            String array=object.getString("data");
            Log.d("请求array：",array);
            if(array.equals("null"))
            {

            }else
            {
                Gson gson=new Gson();
                capacityVerificationPlan=gson.fromJson(array,new TypeToken<CapacityVerificationPlan>(){}.getType());
            }

        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public void  initViews() {
       nameText.setText(capacityVerificationPlan.getName());
       organizerText.setText(capacityVerificationPlan.getOrganizer());
       yearText.setText(capacityVerificationPlan.getYear());
       noteText.setText(capacityVerificationPlan.getNote());
       downloadLayout.setVisibility(View.VISIBLE);
       uploadLayout.setVisibility(View.VISIBLE);
       if(capacityVerificationPlan.getAnalysis()==null)
           downloadLayout.setVisibility(View.GONE);
       else
           uploadLayout.setVisibility(View.GONE);
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


    @Override
    protected void onResume() {
        super.onResume();
        initData(capacityVerificationPlan.getPlanId());
    }
}

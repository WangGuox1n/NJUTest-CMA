package com.example.cma.ui.quality_system;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cma.R;
import com.example.cma.model.quality_system.QualityManual;
import com.example.cma.model.staff_management.Result;
import com.example.cma.utils.DownloadUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class QualityManualCurrent_Main extends AppCompatActivity implements View.OnClickListener{
    Button button;
    private QualityManual temp;
    Toolbar toolbar;
    TextView attachment;
    String function;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quality_manual_current__main);
        Intent intent=getIntent();
        function=intent.getStringExtra("function");

        init();
        //在Activity代码中使用Toolbar对象替换ActionBar
        toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        button=(Button)findViewById(R.id.modify_button);
        button.setOnClickListener(this);
        attachment=(TextView)findViewById(R.id.file_text);
        attachment.setOnClickListener(this);
        //设置Toolbar左边显示一个返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    public void init(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String temp="http://119.23.38.100:8080/cma/"+function+"/getCurrent";
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            // 指定访问的服务器地址，后续在这里修改
                            .url(temp)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.d("!!!!!!",responseData);
                    parseJSONWithGSON(responseData);
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
    private void  parseJSONWithGSON(String responseData){
        // JSONArray array=new JSONArray();
        try{
            Gson gson = new Gson();
            Result<QualityManual> userListResult = gson.fromJson(responseData,new TypeToken<Result<QualityManual>>(){}.getType());
            //Result<Array<AnnualPlan>> userListResult = gson.fromJson(jsonData,new TypeToken<Result<List<AnnualPlan>>>(){}.getType());
            temp= userListResult.data;

            //判断是否为空

        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void initView()
    {
        if(temp==null)
        {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(QualityManualCurrent_Main.this, "没有线性！", Toast.LENGTH_SHORT).show();
                }
            });
            Log.d("为空！！！！","!!!!!");
           return ;
        }

        TextView t1=(TextView)findViewById(R.id.id_text);
        TextView t2=(TextView)findViewById(R.id.name_text);
        TextView t4=(TextView)findViewById(R.id.time_text);
        TextView t5=(TextView)findViewById(R.id.person_text);
        TextView t6=(TextView)findViewById(R.id.content_text);
        t1.setText(temp.getFileId());
        t2.setText(temp.getFileName());
        t4.setText(temp.getModifyTime());
        t5.setText(temp.getModifier());
        t6.setText(temp.getModifyContent());
    }
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.modify_button:
                Intent intent = new Intent(QualityManualCurrent_Main.this, QualityManual_Add.class);
                intent.putExtra("version","1");
                intent.putExtra("function",function);
                intent.putExtra("state","0");
                startActivity(intent);
                break;
            case R.id.file_text:
                String filename=temp.getFile();
                String path="http://119.23.38.100:8080/cma/"+function+"/getCurrentFile";
                String fileUrl = Environment.getExternalStorageDirectory().getAbsolutePath()+"/CMA/质量体系";
                final ProgressDialog progressDialog=new ProgressDialog(QualityManualCurrent_Main.this);
                progressDialog.setTitle("正在下载中");
                progressDialog.setMessage("请稍等....");
                progressDialog.setCancelable(true);
                progressDialog.show();
                DownloadUtil.getInstance().download(path,fileUrl,filename,new DownloadUtil.OnDownloadListener() {

                    @Override
                    public void onDownloadSuccess(String path) {
                        progressDialog.dismiss();
                        Toast.makeText(QualityManualCurrent_Main.this, "下载成功！", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onDownloading(int progress) {

                    }

                    @Override
                    public void onDownloadFailed() {
                        Toast.makeText(QualityManualCurrent_Main.this, "下载失败！", Toast.LENGTH_SHORT).show();

                    }
                });
                break;


            default:
                break;
        }
    }

    public void postDelete()
    {
        String id=String.valueOf(temp.getId());
        OkHttpClient okHttpClient=new OkHttpClient();
        RequestBody requestBody1=new FormBody.Builder()
                .add("id",id)
                .build();
        Request request = new Request.Builder()
                .url("http://119.23.38.100:8080/cma/"+function+"/delete")//url的地址
                .post(requestBody1)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(QualityManualCurrent_Main.this, "删除失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(QualityManualCurrent_Main.this, "删除成功！", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }
}

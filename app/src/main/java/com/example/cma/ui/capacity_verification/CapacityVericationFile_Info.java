package com.example.cma.ui.capacity_verification;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cma.R;
import com.example.cma.model.capacity_verification.CapacityVerificationPlan;
import com.example.cma.model.internal_audit.DownloadUtil;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CapacityVericationFile_Info extends AppCompatActivity {


    Toolbar toolbar;
    CapacityVerificationPlan capacityVerificationPlan;
    TextView downLoad;
    TextView deleteFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.capacity_verication_file__info);
        toolbar = (Toolbar) findViewById(R.id.mToolbar);
        setSupportActionBar(toolbar);

        Intent intent=getIntent();
        capacityVerificationPlan=(CapacityVerificationPlan) intent.getSerializableExtra("chuandi");

        //设置Toolbar左边显示一个返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        downLoad=(TextView)findViewById(R.id.download);
        downLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog=new AlertDialog.Builder(CapacityVericationFile_Info.this);
                dialog.setTitle("确定下载该文件吗？");
                dialog.setCancelable(false);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String url="http://119.23.38.100:8080/cma/CapacityVerification/downloadAnalysis?id="+capacityVerificationPlan.getPlanId();
                        downFile(url,capacityVerificationPlan.getAnalysis());
                    }
                });
                dialog.setNegativeButton("取消", null);
                dialog.show();
            }
        });

        deleteFile=(TextView)findViewById(R.id.delete);
        deleteFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog=new AlertDialog.Builder(CapacityVericationFile_Info.this);
                dialog.setTitle("确定删除该文件吗？");
                dialog.setCancelable(false);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        postDelete(capacityVerificationPlan.getPlanId());
                        finish();
                    }
                });
                dialog.setNegativeButton("取消", null);
                dialog.show();
            }
        });



    }

    public void postDelete(long fileId){
        OkHttpClient okHttpClient=new OkHttpClient();
        RequestBody requestBody=new FormBody.Builder().add("id",Long.toString(fileId)).build();
        Request request = new Request.Builder()
                .url("http://119.23.38.100:8080/cma/CapacityVerification/deleteAnalysis")//url的地址
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CapacityVericationFile_Info.this, "删除失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("???:",response.toString());
                        Toast.makeText(CapacityVericationFile_Info.this, "删除成功！", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }

    public void downFile(final String url,final String name) {
        final ProgressDialog progressDialog = new ProgressDialog(CapacityVericationFile_Info.this);
        progressDialog.setTitle("正在下载中");//2.设置标题
        progressDialog.setMessage("请稍等......");//3.设置显示内容
        progressDialog.setCancelable(true);//4.设置可否用back键关闭对话框
        progressDialog.show();//5.将ProgessDialog显示出来

        final DownloadUtil downloadUtil= DownloadUtil.get();

        String path= Environment.getExternalStorageDirectory().getAbsolutePath()+"/CMA/能力验证";
        downloadUtil.download(url, path, name, new DownloadUtil.OnDownloadListener() {
            @Override
            public void onDownloadSuccess(File file) {
                progressDialog.dismiss();
                Looper.prepare();
                Toast.makeText(CapacityVericationFile_Info.this,"下载成功！",Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            @Override
            public void onDownloading(int progress) {
            }

            @Override
            public void onDownloadFailed(Exception e) {
                Looper.prepare();
                Toast.makeText(CapacityVericationFile_Info.this,"下载失败！",Toast.LENGTH_SHORT).show();
                Looper.loop();
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


    @Override
    protected void onResume() {
        super.onResume();
    }
}

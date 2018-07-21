package com.example.cma.ui.internal_audit;

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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cma.R;
import com.example.cma.model.internal_audit.DownloadUtil;
import com.example.cma.model.internal_audit.InternalAuditDocument;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class InternalAuditDocument_Info extends AppCompatActivity {


    InternalAuditDocument internalAuditDocument;
    Toolbar toolbar;
    TextView filename;
    TextView year;
    TextView downLoad;
    Button modifyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.internal_audit_document__info);

        toolbar = (Toolbar) findViewById(R.id.mToolbar);
        setSupportActionBar(toolbar);

        //设置Toolbar左边显示一个返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent=getIntent();
        internalAuditDocument=(InternalAuditDocument) intent.getSerializableExtra("chuandi");
        filename=(TextView)findViewById(R.id.textname);
        year=(TextView)findViewById(R.id.year);
        filename.setText(internalAuditDocument.getFileName());
        year.setText(Long.toString(internalAuditDocument.getYear())+"年");
        modifyButton=(Button)findViewById(R.id.modify_button);
        downLoad=(TextView)findViewById(R.id.download);

        downLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog=new AlertDialog.Builder(InternalAuditDocument_Info.this);
                dialog.setTitle("确定下载该文件吗？");
                dialog.setCancelable(false);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String url="http://119.23.38.100:8080/cma/InternalAuditManagement/downloadFile?fileId="+internalAuditDocument.getId();
                        downFile(url,internalAuditDocument.getFile());
                    }
                });
                dialog.setNegativeButton("取消", null);
                dialog.show();
            }
        });

        modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(InternalAuditDocument_Info.this,InternalAuditDocument_Modify.class);
                intent.putExtra("chuandi",internalAuditDocument);
                startActivity(intent);
            }
        });



    }


    //这里是获取json数据
    private void sendRuquestWithOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String temp="http://119.23.38.100:8080/cma/InternalAuditManagement/getAllFile?year="+internalAuditDocument.getYear();
                    Log.d("点击获取的", "here is json11");
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            // 指定访问的服务器地址，后续在这里修改
                            .url(temp)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    parseJSONWithGSON(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void parseJSONWithGSON(String jsonData) {
        System.out.print(jsonData);
        String[] strings=jsonData.split("\\[");
        String[] strings1=strings[1].split("\\]");
        Log.d("sss:",strings1[0]);
        String string="["+strings1[0]+"]";

        Gson gson = new Gson();

        List<InternalAuditDocument> dangAns = new ArrayList<InternalAuditDocument>();
        dangAns = gson.fromJson(string, new TypeToken<List<InternalAuditDocument>>(){}.getType());
        for(int i=0;i<dangAns.size();i++)
        {
            if(dangAns.get(i).getId()==internalAuditDocument.getId())
            {
                internalAuditDocument=dangAns.get(i);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        filename.setText(internalAuditDocument.getFileName());
                    }
                });
                break;
            }
        }
    }



    public void downFile(final String url,final String name) {
        final ProgressDialog progressDialog = new ProgressDialog(InternalAuditDocument_Info.this);
        /*
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("正在下载");
        progressDialog.setMessage("请稍后...");
       // progressDialog.setProgress(0);
        progressDialog.setMax(100);
        progressDialog.setCancelable(false);
        progressDialog.show();*/

        progressDialog.setTitle("正在下载中");//2.设置标题
        progressDialog.setMessage("请稍等......");//3.设置显示内容
        progressDialog.setCancelable(true);//4.设置可否用back键关闭对话框
        progressDialog.show();//5.将ProgessDialog显示出来

        final DownloadUtil downloadUtil= DownloadUtil.get();

        Log.d("????::", Environment.getExternalStorageDirectory().getAbsolutePath());
        String path= Environment.getExternalStorageDirectory().getAbsolutePath()+"/CMA/内审";
        downloadUtil.download(url, path, name, new DownloadUtil.OnDownloadListener() {
            @Override
            public void onDownloadSuccess(File file) {
                progressDialog.dismiss();
                Looper.prepare();
                Toast.makeText(InternalAuditDocument_Info.this,"下载成功！",Toast.LENGTH_SHORT).show();
                Looper.loop();


            }

            @Override
            public void onDownloading(int progress) {
                //Looper.prepare();
                //progressDialog.setProgress(progress);
                // Looper.loop();
            }

            @Override
            public void onDownloadFailed(Exception e) {
                Looper.prepare();
                Toast.makeText(InternalAuditDocument_Info.this,"下载失败！",Toast.LENGTH_SHORT).show();
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
        sendRuquestWithOkHttp();
    }
}

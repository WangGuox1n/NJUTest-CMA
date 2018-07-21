package com.example.cma.ui.standard_management;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cma.R;
import com.example.cma.model.internal_audit.DownloadUtil;
import com.example.cma.model.standard_management.StandardManagement;
import com.example.cma.model.standard_management.UploadUtil;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class StandardManagement_Info extends AppCompatActivity {

    Toolbar toolbar;
    StandardManagement standardManagement;
    TextView downLoad;
    TextView upload;
    TextView file_name;
    TextView file_size;
    RelativeLayout relativeLayout;
    String path;
    ImageView okupload;
    File myfile;
    UploadUtil uploadUtil=new UploadUtil(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.standard_management__info);
        toolbar = (Toolbar) findViewById(R.id.mToolbar);
        setSupportActionBar(toolbar);

        Intent intent=getIntent();
        standardManagement=(StandardManagement) intent.getSerializableExtra("chuandi");

        //设置Toolbar左边显示一个返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        downLoad=(TextView)findViewById(R.id.download);
        upload=(TextView)findViewById(R.id.upload);
        file_name=(TextView)findViewById(R.id.attachment_name);
        file_size=(TextView)findViewById(R.id.attachment_size);
        relativeLayout=(RelativeLayout)findViewById(R.id.attachment_layout);
        relativeLayout.setVisibility(View.GONE);
        okupload=(ImageView)findViewById(R.id.okupload);

        downLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog=new AlertDialog.Builder(StandardManagement_Info.this);
                dialog.setTitle("确定下载该文件吗？");
                dialog.setCancelable(false);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String url="http://119.23.38.100:8080/cma/StandardManagement/downloadFile?fileId="+standardManagement.getFileId();
                        downFile(url,standardManagement.getFileName());
                    }
                });
                dialog.setNegativeButton("取消", null);
                dialog.show();
            }
        });
        addListen(upload);
        okupload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myfile=new File(path);
                    sendOKhttp();
                }
            });


    }


    public void sendOKhttp()
    {
        final ProgressDialog progressDialog = new ProgressDialog(StandardManagement_Info.this);
        progressDialog.setTitle("正在上传中");//2.设置标题
        progressDialog.setMessage("请稍等......");//3.设置显示内容
        progressDialog.setCancelable(true);//4.设置可否用back键关闭对话框
        progressDialog.show();//5.将ProgessDialog显示出来
        new Thread(new Runnable() {
            @Override
            public void run() {
                String temp="http://119.23.38.100:8080/cma/StandardManagement/modifyOne";
                RequestBody requestBody=new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("fileId",Long.toString(standardManagement.getFileId()))
                        .addFormDataPart("file",myfile.getName(),
                                RequestBody.create(MediaType.parse("*/*"),myfile))
                        .build();
                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder()
                        // 指定访问的服务器地址，后续在这里修改
                        .url(temp)
                        .post(requestBody)
                        .build();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                Toast.makeText(StandardManagement_Info.this, "上传失败！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result = response.body().string();
                        Log.d("resulthhhere",result);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                Toast.makeText(StandardManagement_Info.this, "上传成功！", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });

                    }
                });
            }
        }).start();
    }

    public void downFile(final String url,final String name) {
        final ProgressDialog progressDialog = new ProgressDialog(StandardManagement_Info.this);
        progressDialog.setTitle("正在下载中");//2.设置标题
        progressDialog.setMessage("请稍等......");//3.设置显示内容
        progressDialog.setCancelable(true);//4.设置可否用back键关闭对话框
        progressDialog.show();//5.将ProgessDialog显示出来

        final DownloadUtil downloadUtil= DownloadUtil.get();

        String path= Environment.getExternalStorageDirectory().getAbsolutePath()+"/CMA/标准管理";
        downloadUtil.download(url, path, name, new DownloadUtil.OnDownloadListener() {
            @Override
            public void onDownloadSuccess(File file) {
                progressDialog.dismiss();
                Looper.prepare();
                Toast.makeText(StandardManagement_Info.this,"下载成功！",Toast.LENGTH_SHORT).show();
                Looper.loop();


            }

            @Override
            public void onDownloading(int progress) {
            }

            @Override
            public void onDownloadFailed(Exception e) {
                Looper.prepare();
                Toast.makeText(StandardManagement_Info.this,"下载失败！",Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        });


    }


    public void addListen(TextView textView)
    {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");//无类型限制
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 1);
                //refreshUI();
                //sendOKhttp();
            }
        });
    }

    public void refreshUI()
    {
        String size="";
        Log.d("path:",path+"mnb");
        File myFile=new File(path);
        try {
            size=uploadUtil.getFileSize(myFile);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        String name=myFile.getName();
        relativeLayout.setVisibility(View.VISIBLE);
        file_size.setText(size);
        file_name.setText(name);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            if ("file".equalsIgnoreCase(uri.getScheme())){//使用第三方应用打开
                path = uri.getPath();
                //tv.setText(path);
                Toast.makeText(this,path+"11111",Toast.LENGTH_SHORT).show();
                return;
            }
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {//4.4以后
                path = uploadUtil.getPath(this, uri);
                //tv.setText(path);
                Toast.makeText(this,path,Toast.LENGTH_SHORT).show();
            } else {//4.4以下下系统调用方法
                path = uploadUtil.getRealPathFromURI(uri);
                // tv.setText(path);
                Toast.makeText(this, path+"222222", Toast.LENGTH_SHORT).show();
            }

            refreshUI();//单独的话出现线程问题。
        }
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

package com.example.cma.ui.testing_institution;

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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cma.R;
import com.example.cma.model.internal_audit.DownloadUtil;
import com.example.cma.model.testing_institution.Certificate;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Certificate_Info extends AppCompatActivity {
    Toolbar toolbar;
    Certificate certificate;
    Certificate certificatetemp;
    TextView textView1;
    TextView textView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.certificate__info);

        Intent intent=getIntent();
        certificatetemp=(Certificate) intent.getSerializableExtra("document");
        init(certificatetemp.getFileId());

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
                AlertDialog.Builder dialog=new AlertDialog.Builder(Certificate_Info.this);
                dialog.setTitle("确定删除此文件吗？");
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
                                postDelete(certificatetemp.getFileId());
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
                Intent intent=new Intent(Certificate_Info.this,Certificate_Modify.class);
                intent.putExtra("document",certificate);
                startActivity(intent);
            }
        });
        textView1=(TextView) findViewById(R.id.text_view1);
        textView2=(TextView) findViewById(R.id.text_view2);

        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final long id=certificate.getFileId();
                final String name=certificate.getFileName();

                AlertDialog.Builder dialog=new AlertDialog.Builder(Certificate_Info.this);
                dialog.setTitle("确定下载该文件吗？");
                dialog.setCancelable(false);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String url="http://119.23.38.100:8080/cma/Certificate/downloadOne?fileId="+id;
                        Log.d("fileId is:",String.valueOf(id));
                        downFile(url,name);
                    }
                });
                dialog.setNegativeButton("取消", null);
                dialog.show();
            }
        });
    }

    public void initView(){

        textView1.setText(certificate.getFileName());

    }

    private void postDelete(long id){
        OkHttpClient okHttpClient=new OkHttpClient();
        RequestBody requestBody=new FormBody.Builder()
                .add("fileId",Long.toString(id))
                .build();
        Request request = new Request.Builder()
                .url("http://119.23.38.100:8080/cma/Certificate/deleteOne")//url的地址
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Certificate_Info.this, "删除失败！", Toast.LENGTH_SHORT).show();
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

                        Toast.makeText(Certificate_Info.this, "删除成功！", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    public void downFile(final String url,final String name) {
        final ProgressDialog progressDialog = new ProgressDialog(Certificate_Info.this);
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

        downloadUtil.download(url, Environment.getExternalStorageDirectory().getAbsolutePath()+"/CMA/TestingInstitution", name, new DownloadUtil.OnDownloadListener() {
            @Override
            public void onDownloadSuccess(File file) {
                progressDialog.dismiss();
                Looper.prepare();
                Toast.makeText(Certificate_Info.this,"下载成功！",Toast.LENGTH_SHORT).show();
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
                Toast.makeText(Certificate_Info.this,"下载失败！",Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        });
    }
    public void init(final long id){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String temp="http://119.23.38.100:8080/cma/Certificate/getOne?fileId="+id;
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
                certificate=gson.fromJson(array,new TypeToken<Certificate>(){}.getType());
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        init(certificatetemp.getFileId());
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

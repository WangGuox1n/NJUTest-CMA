package com.example.cma.ui.internal_audit;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.cma.R;
import com.example.cma.adapter.internal_audit.InternalAuditDocumentAdapter;
import com.example.cma.model.internal_audit.DownloadUtil;
import com.example.cma.model.internal_audit.InternalAuditDocument;
import com.example.cma.model.internal_audit.InternalAuditManagement;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class InternalAuditDocument_Main extends AppCompatActivity {

    public List<InternalAuditDocument> dangAns = new ArrayList<InternalAuditDocument>();
    private InternalAuditDocumentAdapter adapter;
    private ListView listView;
    Toolbar toolbar;
    InternalAuditManagement internalAuditManagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.internal_audit_document__main);
        toolbar = (Toolbar) findViewById(R.id.mToolbar2);
        setSupportActionBar(toolbar);
        listView = (ListView) findViewById(R.id.list_view);


        Intent intent=getIntent();
        internalAuditManagement=(InternalAuditManagement) intent.getSerializableExtra("chuandi");

        //设置Toolbar左边显示一个返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        //设置搜索文本监听
        listView.setTextFilterEnabled(true);

        //showResponse();//对于从数据库获取的数据重新开UI线程否则出错。很奇怪，没有init只有这个照样成功
        initDangan();


        //长按删除
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           final int position, long id) {
                AlertDialog.Builder builder=new AlertDialog.Builder(InternalAuditDocument_Main.this);
                builder.setMessage("确定删除?");
                builder.setTitle("提示");
                builder.setPositiveButton("确定", new DatePickerDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        InternalAuditDocument temp=(InternalAuditDocument) listView.getItemAtPosition(position);
                        postDelete(temp.getId());
                        adapter.notifyDataSetChanged();
                    }
                });

                builder.setNegativeButton("取消", null);
                builder.create().show();
                return true;
            }
        });



        //点击listview中的一个元素
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                InternalAuditDocument d=(InternalAuditDocument) listView.getItemAtPosition(i);
                Intent intent=new Intent(InternalAuditDocument_Main.this,InternalAuditDocument_Info.class);
                intent.putExtra("chuandi",d);
                startActivity(intent);

              /*  InternalAuditDocument d=(InternalAuditDocument) listView.getItemAtPosition(i);
                final long id=d.getId();
                final String name=d.getFile();

                AlertDialog.Builder dialog=new AlertDialog.Builder(InternalAuditDocument_Main.this);
                dialog.setTitle("确定下载该文件吗？");
                dialog.setCancelable(false);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String url="http://119.23.38.100:8080/cma/InternalAuditManagement/downloadFile?fileId="+id;
                        Log.d("fileId is:",String.valueOf(id));
                        downFile(url,name);
                    }
                });
                dialog.setNegativeButton("取消", null);
                dialog.show();*/
            }
        });



        //添加按钮
        FloatingActionButton button=(FloatingActionButton)findViewById(R.id.add_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(InternalAuditDocument_Main.this,InternalAuditDocument_Add.class);
                intent.putExtra("chuandi",internalAuditManagement);
                startActivity(intent);
            }
        });

    }

    public void downFile(final String url,final String name) {
        final ProgressDialog progressDialog = new ProgressDialog(InternalAuditDocument_Main.this);
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

        downloadUtil.download(url, Environment.getExternalStorageDirectory().getAbsolutePath(), name, new DownloadUtil.OnDownloadListener() {
            @Override
            public void onDownloadSuccess(File file) {
                progressDialog.dismiss();
                Looper.prepare();
                Toast.makeText(InternalAuditDocument_Main.this,"下载成功！",Toast.LENGTH_SHORT).show();
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
                Toast.makeText(InternalAuditDocument_Main.this,"下载失败！",Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        });


    }




    public void postDelete(long fileID){
        OkHttpClient okHttpClient=new OkHttpClient();
        RequestBody requestBody=new FormBody.Builder().add("fileId",Long.toString(fileID)).build();
        Request request = new Request.Builder()
                .url("http://119.23.38.100:8080/cma/InternalAuditManagement/deleteOneFile")//url的地址
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(InternalAuditDocument_Main.this, "删除失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(InternalAuditDocument_Main.this, "删除成功！", Toast.LENGTH_SHORT).show();
                        sendRuquestWithOkHttp();
                    }
                });

            }
        });


    }

    /*
    private void showAlertDialog()
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(InternalAuditManagementMain.this);
        dialog.setTitle("新增年度内审管理文档集");

        // 取得自定义View
        LayoutInflater layoutInflater = LayoutInflater.from(InternalAuditManagementMain.this);
        final View myLoginView = layoutInflater.inflate(
                R.layout.internal_audit_add, null);
        dialog.setView(myLoginView);
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        final String now=simpleDateFormat.format(new Date());
        final TextView textView=(TextView)myLoginView.findViewById(R.id.time_select);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimeSelector timeSelector = new TimeSelector(InternalAuditManagementMain.this,new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        textView.setText("   "+time.split(" ")[0]);
                    }
                }, "2000-01-01 00:00", now);
                timeSelector.setIsLoop(false);//设置不循环,true循环
                timeSelector.setMode(TimeSelector.MODE.YMD);//只显示 年月日
                timeSelector.show();
            }
        });

        dialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(textView.getText()=="")
                        {
                            Toast.makeText(InternalAuditManagementMain.this,"你并未添加",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            String date=textView.getText().toString();
                            String year=date.split("-")[0];
                            postJson(year,date);
                            Toast.makeText(InternalAuditManagementMain.this, "添加成功",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        dialog.setNegativeButton("取消",null);
        dialog.show();
    }

    private void postJson(String s1,String s2){

        OkHttpClient okHttpClient=new OkHttpClient();
        RequestBody requestBody=new FormBody.Builder()
                .add("year",s1)
                .add("date",s2)
                .build();
        Request request = new Request.Builder()
                .url("http://119.23.38.100:8080/cma/InternalAuditManagement/addOne")//url的地址
                .post(requestBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(InternalAuditManagementMain.this, "上传失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        sendRuquestWithOkHttp();//更新数据
                        //Toast.makeText(InternalAuditManagementMain.this, "上传成功！", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }*/
    //这里是获取json数据
    private void sendRuquestWithOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String temp="http://119.23.38.100:8080/cma/InternalAuditManagement/getAllFile?year="+internalAuditManagement.getYear();
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


        dangAns = gson.fromJson(string, new TypeToken<List<InternalAuditDocument>>(){}.getType());
        Log.d("点击获取的", "here is json33");
        showResponse();
    }

    private void showResponse() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 在这里进行UI操作，将结果显示到界面上
                adapter = new InternalAuditDocumentAdapter(InternalAuditDocument_Main.this, R.layout.internal_document_item, dangAns);
                listView.setAdapter(adapter);
            }
        });
    }


    private void initDangan() {
        dangAns.clear();
        Log.d("点击获取的", "init");
        sendRuquestWithOkHttp();
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
        initDangan();
    }


}



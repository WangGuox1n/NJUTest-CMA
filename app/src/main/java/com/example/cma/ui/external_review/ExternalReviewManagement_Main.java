package com.example.cma.ui.external_review;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cma.R;
import com.example.cma.adapter.external_review.ExternalReviewManagementAdapter;
import com.example.cma.model.external_review.ExternalReviewManagement;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.feezu.liuli.timeselector.TimeSelector;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ExternalReviewManagement_Main extends AppCompatActivity {
    public List<ExternalReviewManagement> dangAns = new ArrayList<ExternalReviewManagement>();
    private ExternalReviewManagementAdapter adapter;
    private ListView listView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.external_review_management__main);
        toolbar = (Toolbar) findViewById(R.id.mToolbar2);
        setSupportActionBar(toolbar);
        listView = (ListView) findViewById(R.id.list_view);


        //设置Toolbar左边显示一个返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        //设置搜索文本监听
        listView.setTextFilterEnabled(true);

        showResponse();//对于从数据库获取的数据重新开UI线程否则出错。

        //长按删除
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           final int position, long id) {
                AlertDialog.Builder builder=new AlertDialog.Builder(ExternalReviewManagement_Main.this);
                builder.setMessage("确定删除?");
                builder.setTitle("提示");
                builder.setPositiveButton("确定", new DatePickerDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ExternalReviewManagement temp=(ExternalReviewManagement) listView.getItemAtPosition(position);
                        postDelete(temp.getYear());
                        adapter.notifyDataSetChanged();
                    }
                });

                builder.setNegativeButton("取消", null);
                builder.create().show();
                return true ;
            }
        });

        //点击listview中的一个元素
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ExternalReviewManagement d=(ExternalReviewManagement) listView.getItemAtPosition(i);
                Intent intent=new Intent(ExternalReviewManagement_Main.this,ExternalReviewDocument_Main.class);
                intent.putExtra("externalreview",d);
                startActivity(intent);
            }
        });

        //添加按钮
        Button button = (Button) findViewById(R.id.add_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog();
            }
        });

    }

    public void postDelete(long year){
        OkHttpClient okHttpClient=new OkHttpClient();
        RequestBody requestBody=new FormBody.Builder().add("year",Long.toString(year)).build();
        Request request = new Request.Builder()
                .url("http://119.23.38.100:8080/cma/ExternalReviewManagement/deleteOne")//url的地址
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ExternalReviewManagement_Main.this, "删除失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ExternalReviewManagement_Main.this, "删除成功！", Toast.LENGTH_SHORT).show();
                        sendRuquestWithOkHttp();
                    }
                });

            }
        });
    }

    private void showAlertDialog()
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(ExternalReviewManagement_Main.this);
        dialog.setTitle("新增外审管理文档集");

        // 取得自定义View
        LayoutInflater layoutInflater = LayoutInflater.from(ExternalReviewManagement_Main.this);
        final View myLoginView = layoutInflater.inflate(
                R.layout.external_review_add, null);
        dialog.setView(myLoginView);
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        final String now=simpleDateFormat.format(new Date());
        final TextView textView=(TextView)myLoginView.findViewById(R.id.time_select);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimeSelector timeSelector = new TimeSelector(ExternalReviewManagement_Main.this,new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        textView.setText(time.split(" ")[0]+"");
                    }
                }, "2000-01-01 00:00", "2050-01-01 00:00");
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
                            Toast.makeText(ExternalReviewManagement_Main.this,"你并未添加",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            String date=textView.getText().toString();
                            String year=date.split("-")[0];
                            postJson(year,date);
                            Toast.makeText(ExternalReviewManagement_Main.this, "添加成功",Toast.LENGTH_SHORT).show();
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
                .url("http://119.23.38.100:8080/cma/ExternalReviewManagement/addOne")//url的地址
                .post(requestBody)
                .build();
        Log.d("s1",s1);
        Log.d("s2",s2);

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ExternalReviewManagement_Main.this, "上传失败！", Toast.LENGTH_SHORT).show();
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
    }

    //这里是获取json数据
    private void sendRuquestWithOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("点击获取的", "here is json11");
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            // 指定访问的服务器地址，后续在这里修改
                            .url("http://119.23.38.100:8080/cma/ExternalReviewManagement/getAll")
                            .build();
                    Response response = client.newCall(request).execute();
                    // Log.d("点击获取的", "here is json22");
                    String responseData = response.body().string();
                    //Log.d("点击获取的", "here is json44");
                    //Log.d("获得的数据:", responseData);
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


        dangAns = gson.fromJson(string, new TypeToken<List<ExternalReviewManagement>>(){}.getType());
        Log.d("点击获取的", "here is json33");
        showResponse();
    }

    private void showResponse() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 在这里进行UI操作，将结果显示到界面上
                adapter = new ExternalReviewManagementAdapter(ExternalReviewManagement_Main.this, R.layout.external_review_management_listitem, dangAns);
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

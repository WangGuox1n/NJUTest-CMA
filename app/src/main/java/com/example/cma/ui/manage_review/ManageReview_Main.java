package com.example.cma.ui.manage_review;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cma.R;

import com.example.cma.adapter.manage_review.ManageReviewAdapter;
import com.example.cma.model.manage_review.ManageReview;
import com.example.cma.model.staff_management.Result;
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

public class ManageReview_Main extends AppCompatActivity {
    static public List<ManageReview> dangAns=new ArrayList<ManageReview>();

    private ManageReviewAdapter adapter;
    private ListView listView;
    private SearchView searchView;
    Toolbar toolbar;
    private ManageReview temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_review__main);  initView();
        initDangan();
        //在Activity代码中使用Toolbar对象替换ActionBar
        toolbar = (Toolbar) findViewById(R.id.mToolbar2);
        setSupportActionBar(toolbar);

        //设置Toolbar左边显示一个返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        //设置搜索文本监听
        listView.setTextFilterEnabled(true);
        searchView.setFocusable(false);
        //当点击搜索按钮时触发
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //当点击搜索按钮触发该方法进行回调
            @Override
            public boolean onQueryTextSubmit(String s) {
                ListAdapter listAdapter=listView.getAdapter();
                if(listAdapter instanceof Filterable){
                    Filter filter=((Filterable)listAdapter).getFilter();
                    if(s==null||s.length()==0){
                        filter.filter(null);
                    }else{
                        filter.filter(s);
                    }
                }
                return false;
            }

            //当搜索内容改变时触发该方法进行回调
            @Override
            public boolean onQueryTextChange(String s) {
                ListAdapter adapter=listView.getAdapter();
                if(adapter instanceof Filterable){
                    Filter filter=((Filterable)adapter).getFilter();
                    if(s==null||s.length()==0){
                        filter.filter(null);
                    }else{
                        filter.filter(s);
                    }
                }
                return true;
            }
        });

        showResponse();//对于从数据库获取的数据重新开UI线程否则出错。

        registerForContextMenu(listView);

        //添加按钮
        Button button=(Button)findViewById(R.id.add_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showAddDialog();

            }
        });

    }

    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        temp = (ManageReview)listView.getItemAtPosition(((AdapterView.AdapterContextMenuInfo) menuInfo).position);//获取listview的item对象
        getMenuInflater().inflate(R.menu.context_menu_1, menu);
    }
    private void postDelete(String year) {

        OkHttpClient okHttpClient = new OkHttpClient();

        RequestBody requestBody1 = new FormBody.Builder().add("year", year).build();

        Request request = new Request.Builder()
                .url("http://119.23.38.100:8080/cma/ManagementReview/deleteOne")//url的地址
                .post(requestBody1)
                .build();

        //同步上传
        //异步post
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {


            }


            @Override
            public void onResponse (Call call, Response response) throws IOException {
                        initDangan();

            }

        });
    }
    public boolean onContextItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_item2) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ManageReview_Main.this);
            builder.setTitle("确定删除吗");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    postDelete(String.valueOf(temp.getYear()));
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {

                }
            });
            builder.show();



        }
        else
        {
            Intent intent = new Intent(ManageReview_Main.this, ManageReviewOne_Main.class);
            intent.putExtra("year",String.valueOf(temp.getYear()));
            startActivity(intent);
        }


        return super.onContextItemSelected(item);
    }

    protected void showAddDialog() {

        LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.manage_review_dialog, null);
        final EditText editText1 = (EditText) textEntryView.findViewById(R.id.editText1);
        final EditText editText2= (EditText) textEntryView.findViewById(R.id.editText2);
        editText2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
                final String now=simpleDateFormat.format(new Date());
                //editText.setText();
                TimeSelector timeSelector = new TimeSelector(ManageReview_Main.this,new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        // Toast.makeText(StaffTraining_Add.this, time, Toast.LENGTH_SHORT).show();
                        editText2.setText(time.split(" ")[0]);
                    }
                }, "2000-01-01 00:00", now);
                timeSelector.setIsLoop(false);//设置不循环,true循环
                timeSelector.setTitle("请选择日期");
                //        timeSelector.setMode(TimeSelector.MODE.YMDHM);//显示 年月日时分（默认）
                timeSelector.setMode(TimeSelector.MODE.YMD);//只显示 年月日
                timeSelector.show();
            }
        });
        AlertDialog.Builder ad1 = new AlertDialog.Builder(ManageReview_Main.this);
        ad1.setTitle("增加条目:");
        ad1.setIcon(android.R.drawable.ic_dialog_info);
        ad1.setView(textEntryView);
        ad1.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {


                String s1=editText1.getText().toString();
                String s2=editText2.getText().toString();
                postAdd(s1,s2);


            }
        });
        ad1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {

            }
        });
        ad1.show();// 显示对话框

    }
    private void postAdd(String s1,String s2)
    {
        OkHttpClient okHttpClient=new OkHttpClient();

        RequestBody requestBody1= new FormBody.Builder()
                .add("year",s1)
                .add("date",s2)
                .build();
        Request request = new Request.Builder()
                .url("http://119.23.38.100:8080/cma/ManagementReview/addOne")//url的地址
                .post(requestBody1)
                .build();

        //同步上传
        //异步post
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ManageReview_Main.this, "添加失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String c=response.toString();
                        Log.d("查看返回信息",c);
                        Toast.makeText(ManageReview_Main.this, "添加成功！", Toast.LENGTH_SHORT).show();
                        onResume();
                    }
                });

            }
        });

    }
    //这里是获取json数据
    private void sendRequestWithOkHttp(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("点击获取的","here is json11");
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()

                            .url("http://119.23.38.100:8080/cma/ManagementReview/getAll")
                            .build();
                    Response response = client.newCall(request).execute();
                    Log.d("点击获取的","here is json22");
                    String responseData = response.body().string();
                    Log.d("点击获取的","here is json44");
                    Log.d("获得的数据:",responseData);
                    parseJSONWithGSON(responseData);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void parseJSONWithGSON(String jsonData) {

        System.out.print(jsonData);
        Gson gson = new Gson();
        //Log.d("点击获取得数据","jsonData");
        //dangAns = gson.fromJson(jsonData, new TypeToken<List<ManageReview>>() {}.getType());
        Log.d("点击获取的","here is json33");
        Result<List<ManageReview>> userListResult = gson.fromJson(jsonData,new TypeToken<Result<List<ManageReview>>>(){}.getType());
        //Result<Array<ManageReview>> userListResult = gson.fromJson(jsonData,new TypeToken<Result<List<ManageReview>>>(){}.getType());
        dangAns= userListResult.data;
        showResponse();
    }

    private void showResponse() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 在这里进行UI操作，将结果显示到界面上
                adapter=new ManageReviewAdapter(ManageReview_Main.this,R.layout.manage_review_item,dangAns);
                listView.setAdapter(adapter);
            }
        });
    }


    private void initDangan(){
        dangAns.clear();
        Log.d("点击获取的","init");
        sendRequestWithOkHttp();



    }

    private void initView(){
        listView=(ListView)findViewById(R.id.list_view);
        searchView=(SearchView)findViewById(R.id.searchview);
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
    protected void onResume(){
        super.onResume();
        initDangan();
    }
}


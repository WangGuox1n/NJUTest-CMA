package com.example.cma.ui.training_management;


import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.cma.R;
import com.example.cma.adapter.training_management.AnnualTrainingPlanAdapter;
import com.example.cma.model.staff_management.Result;
import com.example.cma.model.training_management.AnnualTrainingPlan;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AnnualTrainingPlan_Main  extends AppCompatActivity {


    static public List<AnnualTrainingPlan> dangAns=new ArrayList<AnnualTrainingPlan>();

    private AnnualTrainingPlanAdapter adapter;
    private ListView listView;
    private SearchView searchView;
    Toolbar toolbar;
    String year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.annual_training_plan__main);
        Intent intent = getIntent();
        year = (String) intent.getSerializableExtra("year");
        initView();
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

        //选择listview中的一个元素
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent=new Intent(AnnualTrainingPlan_Main.this,AnnualTrainingPlan_See.class);
                AnnualTrainingPlan d=(AnnualTrainingPlan) listView.getItemAtPosition(i);
                String k=String.valueOf(d.getPlanId());
                intent.putExtra("id",k);
                startActivity(intent);


            }
        });
        //添加按钮
        Button button=(Button)findViewById(R.id.add_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(AnnualTrainingPlan_Main.this,AnnualTrainingPlan_Add.class);
                startActivity(intent);

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
                    String path="http://119.23.38.100:8080/cma/AnnualTrainingPlan/getAll?year="+year;
                    Request request = new Request.Builder()
                            // 指定访问的服务器地址，后续在这里修改
                            //.url("http://10.0.2.2:3000/stars")
                            .url(path)
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
        //dangAns = gson.fromJson(jsonData, new TypeToken<List<AnnualTrainingPlan>>() {}.getType());
        Log.d("点击获取的","here is json33");
        Result<List<AnnualTrainingPlan>> userListResult = gson.fromJson(jsonData,new TypeToken<Result<List<AnnualTrainingPlan>>>(){}.getType());
        //Result<Array<AnnualTrainingPlan>> userListResult = gson.fromJson(jsonData,new TypeToken<Result<List<AnnualTrainingPlan>>>(){}.getType());
        dangAns= userListResult.data;
        showResponse();
    }

    private void showResponse() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 在这里进行UI操作，将结果显示到界面上
                adapter=new AnnualTrainingPlanAdapter(AnnualTrainingPlan_Main.this,R.layout.annual_training_plan_item,dangAns);
                listView.setAdapter(adapter);
            }
        });
    }


    private void initDangan(){
        dangAns.clear();
        Log.d("点击获取的","init");
        sendRequestWithOkHttp();

     /*   AnnualTrainingPlan d1=new AnnualTrainingPlan(1,"java培训","2015-01-01","仙林","张天","第一课", "无");
        dangAns.add(d1);
        AnnualTrainingPlan d2=new AnnualTrainingPlan(2,"python培训","2017-01-01","鼓楼","唐杰","第一课", "无");
        dangAns.add(d2);
        */

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



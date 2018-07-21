package com.example.cma.ui.capacity_verification;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.cma.R;
import com.example.cma.adapter.capacity_verification.CapacityVerificationProjectAdapter;
import com.example.cma.model.capacity_verification.CapacityVerificationPlan;
import com.example.cma.model.capacity_verification.CapacityVerificationProject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CapacityVerificationProject_Main extends AppCompatActivity {

    Toolbar toolbar;
    CapacityVerificationPlan capacityVerificationPlan;
    public List<CapacityVerificationProject> dangAns = new ArrayList<CapacityVerificationProject>();
    private CapacityVerificationProjectAdapter adapter;
    private ListView listView;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.capacity_verification_project__main);

        toolbar = (Toolbar) findViewById(R.id.mToolbar);
        setSupportActionBar(toolbar);
        //设置Toolbar左边显示一个返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Intent intent=getIntent();
        capacityVerificationPlan=(CapacityVerificationPlan)intent.getSerializableExtra("chuandi");


        listView = (ListView) findViewById(R.id.list_view);
        searchView = (SearchView) findViewById(R.id.searchview);
        initDangan();


        //设置搜索文本监听
        listView.setTextFilterEnabled(true);

        searchView.setFocusable(false);
        //当点击搜索按钮时触发
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //当点击搜索按钮触发该方法进行回调
            @Override
            public boolean onQueryTextSubmit(String s) {
                ListAdapter listAdapter = listView.getAdapter();
                if (listAdapter instanceof Filterable) {
                    Filter filter = ((Filterable) listAdapter).getFilter();
                    if (s == null || s.length() == 0) {
                        filter.filter(null);
                    } else {
                        filter.filter(s);
                    }
                }
                return false;
            }

            //当搜索内容改变时触发该方法进行回调
            @Override
            public boolean onQueryTextChange(String s) {
                ListAdapter adapter = listView.getAdapter();
                if (adapter instanceof Filterable) {
                    Filter filter = ((Filterable) adapter).getFilter();
                    if (s == null || s.length() == 0) {
                        filter.filter(null);
                    } else {
                        filter.filter(s);
                    }
                }
                return true;
            }
        });



        //选择listview中的一个元素
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CapacityVerificationProject d=(CapacityVerificationProject) listView.getItemAtPosition(i);
                Intent intent=new Intent(CapacityVerificationProject_Main.this,CapacityVericationProject_Info.class);
                intent.putExtra("chuandi",d);
                startActivity(intent);
            }
        });


        //添加按钮
        FloatingActionButton button=(FloatingActionButton)findViewById(R.id.add_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CapacityVerificationProject_Main.this,CapacityVerificationProject_Add.class);
                intent.putExtra("chuandi",capacityVerificationPlan);
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
                    String temp="http://119.23.38.100:8080/cma/CapacityVerification/getAllProject?planId="+capacityVerificationPlan.getPlanId();
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            // 指定访问的服务器地址，后续在这里修改
                            .url(temp)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                   // Log.d("url:",temp);
                    //Log.d("responseData:",responseData);
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
        String string="["+strings1[0]+"]";

        Gson gson = new Gson();


        dangAns = gson.fromJson(string, new TypeToken<List<CapacityVerificationProject>>(){}.getType());
        showResponse();
    }

    private void showResponse() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 在这里进行UI操作，将结果显示到界面上
                adapter = new CapacityVerificationProjectAdapter(CapacityVerificationProject_Main.this, R.layout.capacity_project_item, dangAns);
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

package com.example.cma.ui.staff_management;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
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
import com.example.cma.adapter.staff_management.StaffTrainingAdapter;
import com.example.cma.model.staff_management.Result;
import com.example.cma.model.staff_management.StaffTraining;
import com.example.cma.model.staff_management.TrainingResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class StaffTraining_result_main extends AppCompatActivity {

    static public List<StaffTraining> dangAns = new ArrayList<StaffTraining>();

    private StaffTrainingAdapter adapter;
    private ListView listView;
    private SearchView searchView;
    Toolbar toolbar;
    String id;   //人员id
    String staffname ; //人员名称

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_training_main);
        Intent intent=getIntent();
        id=(String) intent.getSerializableExtra("chuandi");
        staffname=(String)intent.getSerializableExtra("chuandi1");
        Log.d("DDDDDDD",id);
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

        showResponse();//对于从数据库获取的数据重新开UI线程否则出错。

        //选择listview中的一个元素
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // Intent intent = new Intent(StaffTraining_result_main.this, StaffTraining_modify.class);
                StaffTraining d = (StaffTraining) listView.getItemAtPosition(i);
                String trainingid=Long.toString(d.getTrainingId());
                check(trainingid,id,staffname,d);
                // intent.putExtra("chuandi", d);
                //startActivity(intent);


            }
        });
        //添加按钮
        Button button = (Button) findViewById(R.id.add_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Log.d("现在！！！！！！！","!!!!!!!!");
                Intent intent = new Intent(StaffTraining_result_main.this, StaffTraining_result_add.class);
                intent.putExtra("chuandi",id);
                startActivity(intent);

            }
        });

    }

    //这里是获取json数据
    private void sendRequestWithOkHttp(final String id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("点击获取的", "here is json11");
                    OkHttpClient client = new OkHttpClient();
                    Log.d("￥￥￥￥￥￥￥￥￥￥￥￥￥￥￥",id);
                    String path="http://119.23.38.100:8080/cma/StaffTraining/getAllByStaff";
                    String url_path = path +"?id=" + URLEncoder.encode(id, "utf-8");
                    Request request = new Request.Builder()
                            // 指定访问的服务器地址，后续在这里修改
                            //.url("http://10.0.2.2:3000/stars")
                            .url(url_path)

                            .build();

                    Response response = client.newCall(request).execute();
                    Log.d("点击获取的", "here is json22");
                    String responseData = response.body().string();
                    Log.d("点击获取的", "here is json44");
                    Log.d("获得的数据:", responseData);
                    parseJSONWithGSON(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void parseJSONWithGSON(String jsonData) {

        System.out.print(jsonData);
        Gson gson = new Gson();
        //Log.d("点击获取得数据","jsonData");
        //dangAns = gson.fromJson(jsonData, new TypeToken<List<StaffTraining>>() {}.getType());
        Log.d("获取的!!!!!!!!!!!", "here is json33");
        Result<List<StaffTraining>> userListResult = gson.fromJson(jsonData, new TypeToken<Result<List<StaffTraining>>>() {
        }.getType());
        if(userListResult.data!=null)
        {

        dangAns = userListResult.data;

            showResponse();
        }
        else
        {
            Log.d("没有信息！！！！！！","!!!!!!!");
        }
    }

    private void showResponse() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 在这里进行UI操作，将结果显示到界面上

                    adapter = new StaffTrainingAdapter(StaffTraining_result_main.this, R.layout.stafftraining_item, dangAns);
                    listView.setAdapter(adapter);

            }
        });
    }


    private void initDangan() {
        dangAns.clear();
        Log.d("点击获取的", "init");
        sendRequestWithOkHttp(id);


    }

    private void initView() {
        listView = (ListView) findViewById(R.id.list_view);
        searchView = (SearchView) findViewById(R.id.searchview);
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
    void check(final String trainingId,final String peopleid,final String staffname,final StaffTraining train)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("点击获取的","here is json11");
                    OkHttpClient client = new OkHttpClient();

                    String path="http://119.23.38.100:8080/cma/StaffTraining/getOne";
                    String url_path = path +"?id=" + URLEncoder.encode(peopleid, "utf-8")+"&trainingId=" + URLEncoder.encode(trainingId, "utf-8");
                    Log.d("传递的url",url_path);
                    Request request = new Request.Builder()
                            // 指定访问的服务器地址，后续在这里修改
                            //.url("http://10.0.2.2:3000/stars")
                            .url(url_path)

                            .build();
                    Response response = client.newCall(request).execute();
                    Log.d("点击获取的","here is json22");
                    String responseData = response.body().string();
                    Log.d("点击获取的","here is json44");
                    Log.d("获得的数据:",responseData);
                    Gson gson = new Gson();
                    Result<TrainingResult> userListResult = gson.fromJson(responseData,new TypeToken<Result<TrainingResult>>(){}.getType());
                    TrainingResult result=userListResult.data;
                    int code=userListResult.code;
                    if(code==404 || result==null)
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(StaffTraining_result_main.this);
                        builder.setTitle("无考核结果");
                        builder.setPositiveButton("添加", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {

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
                    else {
                        if(result.getResult()==null ||result.getResult().length()<0)
                        {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(StaffTraining_result_main.this);
                                    builder.setTitle("无考核结果");
                                    builder.setPositiveButton("添加", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which)
                                        {
                                            Intent intent=new Intent(StaffTraining_result_main.this,StaffTraining_result_add.class);
                                            intent.putExtra("trainingId",trainingId);
                                            intent.putExtra("id",peopleid);
                                            startActivity(intent);
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
                            });

                        }
                        else {
                            Intent intent = new Intent(StaffTraining_result_main.this, StaffTraining_result_See.class);
                            Log.d("222222222", result.getProgram());
                            intent.putExtra("chuan", result);
                            intent.putExtra("id", peopleid);
                            intent.putExtra("staffname", staffname);
                            intent.putExtra("training", train);

                            startActivity(intent);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

    }
}


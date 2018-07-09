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
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.cma.R;
import com.example.cma.adapter.staff_management.StaffTrainingPeopleAdapter;
import com.example.cma.model.staff_management.Result;
import com.example.cma.model.staff_management.StaffTraining;
import com.example.cma.model.staff_management.StaffTrainingPeople;
import com.example.cma.model.staff_management.TrainingResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class StaffTraining_staff_main extends AppCompatActivity {
    private StaffTrainingPeopleAdapter adapter;
    private ListView listView;
    private SearchView searchView;
    private TrainingResult result;
    private StaffTraining train;
    Toolbar toolbar;
    private String c;
    static public List<StaffTrainingPeople> dangAns=new ArrayList<StaffTrainingPeople>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_training_staff_main);

        Intent intent=getIntent();
        c=(String)intent.getSerializableExtra("chuandi");
        train=(StaffTraining)intent.getSerializableExtra("programName");
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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // Intent intent=new Intent(StaffTraining_staff_main.this,StaffTraining_staff_modify.class);
                StaffTrainingPeople d = (StaffTrainingPeople) listView.getItemAtPosition(i);
                String trainingid = c;
                String id = String.valueOf(d.getId());
                String staffname=d.getName();
                check(trainingid,id,staffname);


            }
        });
        Button button=(Button)findViewById(R.id.add_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText et = new EditText(StaffTraining_staff_main.this);

                new AlertDialog.Builder(StaffTraining_staff_main.this).setTitle("请输入人员编号").setIcon(android.R.drawable.ic_dialog_info).setView(et).setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String input = et.getText().toString();
                        if (input.equals("")) {
                            Toast.makeText(getApplicationContext(), "搜索内容不能为空！" + input, Toast.LENGTH_LONG).show();
                        }
                        else {
                            Log.d("获得编号",input);
                            String result="{"+"\"trainingId\""+":"+c+","+"\"data\""+":"+"[{"+"\"id\""+":"+input+"}]}";
                            Log.d("结果！！",result);
                            OkHttpClient okHttpClient=new OkHttpClient();
                            //创建一个请求对象
                            MediaType JSON=MediaType.parse("application/json; charset=utf-8");
                            RequestBody requestBody=RequestBody.create(JSON,result);
                            Request request = new Request.Builder()
                                    .url("http://119.23.38.100:8080/cma/StaffTraining/addTrainingPeople")//url的地址
                                    .post(requestBody)
                                    .build();
                            okHttpClient.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    Log.d("androixx.cn", "失败！！！！");
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(StaffTraining_staff_main.this, "上传失败！", Toast.LENGTH_SHORT).show();
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

                                            Toast.makeText(StaffTraining_staff_main.this, "上传成功！", Toast.LENGTH_SHORT).show();

                                        }
                                    });

                                }
                            });
                        }
                    }
                }).setNegativeButton("取消", null).show();

            }
        });

        showResponse();//对于从数据库获取的数据重新开UI线程否则出错。

    }
    private void sendRequestWithOkHttp(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("点击获取的","here is json11");
                    OkHttpClient client = new OkHttpClient();

                    String path="http://119.23.38.100:8080/cma/StaffTraining/getTrainingPeople";
                    String url_path = path +"?trainingId=" + URLEncoder.encode(c, "utf-8");
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
        //dangAns = gson.fromJson(jsonData, new TypeToken<List<StaffTraining>>() {}.getType());
        Log.d("点击获取的","here is json33");
        Result<List<StaffTrainingPeople>> userListResult = gson.fromJson(jsonData,new TypeToken<Result<List<StaffTrainingPeople>>>(){}.getType());

        dangAns= userListResult.data;

        showResponse();
    }

    private void showResponse() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 在这里进行UI操作，将结果显示到界面上
                adapter=new StaffTrainingPeopleAdapter(StaffTraining_staff_main.this,R.layout.stafftraining_people,dangAns);
                listView.setAdapter(adapter);
            }
        });
    }
    private void initView(){
        listView=(ListView)findViewById(R.id.list_view);
        searchView=(SearchView)findViewById(R.id.searchview);
    }
    private void initDangan(){
        dangAns.clear();
        Log.d("点击获取的","init");
        sendRequestWithOkHttp();


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
    void check(final String trainingId,final String peopleid,final String staffname)
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
                    result=userListResult.data;
                    Intent intent=new Intent(StaffTraining_staff_main.this,StaffTraining_staff_modify.class);
                    Log.d("222222222",result.getProgram());
                    intent.putExtra("chuan",result);
                    intent.putExtra("id",peopleid);
                    intent.putExtra("staffname",staffname);
                    intent.putExtra("training",train);

                    startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

    }

}


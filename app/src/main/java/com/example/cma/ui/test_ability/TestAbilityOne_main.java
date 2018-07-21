package com.example.cma.ui.test_ability;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.example.cma.adapter.test_ability.TestAbilityOneAdapter;
import com.example.cma.model.staff_management.Result;
import com.example.cma.model.test_ability.TestAbilityOne;
import com.example.cma.utils.FileUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TestAbilityOne_main extends AppCompatActivity implements View.OnClickListener{

    static public List<TestAbilityOne> dangAns=new ArrayList<TestAbilityOne>();

    private TestAbilityOneAdapter adapter;
    private ListView listView;
    private SearchView searchView;
    Toolbar toolbar;
    private String year;
    private Button addButton;
    private TextView attachment_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_ability_one_main);
        Intent intent = getIntent();
        year = (String) intent.getStringExtra("year");
        initView();
        initDangan();
        //在Activity代码中使用Toolbar对象替换ActionBar
        toolbar = (Toolbar) findViewById(R.id.mToolbar);
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

    }
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.add_button:
                Log.d("添加","!!!!!!!!");
                showAddDialog();
               // FileUtil.getInstance().localStorage(this);
                break;
            default:
                break;
        }
    }

    protected void showAddDialog() {

        LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.test_ability_dialog, null);
        final EditText editText1 = (EditText) textEntryView.findViewById(R.id.editText1);
        final EditText editText2= (EditText)textEntryView.findViewById(R.id.editText2);
        final EditText editText3= (EditText)textEntryView.findViewById(R.id.editText3);
        AlertDialog.Builder ad1 = new AlertDialog.Builder(TestAbilityOne_main.this);
        ad1.setTitle("增加条目:");
        ad1.setIcon(android.R.drawable.ic_dialog_info);
        ad1.setView(textEntryView);
        ad1.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {


                String s1=editText1.getText().toString();
                String s2=editText2.getText().toString();
                String s3=editText3.getText().toString();
                postSave(s1,s2,s3);


            }
        });
        ad1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {

            }
        });
        ad1.show();// 显示对话框

    }
    private void postSave(String s1,String s2,String s3){

        OkHttpClient okHttpClient=new OkHttpClient();

        RequestBody requestBody1= new FormBody.Builder()
                .add("year",year)
                .add("productionName",s1)
                .add("ability",s2)
                .add("referrence",s3)
                .build();

        Request request = new Request.Builder()
                .url("http://119.23.38.100:8080/cma/TestAbility/addOneItem")//url的地址
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
                        Toast.makeText(TestAbilityOne_main.this, "添加失败！", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(TestAbilityOne_main.this, "添加成功！", Toast.LENGTH_SHORT).show();
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
                    String path="http://119.23.38.100:8080/cma/TestAbility/getAllItem?year="+year;
                    Log.d("地址是！！！！",path);
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()

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
        //dangAns = gson.fromJson(jsonData, new TypeToken<List<TestAbilityOne>>() {}.getType());
        Log.d("点击获取的","here is json33");
        Result<List<TestAbilityOne>> userListResult = gson.fromJson(jsonData,new TypeToken<Result<List<TestAbilityOne>>>(){}.getType());
        //Result<Array<TestAbilityOne>> userListResult = gson.fromJson(jsonData,new TypeToken<Result<List<TestAbilityOne>>>(){}.getType());
        dangAns= userListResult.data;
        showResponse();
    }

    private void showResponse() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 在这里进行UI操作，将结果显示到界面上
                adapter=new TestAbilityOneAdapter(TestAbilityOne_main.this,R.layout.test_ability_one_item,dangAns);
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
        addButton = (Button) findViewById(R.id.add_button);
        addButton.setOnClickListener(this);
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



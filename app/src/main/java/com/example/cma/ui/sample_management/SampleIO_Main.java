package com.example.cma.ui.sample_management;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.Toast;

import com.example.cma.R;
import com.example.cma.adapter.sample_management.SampleIOAdapter;
import com.example.cma.model.sample_management.SampleIO;
import com.example.cma.utils.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class SampleIO_Main extends AppCompatActivity implements SearchView.OnQueryTextListener,View.OnClickListener,AdapterView.OnItemClickListener{
    //data
    private List<SampleIO> list= new ArrayList<>();;

    //View
    private Toolbar toolbar;
    private ListView listView;
    private SearchView searchView;
    private Button addButton;
    private SampleIOAdapter adapter;
    private long preid=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_io__main);
        Intent intent = getIntent();
        preid = intent.getLongExtra("id", -1);
        initView();
        getDataFromServer();
    }
    //初始化所有控件
    public void initView(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        listView =(ListView)findViewById(R.id.list_view);
        searchView =(SearchView)findViewById(R.id.searchview);
        addButton = (Button)findViewById(R.id.add_button);

        //使用Toolbar对象替换ActionBar
        setSupportActionBar(toolbar);
        //设置Toolbar左边显示一个返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setSupportActionBar(toolbar);

        //默认不弹出键盘
        searchView.setFocusable(false);
        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(false);
        //listView可筛选
        listView.setTextFilterEnabled(true);
        listView.setOnItemClickListener(this);
        addButton.setOnClickListener(this);
    }

    //监听searchView中文本的改变
    @Override
    public boolean onQueryTextChange(String newText) {
        ListAdapter adapter=listView.getAdapter();
        if(adapter instanceof Filterable){
            Filter filter=((Filterable)adapter).getFilter();
            if(newText.isEmpty()){
                filter.filter(null);
            }else{
                filter.filter(newText);
            }
        }
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        // TODO Auto-generated method stub
        ListAdapter listAdapter=listView.getAdapter();
        if(listAdapter instanceof Filterable){
            Filter filter=((Filterable)listAdapter).getFilter();
            if(query.isEmpty()){
                filter.filter(null);
            }else{
                filter.filter(query);
            }
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_button:{
                Intent intent=new Intent(SampleIO_Main.this,SampleIO_Add.class);
                startActivity(intent);
                break;
            }
            default:break;
        }
    }

    //listView 的Item点击事件,跳转到编辑页面
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent=new Intent(SampleIO_Main.this,SampleIO_Info.class);
        SampleIO sampleIO = (SampleIO) listView.getItemAtPosition(position);
        intent.putExtra("sampleIO", sampleIO);
        startActivity(intent);
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

    //向后端发送请求，返回所有人员记录
    public void getDataFromServer(){
        //String address = "http://10.0.2.2/get_data.json";
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpUtil.sendOkHttpRequest("http://119.23.38.100:8080/cma/SampleIo/getAll",new okhttp3.Callback(){
                    @Override
                    public void onResponse(Call call, Response response)throws IOException {
                        String responseData = response.body().string();
                        Log.d("responseData:",responseData);
                        parseJSONWithGSON(responseData);
                        showResponse();
                    }
                    @Override
                    public void onFailure(Call call,IOException e){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(SampleIO_Main.this, "请求数据失败！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        }).start();
    }

    private void parseJSONWithGSON(String jsondata){
        JSONArray array = new JSONArray();
        try {
            JSONObject object = new JSONObject(jsondata);//最外层的JSONObject对象
            array = object.getJSONArray("data");
        }catch (Exception e){
            e.printStackTrace();
        }
        if(array.equals("null")){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(SampleIO_Main.this, "样品进出登记表为空", Toast.LENGTH_LONG).show();
                }
            });
        }
        Gson gson = new Gson();
        List<SampleIO> newList = gson.fromJson(array.toString(),new TypeToken<List<SampleIO>>(){}.getType());
        if(preid != -1){
            list.clear();
            for(SampleIO t : newList){
                if(t.getSampleIoId()==preid)
                    list.add(t);
            }
        } else
            list=gson.fromJson(array.toString(),new TypeToken<List<SampleIO>>(){}.getType());


    }

    private void showResponse() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter = new SampleIOAdapter(SampleIO_Main.this, R.layout.sample_io__main_listview,list);
                listView.setAdapter(adapter);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDataFromServer();
    }
}

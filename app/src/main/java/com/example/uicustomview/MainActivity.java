package com.example.uicustomview;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.appdatasearch.GetRecentContextCall;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener,View.OnClickListener{

    private List<PersonnelFile> list = new ArrayList<>();
    private SearchView searchView;
    private ListView listView;
    private PersonnelFileAdapter adapter;
    Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        //initData();
        findView();
        getAll();
        adapter = new PersonnelFileAdapter(MainActivity.this,R.layout.list_item,list);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(MainActivity.this,PersonnelFile_Modified.class);
                PersonnelFile personnelFile = (PersonnelFile)listView.getItemAtPosition(position);
                intent.putExtra("PersonnelFile",personnelFile);
                startActivity(intent);
            }
        });

        //从前端得到数据后展示list
        showResponse();

    }

    public void initData(){
        list.add(new PersonnelFile("王国新1","计算机系","测试员","1","档案室","图片1"));
        list.add(new PersonnelFile("王国新2","计算机系","测试员","2","档案室","图片2"));
        list.add(new PersonnelFile("王国新2","计算机系","测试员","3","档案室","图片3"));
        list.add(new PersonnelFile("王国新2","计算机系","测试员","4","档案室","图片4"));
        list.add(new PersonnelFile("王国新5","计算机系","测试员","5","档案室","图片5"));
    }


    //初始化控件
    public void findView(){
        searchView = (SearchView)findViewById(R.id.searchView);
        listView = (ListView)findViewById(R.id.list_View);
        addButton = (Button)findViewById(R.id.add);

        //默认不弹出键盘
        searchView.setFocusable(false);
        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(false);

        //listView可筛选
        listView.setTextFilterEnabled(true);

        addButton.setOnClickListener(this);
    }

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
            case R.id.add:{
                Intent intent=new Intent(MainActivity.this,PersonnelFile_Add.class);
                startActivity(intent);
                break;
            }
            default:break;
        }
    }

    //向后端发送请求，返回所有人员档案记录
    public void getAll(){
        //String address = "http://119.23.38.100:8080/cma/StaffFile/getAllwithoutpics";
        String address = "http://10.0.2.2/get_data.json";
        HttpUtil.sendOkHttpRequest(address,new okhttp3.Callback(){
            @Override
            public void onResponse(Call call, Response response)throws IOException {
                String responseData = response.body().string();
                parseJSONWithGSON(responseData);
            }
            @Override
            public void onFailure(Call call,IOException e){
                ToastUtils.showShort(MainActivity.this,"请求数据失败");
            }
        });
    }

    private void parseJSONWithGSON(String jsondata){
        Log.d("failure on okttp",jsondata);
        Gson gson = new Gson();
        //不能直接赋值给list，这样list就指向别的内存了，这样再刷新adapter也没有。
        //list = gson.fromJson(jsondata,new TypeToken<List<PersonnelFile>>(){}.getType());
        List<PersonnelFile> newList = gson.fromJson(jsondata,new TypeToken<List<PersonnelFile>>(){}.getType());
        list.clear();
        list.addAll(newList);
    }

    private void showResponse() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 在这里进行UI操作，将结果显示到界面上
                listView.setAdapter(adapter);
                //刷新适配器
                adapter.notifyDataSetChanged();
            }
        });
    }
}

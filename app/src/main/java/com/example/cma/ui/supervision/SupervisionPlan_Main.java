package com.example.cma.ui.supervision;

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
import com.example.cma.adapter.supervision.SupervisionPlanAdapter;
import com.example.cma.model.supervision.Supervision;
import com.example.cma.model.supervision.SupervisionPlan;
import com.example.cma.utils.AddressUtil;
import com.example.cma.utils.HttpUtil;
import com.example.cma.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class SupervisionPlan_Main extends AppCompatActivity implements SearchView.OnQueryTextListener,View.OnClickListener,AdapterView.OnItemClickListener{

    //data
    private List<SupervisionPlan> list= new ArrayList<>();;
    private Supervision supervision;
    //View
    private Toolbar toolbar;
    private ListView listView;
    private SearchView searchView;
    private Button addButton;
    private SupervisionPlanAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.supervision_plan_main);
        initView();
        Intent intent = getIntent();
        supervision = (Supervision)intent.getSerializableExtra("Supervision");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(supervision == null){
            ToastUtil.showShort(SupervisionPlan_Main.this,"数据传送失败！");
            return;
        }
        getDataFromServer();
    }

    //初始化所有控件
    public void initView(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        listView =(ListView)findViewById(R.id.list_view);
        searchView =(SearchView)findViewById(R.id.searchview);
        addButton = (Button)findViewById(R.id.add_button);
        setSupportActionBar(toolbar);
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
                Intent intent=new Intent(SupervisionPlan_Main.this,SupervisionPlan_Add.class);
                intent.putExtra("Supervision", supervision);
                startActivity(intent);
                break;
            }
            default:break;
        }
    }

    //listView 的Item点击事件,跳转到编辑页面
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent=new Intent(SupervisionPlan_Main.this,SupervisionPlan_Info.class);
        SupervisionPlan supervisionPlan = (SupervisionPlan)listView.getItemAtPosition(position);
        intent.putExtra("SupervisionPlan", supervisionPlan);
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                String address = AddressUtil.SupervisionPlan_getAll(supervision.getId());
                HttpUtil.sendOkHttpRequest(address,new okhttp3.Callback(){
                    @Override
                    public void onResponse(Call call, Response response)throws IOException {
                        String responseData = response.body().string();
                        Log.d("SupervisionPlan_Main",responseData);
                        parseJSONWithGSON(responseData);
                        showResponse();
                    }
                    @Override
                    public void onFailure(Call call,IOException e){
                        ToastUtil.showShort(SupervisionPlan_Main.this, "请求数据失败！");
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
            ToastUtil.showLong(SupervisionPlan_Main.this, "监督计划为空！");
        }
        Gson gson = new Gson();
        List<SupervisionPlan> newList = gson.fromJson(array.toString(),new TypeToken<List<SupervisionPlan>>(){}.getType());
        list.clear();
        list.addAll(newList);
    }

    private void showResponse() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter = new SupervisionPlanAdapter(SupervisionPlan_Main.this, R.layout.supervision_plan_main_listitem,list);
                listView.setAdapter(adapter);
            }
        });
    }
}

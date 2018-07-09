package com.example.cma.ui.equipment_management;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.example.cma.R;
import com.example.cma.adapter.equipment_management.EquipmentApplicationAdapter;
import com.example.cma.model.equipment_management.EquipmentApplication;
import com.example.cma.utils.AddressUtil;
import com.example.cma.utils.HttpUtil;
import com.example.cma.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class EquipmentApplication_Main extends AppCompatActivity implements SearchView.OnQueryTextListener,View.OnClickListener{

    //data
    private List<EquipmentApplication> list= new ArrayList<>();;

    //View
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private SearchView searchView;
    private FloatingActionButton addButton;
    private EquipmentApplicationAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.equipment_application_main);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDataFromServer();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //初始化所有控件
    public void initView(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView =(RecyclerView) findViewById(R.id.recycler_view);
        searchView =(SearchView)findViewById(R.id.searchview);
        addButton = (FloatingActionButton)findViewById(R.id.add_button);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
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
        addButton.setOnClickListener(this);
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.filter(newText);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        adapter.filter(query);
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_button:{
                startActivity(new Intent(EquipmentApplication_Main.this,EquipmentApplication_Add.class));
                break;
            }
            default:break;
        }
    }

    public void getDataFromServer(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String address = AddressUtil.EquipmentApplication_getAll();
                HttpUtil.sendOkHttpRequest(address,new okhttp3.Callback(){
                    @Override
                    public void onResponse(Call call, Response response)throws IOException {
                        String responseData = response.body().string();
                        Log.d("EquipmentApplication",responseData);
                        parseJSONWithGSON(responseData);
                        showResponse();
                    }
                    @Override
                    public void onFailure(Call call,IOException e){
                        ToastUtil.showShort(EquipmentApplication_Main.this, "请求数据失败！");
                    }
                });
            }
        }).start();
    }

    private void parseJSONWithGSON(String jsonData){
        JSONArray array = new JSONArray();
        try {
            JSONObject object = new JSONObject(jsonData);//最外层的JSONObject对象
            array = object.getJSONArray("data");
        }catch (Exception e){
            e.printStackTrace();
        }
        if(array.equals("null")){
            ToastUtil.showLong(EquipmentApplication_Main.this, "设备验收记录为空");
        }
        List<EquipmentApplication> newList = new Gson().fromJson(array.toString(),new TypeToken<List<EquipmentApplication>>(){}.getType());
        list.clear();
        list.addAll(newList);
    }

    private void showResponse() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Collections.reverse(list);
                adapter = new EquipmentApplicationAdapter(list);
                recyclerView.setAdapter(adapter);
            }
        });
    }
}
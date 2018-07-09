package com.example.cma.ui.equipment_management;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.cma.R;
import com.example.cma.adapter.equipment_management.EquipmentMaintenanceAdapter;
import com.example.cma.model.equipment_management.Equipment;
import com.example.cma.model.equipment_management.EquipmentMaintenance;
import com.example.cma.model.equipment_management.EquipmentUse;
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

public class EquipmentMaintenance_Main extends AppCompatActivity implements SearchView.OnQueryTextListener, View.OnClickListener {

    //data
    private List<Equipment> equipmentList= new ArrayList<>();
    private List<EquipmentMaintenance> equipmentMaintenanceList = new ArrayList<>();

    //View
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private SearchView searchView;
    private FloatingActionButton addButton;
    private EquipmentMaintenanceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.equipment_maintenance_main);
        initView();
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

    @Override
    protected void onResume() {
        super.onResume();
/*        String equipment_getAll = AddressUtil.Equipment_getAll();
        String equipmentMaintenance_getAll = AddressUtil.EquipmentMaintenance_getAll();
        getDataFromServer(equipment_getAll,false);
        getDataFromServer(equipmentMaintenance_getAll,true);*/
        getDataFromServer();
    }

    //初始化所有控件
    public void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        searchView = (SearchView) findViewById(R.id.searchview);
        addButton = (FloatingActionButton) findViewById(R.id.add_button);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
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
        switch (v.getId()) {
            case R.id.add_button: {
                startActivity(new Intent(EquipmentMaintenance_Main.this, EquipmentMaintenance_Add.class));
                break;
            }
            default:
                break;
        }
    }

    public void getDataFromServer() {
        String equipment_getAll = AddressUtil.Equipment_getAll();
        String maintenance_getAll = AddressUtil.EquipmentMaintenance_getAll();

        Thread getEquipment = getDataThread(equipment_getAll,false);
        Thread getEquipmentMaintenance = getDataThread(maintenance_getAll,true);
        getEquipment.start();
        try {
            getEquipment.join();   //等待getEquipment线程执行完再执行getEquipmentMaintenance
        }catch (InterruptedException e){
            ToastUtil.showShort(EquipmentMaintenance_Main.this,"读取数据错误");
        }
        getEquipmentMaintenance.start();
    }

    public Thread getDataThread(final String address,final boolean getEquipmentMaintenance){
        return new Thread(new Runnable() {
            @Override
            public void run() {
                HttpUtil.sendOkHttpRequest(address,new okhttp3.Callback(){
                    @Override
                    public void onResponse(Call call, Response response)throws IOException {
                        String responseData = response.body().string();
                        Log.d("Maintenance_Main",responseData);
                        parseJSONWithGSON(responseData,getEquipmentMaintenance);
                        if(getEquipmentMaintenance)  //get EquipmentUse List and show
                            showResponse();
                    }
                    @Override
                    public void onFailure(Call call,IOException e){
                        ToastUtil.showShort(EquipmentMaintenance_Main.this, "请求数据失败！");
                    }
                });
            }
        });
    }

    private void parseJSONWithGSON(String jsonData,boolean getEquipmentMaintenance) {
        JSONArray array = new JSONArray();
        try {
            JSONObject object = new JSONObject(jsonData);//最外层的JSONObject对象
            array = object.getJSONArray("data");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (array.equals("null")) {
            ToastUtil.showLong(EquipmentMaintenance_Main.this, "设备列表为空");
        }

        if(getEquipmentMaintenance){
            List<EquipmentMaintenance> newList = new Gson().fromJson(array.toString(),new TypeToken<List<EquipmentMaintenance>>(){}.getType());
            equipmentMaintenanceList.clear();
            equipmentMaintenanceList.addAll(newList);
        }else{
            List<Equipment> newList = new Gson().fromJson(array.toString(),new TypeToken<List<Equipment>>(){}.getType());
            equipmentList.clear();
            equipmentList.addAll(newList);
        }
    }

    private void showResponse() {
        for(Equipment equipment:equipmentList){
            for(EquipmentMaintenance maintenance:equipmentMaintenanceList){ //在使用记录中添加设备的 名字、编号等信息
                Log.d(equipment.getId()+" ",maintenance.getEquipmentId()+" ");
                if(equipment.getId().equals(maintenance.getEquipmentId())){
                    maintenance.setName(equipment.getName());
                    maintenance.setModel(equipment.getModel());
                    maintenance.setEquipmentNumber(equipment.getEquipmentNumber());
                }
            }
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Collections.reverse(equipmentMaintenanceList);
                adapter = new EquipmentMaintenanceAdapter(equipmentMaintenanceList);
                recyclerView.setAdapter(adapter);
            }
        });
    }

}

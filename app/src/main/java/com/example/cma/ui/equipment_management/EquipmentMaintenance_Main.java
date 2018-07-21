package com.example.cma.ui.equipment_management;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.example.cma.R;
import com.example.cma.adapter.equipment_management.EquipmentMaintenanceAdapter;
import com.example.cma.model.equipment_management.Equipment;
import com.example.cma.model.equipment_management.EquipmentMaintenance;
import com.example.cma.utils.AddressUtil;
import com.example.cma.utils.HttpUtil;
import com.example.cma.utils.ToastUtil;
import com.example.cma.utils.ViewUtil;
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

/*
* 设备维修保养记录主页面
* */
public class EquipmentMaintenance_Main extends AppCompatActivity implements SearchView.OnQueryTextListener, View.OnClickListener {

    //data
    private List<Equipment> equipmentList = new ArrayList<>();
    private List<EquipmentMaintenance> equipmentMaintenanceList = new ArrayList<>();

    private RecyclerView recyclerView;
    private EquipmentMaintenanceAdapter adapter;

    private boolean thread1_done = false;
    private boolean thread2_done = false;

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
        String getEquipmentList = AddressUtil.getAddress(AddressUtil.Equipment_getAll);
        String getEquipmentMaintenanceList = AddressUtil.getAddress(AddressUtil.EquipmentMaintenance_getAll);
        getDataFromServer(getEquipmentList, false);
        getDataFromServer(getEquipmentMaintenanceList, true);
    }

    //初始化所有控件
    public void initView() {
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //增加分割线
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        Toolbar toolbar = findViewById(R.id.toolbar);
        ViewUtil.getInstance().setSupportActionBar(this, toolbar);
        //默认不弹出键盘
        SearchView searchView = findViewById(R.id.searchview);
        searchView.setFocusable(false);
        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(false);
        findViewById(R.id.add_button).setOnClickListener(this);
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

    public void getDataFromServer(final String address, final boolean flag) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpUtil.sendOkHttpRequest(address, new okhttp3.Callback() {
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseData = response.body().string();
                        Log.d("Maintenance_Main", responseData);
                        parseJSONWithGSON(responseData, flag);
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        ToastUtil.showShort(EquipmentMaintenance_Main.this, "请求数据失败");
                    }
                });
            }
        }).start();
    }

    /**
     * @param jsonData 服务器返回的数据
     * @param flag 判断当前解析的是 equipmentMaintenance（设备维修记录） 还是 equipment（设备信息）
     */
    private void parseJSONWithGSON(String jsonData, boolean flag) {
        JSONArray array = new JSONArray();
        try {
            JSONObject object = new JSONObject(jsonData);
            array = object.getJSONArray("data");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (array.length()==0) {
            ToastUtil.showShort(EquipmentMaintenance_Main.this, "设备维修保养记录为空");
        }

        if (flag) {
            List<EquipmentMaintenance> newList = new Gson().fromJson(array.toString(), new TypeToken<List<EquipmentMaintenance>>() {
            }.getType());
            equipmentMaintenanceList.clear();
            equipmentMaintenanceList.addAll(newList);
            thread1_done = true;
        } else {
            List<Equipment> newList = new Gson().fromJson(array.toString(), new TypeToken<List<Equipment>>() {
            }.getType());
            equipmentList.clear();
            equipmentList.addAll(newList);
            thread2_done = true;
        }

        //等两个线程都结束了才showResponse，主要是为了解决线程随机的问题
        if (thread1_done && thread2_done) {
            showResponse();
            thread1_done = false;
            thread2_done = false;
        }
    }

    private void showResponse() {
        for (Equipment equipment : equipmentList) {
            for (EquipmentMaintenance maintenance : equipmentMaintenanceList) { //在使用记录中添加设备的 名字、编号等信息
                Log.d(equipment.getId() + " ", maintenance.getEquipmentId() + " ");
                if (equipment.getId().equals(maintenance.getEquipmentId())) {
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

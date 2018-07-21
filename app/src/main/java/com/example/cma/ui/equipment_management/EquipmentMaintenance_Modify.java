package com.example.cma.ui.equipment_management;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.cma.R;
import com.example.cma.model.equipment_management.Equipment;
import com.example.cma.model.equipment_management.EquipmentMaintenance;
import com.example.cma.utils.AddressUtil;
import com.example.cma.utils.HttpUtil;
import com.example.cma.utils.ToastUtil;
import com.example.cma.utils.ViewUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EquipmentMaintenance_Modify extends AppCompatActivity implements View.OnClickListener,Spinner.OnItemSelectedListener{

    //data
    private EquipmentMaintenance equipmentMaintenance;
    private List<Equipment> equipmentList = new ArrayList<>();
    private List<String> spinnerData = new ArrayList<>();
    private ArrayAdapter<String> spinnerAdapter;

    private TextView name_text;
    private TextView model_text;
    private EditText maintenanceContent_text;
    private EditText maintenancePerson_text;
    private EditText confirmer_text;
    private TextView maintenanceDate_text;
    private Spinner spinner;

    private String equipmentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.equipment_maintenance_modify);
        initView();
        getEquipmentList();
        equipmentMaintenance = (EquipmentMaintenance)getIntent().getSerializableExtra("EquipmentMaintenance");
        setText();
    }

    public void initView() {
        name_text =  findViewById(R.id.name_text);
        model_text =  findViewById(R.id.model_text);
        maintenanceContent_text =  findViewById(R.id.maintenanceContent_text);
        maintenancePerson_text =  findViewById(R.id.maintenancePerson_text);
        confirmer_text =  findViewById(R.id.confirmer_text);
        maintenanceDate_text =  findViewById(R.id.maintenanceDate_text);

        spinner =  findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);

        findViewById(R.id.submit_button).setOnClickListener(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        ViewUtil.getInstance().setSupportActionBar(this, toolbar);
        maintenanceDate_text.setOnClickListener(this);
    }

    public void setText(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                name_text.setText(equipmentMaintenance.getName());
                model_text.setText(equipmentMaintenance.getModel());
                maintenanceContent_text.setText(equipmentMaintenance.getMaintenanceContent());
                maintenancePerson_text.setText(equipmentMaintenance.getMaintenanceContent());
                confirmer_text.setText(equipmentMaintenance.getConfirmer());
                maintenanceDate_text.setText(equipmentMaintenance.getMaintenanceDate());
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_button:
                onSave();
                break;
            case R.id.maintenanceDate_text:
                ViewUtil.getInstance().selectDate(EquipmentMaintenance_Modify.this, maintenanceDate_text);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackConfirm(true);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        onBackConfirm(false);
    }


    public void onSave() {
        if (name_text.getText().toString().isEmpty() ||
                model_text.getText().toString().isEmpty() ||
                maintenanceContent_text.getText().toString().isEmpty() ||
                maintenancePerson_text.getText().toString().isEmpty() ||
                confirmer_text.getText().toString().isEmpty() ||
                maintenanceDate_text.getText().toString().isEmpty()) {
            ToastUtil.showShort(EquipmentMaintenance_Modify.this, "请填写完整");
            return;
        }
        postSave();
    }

    public void postSave() {
        String address = AddressUtil.getAddress(AddressUtil.EquipmentMaintenance_modifyOne);
        RequestBody requestBody = new FormBody.Builder()
                .add("id",equipmentMaintenance.getId()+"")
                .add("equipmentId", equipmentId)
                .add("maintenanceContent", maintenanceContent_text.getText().toString())
                .add("maintenancePerson", maintenancePerson_text.getText().toString())
                .add("confirmer", confirmer_text.getText().toString())
                .add("maintenanceDate", maintenanceDate_text.getText().toString())
                .build();

        HttpUtil.sendOkHttpWithRequestBody(address, requestBody, new okhttp3.Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                Log.d("Maintanance_Add:", responseData);
                int code = 0;
                String msg = "";
                try {
                    JSONObject object = new JSONObject(responseData);
                    code = object.getInt("code");
                    msg = object.getString("msg");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (code == 200 && msg.equals("成功")) {
                    ToastUtil.showShort(EquipmentMaintenance_Modify.this, "修改成功");
                    finish();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showShort(EquipmentMaintenance_Modify.this, "修改失败");
            }
        });
    }

    public void onBackConfirm(boolean flag) {
        if (!name_text.getText().toString().isEmpty() ||
                !model_text.getText().toString().isEmpty() ||
                !maintenanceContent_text.getText().toString().isEmpty() ||
                !maintenancePerson_text.getText().toString().isEmpty() ||
                !confirmer_text.getText().toString().isEmpty() ||
                !maintenanceDate_text.getText().toString().isEmpty()) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(EquipmentMaintenance_Modify.this);
            dialog.setTitle("内容尚未保存");
            dialog.setMessage("是否退出？");
            dialog.setCancelable(true);
            dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            dialog.show();
        } else if (flag)
            finish();
        else
            super.onBackPressed();
    }

    public void getEquipmentList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String address = AddressUtil.getAddress(AddressUtil.Equipment_getAll);
                HttpUtil.sendOkHttpRequest(address, new okhttp3.Callback() {
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseData = response.body().string();
                        Log.d("Maintenance_Add", responseData);
                        parseJSONWithGSON(responseData);
                        showResponse();
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        ToastUtil.showShort(EquipmentMaintenance_Modify.this, "请求数据失败！");
                    }
                });
            }
        }).start();
    }

    private void parseJSONWithGSON(String jsonData) {
        JSONArray array = new JSONArray();
        try {
            JSONObject object = new JSONObject(jsonData);//最外层的JSONObject对象
            array = object.getJSONArray("data");
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<Equipment> newList = new Gson().fromJson(array.toString(), new TypeToken<List<Equipment>>() {
        }.getType());
        equipmentList.clear();
        equipmentList.addAll(newList);
    }

    private void showResponse() {
        for (Equipment equipment : equipmentList) {
            spinnerData.add(equipment.getEquipmentNumber());
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //适配器
                spinnerAdapter = new ArrayAdapter<>(EquipmentMaintenance_Modify.this, android.R.layout.simple_spinner_item, spinnerData);
                //设置样式
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //加载适配器
                spinner.setAdapter(spinnerAdapter);
                int i = 0;
                for(Equipment equipment:equipmentList){
                    if(equipment.getEquipmentNumber().equals(equipmentMaintenance.getEquipmentNumber()))
                        spinner.setSelection(i,true); //设置初始显示值
                    i++;
                }
                spinnerAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        for (Equipment equipment : equipmentList) {
            String selectedItem = arg0.getSelectedItem().toString();
            if (equipment.getEquipmentNumber().equals(selectedItem)){
                equipmentId = equipment.getId() + "";
                equipmentMaintenance.setName(equipment.getName());
                equipmentMaintenance.setModel(equipment.getModel());
                equipmentMaintenance.setEquipmentId(equipment.getId());
            }
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                name_text.setText(equipmentMaintenance.getName());
                model_text.setText(equipmentMaintenance.getModel());
            }
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        equipmentId = equipmentList.get(0).getId() + "";
    }
}

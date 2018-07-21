package com.example.cma.ui.equipment_management;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.cma.R;
import com.example.cma.model.equipment_management.Equipment;
import com.example.cma.model.equipment_management.EquipmentMaintenance;
import com.example.cma.utils.AddressUtil;
import com.example.cma.utils.HttpUtil;
import com.example.cma.utils.ToastUtil;
import com.example.cma.utils.ViewUtil;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EquipmentMaintenance_Info extends AppCompatActivity implements View.OnClickListener {

    private EquipmentMaintenance equipmentMaintenance;
    private TextView equipmentNumber_text;
    private TextView name_text;
    private TextView model_text;
    private TextView maintenanceContent_text;
    private TextView maintenancePerson_text;
    private TextView confirmer_text;
    private TextView maintenanceDate_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.equipment_maintenance_info);
        initView();
        equipmentMaintenance = (EquipmentMaintenance) getIntent().getSerializableExtra("EquipmentMaintenance");
        setText();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getEquipmentMaintenanceInfo();
    }

    public void initView() {
        equipmentNumber_text = findViewById(R.id.equipmentNumber_text);
        name_text = findViewById(R.id.name_text);
        model_text = findViewById(R.id.model_text);
        maintenanceContent_text = findViewById(R.id.maintenanceContent_text);
        maintenancePerson_text = findViewById(R.id.maintenancePerson_text);
        confirmer_text = findViewById(R.id.confirmer_text);
        maintenanceDate_text = findViewById(R.id.maintenanceDate_text);

        findViewById(R.id.delete_button).setOnClickListener(this);
        findViewById(R.id.edit_button).setOnClickListener(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        ViewUtil.getInstance().setSupportActionBar(this, toolbar);
        maintenanceDate_text.setOnClickListener(this);
    }

    public void setText() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                equipmentNumber_text.setText(equipmentMaintenance.getEquipmentNumber());
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
            case R.id.edit_button:
                if (equipmentMaintenance == null) {
                    ToastUtil.showShort(EquipmentMaintenance_Info.this, "此记录可能在别处已被删除");
                    return;
                }
                Intent intent = new Intent(EquipmentMaintenance_Info.this, EquipmentMaintenance_Modify.class);
                intent.putExtra("EquipmentMaintenance", equipmentMaintenance);
                startActivity(intent);
                break;
            case R.id.delete_button:
                if (equipmentMaintenance == null) {
                    ToastUtil.showShort(EquipmentMaintenance_Info.this, "此记录可能在别处已被删除");
                    return;
                }
                onDeleteConfirm();
                break;

            default:
                break;
        }
    }

    public void onDeleteConfirm() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(EquipmentMaintenance_Info.this);
        dialog.setMessage("确定删除？");
        dialog.setCancelable(false);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onDelete();
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Do nothing
            }
        });
        dialog.show();
    }

    public void onDelete() {
        String address = AddressUtil.getAddress(AddressUtil.EquipmentMaintenance_deleteOne);
        RequestBody requestBody = new FormBody.Builder().add("id", "" + equipmentMaintenance.getId()).build();
        HttpUtil.sendOkHttpWithRequestBody(address, requestBody, new okhttp3.Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                Log.d("Miantenance Delete", responseData);
                int code = 0;
                String msg = "";
                try {
                    JSONObject object = new JSONObject(responseData);
                    code = object.getInt("code");
                    msg = object.getString("msg");
                } catch (JSONException e) {
                    e.printStackTrace();
                    ToastUtil.showShort(EquipmentMaintenance_Info.this, "删除失败");
                }
                if (code == 200 && msg.equals("成功")) {
                    ToastUtil.showShort(EquipmentMaintenance_Info.this, "删除成功");
                    finish();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showShort(EquipmentMaintenance_Info.this, "删除失败");
            }
        });
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

    public void getEquipmentMaintenanceInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String address = AddressUtil.getAddress(AddressUtil.EquipmentMaintenance_getOne) + equipmentMaintenance.getId();
                HttpUtil.sendOkHttpRequest(address, new okhttp3.Callback() {
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseData = response.body().string();
                        Log.d("EquipMaintenance_Info", responseData);
                        parseJSONWithGSON(responseData, true);
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        ToastUtil.showShort(EquipmentMaintenance_Info.this, "请求数据失败！");
                    }
                });
            }
        }).start();
    }

    public void getEquipmentInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String address = AddressUtil.getAddress(AddressUtil.Equipment_getOne) + equipmentMaintenance.getEquipmentId();
                HttpUtil.sendOkHttpRequest(address, new okhttp3.Callback() {
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseData = response.body().string();
                        Log.d("Equipment_Info", responseData);
                        parseJSONWithGSON(responseData, false);
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        ToastUtil.showShort(EquipmentMaintenance_Info.this, "请求数据失败！");
                    }
                });
            }
        }).start();
    }

    private void parseJSONWithGSON(String jsonData, boolean flag) {
        String info = "";
        try {
            JSONObject object = new JSONObject(jsonData);//最外层的JSONObject对象
            info = object.getString("data");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (flag) {
            equipmentMaintenance = new Gson().fromJson(info, EquipmentMaintenance.class);
            //等获取“设备维修记录（equipmentMaintenance）”成功再去获取“设备信息（equipment）”
            getEquipmentInfo();
        } else {
            Equipment equipment = new Gson().fromJson(info, Equipment.class);
            if (equipment != null && equipmentMaintenance != null) {
                equipmentMaintenance.setName(equipment.getName());
                equipmentMaintenance.setModel(equipment.getModel());
                equipmentMaintenance.setEquipmentNumber(equipment.getEquipmentNumber());
                setText();
            }
        }
    }
}

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
import com.example.cma.model.equipment_management.EquipmentUse;
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

public class EquipmentUse_Info extends AppCompatActivity implements View.OnClickListener {

    private EquipmentUse equipmentUse;

    private TextView equipmentNumber_text;
    private TextView name_text;
    private TextView useDate_text;
    private TextView openDate_text;
    private TextView closeDate_text;
    private TextView sampleNumber_text;
    private TextView testProject_text;
    private TextView beforeUse_text;
    private TextView afterUse_text;
    private TextView user_text;
    private TextView remark_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.equipment_use_info);
        initView();
        equipmentUse = (EquipmentUse) getIntent().getSerializableExtra("EquipmentUse");
        setText();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getEquipmentUseInfo();
    }

    public void initView() {
        equipmentNumber_text = findViewById(R.id.equipmentNumber_text);
        name_text = findViewById(R.id.name_text);
        useDate_text = findViewById(R.id.useDate_text);
        openDate_text = findViewById(R.id.openDate_text);
        closeDate_text = findViewById(R.id.closeDate_text);
        sampleNumber_text = findViewById(R.id.sampleNumber_text);
        testProject_text = findViewById(R.id.testProject_text);
        beforeUse_text = findViewById(R.id.beforeUse_text);
        afterUse_text = findViewById(R.id.afterUse_text);
        user_text = findViewById(R.id.user_text);
        remark_text = findViewById(R.id.remark_text);

        findViewById(R.id.delete_button).setOnClickListener(this);
        findViewById(R.id.edit_button).setOnClickListener(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        ViewUtil.getInstance().setSupportActionBar(this, toolbar);
    }

    public void setText() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                equipmentNumber_text.setText(equipmentUse.getEquipmentNumber());
                name_text.setText(equipmentUse.getName());
                useDate_text.setText(equipmentUse.getUseDate());
                openDate_text.setText(equipmentUse.getOpenDate());
                closeDate_text.setText(equipmentUse.getCloseDate());
                sampleNumber_text.setText(equipmentUse.getSampleNumber());
                testProject_text.setText(equipmentUse.getTestProject());
                beforeUse_text.setText(equipmentUse.getBeforeUse());
                afterUse_text.setText(equipmentUse.getafterUse());
                user_text.setText(equipmentUse.getUser());
                remark_text.setText(equipmentUse.getRemark());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_button:
                Intent intent = new Intent(EquipmentUse_Info.this, EquipmentUse_Modify.class);
                intent.putExtra("EquipmentUse", equipmentUse);
                startActivity(intent);
                break;
            case R.id.delete_button:
                onDeleteConfirm();
                break;

            default:
                break;
        }
    }

    public void onDeleteConfirm() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(EquipmentUse_Info.this);
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
        String address = AddressUtil.getAddress(AddressUtil.EquipmentUse_deleteOne);
        RequestBody requestBody = new FormBody.Builder().add("id", "" + equipmentUse.getId()).build();
        HttpUtil.sendOkHttpWithRequestBody(address, requestBody, new okhttp3.Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                Log.d("EquipmentUse Delete", responseData);
                int code = 0;
                String msg = "";
                try {
                    JSONObject object = new JSONObject(responseData);
                    code = object.getInt("code");
                    msg = object.getString("msg");
                } catch (JSONException e) {
                    e.printStackTrace();
                    ToastUtil.showShort(EquipmentUse_Info.this, "删除失败");
                }
                if (code == 200 && msg.equals("成功")) {
                    ToastUtil.showShort(EquipmentUse_Info.this, "删除成功");
                    finish();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showShort(EquipmentUse_Info.this, "删除失败");
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

    public void getEquipmentUseInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String address = AddressUtil.getAddress(AddressUtil.EquipmentUse_getOne)+equipmentUse.getId();
                HttpUtil.sendOkHttpRequest(address, new okhttp3.Callback() {
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseData = response.body().string();
                        Log.d("EquipmentUse_Info", responseData);
                        parseJSONWithGSON(responseData, true);
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        ToastUtil.showShort(EquipmentUse_Info.this, "请求数据失败");
                    }
                });
            }
        }).start();
    }

    public void getEquipmentInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String address = AddressUtil.getAddress(AddressUtil.Equipment_getOne) + equipmentUse.getEquipmentId();
                HttpUtil.sendOkHttpRequest(address, new okhttp3.Callback() {
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseData = response.body().string();
                        Log.d("Equipment_Info", responseData);
                        parseJSONWithGSON(responseData, false);
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        ToastUtil.showShort(EquipmentUse_Info.this, "请求数据失败！");
                    }
                });
            }
        }).start();
    }

    /**
     * @param jsonData 服务器返回的数据
     * @param flag     判断当前解析的是 equipmentUse（设备使用记录） 还是 equipment（设备信息）
     */
    private void parseJSONWithGSON(String jsonData, boolean flag) {
        String info = "";
        try {
            JSONObject object = new JSONObject(jsonData);//最外层的JSONObject对象
            info = object.getString("data");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (flag) {
            equipmentUse = new Gson().fromJson(info, EquipmentUse.class);
            //等获取 设备使用记录（equipmentUse） 成功再去获取 设备信息（equipment）
            getEquipmentInfo();
        } else {
            Equipment equipment = new Gson().fromJson(info, Equipment.class);
            if (equipment != null && equipmentUse != null) {
                equipmentUse.setName(equipment.getName());
                equipmentUse.setEquipmentNumber(equipment.getEquipmentNumber());
                setText();
            }
        }
    }
}
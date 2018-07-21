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

public class Equipment_Info extends AppCompatActivity implements View.OnClickListener {

    private Equipment equipment;
    private TextView name_text;
    private TextView model_text;
    private TextView cpu_text;
    private TextView memory_text;
    private TextView hardDisk_text;
    private TextView equipmentNumber_text;
    private TextView application_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.equipment_info);
        initView();
        Intent intent = getIntent();
        equipment = (Equipment) intent.getSerializableExtra("Equipment");
        setText();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getEquipmentInfo();
    }

    public void initView() {
        name_text = findViewById(R.id.name_text);
        model_text = findViewById(R.id.model_text);
        cpu_text = findViewById(R.id.cpu_text);
        memory_text = findViewById(R.id.memory_text);
        hardDisk_text = findViewById(R.id.hardDisk_text);
        equipmentNumber_text = findViewById(R.id.equipmentNumber_text);
        application_text = findViewById(R.id.application_text);

        findViewById(R.id.delete_button).setOnClickListener(this);
        findViewById(R.id.edit_button).setOnClickListener(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        ViewUtil.getInstance().setSupportActionBar(this, toolbar);
    }

    public void setText() {
        Log.d("equipment", "aaaaa");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                name_text.setText(equipment.getName());
                model_text.setText(equipment.getModel());
                cpu_text.setText(equipment.getCpu());
                memory_text.setText(equipment.getMemory());
                hardDisk_text.setText(equipment.getHardDisk());
                equipmentNumber_text.setText(equipment.getEquipmentNumber());
                application_text.setText(equipment.getApplication());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_button:
                Intent intent = new Intent(Equipment_Info.this, Equipment_Modify.class);
                intent.putExtra("Equipment", equipment);
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
        AlertDialog.Builder dialog = new AlertDialog.Builder(Equipment_Info.this);
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
        String address = AddressUtil.getAddress(AddressUtil.Equipment_deleteOne);
        RequestBody requestBody = new FormBody.Builder().add("id", "" + equipment.getId()).build();
        HttpUtil.sendOkHttpWithRequestBody(address, requestBody, new okhttp3.Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                Log.d("Equipment_Info Delete", responseData);
                int code = 0;
                String msg = "";
                try {
                    JSONObject object = new JSONObject(responseData);
                    code = object.getInt("code");
                    msg = object.getString("msg");
                } catch (JSONException e) {
                    e.printStackTrace();
                    ToastUtil.showShort(Equipment_Info.this, "删除失败");
                }
                if (code == 200 && msg.equals("成功")) {
                    ToastUtil.showShort(Equipment_Info.this, "删除成功");
                    finish();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showShort(Equipment_Info.this, "删除失败");
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

    public void getEquipmentInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String address = AddressUtil.getAddress(AddressUtil.Equipment_getOne) + equipment.getId();
                HttpUtil.sendOkHttpRequest(address, new okhttp3.Callback() {
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseData = response.body().string();
                        Log.d("Equipment_Info", responseData);
                        parseJSONWithGSON(responseData);
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        ToastUtil.showShort(Equipment_Info.this, "请求数据失败");
                    }
                });
            }
        }).start();
    }

    private void parseJSONWithGSON(String jsonData) {
        String info = "";
        try {
            JSONObject object = new JSONObject(jsonData);
            info = object.getString("data");
        } catch (Exception e) {
            e.printStackTrace();
        }
        equipment = new Gson().fromJson(info, Equipment.class);
        if (equipment != null)
            setText();
    }
}

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

public class EquipmentUse_Add extends AppCompatActivity implements View.OnClickListener, Spinner.OnItemSelectedListener {
    private static final String TAG = "EquipmentUse_Add";
    //data
    private List<Equipment> equipmentList = new ArrayList<>();
    private List<String> spinnerData = new ArrayList<>();
    private ArrayAdapter<String> spinnerAdapter;

    private TextView useDate_text;
    private TextView openDate_text;
    private TextView closeDate_text;
    private EditText sampleNumber_text;
    private EditText testProject_text;
    private EditText beforeUse_text;
    private EditText afterUse_text;
    private EditText user_text;
    private EditText remark_text;
    private Spinner spinner;

    private String equipmentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.equipment_use_add);
        initView();
        getEquipmentList();
    }

    public void initView() {
        useDate_text = findViewById(R.id.useDate_text);
        openDate_text = findViewById(R.id.openDate_text);
        closeDate_text = findViewById(R.id.closeDate_text);
        sampleNumber_text = findViewById(R.id.sampleNumber_text);
        testProject_text = findViewById(R.id.testProject_text);
        beforeUse_text = findViewById(R.id.beforeUse_text);
        afterUse_text = findViewById(R.id.afterUse_text);
        user_text = findViewById(R.id.user_text);
        remark_text = findViewById(R.id.remark_text);

        spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        ViewUtil.getInstance().setSupportActionBar(this, toolbar);
        findViewById(R.id.submit_button).setOnClickListener(this);
        useDate_text.setOnClickListener(this);
        openDate_text.setOnClickListener(this);
        closeDate_text.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_button:
                onSave();
                break;
            case R.id.useDate_text:
                ViewUtil.getInstance().selectDate(EquipmentUse_Add.this, useDate_text);
                break;
            case R.id.openDate_text:
                ViewUtil.getInstance().selectTime(EquipmentUse_Add.this, openDate_text);
                break;
            case R.id.closeDate_text:
                ViewUtil.getInstance().selectTime(EquipmentUse_Add.this, closeDate_text);
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
        if (equipmentList.size() == 0) {
            ToastUtil.showShort(EquipmentUse_Add.this, "无可操作设备");
            return;
        }

        if (useDate_text.getText().toString().isEmpty() ||
                openDate_text.getText().toString().isEmpty() ||
                closeDate_text.getText().toString().isEmpty() ||
                sampleNumber_text.getText().toString().isEmpty() ||
                testProject_text.getText().toString().isEmpty() ||
                beforeUse_text.getText().toString().isEmpty() ||
                afterUse_text.getText().toString().isEmpty() ||
                user_text.getText().toString().isEmpty() ||
                remark_text.getText().toString().isEmpty()) {
            ToastUtil.showShort(EquipmentUse_Add.this, "请填写完整");
            return;
        }
        postSave();
    }

    public void postSave() {
        String address = AddressUtil.getAddress(AddressUtil.EquipmentUse_addOne);
        RequestBody requestBody = new FormBody.Builder()
                .add("equipmentId", equipmentId)
                .add("useDate", useDate_text.getText().toString())
                .add("openDate", openDate_text.getText().toString())
                .add("closeDate", closeDate_text.getText().toString())
                .add("sampleNumber", sampleNumber_text.getText().toString())
                .add("testProject", testProject_text.getText().toString())
                .add("beforeUse", beforeUse_text.getText().toString())
                .add("afterUse", afterUse_text.getText().toString())
                .add("user", user_text.getText().toString())
                .add("remark", remark_text.getText().toString())
                .build();

        HttpUtil.sendOkHttpWithRequestBody(address, requestBody, new okhttp3.Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                Log.d(TAG, responseData);
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
                    ToastUtil.showShort(EquipmentUse_Add.this, "添加成功");
                    finish();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showShort(EquipmentUse_Add.this, "添加失败");
            }
        });
    }

    public void onBackConfirm(boolean flag) {
        if (!useDate_text.getText().toString().isEmpty() ||
                !openDate_text.getText().toString().isEmpty() ||
                !closeDate_text.getText().toString().isEmpty() ||
                !sampleNumber_text.getText().toString().isEmpty() ||
                !testProject_text.getText().toString().isEmpty() ||
                !beforeUse_text.getText().toString().isEmpty() ||
                !afterUse_text.getText().toString().isEmpty() ||
                !user_text.getText().toString().isEmpty() ||
                !remark_text.getText().toString().isEmpty()) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(EquipmentUse_Add.this);
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
                        Log.d(TAG, responseData);
                        parseJSONWithGSON(responseData);
                        showResponse();
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        ToastUtil.showShort(EquipmentUse_Add.this, "请求数据失败");
                    }
                });
            }
        }).start();
    }

    private void parseJSONWithGSON(String jsonData) {
        JSONArray array = new JSONArray();
        try {
            JSONObject object = new JSONObject(jsonData);
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
            //检查该设备是否处于准用状态
            //if (equipment.getState() == 1)
                spinnerData.add(equipment.getEquipmentNumber());
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                /*
                * 设置适配器的样式，加载适配器
                * */
                spinnerAdapter = new ArrayAdapter<>(EquipmentUse_Add.this, android.R.layout.simple_spinner_item, spinnerData);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(spinnerAdapter);
                spinnerAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        for (Equipment equipment : equipmentList) {
            String selectedItem = arg0.getSelectedItem().toString();
            if (equipment.getEquipmentNumber().equals(selectedItem))
                equipmentId = equipment.getId() + "";
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        equipmentId = equipmentList.get(0).getId() + "";
    }

}

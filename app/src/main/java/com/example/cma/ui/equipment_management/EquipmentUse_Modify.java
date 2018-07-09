package com.example.cma.ui.equipment_management;

import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.cma.R;
import com.example.cma.model.equipment_management.Equipment;
import com.example.cma.model.equipment_management.EquipmentUse;
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

public class EquipmentUse_Modify extends AppCompatActivity implements View.OnClickListener,Spinner.OnItemSelectedListener {

    //data
    private EquipmentUse equipmentUse;
    private List<Equipment> equipmentList = new ArrayList<>();
    private List<String> spinnerData = new ArrayList<String>();
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
    private Button submitButton;
    private Toolbar toolbar;

    private String equipmentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.equipment_use_modify);
        initView();
        equipmentUse = (EquipmentUse)getIntent().getSerializableExtra("EquipmentUse");
        getEquipmentList();
        setText();
    }

    public void initView() {
        useDate_text = (TextView) findViewById(R.id.useDate_text);
        openDate_text = (TextView) findViewById(R.id.openDate_text);
        closeDate_text = (TextView) findViewById(R.id.closeDate_text);
        sampleNumber_text = (EditText) findViewById(R.id.sampleNumber_text);
        testProject_text = (EditText) findViewById(R.id.testProject_text);
        beforeUse_text = (EditText) findViewById(R.id.beforeUse_text);
        afterUse_text = (EditText) findViewById(R.id.afterUse_text);
        user_text = (EditText) findViewById(R.id.user_text);
        remark_text = (EditText) findViewById(R.id.remark_text);

        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);

        submitButton = (Button) findViewById(R.id.submit_button);
        submitButton.setOnClickListener(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //设置Toolbar左边显示一个返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        useDate_text.setOnClickListener(this);
        openDate_text.setOnClickListener(this);
        closeDate_text.setOnClickListener(this);
    }

    public void setText(){
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_button:
                onSave();
                break;
            case R.id.useDate_text:
                ViewUtil.getInstance().selectDate(EquipmentUse_Modify.this, useDate_text);
                break;
            case R.id.openDate_text:
                ViewUtil.getInstance().selectDate(EquipmentUse_Modify.this, openDate_text);
                break;
            case R.id.closeDate_text:
                ViewUtil.getInstance().selectDate(EquipmentUse_Modify.this, closeDate_text);
                break;
            default:
                break;
        }
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

    public void onSave() {
        if (useDate_text.getText().toString().isEmpty() ||
                openDate_text.getText().toString().isEmpty() ||
                closeDate_text.getText().toString().isEmpty() ||
                sampleNumber_text.getText().toString().isEmpty() ||
                testProject_text.getText().toString().isEmpty() ||
                beforeUse_text.getText().toString().isEmpty() ||
                afterUse_text.getText().toString().isEmpty() ||
                user_text.getText().toString().isEmpty() ||
                remark_text.getText().toString().isEmpty()) {
            ToastUtil.showShort(EquipmentUse_Modify.this, "请填写完整！");
            return;
        }
        postSave();
    }

    public void postSave() {
        String address = AddressUtil.EquipmentUse_modifyOne();
        RequestBody requestBody = new FormBody.Builder()
                .add("id",equipmentUse.getId()+"")
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
                Log.d("EquipmentApp_Add:", responseData);
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
                    ToastUtil.showShort(EquipmentUse_Modify.this, "提交成功！");
                    finish();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showShort(EquipmentUse_Modify.this, "提交失败！");
            }
        });
    }

    public void getEquipmentList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String address = AddressUtil.Equipment_getAll();
                HttpUtil.sendOkHttpRequest(address, new okhttp3.Callback() {
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseData = response.body().string();
                        Log.d("EquipmentApp_Add", responseData);
                        parseJSONWithGSON(responseData);
                        showResponse();
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        ToastUtil.showShort(EquipmentUse_Modify.this, "请求数据失败！");
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
        List<Equipment> newList = new Gson().fromJson(array.toString(), new TypeToken<List<Equipment>>() {}.getType());
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
                spinnerAdapter = new ArrayAdapter<String>(EquipmentUse_Modify.this, android.R.layout.simple_spinner_item, spinnerData);
                //设置样式
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //加载适配器
                spinner.setAdapter(spinnerAdapter);
                int i = 0;
                for(Equipment equipment:equipmentList){
                    if(equipment.getEquipmentNumber().equals(equipmentUse.getEquipmentNumber()))
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
            if (equipment.getEquipmentNumber().equals(selectedItem))
                equipmentId = equipment.getId() + "";
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        equipmentId = equipmentList.get(0).getId() + "";
    }
}

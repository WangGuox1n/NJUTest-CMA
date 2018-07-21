package com.example.cma.ui.equipment_management;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.cma.R;
import com.example.cma.model.equipment_management.Equipment;
import com.example.cma.model.equipment_management.EquipmentApplication;
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

public class EquipmentApplication_Modify extends AppCompatActivity implements View.OnClickListener,CompoundButton.OnCheckedChangeListener,Spinner.OnItemSelectedListener{

    //data
    private EquipmentApplication equipmentApplication;
    private List<Equipment> equipmentList= new ArrayList<>();
    private List<String> spinnerData = new ArrayList<>();
    private ArrayAdapter<String> spinnerAdapter;

    private EditText applicant_text;
    private TextView applicationDate_text;
    private EditText applicationPurpose_text;
    private EditText softwareInfo_text;
    private EditText auditor_text;
    private TextView auditDate_text;
    private EditText auditOpinion_text;
    private CheckBox service_check;
    private CheckBox test_check;
    private Spinner spinner;


    private boolean isServiceSelected;
    private boolean isTestSelected;
    private String equipmentNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.equipment_application_modify);
        initView();
        Intent intent = getIntent();
        equipmentApplication = (EquipmentApplication)intent.getSerializableExtra("EquipmentApplication");
        setText();
        getEquipmentList();
    }

    public void initView(){
        applicant_text = findViewById(R.id.applicant_text);
        applicationDate_text = findViewById(R.id.applicationDate_text);
        applicationPurpose_text = findViewById(R.id.applicationPurpose_text);
        softwareInfo_text = findViewById(R.id.softwareInfo_text);
        auditor_text = findViewById(R.id.auditor_text);
        auditDate_text = findViewById(R.id.auditDate_text);
        auditOpinion_text = findViewById(R.id.auditOpinion_text);
        service_check = findViewById(R.id.service_check);
        test_check = findViewById(R.id.test_check);
        spinner = findViewById(R.id.spinner);
        service_check.setOnCheckedChangeListener(this);
        test_check.setOnCheckedChangeListener(this);
        spinner.setOnItemSelectedListener(this);

        findViewById(R.id.submit_button).setOnClickListener(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        ViewUtil.getInstance().setSupportActionBar(this, toolbar);

        LinearLayout applicationDate_layout=findViewById(R.id.applicationDate_layout);
        applicationDate_text.setOnClickListener(this);
        applicationDate_layout.setOnClickListener(this);

        LinearLayout auditDate_layout=findViewById(R.id.auditDate_layout);
        auditDate_text.setOnClickListener(this);
        auditDate_layout.setOnClickListener(this);
    }

    public void setText(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                applicant_text.setText(equipmentApplication.getApplicant());
                applicationDate_text.setText(equipmentApplication.getApplicationDate());
                applicationPurpose_text.setText(equipmentApplication.getApplicationPurpose());
                softwareInfo_text.setText(equipmentApplication.getSoftwareInfo());
                auditor_text.setText(equipmentApplication.getAuditor());
                auditDate_text.setText(equipmentApplication.getAuditDate());
                auditOpinion_text.setText(equipmentApplication.getAuditOpinion());
                if(equipmentApplication.getEquipmentUse()==3){
                    service_check.setChecked(true);
                    test_check.setChecked(true);
                }else if(equipmentApplication.getEquipmentUse()==2)
                    test_check.setChecked(true);
                else if(equipmentApplication.getEquipmentUse()==1)
                    service_check.setChecked(true);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submit_button:
                onSave();
                break;
            case R.id.applicationDate_layout:
            case R.id.applicationDate_text:
                ViewUtil.getInstance().selectDate(EquipmentApplication_Modify.this,applicationDate_text);
                break;
            case R.id.auditDate_layout:
            case R.id.auditDate_text:
                ViewUtil.getInstance().selectDate(EquipmentApplication_Modify.this,auditDate_text);
                break;
            default:break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(buttonView.getId() == R.id.service_check){
            isServiceSelected = isChecked;
        }else if(buttonView.getId() == R.id.test_check){
            isTestSelected = isChecked;
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

    public void onSave(){
        if(applicant_text.getText().toString().isEmpty()||
                applicationDate_text.getText().toString().isEmpty()||
                applicationPurpose_text.getText().toString().isEmpty() ||
                softwareInfo_text.getText().toString().isEmpty()||
                auditor_text.getText().toString().isEmpty()||
                auditDate_text.getText().toString().isEmpty()||
                auditOpinion_text.getText().toString().isEmpty()){
            ToastUtil.showShort(EquipmentApplication_Modify.this, "请填写完整");
            return;
        }
        if(equipmentNumber == null){
            ToastUtil.showShort(EquipmentApplication_Modify.this, "无可申请使用的设备");
            return;
        }
        postSave();
    }

    public void postSave(){
        String address = AddressUtil.getAddress(AddressUtil.EquipmentApplication_modifyOne);
        RequestBody requestBody = new FormBody.Builder()
                .add("id",equipmentApplication.getId()+"")
                .add("applicant",applicant_text.getText().toString())
                .add("applicationDate",applicationDate_text.getText().toString())
                .add("applicationPurpose",applicationPurpose_text.getText().toString())
                .add("equipmentUse",getEquipmentUse())
                .add("equipmentNumber",equipmentNumber)
                .add("softwareInfo",softwareInfo_text.getText().toString())
                .add("auditor",auditor_text.getText().toString())
                .add("auditDate",auditDate_text.getText().toString())
                .add("auditOpinion",auditOpinion_text.getText().toString())
                .build();

        HttpUtil.sendOkHttpWithRequestBody(address,requestBody,new okhttp3.Callback(){
            @Override
            public void onResponse(Call call, Response response)throws IOException {
                String responseData = response.body().string();
                Log.d("EquipmentApp_Add:",responseData);
                int code = 0;
                String msg = "";
                try {
                    JSONObject object = new JSONObject(responseData);
                    code = object.getInt("code");
                    msg = object.getString("msg");
                }catch (JSONException e){
                    e.printStackTrace();
                }
                if(code == 200 && msg.equals("成功")) {
                    ToastUtil.showShort(EquipmentApplication_Modify.this, "修改成功");
                    finish();
                }
            }
            @Override
            public void onFailure(Call call,IOException e){
                ToastUtil.showShort(EquipmentApplication_Modify.this, "修改失败");
            }
        });
    }

    private String getEquipmentUse(){
        if(isServiceSelected&&isTestSelected)
            return Integer.toString(3);
        else if(isTestSelected)
            return Integer.toString(2);
        else if(isServiceSelected)
            return Integer.toString(1);
        else
            return Integer.toString(0);
    }

    public void getEquipmentList(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String address = AddressUtil.getAddress(AddressUtil.Equipment_getAll);
                HttpUtil.sendOkHttpRequest(address,new okhttp3.Callback(){
                    @Override
                    public void onResponse(Call call, Response response)throws IOException {
                        String responseData = response.body().string();
                        Log.d("EquipmentApp_Add",responseData);
                        parseJSONWithGSON(responseData);
                        showResponse();
                    }
                    @Override
                    public void onFailure(Call call,IOException e){
                        ToastUtil.showShort(EquipmentApplication_Modify.this, "请求数据失败");
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
        List<Equipment> newList = new Gson().fromJson(array.toString(),new TypeToken<List<Equipment>>(){}.getType());
        equipmentList.clear();
        equipmentList.addAll(newList);
    }

    private void showResponse() {
        for(Equipment equipment:equipmentList){
            spinnerData.add(equipment.getEquipmentNumber());
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //适配器
                spinnerAdapter = new ArrayAdapter<>(EquipmentApplication_Modify.this, android.R.layout.simple_spinner_item, spinnerData);
                //设置样式
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //加载适配器
                spinner.setAdapter(spinnerAdapter);
                int i = 0;
                for(Equipment equipment:equipmentList){
                    if(equipment.getEquipmentNumber().equals(equipmentApplication.getEquipmentNumber()))
                        spinner.setSelection(i,true); //设置初始显示值
                    i++;
                }
                spinnerAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        for(Equipment equipment : equipmentList) {
            String selectedItem = arg0.getSelectedItem().toString();
            if(equipment.getEquipmentNumber().equals(selectedItem))
                equipmentNumber = selectedItem;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        equipmentNumber = equipmentList.get(0).getEquipmentNumber();
    }
}

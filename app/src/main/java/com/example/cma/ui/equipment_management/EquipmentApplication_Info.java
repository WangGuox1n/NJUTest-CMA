package com.example.cma.ui.equipment_management;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.cma.R;
import com.example.cma.model.equipment_management.EquipmentApplication;
import com.example.cma.model.equipment_management.EquipmentApplication;
import com.example.cma.utils.AddressUtil;
import com.example.cma.utils.HttpUtil;
import com.example.cma.utils.ToastUtil;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EquipmentApplication_Info extends AppCompatActivity implements View.OnClickListener{

    private EquipmentApplication equipmentApplication;
    private TextView applicant_text;
    private TextView applicationDate_text;
    private TextView applicationPurpose_text;
    private TextView softwareInfo_text;
    private TextView equipmentNumber_text;
    private TextView auditor_text;
    private TextView auditDate_text;
    private TextView auditOpinion_text;
    private CheckBox service_check;
    private CheckBox test_check;
    private Button deleteButton;
    private Button editButton;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.equipment_application_info);
        initView();
        equipmentApplication = (EquipmentApplication)getIntent().getSerializableExtra("EquipmentApplication");
        setText();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getEquipmentApplicationInfo();
    }

    public void initView(){
        applicant_text = (TextView)findViewById(R.id.applicant_text);
        applicationDate_text = (TextView)findViewById(R.id.applicationDate_text);
        applicationPurpose_text = (TextView)findViewById(R.id.applicationPurpose_text);
        equipmentNumber_text = (TextView)findViewById(R.id.equipmentNumber_text);
        softwareInfo_text = (TextView)findViewById(R.id.softwareInfo_text);
        auditor_text = (TextView)findViewById(R.id.auditor_text);
        auditDate_text = (TextView)findViewById(R.id.auditDate_text);
        auditOpinion_text = (TextView)findViewById(R.id.auditOpinion_text);
        service_check = (CheckBox)findViewById(R.id.service_check);
        test_check = (CheckBox)findViewById(R.id.test_check);

        deleteButton = (Button)findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(this);
        editButton = (Button)findViewById(R.id.edit_button);
        editButton.setOnClickListener(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //设置Toolbar左边显示一个返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void setText(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                applicant_text.setText(equipmentApplication.getApplicant());
                applicationDate_text.setText(equipmentApplication.getApplicationDate());
                applicationPurpose_text.setText(equipmentApplication.getApplicationPurpose());
                equipmentNumber_text.setText(equipmentApplication.getEquipmentNumber());
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
            case R.id.edit_button:
                if(equipmentApplication == null){
                    ToastUtil.showShort(EquipmentApplication_Info.this,"此记录可能在别处已被删除");
                    return;
                }
                Intent intent = new Intent(EquipmentApplication_Info.this, EquipmentApplication_Modify.class);
                intent.putExtra("EquipmentApplication", equipmentApplication);
                startActivity(intent);
                break;
            case R.id.delete_button:
                if(equipmentApplication == null){
                    ToastUtil.showShort(EquipmentApplication_Info.this,"此记录可能在别处已被删除");
                    return;
                }
                onDeleteConfirm();
                break;

            default:break;
        }
    }

    public void onDeleteConfirm(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(EquipmentApplication_Info.this);
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

    public void onDelete(){
        String address = AddressUtil.EquipmentApplication_deleteOne();
        RequestBody requestBody=new FormBody.Builder().add("id",""+equipmentApplication.getId()).build();
        HttpUtil.sendOkHttpWithRequestBody(address,requestBody,new okhttp3.Callback(){
            @Override
            public void onResponse(Call call, Response response)throws IOException {
                String responseData = response.body().string();
                Log.d("EquipmentApp Delete",responseData);
                int code = 0;
                String msg = "";
                try {
                    JSONObject object = new JSONObject(responseData);
                    code = object.getInt("code");
                    msg = object.getString("msg");
                }catch (JSONException e){
                    e.printStackTrace();
                    ToastUtil.showShort(EquipmentApplication_Info.this,"删除失败");
                }
                if(code == 200 && msg.equals("成功")) {
                    ToastUtil.showShort(EquipmentApplication_Info.this,"删除成功");
                    finish();
                }
            }
            @Override
            public void onFailure(Call call,IOException e){
                ToastUtil.showShort(EquipmentApplication_Info.this,"删除失败");
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

    public void getEquipmentApplicationInfo(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String address = AddressUtil.EquipmentApplication_getOne(equipmentApplication.getId());
                HttpUtil.sendOkHttpRequest(address,new okhttp3.Callback(){
                    @Override
                    public void onResponse(Call call, Response response)throws IOException {
                        String responseData = response.body().string();
                        Log.d("EquipmentApp_Info",responseData);
                        parseJSONWithGSON(responseData);
                    }
                    @Override
                    public void onFailure(Call call,IOException e){
                        ToastUtil.showShort(EquipmentApplication_Info.this, "请求数据失败！");
                    }
                });
            }
        }).start();
    }

    private void parseJSONWithGSON(String jsonData){
        String info = "";
        try {
            JSONObject object = new JSONObject(jsonData);//最外层的JSONObject对象
            info = object.getString("data");
        }catch (Exception e){
            e.printStackTrace();
        }
        equipmentApplication = new Gson().fromJson(info,EquipmentApplication.class);
        if(equipmentApplication!=null)
            setText();
    }
}

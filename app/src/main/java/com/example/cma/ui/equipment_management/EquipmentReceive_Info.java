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
import android.widget.TextView;

import com.example.cma.R;
import com.example.cma.model.equipment_management.EquipmentReceive;
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

public class EquipmentReceive_Info extends AppCompatActivity implements View.OnClickListener{

    private EquipmentReceive equipmentReceive;
    private TextView name_text;
    private TextView model_text;
    private TextView manufacturer_text;
    private TextView receiveSituation_text;
    private TextView recipient_text;
    private TextView receiveDate_text;
    private TextView equipmentSituation_text;
    private TextView acceptance_text;
    private TextView acceptancePerson_text;
    private TextView acceptanceDate_text;
    private Button deleteButton;
    private Button editButton;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.equipment_receive_info);
        initView();
        Intent intent = getIntent();
        equipmentReceive = (EquipmentReceive)intent.getSerializableExtra("EquipmentReceive");
        setText();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getEquipmentReceiveInfo();
    }

    public void initView(){
        name_text = (TextView)findViewById(R.id.name_text);
        model_text = (TextView)findViewById(R.id.model_text);
        manufacturer_text = (TextView)findViewById(R.id.manufacturer_text);
        receiveSituation_text = (TextView)findViewById(R.id.receiveSituation_text);
        recipient_text = (TextView)findViewById(R.id.recipient_text);
        receiveDate_text = (TextView)findViewById(R.id.receiveDate_text);
        equipmentSituation_text = (TextView)findViewById(R.id.equipmentSituation_text);
        acceptance_text = (TextView)findViewById(R.id.acceptance_text);
        acceptancePerson_text = (TextView)findViewById(R.id.acceptancePerson_text);
        acceptanceDate_text = (TextView)findViewById(R.id.acceptanceDate_text);

        deleteButton = (Button)findViewById(R.id.delete_button);
        editButton = (Button)findViewById(R.id.edit_button);
        deleteButton.setOnClickListener(this);
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
                name_text.setText(equipmentReceive.getName());
                model_text.setText(equipmentReceive.getModel());
                manufacturer_text.setText(equipmentReceive.getManufacturer());
                receiveSituation_text.setText(equipmentReceive.getReceiveSituation());
                recipient_text.setText(equipmentReceive.getRecipient());
                receiveDate_text.setText(equipmentReceive.getReceiveDate());
                equipmentSituation_text.setText(equipmentReceive.getEquipmentSituation());
                acceptance_text.setText(equipmentReceive.getAcceptance());
                acceptancePerson_text.setText(equipmentReceive.getAcceptancePerson());
                acceptanceDate_text.setText(equipmentReceive.getAcceptanceDate());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edit_button:
                Intent intent = new Intent(EquipmentReceive_Info.this, EquipmentReceive_Modify.class);
                intent.putExtra("EquipmentReceive", equipmentReceive);
                startActivity(intent);
                break;
            case R.id.delete_button:
                onDeleteConfirm();
                break;

            default:break;
        }
    }

    public void onDeleteConfirm(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(EquipmentReceive_Info.this);
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
        String address = AddressUtil.EquipmentReceive_deleteOne();
        RequestBody requestBody=new FormBody.Builder().add("id",""+equipmentReceive.getId()).build();
        HttpUtil.sendOkHttpWithRequestBody(address,requestBody,new okhttp3.Callback(){
            @Override
            public void onResponse(Call call, Response response)throws IOException {
                String responseData = response.body().string();
                Log.d("EquipmentReceive Delete",responseData);
                int code = 0;
                String msg = "";
                try {
                    JSONObject object = new JSONObject(responseData);
                    code = object.getInt("code");
                    msg = object.getString("msg");
                }catch (JSONException e){
                    e.printStackTrace();
                    ToastUtil.showShort(EquipmentReceive_Info.this,"删除失败");
                }
                if(code == 200 && msg.equals("成功")) {
                    ToastUtil.showShort(EquipmentReceive_Info.this,"删除成功");
                    finish();
                }
            }
            @Override
            public void onFailure(Call call,IOException e){
                ToastUtil.showShort(EquipmentReceive_Info.this,"删除失败");
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

    public void getEquipmentReceiveInfo(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String address = AddressUtil.EquipmentReceive_getOne(equipmentReceive.getId());
                HttpUtil.sendOkHttpRequest(address,new okhttp3.Callback(){
                    @Override
                    public void onResponse(Call call, Response response)throws IOException {
                        String responseData = response.body().string();
                        Log.d("EquipmentReceive_Info",responseData);
                        parseJSONWithGSON(responseData);
                    }
                    @Override
                    public void onFailure(Call call,IOException e){
                        ToastUtil.showShort(EquipmentReceive_Info.this, "请求数据失败！");
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
        equipmentReceive = new Gson().fromJson(info,EquipmentReceive.class);
        if(equipmentReceive!=null)
            setText();
    }
}

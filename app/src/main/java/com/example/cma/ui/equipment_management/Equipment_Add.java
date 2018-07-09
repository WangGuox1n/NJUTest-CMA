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
import android.widget.Button;
import android.widget.EditText;

import com.example.cma.R;
import com.example.cma.utils.AddressUtil;
import com.example.cma.utils.HttpUtil;
import com.example.cma.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Equipment_Add extends AppCompatActivity implements View.OnClickListener{

    private EditText name_text;
    private EditText model_text;
    private EditText cpu_text;
    private EditText memory_text;
    private EditText hardDisk_text;
    private EditText equipmentNumber_text;
    private EditText application_text;
    private Button submitButton;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.equipment_add);
        initView();
    }

    public void initView(){
        name_text = (EditText)findViewById(R.id.name_text);
        model_text = (EditText)findViewById(R.id.model_text);
        cpu_text = (EditText)findViewById(R.id.cpu_text);
        memory_text = (EditText)findViewById(R.id.memory_text);
        hardDisk_text = (EditText)findViewById(R.id.hardDisk_text);
        equipmentNumber_text = (EditText)findViewById(R.id.equipmentNumber_text);
        application_text = (EditText)findViewById(R.id.application_text);

        submitButton = (Button)findViewById(R.id.submit_button);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //设置Toolbar左边显示一个返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        submitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submit_button:
                onSave();
                break;
            default:break;
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


    public void onSave(){
        if(name_text.getText().toString().isEmpty()||
                model_text.getText().toString().isEmpty()||
                cpu_text.getText().toString().isEmpty() ||
                memory_text.getText().toString().isEmpty()||
                hardDisk_text.getText().toString().isEmpty()||
                equipmentNumber_text.getText().toString().isEmpty()||
                application_text.getText().toString().isEmpty()){
            ToastUtil.showShort(Equipment_Add.this, "请填写完整！");
            return;
        }
        postSave();
    }

    public void postSave(){
        String address = AddressUtil.Equipment_addOne();
        RequestBody requestBody = new FormBody.Builder()
                .add("name",name_text.getText().toString())
                .add("model",model_text.getText().toString())
                .add("cpu",cpu_text.getText().toString())
                .add("memory",memory_text.getText().toString())
                .add("hardDisk",hardDisk_text.getText().toString())
                .add("equipmentNumber",equipmentNumber_text.getText().toString())
                .add("application",application_text.getText().toString())
                .add("state","0")
                .build();

        HttpUtil.sendOkHttpWithRequestBody(address,requestBody,new okhttp3.Callback(){
            @Override
            public void onResponse(Call call, Response response)throws IOException {
                String responseData = response.body().string();
                Log.d("Equipment_Add:",responseData);
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
                    ToastUtil.showShort(Equipment_Add.this, "提交成功！");
                    finish();
                }
            }
            @Override
            public void onFailure(Call call,IOException e){
                ToastUtil.showShort(Equipment_Add.this, "提交失败！");
            }
        });
    }

    public void onBackConfirm(boolean flag){
        if(!name_text.getText().toString().isEmpty()||
                !model_text.getText().toString().isEmpty()||
                !cpu_text.getText().toString().isEmpty() ||
                !memory_text.getText().toString().isEmpty()||
                !hardDisk_text.getText().toString().isEmpty()||
                !equipmentNumber_text.getText().toString().isEmpty()||
                !application_text.getText().toString().isEmpty())  {
            AlertDialog.Builder dialog=new AlertDialog.Builder(Equipment_Add.this);
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
        }else if(flag)
            finish();
        else
            super.onBackPressed();
    }
}

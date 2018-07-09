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
import android.widget.TextView;

import com.example.cma.R;
import com.example.cma.utils.AddressUtil;
import com.example.cma.utils.HttpUtil;
import com.example.cma.utils.ToastUtil;
import com.example.cma.utils.ViewUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EquipmentReceive_Add extends AppCompatActivity implements View.OnClickListener{

    private EditText name_text;
    private EditText model_text;
    private EditText manufacturer_text;
    private EditText receiveSituation_text;
    private EditText recipient_text;
    private TextView receiveDate_text;
    private EditText equipmentSituation_text;
    private EditText acceptance_text;
    private EditText acceptancePerson_text;
    private TextView acceptanceDate_text;
    private Button submitButton;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.equipment_receive_add);
        initView();
    }

    public void initView(){
        name_text = (EditText)findViewById(R.id.name_text);
        model_text = (EditText)findViewById(R.id.model_text);
        manufacturer_text = (EditText)findViewById(R.id.manufacturer_text);
        receiveSituation_text = (EditText)findViewById(R.id.receiveSituation_text);
        recipient_text = (EditText)findViewById(R.id.recipient_text);
        receiveDate_text = (TextView)findViewById(R.id.receiveDate_text);
        equipmentSituation_text = (EditText)findViewById(R.id.equipmentSituation_text);
        acceptance_text = (EditText)findViewById(R.id.acceptance_text);
        acceptancePerson_text = (EditText)findViewById(R.id.acceptancePerson_text);
        acceptanceDate_text = (TextView)findViewById(R.id.acceptanceDate_text);

        submitButton = (Button)findViewById(R.id.submit_button);
        submitButton.setOnClickListener(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //设置Toolbar左边显示一个返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        receiveDate_text.setOnClickListener(this);
        acceptanceDate_text.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submit_button:
                onSave();
                break;
            case R.id.receiveDate_text:
                ViewUtil.getInstance().selectDate(EquipmentReceive_Add.this,receiveDate_text);
                break;
            case R.id.acceptanceDate_text:
                ViewUtil.getInstance().selectDate(EquipmentReceive_Add.this,acceptanceDate_text);
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
                manufacturer_text.getText().toString().isEmpty() ||
                receiveSituation_text.getText().toString().isEmpty()||
                recipient_text.getText().toString().isEmpty()||
                receiveDate_text.getText().toString().isEmpty()||
                equipmentSituation_text.getText().toString().isEmpty()||
                acceptance_text.getText().toString().isEmpty()||
                acceptancePerson_text.getText().toString().isEmpty()||
                acceptanceDate_text.getText().toString().isEmpty()){
            ToastUtil.showShort(EquipmentReceive_Add.this, "请填写完整！");
            return;
        }
        postSave();
    }

    public void postSave(){
        String address = AddressUtil.EquipmentReceive_addOne();
        RequestBody requestBody = new FormBody.Builder()
                .add("name",name_text.getText().toString())
                .add("model",model_text.getText().toString())
                .add("manufacturer",manufacturer_text.getText().toString())
                .add("receiveSituation",receiveSituation_text.getText().toString())
                .add("recipient",recipient_text.getText().toString())
                .add("receiveDate",receiveDate_text.getText().toString())
                .add("equipmentSituation",equipmentSituation_text.getText().toString())
                .add("acceptance",acceptance_text.getText().toString())
                .add("acceptancePerson",acceptancePerson_text.getText().toString())
                .add("acceptanceDate",acceptanceDate_text.getText().toString())
                .build();

        HttpUtil.sendOkHttpWithRequestBody(address,requestBody,new okhttp3.Callback(){
            @Override
            public void onResponse(Call call, Response response)throws IOException {
                String responseData = response.body().string();
                Log.d("EquipmentReceive_Add:",responseData);
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
                    ToastUtil.showShort(EquipmentReceive_Add.this, "提交成功！");
                    finish();
                }
            }
            @Override
            public void onFailure(Call call,IOException e){
                ToastUtil.showShort(EquipmentReceive_Add.this, "提交失败！");
            }
        });
    }

    public void onBackConfirm(boolean flag){
        if(!name_text.getText().toString().isEmpty()||
                !model_text.getText().toString().isEmpty()||
                !manufacturer_text.getText().toString().isEmpty() ||
                !receiveSituation_text.getText().toString().isEmpty()||
                !recipient_text.getText().toString().isEmpty()||
                !receiveDate_text.getText().toString().isEmpty()||
                !equipmentSituation_text.getText().toString().isEmpty()||
                !acceptance_text.getText().toString().isEmpty()||
                !acceptancePerson_text.getText().toString().isEmpty()||
                !acceptanceDate_text.getText().toString().isEmpty())  {
            AlertDialog.Builder dialog=new AlertDialog.Builder(EquipmentReceive_Add.this);
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

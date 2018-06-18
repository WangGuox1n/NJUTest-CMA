package com.example.cma.ui.supervision;

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
import android.widget.EditText;
import android.widget.Toast;

import com.example.cma.R;
import com.example.cma.model.supervision.Supervision;
import com.example.cma.model.supervision.SupervisionPlan;
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

public class SupervisionPlan_Modify extends AppCompatActivity implements View.OnClickListener{

    private SupervisionPlan supervisionPlan;
    private EditText content_text;
    private EditText object_text;
    private EditText dateFrequency_text;
    private Button submitButton;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.supervision_plan_modify);
        initView();
        Intent intent = getIntent();
        supervisionPlan = (SupervisionPlan)intent.getSerializableExtra("SupervisionPlan");
        setText();
    }

    public void initView(){
        content_text = (EditText)findViewById(R.id.content_text);
        object_text = (EditText)findViewById(R.id.object_text);
        dateFrequency_text = (EditText)findViewById(R.id.dateFrequency_text);
        submitButton = (Button)findViewById(R.id.submit_button);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //设置Toolbar左边显示一个返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        submitButton.setOnClickListener(this);

        //设置监听
        ViewUtil.ShowCursor(content_text);
        ViewUtil.ShowCursor(object_text);
        ViewUtil.ShowCursor(dateFrequency_text);
    }

    public void setText(){
        if(supervisionPlan == null){
            ToastUtil.showShort(SupervisionPlan_Modify.this,"数据传送失败！");
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                content_text.setText(supervisionPlan.getContent());
                object_text.setText(supervisionPlan.getObject());
                dateFrequency_text.setText(supervisionPlan.getDateFrequency());
            }
        });
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
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
/*        if(supervisionPlan == null)
            finish();*/
        if(supervisionPlan.getContent().equals(content_text.getText().toString())
                && supervisionPlan.getObject().equals(object_text.getText().toString())
                && supervisionPlan.getDateFrequency().equals(dateFrequency_text.getText().toString())){
            super.onBackPressed();
            return;
        }

        AlertDialog.Builder dialog=new AlertDialog.Builder(SupervisionPlan_Modify.this);
        dialog.setTitle("修改内容尚未保存");
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
    }

    public void onSave(){
        if(supervisionPlan == null){
            ToastUtil.showShort(SupervisionPlan_Modify.this,"数据传送失败！");
            return;
        }

        //保存前先判断
        if(content_text.getText().toString().isEmpty()||
                object_text.getText().toString().isEmpty()||
                dateFrequency_text.getText().toString().isEmpty()) {
            ToastUtil.showShort(SupervisionPlan_Modify.this, "请填写完整！");
            return;
        }

        AlertDialog.Builder dialog=new AlertDialog.Builder(SupervisionPlan_Modify.this);
        dialog.setMessage("确定修改？");
        dialog.setCancelable(true);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                postSave();
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        dialog.show();
    }

    public void postSave(){
        String address = AddressUtil.SupervisionPlan_modifyOne();
        RequestBody requestBody = new FormBody.Builder()
                .add("planId",""+supervisionPlan.getPlanId())
                .add("content",content_text.getText().toString())
                .add("object",object_text.getText().toString())
                .add("dateFrequency",dateFrequency_text.getText().toString())
                .build();

        HttpUtil.sendOkHttpWithRequestBody(address,requestBody,new okhttp3.Callback(){
            @Override
            public void onResponse(Call call, Response response)throws IOException {
                String responseData = response.body().string();
                Log.d("SupervisionPlan_Modify:",responseData);
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
                    ToastUtil.showShort(SupervisionPlan_Modify.this, "提交成功！");
                    finish();
                }
            }
            @Override
            public void onFailure(Call call,IOException e){
                ToastUtil.showShort(SupervisionPlan_Modify.this, "提交失败！");
            }
        });
    }
}

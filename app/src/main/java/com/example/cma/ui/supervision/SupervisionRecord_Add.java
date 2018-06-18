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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cma.R;
import com.example.cma.model.supervision.Supervision;
import com.example.cma.model.supervision.SupervisionPlan;
import com.example.cma.model.supervision.SupervisionRecord;
import com.example.cma.ui.staff_management.StaffLeaving_Add;
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

public class SupervisionRecord_Add extends AppCompatActivity implements View.OnClickListener{

    private SupervisionPlan supervisionPlan;
    private EditText department_text;
    private EditText supervisor_text;
    private TextView superviseDate_text;
    private EditText supervisedPerson_text;
    private EditText record_text;
    private EditText conclusion_text;
    private EditText operator_text;
    private TextView recordDate_text;
    private Button submitButton;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.supervision_record_add);
        initView();
        Intent intent = getIntent();
        supervisionPlan = (SupervisionPlan)intent.getSerializableExtra("SupervisionPlan");
    }

    public void initView(){
        department_text = (EditText)findViewById(R.id.department_text);
        supervisor_text = (EditText)findViewById(R.id.supervisor_text);
        superviseDate_text = (TextView)findViewById(R.id.superviseDate_text);
        supervisedPerson_text = (EditText)findViewById(R.id.supervisedPerson_text);
        record_text = (EditText)findViewById(R.id.record_text);
        conclusion_text = (EditText)findViewById(R.id.conclusion_text);
        operator_text = (EditText)findViewById(R.id.operator_text);
        recordDate_text = (TextView)findViewById(R.id.recordDate_text);

        submitButton = (Button)findViewById(R.id.submit_button);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        submitButton.setOnClickListener(this);
        superviseDate_text.setOnClickListener(this);
        recordDate_text.setOnClickListener(this);

        LinearLayout superviseDateLayout=(LinearLayout)findViewById(R.id.superviseDate_layout);
        LinearLayout recordDateLayout=(LinearLayout)findViewById(R.id.recordDate_layout);
        superviseDateLayout.setOnClickListener(this);
        recordDateLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submit_button:
                onSave();
                break;
            case R.id.superviseDate_layout:
            case R.id.superviseDate_text:
                ViewUtil.getInstance().selectDate(SupervisionRecord_Add.this,superviseDate_text);
                break;
            case R.id.recordDate_layout:
            case R.id.recordDate_text:
                ViewUtil.getInstance().selectDate(SupervisionRecord_Add.this,recordDate_text);
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
        if(!department_text.getText().toString().isEmpty()||
                !supervisor_text.getText().toString().isEmpty()||
                !superviseDate_text.getText().toString().isEmpty()||
                !supervisedPerson_text.getText().toString().isEmpty()||
                !record_text.getText().toString().isEmpty()||
                !conclusion_text.getText().toString().isEmpty()||
                !operator_text.getText().toString().isEmpty()||
                !recordDate_text.getText().toString().isEmpty()) {
            AlertDialog.Builder dialog=new AlertDialog.Builder(SupervisionRecord_Add.this);
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
        }else
            super.onBackPressed();
    }

    public void onSave(){
        if(supervisionPlan == null){
            ToastUtil.showShort(SupervisionRecord_Add.this,"数据传送失败！");
            return;
        }

        //保存前先判断
        if(department_text.getText().toString().isEmpty()||
                supervisor_text.getText().toString().isEmpty()||
                superviseDate_text.getText().toString().isEmpty()||
                supervisedPerson_text.getText().toString().isEmpty()||
                record_text.getText().toString().isEmpty()||
                conclusion_text.getText().toString().isEmpty()||
                operator_text.getText().toString().isEmpty()||
                recordDate_text.getText().toString().isEmpty()) {
            ToastUtil.showShort(SupervisionRecord_Add.this, "请填写完整！");
            return;
        }
        postSave();
    }

    public void postSave(){
        String address = AddressUtil.SupervisionRecord_addOne();
        RequestBody requestBody = new FormBody.Builder()
                .add("planId",""+supervisionPlan.getPlanId())
                .add("department",department_text.getText().toString())
                .add("supervisor",supervisor_text.getText().toString())
                .add("superviseDate",superviseDate_text.getText().toString())
                .add("supervisedPerson",supervisedPerson_text.getText().toString())
                .add("record",record_text.getText().toString())
                .add("conclusion",conclusion_text.getText().toString())
                .add("operator",operator_text.getText().toString())
                .add("recordDate",recordDate_text.getText().toString())
                .build();

        HttpUtil.sendOkHttpWithRequestBody(address,requestBody,new okhttp3.Callback(){
            @Override
            public void onResponse(Call call, Response response)throws IOException {
                String responseData = response.body().string();
                Log.d("SupervisionRecord_Add:",responseData);
                int code = 0;
                String msg = "";
                try {
                    JSONObject object = new JSONObject(responseData);
                    code = object.getInt("code");
                    msg = object.getString("msg");
                }catch (JSONException e){
                    ToastUtil.showShort(SupervisionRecord_Add.this,"网络连接错误，提交失败！");
                    e.printStackTrace();
                }
                if(code == 200 && msg.equals("成功")) {
                    ToastUtil.showShort(SupervisionRecord_Add.this,"提交成功！");
                    finish();
                }
            }
            @Override
            public void onFailure(Call call,IOException e){
                ToastUtil.showShort(SupervisionRecord_Add.this,"提交失败！");
            }
        });
    }
}

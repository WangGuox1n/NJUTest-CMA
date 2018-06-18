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
import com.example.cma.model.supervision.SupervisionPlan;
import com.example.cma.model.supervision.SupervisionRecord;
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

public class SupervisionRecord_Modify extends AppCompatActivity implements View.OnClickListener{

    private SupervisionRecord supervisionRecord;
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
        setContentView(R.layout.supervision_record_modify);
        initView();
        Intent intent = getIntent();
        supervisionRecord = (SupervisionRecord)intent.getSerializableExtra("SupervisionRecord");
        setText();
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

        ViewUtil.ShowCursor(department_text);
        ViewUtil.ShowCursor(supervisor_text);
        ViewUtil.ShowCursor(supervisedPerson_text);
        ViewUtil.ShowCursor(record_text);
        ViewUtil.ShowCursor(conclusion_text);
        ViewUtil.ShowCursor(operator_text);

    }

    public void setText(){
        if(supervisionRecord == null){
            ToastUtil.showShort(SupervisionRecord_Modify.this,"数据传送失败！");
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                department_text.setText(supervisionRecord.getDepartment());
                supervisor_text.setText(supervisionRecord.getSupervisor());
                superviseDate_text.setText(supervisionRecord.getSuperviseDate());
                supervisedPerson_text.setText(supervisionRecord.getSupervisedPerson());
                record_text.setText(supervisionRecord.getRecord());
                conclusion_text.setText(supervisionRecord.getConclusion());
                operator_text.setText(supervisionRecord.getOperator());
                recordDate_text.setText(supervisionRecord.getRecordDate());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submit_button:
                onSave();
                break;
            case R.id.superviseDate_layout:
            case R.id.superviseDate_text:
                ViewUtil.getInstance().selectDate(SupervisionRecord_Modify.this,superviseDate_text);
                break;
            case R.id.recordDate_layout:
            case R.id.recordDate_text:
                ViewUtil.getInstance().selectDate(SupervisionRecord_Modify.this,recordDate_text);
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
        if(supervisionRecord == null)
            super.onBackPressed();
        if(!supervisionRecord.getDepartment().equals(department_text.getText().toString())||
                !supervisionRecord.getSupervisor().equals(supervisor_text.getText().toString())||
                !supervisionRecord.getSuperviseDate().equals(superviseDate_text.getText().toString())||
                !supervisionRecord.getSupervisedPerson().equals(supervisedPerson_text.getText().toString())||
                !supervisionRecord.getRecord().equals(record_text.getText().toString())||
                !supervisionRecord.getConclusion().equals(conclusion_text.getText().toString())||
                !supervisionRecord.getOperator().equals(operator_text.getText().toString())||
                !supervisionRecord.getRecordDate().equals(recordDate_text.getText().toString())) {
            AlertDialog.Builder dialog=new AlertDialog.Builder(SupervisionRecord_Modify.this);
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
        if(supervisionRecord == null){
            ToastUtil.showShort(SupervisionRecord_Modify.this,"数据传送失败！");
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
            ToastUtil.showShort(SupervisionRecord_Modify.this, "请填写完整！");
            return;
        }
        AlertDialog.Builder dialog=new AlertDialog.Builder(SupervisionRecord_Modify.this);
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
        String address = AddressUtil.SupervisionRecord_modifyOne();
        RequestBody requestBody = new FormBody.Builder()
                .add("recordId",""+supervisionRecord.getRecordId())
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
                Log.d("Record_Modify",responseData);
                int code = 0;
                String msg = "";
                try {
                    JSONObject object = new JSONObject(responseData);
                    code = object.getInt("code");
                    msg = object.getString("msg");
                }catch (JSONException e){
                    ToastUtil.showShort(SupervisionRecord_Modify.this, "网络连接错误，提交失败！");
                    e.printStackTrace();
                }
                if(code == 200 && msg.equals("成功")) {
                    ToastUtil.showShort(SupervisionRecord_Modify.this, "提交成功！");
                    finish();
                }
            }
            @Override
            public void onFailure(Call call,IOException e){
                ToastUtil.showShort(SupervisionRecord_Modify.this, "提交失败！");
            }
        });
    }
}

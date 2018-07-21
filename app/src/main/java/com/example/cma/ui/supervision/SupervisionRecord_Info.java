package com.example.cma.ui.supervision;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.cma.R;
import com.example.cma.model.supervision.Supervision;
import com.example.cma.model.supervision.SupervisionRecord;
import com.example.cma.utils.AddressUtil;
import com.example.cma.utils.HttpUtil;
import com.example.cma.utils.ToastUtil;
import com.example.cma.utils.ViewUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SupervisionRecord_Info extends AppCompatActivity implements View.OnClickListener {

    private Supervision supervision;
    private SupervisionRecord supervisionRecord;
    private TextView department_text;
    private TextView supervisor_text;
    private TextView superviseDate_text;
    private TextView supervisedPerson_text;
    private TextView record_text;
    private TextView conclusion_text;
    private TextView operator_text;
    private TextView recordDate_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.supervision_record_info);
        Intent intent = getIntent();
        supervision = (Supervision) intent.getSerializableExtra("Supervision");
        supervisionRecord = (SupervisionRecord) intent.getSerializableExtra("SupervisionRecord");
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupervisionRecord();
    }

    public void initView() {
        department_text = findViewById(R.id.department_text);
        supervisor_text = findViewById(R.id.supervisor_text);
        superviseDate_text = findViewById(R.id.superviseDate_text);
        supervisedPerson_text = findViewById(R.id.supervisedPerson_text);
        record_text = findViewById(R.id.record_text);
        conclusion_text = findViewById(R.id.conclusion_text);
        operator_text = findViewById(R.id.operator_text);
        recordDate_text = findViewById(R.id.recordDate_text);


        Toolbar toolbar = findViewById(R.id.toolbar);
        Button edit_Button = findViewById(R.id.edit_button);
        Button delete_Button = findViewById(R.id.delete_button);
        ViewUtil.getInstance().setSupportActionBar(this, toolbar);

        edit_Button.setOnClickListener(this);
        delete_Button.setOnClickListener(this);
        if (supervision.getSituation() == 2) {
            edit_Button.setEnabled(false);
            delete_Button.setEnabled(false);
        }
    }

    public void setText() {
        if (supervisionRecord == null) {
            ToastUtil.showShort(SupervisionRecord_Info.this, "数据传送失败！");
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
        switch (v.getId()) {
            case R.id.edit_button:
                Intent intent = new Intent(SupervisionRecord_Info.this, SupervisionRecord_Modify.class);
                intent.putExtra("SupervisionRecord", supervisionRecord);
                startActivity(intent);
                break;
            case R.id.delete_button:  //点击删除，弹出弹窗
                onDeleteConfirm();
                break;
            default:
                break;
        }
    }

    //是否确认删除的对话框
    public void onDeleteConfirm() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(SupervisionRecord_Info.this);
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

    public void onDelete() {
        String address = AddressUtil.getAddress(AddressUtil.SupervisionRecord_deleteOne);
        RequestBody requestBody = new FormBody.Builder().add("recordId", "" + supervisionRecord.getRecordId()).build();
        HttpUtil.sendOkHttpWithRequestBody(address, requestBody, new okhttp3.Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                Log.d("SupervisionPlan Delete:", responseData);
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
                    ToastUtil.showShort(SupervisionRecord_Info.this, "删除成功");
                    finish();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showShort(SupervisionRecord_Info.this, "删除失败");
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

    //从服务器获取计划执行记录
    public void getSupervisionRecord() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String address = AddressUtil.getAddress(AddressUtil.SupervisionRecord_getAll) + supervisionRecord.getPlanId();
                HttpUtil.sendOkHttpRequest(address, new okhttp3.Callback() {
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseData = response.body().string();
                        Log.d("SupervisionRecord", responseData);
                        parseJSONWithGSON(responseData);
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        ToastUtil.showShort(SupervisionRecord_Info.this, "请求数据失败");
                    }
                });
            }
        }).start();
    }

    private void parseJSONWithGSON(String jsonData) {
        String record = "";
        try {
            JSONObject object = new JSONObject(jsonData);
            record = object.getString("data");
        } catch (Exception e) {
            e.printStackTrace();
            supervisionRecord = null;
        }
        if (record.equals("[]")) {
            supervisionRecord = null;
        } else {
            List<SupervisionRecord> list = new Gson().fromJson(record, new TypeToken<List<SupervisionRecord>>() {
            }.getType());
            if (list.size() > 0){
                supervisionRecord = list.get(0);
                setText();
            }
        }
    }
}

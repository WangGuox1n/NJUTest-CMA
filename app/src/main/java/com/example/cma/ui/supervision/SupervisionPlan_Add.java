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
import android.widget.EditText;

import com.example.cma.R;
import com.example.cma.model.supervision.Supervision;
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

public class SupervisionPlan_Add extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "SupervisionPlan_Add";

    private Supervision supervision;
    private EditText content_text;
    private EditText object_text;
    private EditText dateFrequency_text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.supervision_plan_add);
        initView();
        Intent intent = getIntent();
        supervision = (Supervision) intent.getSerializableExtra("Supervision");
        if (supervision == null) {
            ToastUtil.showShort(SupervisionPlan_Add.this, "数据传送失败");
        }
    }

    public void initView() {
        content_text = findViewById(R.id.content_text);
        object_text = findViewById(R.id.object_text);
        dateFrequency_text = findViewById(R.id.dateFrequency_text);
        Toolbar toolbar = findViewById(R.id.toolbar);
        ViewUtil.getInstance().setSupportActionBar(this, toolbar);
        findViewById(R.id.submit_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_button:
                onSave();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackConfirm();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!content_text.getText().toString().isEmpty() ||
                !object_text.getText().toString().isEmpty() ||
                !dateFrequency_text.getText().toString().isEmpty()) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(SupervisionPlan_Add.this);
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
        } else
            super.onBackPressed();
    }


    public void onSave() {
        if (supervision == null) {
            ToastUtil.showShort(SupervisionPlan_Add.this, "数据传送失败");
            return;
        }

        //保存前先判断
        if (content_text.getText().toString().isEmpty() ||
                object_text.getText().toString().isEmpty() ||
                dateFrequency_text.getText().toString().isEmpty()) {
            ToastUtil.showShort(SupervisionPlan_Add.this, "请填写完整");
            return;
        }
        postSave();
    }

    public void postSave() {
        String address = AddressUtil.getAddress(AddressUtil.SupervisionPlan_addOne);
        RequestBody requestBody = new FormBody.Builder()
                .add("id", "" + supervision.getId())
                .add("content", content_text.getText().toString())
                .add("object", object_text.getText().toString())
                .add("dateFrequency", dateFrequency_text.getText().toString())
                .build();

        HttpUtil.sendOkHttpWithRequestBody(address, requestBody, new okhttp3.Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                Log.d(TAG, responseData);
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
                    ToastUtil.showShort(SupervisionPlan_Add.this, "添加成功");
                    finish();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showShort(SupervisionPlan_Add.this, "添加失败");
            }
        });
    }

    public void onBackConfirm() {
        if (!content_text.getText().toString().isEmpty() ||
                !object_text.getText().toString().isEmpty() ||
                !dateFrequency_text.getText().toString().isEmpty()) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(SupervisionPlan_Add.this);
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
        } else
            finish();
    }
}

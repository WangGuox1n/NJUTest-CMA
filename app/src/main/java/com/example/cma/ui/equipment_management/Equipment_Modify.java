package com.example.cma.ui.equipment_management;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.example.cma.R;
import com.example.cma.model.equipment_management.Equipment;
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

public class Equipment_Modify extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "Equipment_Modify";
    private Equipment equipment;
    private TextView state_text;
    private EditText name_text;
    private EditText model_text;
    private EditText cpu_text;
    private EditText memory_text;
    private EditText hardDisk_text;
    private EditText equipmentNumber_text;
    private EditText application_text;
    private Switch switchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.equipment_modify);
        Intent intent = getIntent();
        equipment = (Equipment) intent.getSerializableExtra("Equipment");
        initView();
        setText();
    }

    public void initView() {
        state_text = findViewById(R.id.state_text);
        name_text = findViewById(R.id.name_text);
        model_text = findViewById(R.id.model_text);
        cpu_text = findViewById(R.id.cpu_text);
        memory_text = findViewById(R.id.memory_text);
        hardDisk_text = findViewById(R.id.hardDisk_text);
        equipmentNumber_text = findViewById(R.id.equipmentNumber_text);
        application_text = findViewById(R.id.application_text);
        switchButton = findViewById(R.id.switch_button);

        findViewById(R.id.submit_button).setOnClickListener(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        ViewUtil.getInstance().setSupportActionBar(this, toolbar);

        ViewUtil.ShowCursor(name_text);
        ViewUtil.ShowCursor(model_text);
        ViewUtil.ShowCursor(cpu_text);
        ViewUtil.ShowCursor(memory_text);
        ViewUtil.ShowCursor(hardDisk_text);
        ViewUtil.ShowCursor(equipmentNumber_text);
        ViewUtil.ShowCursor(application_text);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (equipment.getState() == 0)
                    switchButton.setChecked(true);
                else
                    switchButton.setChecked(false);

            }
        });

        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    equipment.setState(0);
                    setState(true);
                } else {
                    equipment.setState(1);
                    setState(false);
                }
            }
        });
    }

    public void setText() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                state_text.setText(equipment.stateToString());
                name_text.setText(equipment.getName());
                model_text.setText(equipment.getModel());
                cpu_text.setText(equipment.getCpu());
                memory_text.setText(equipment.getMemory());
                hardDisk_text.setText(equipment.getHardDisk());
                equipmentNumber_text.setText(equipment.getEquipmentNumber());
                application_text.setText(equipment.getApplication());
            }
        });
    }

    public void setState(final boolean flag) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (flag)
                    state_text.setText("准用");
                else
                    state_text.setText("停用");
            }
        });
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
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onSave() {
        if (name_text.getText().toString().isEmpty() ||
                model_text.getText().toString().isEmpty() ||
                cpu_text.getText().toString().isEmpty() ||
                memory_text.getText().toString().isEmpty() ||
                hardDisk_text.getText().toString().isEmpty() ||
                equipmentNumber_text.getText().toString().isEmpty() ||
                application_text.getText().toString().isEmpty()) {
            ToastUtil.showShort(Equipment_Modify.this, "请填写完整");
            return;
        }
        postSave();
    }

    public void postSave() {
        String address = AddressUtil.getAddress(AddressUtil.Equipment_modifyOne);
        RequestBody requestBody = new FormBody.Builder()
                .add("id", equipment.getId() + "")
                .add("name", name_text.getText().toString())
                .add("model", model_text.getText().toString())
                .add("cpu", cpu_text.getText().toString())
                .add("memory", memory_text.getText().toString())
                .add("hardDisk", hardDisk_text.getText().toString())
                .add("equipmentNumber", equipmentNumber_text.getText().toString())
                .add("application", application_text.getText().toString())
                .add("state", equipment.getState() + "")
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
                    ToastUtil.showShort(Equipment_Modify.this, "修改失败");
                }
                if (code == 200 && msg.equals("成功")) {
                    ToastUtil.showShort(Equipment_Modify.this, "修改成功");
                    finish();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showShort(Equipment_Modify.this, "修改失败");
            }
        });
    }
}

package com.example.cma.ui.staff_management;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
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
import android.widget.Toast;

import com.example.cma.R;
import com.example.cma.model.staff_management.StaffFile;
import com.example.cma.model.staff_management.StaffLeaving;
import com.example.cma.model.staff_management.StaffManagement;
import com.example.cma.utils.HttpUtil;
import com.example.cma.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

public class StaffLeaving_Modify extends AppCompatActivity implements View.OnClickListener{

    private StaffLeaving staffLeaving;
    private TextView name_text;
    private TextView department_text;
    private TextView position_text;
    private TextView date_text;
    private TextView file_text;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_leaving_modify);
        initView();
        Intent intent = getIntent();
        staffLeaving = (StaffLeaving)intent.getSerializableExtra("StaffLeaving");
        setText();
    }

    public void initView(){
        name_text = (TextView)findViewById(R.id.name_text);
        department_text = (TextView)findViewById(R.id.department_text);
        position_text = (TextView)findViewById(R.id.position_text);
        date_text = (TextView)findViewById(R.id.date_text);
        file_text = (TextView)findViewById(R.id.file_text);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        file_text.setOnClickListener(this);
        file_text.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        //使用Toolbar对象替换ActionBar,并在Toolbar左边显示一个返回按钮
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void setText(){
        name_text.setText(staffLeaving.getName());
        department_text.setText(staffLeaving.getDepartment());
        position_text.setText(staffLeaving.getPosition());
        date_text.setText(staffLeaving.getLeavingDate());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.file_text:
                toStaffFile();
                break;
            default:break;
        }
    }

    public void toStaffFile(){
        String address = "http://119.23.38.100:8080/cma/StaffFile/getOne?id=" + staffLeaving.getId();
        HttpUtil.sendOkHttpRequest(address,new okhttp3.Callback(){
            @Override
            public void onResponse(Call call, Response response)throws IOException {
                String responseData = response.body().string();
                parseJSONWithGSON(responseData);
            }
            @Override
            public void onFailure(Call call,IOException e){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showShort(StaffLeaving_Modify.this, "获取数据失败！");
                    }
                });
            }
        });
    }

    private void parseJSONWithGSON(String jsondata){
        String staffData = "";
        try {
            JSONObject object = new JSONObject(jsondata);//最外层的JSONObject对象
            staffData = object.getString("data");
        }catch (Exception e){
            e.printStackTrace();
        }
        if(staffData.equals("null")){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtil.showShort(StaffLeaving_Modify.this,"此人档案记录为空");
                }
            });
            return;
        }

        Gson gson = new Gson();
        StaffFile staffFile = gson.fromJson(staffData,StaffFile.class);
        staffFile.setDepartment(staffLeaving.getDepartment());
        staffFile.setPosition(staffLeaving.getPosition());
        Intent intent=new Intent(StaffLeaving_Modify.this,StaffFile_Info.class);
        intent.putExtra("StaffFile", staffFile);
        startActivity(intent);
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
}
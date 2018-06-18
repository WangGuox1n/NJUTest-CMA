package com.example.cma.ui.supervision;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.cma.R;
import com.example.cma.model.supervision.Supervision;
import com.example.cma.model.supervision.SupervisionPlan;
import com.example.cma.model.supervision.SupervisionRecord;
import com.example.cma.utils.AddressUtil;
import com.example.cma.utils.HttpUtil;
import com.example.cma.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SupervisionPlan_Info extends AppCompatActivity implements View.OnClickListener{

    private SupervisionPlan supervisionPlan;
    private SupervisionRecord supervisionRecord;
    private TextView content_text;
    private TextView object_text;
    private TextView dateFrequency_text;
    private TextView record_button;
    private Button deleteButton;
    private Button editButton;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.supervision_plan_info);
        initView();
        Intent intent = getIntent();
        supervisionPlan = (SupervisionPlan)intent.getSerializableExtra("SupervisionPlan");
    }

    @Override
    protected void onResume() {
        super.onResume();
        setText();
        getSupervisionRecord();
        getSupervisionPlan();
    }

    public void initView(){
        content_text = (TextView)findViewById(R.id.content_text);
        object_text = (TextView)findViewById(R.id.object_text);
        dateFrequency_text = (TextView)findViewById(R.id.dateFrequency_text);
        record_button = (TextView)findViewById(R.id.record_text);
        record_button.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        editButton = (Button)findViewById(R.id.edit_button);
        deleteButton = (Button)findViewById(R.id.delete_button);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //设置Toolbar左边显示一个返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        record_button.setOnClickListener(this);
        editButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
    }

    public void setText(){
        if(supervisionPlan == null){
            ToastUtil.showShort(SupervisionPlan_Info.this,"数据传送失败！");
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
            case R.id.edit_button: {
                    Intent intent = new Intent(SupervisionPlan_Info.this, SupervisionPlan_Modify.class);
                    intent.putExtra("SupervisionPlan", supervisionPlan);
                    startActivity(intent);
            }
            break;
            case R.id.delete_button:  //点击删除，弹出弹窗
                onDeleteComfirm();
                break;
            case R.id.record_text: {
                if(supervisionRecord == null){  //执行记录为空，跳转到添加页面
                    Intent intent = new Intent(SupervisionPlan_Info.this, SupervisionRecord_Add.class);
                    intent.putExtra("SupervisionPlan", supervisionPlan);
                    startActivity(intent);
                }
                else{ //执行记录不为空，跳转到信息查看页面
                    Intent intent = new Intent(SupervisionPlan_Info.this, SupervisionRecord_Info.class);
                    intent.putExtra("SupervisionRecord", supervisionRecord);
                    startActivity(intent);
                }
            }
            default:break;
        }
    }

    //是否确认删除的对话框
    public void onDeleteComfirm(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(SupervisionPlan_Info.this);
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
        String address = AddressUtil.SupervisionPlan_deleteOne();
        Log.d("SupervisionPlan Delete",address);
        RequestBody requestBody=new FormBody.Builder().add("planId",""+supervisionPlan.getPlanId()).build();
        HttpUtil.sendOkHttpWithRequestBody(address,requestBody,new okhttp3.Callback(){
            @Override
            public void onResponse(Call call, Response response)throws IOException {
                String responseData = response.body().string();
                Log.d("SupervisionPlan Delete",responseData);
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
                    ToastUtil.showShort(SupervisionPlan_Info.this,"删除成功！");
                    finish();
                }
            }
            @Override
            public void onFailure(Call call,IOException e){
                ToastUtil.showShort(SupervisionPlan_Info.this,"删除失败！");
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

    //向后端发送请求，返回所有人员记录
    public void getSupervisionPlan(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String address = AddressUtil.SupervisionPlan_getAll(supervisionPlan.getId());
                HttpUtil.sendOkHttpRequest(address,new okhttp3.Callback(){
                    @Override
                    public void onResponse(Call call, Response response)throws IOException {
                        String responseData = response.body().string();
                        Log.d("getSupervisionPlan",responseData);
                        JSONArray array = new JSONArray();
                        try {
                            JSONObject object = new JSONObject(responseData);//最外层的JSONObject对象
                            array = object.getJSONArray("data");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        if(array.equals("null")){
                            ToastUtil.showLong(SupervisionPlan_Info.this, "监督计划为空！");
                        }
                        Gson gson = new Gson();
                        List<SupervisionPlan> list = gson.fromJson(array.toString(),new TypeToken<List<SupervisionPlan>>(){}.getType());
                        for(SupervisionPlan newSupervisionPlan:list){
                            Log.d("getSupervisionPlan",supervisionPlan.getPlanId() + " "+newSupervisionPlan.getPlanId());
                            if(supervisionPlan.getPlanId().equals(newSupervisionPlan.getPlanId())){
                                supervisionPlan = newSupervisionPlan;
                                Log.d("getSupervisionPlan",supervisionPlan.getContent());
                                setText();     //在重新获取数据后，再写到页面上
                                break;
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call call,IOException e){
                        ToastUtil.showShort(SupervisionPlan_Info.this, "请求数据失败！");
                    }
                });
            }
        }).start();
    }

    //从服务器获取计划执行记录
    public void getSupervisionRecord(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String address = AddressUtil.SupervisionRecord_getAll(supervisionPlan.getPlanId());
                //String address = "http://10.0.2.2/get_data2.json";
                HttpUtil.sendOkHttpRequest(address,new okhttp3.Callback(){
                    @Override
                    public void onResponse(Call call, Response response)throws IOException {
                        String responseData = response.body().string();
                        Log.d("getSupervisionRecord",responseData);
                        parseJSONWithGSON(responseData);
                    }
                    @Override
                    public void onFailure(Call call,IOException e){
                        ToastUtil.showShort(SupervisionPlan_Info.this,"请求数据失败！");
                    }
                });
            }
        }).start();
    }

    private void parseJSONWithGSON(String jsondata){
        String record = "";
        try {
            JSONObject object = new JSONObject(jsondata);//最外层的JSONObject对象
            record = object.getString("data");
        }catch (Exception e){
            e.printStackTrace();
            supervisionRecord = null;
        }
        if(record.equals("[]")){
            supervisionRecord = null;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    record_button.setText("点击添加执行记录");
                }
            });
        }else{
            List<SupervisionRecord> list = new Gson().fromJson(record.toString(),new TypeToken<List<SupervisionRecord>>(){}.getType());
            if(list.size()>1){
                ToastUtil.showShort(SupervisionPlan_Info.this,"记录条数大于2");
            }
            supervisionRecord = list.get(0);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    record_button.setText("点击查看执行记录");
                }
            });
        }
    }
}

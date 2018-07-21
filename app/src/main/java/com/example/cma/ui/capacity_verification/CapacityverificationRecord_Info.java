package com.example.cma.ui.capacity_verification;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cma.R;
import com.example.cma.model.capacity_verification.CapacityVerificationProject;
import com.example.cma.model.capacity_verification.CapacityVerificationRecord;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CapacityverificationRecord_Info extends AppCompatActivity {

    CapacityVerificationProject capacityVerificationProject;
    CapacityVerificationRecord capacityVerificationRecord;
    Button editButton;
    Toolbar toolbar;
    TextView dateText;
    EditText methodText;
    EditText equipmentName;
    EditText equipmentId;
    EditText equipmenter;
    EditText resultText;
    EditText resultDeal;
    EditText noteText;
    Button deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.capacityverification_record__info);

        toolbar = (Toolbar) findViewById(R.id.mToolbar);
        setSupportActionBar(toolbar);
        //设置Toolbar左边显示一个返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent=getIntent();
        capacityVerificationProject=(CapacityVerificationProject) intent.getSerializableExtra("chuandi");

        editButton=(Button)findViewById(R.id.modify_button);
        dateText=(TextView)findViewById(R.id.date_text);
        methodText=(EditText) findViewById(R.id.method_text);
        equipmentName=(EditText)findViewById(R.id.equipment_name);
        equipmentId=(EditText)findViewById(R.id.equipment_id);
        equipmenter=(EditText)findViewById(R.id.equipmenter);
        resultText=(EditText)findViewById(R.id.result_text);
        resultDeal=(EditText)findViewById(R.id.result_deal);
        noteText=(EditText)findViewById(R.id.note_text);
        deleteButton=(Button)findViewById(R.id.delete_button);

        dateText.setKeyListener(null);
        methodText.setKeyListener(null);
        equipmentName.setKeyListener(null);
        equipmentId.setKeyListener(null);
        equipmenter.setKeyListener(null);
        resultText.setKeyListener(null);
        resultDeal.setKeyListener(null);
        noteText.setKeyListener(null);

        initData(capacityVerificationProject.getProjectId());
        deleteData();

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CapacityverificationRecord_Info.this,CapacityVerificationRecord_Modify.class);
                intent.putExtra("chuandi",capacityVerificationRecord);
                startActivity(intent);
            }
        });

    }




    public void deleteData(){

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog=new AlertDialog.Builder(CapacityverificationRecord_Info.this);
                dialog.setTitle("确定删除此记录吗？");
                dialog.setCancelable(false);
                dialog.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //将信息提交到数据库
                    }
                });
                dialog.setNegativeButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //从数据库删除这个人的档案 TODO
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                postDelete();
                                modifyState();
                            }
                        }).start();
                        finish();
                    }
                });
                dialog.show();
            }
        });
    }

    private void modifyState(){

        OkHttpClient okHttpClient=new OkHttpClient();
        RequestBody requestBody=new FormBody.Builder()
                .add("id",String.valueOf(capacityVerificationRecord.getProjectId()))
                .add("state",String.valueOf(0))
                .build();
        Request request = new Request.Builder()
                .url("http://119.23.38.100:8080/cma/CapacityVerification/modifyOneProject")//url的地址
                .post(requestBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CapacityverificationRecord_Info.this, "上传失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CapacityverificationRecord_Info.this, "上传成功！", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    public void postDelete(){
        OkHttpClient okHttpClient=new OkHttpClient();
        RequestBody requestBody=new FormBody.Builder().add("id",Long.toString(capacityVerificationRecord.getRecordId())).build();
        Request request = new Request.Builder()
                .url("http://119.23.38.100:8080/cma/CapacityVerification/deleteOneRecord")//url的地址
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CapacityverificationRecord_Info.this, "删除失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CapacityverificationRecord_Info.this, "删除成功！", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });


    }

    public void initData(final long id){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String temp="http://119.23.38.100:8080/cma/CapacityVerification/getRecordByProjectId?projectId="+id;
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            // 指定访问的服务器地址，后续在这里修改
                            .url(temp)
                            .build();
                    Response response = client.newCall(request).execute();;
                    String responseData = response.body().string();
                    parseJSONWithGSON(responseData);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // 在这里进行UI操作，将结果显示到界面上
                            initViews();
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private void  parseJSONWithGSON(String responseData){
        try{
            JSONObject object=new JSONObject(responseData);
            String array=object.getString("data");
            Log.d("请求array：",array);
            if(array.equals("null"))
            {

            }else
            {
                Gson gson=new Gson();
                capacityVerificationRecord=gson.fromJson(array,new TypeToken<CapacityVerificationRecord>(){}.getType());
            }

        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public void  initViews() {
        dateText.setText(capacityVerificationRecord.getDate());
        methodText.setText(capacityVerificationRecord.getMethodId());
        equipmentName.setText(capacityVerificationRecord.getEquipmentName());
        equipmentId.setText(capacityVerificationRecord.getEquipmentId());
        equipmenter.setText(capacityVerificationRecord.getExperimenter());
        resultText.setText(capacityVerificationRecord.getResult());
        resultDeal.setText(capacityVerificationRecord.getResultDeal());
        noteText.setText(capacityVerificationRecord.getNote());
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


    @Override
    protected void onResume() {
        super.onResume();
        initData(capacityVerificationProject.getProjectId());
    }
}

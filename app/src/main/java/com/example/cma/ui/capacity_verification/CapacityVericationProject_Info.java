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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cma.R;
import com.example.cma.model.capacity_verification.CapacityVerificationProject;
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

public class CapacityVericationProject_Info extends AppCompatActivity {


    CapacityVerificationProject capacityVerificationProject;
    Button editButton;
    Toolbar toolbar;
    EditText stateText;
    EditText nameText;
    EditText methodText;
    EditText noteText;
    LinearLayout recordLayout;//查看记录的组件
    TextView recordSee;//点击查看记录
    LinearLayout doLayout;//执行这个项目
    Button tobeRecord;//点击的按钮
    Button deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.capacity_verication_project__info);

        toolbar = (Toolbar) findViewById(R.id.mToolbar);
        setSupportActionBar(toolbar);
        //设置Toolbar左边显示一个返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent=getIntent();
        capacityVerificationProject=(CapacityVerificationProject) intent.getSerializableExtra("chuandi");

        editButton=(Button) findViewById(R.id.edit_button);
        stateText=(EditText)findViewById(R.id.state_text);
        nameText=(EditText)findViewById(R.id.name_text);
        methodText=(EditText)findViewById(R.id.method_text);
        noteText=(EditText)findViewById(R.id.note_text);
        recordLayout=(LinearLayout) findViewById(R.id.record_layout);
        recordSee=(TextView) findViewById(R.id.project_see);
        doLayout=(LinearLayout)findViewById(R.id.weizhixing_layout);
        tobeRecord=(Button)findViewById(R.id.zhixing);
        deleteButton=(Button)findViewById(R.id.delete_button);


        stateText.setKeyListener(null);
        nameText.setKeyListener(null);
        methodText.setKeyListener(null);
        noteText.setKeyListener(null);

        initViews();


        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CapacityVericationProject_Info.this,CapacityVerificationProject_Modify.class);
                intent.putExtra("chuandi",capacityVerificationProject);
                startActivity(intent);
            }
        });

        //点击查看记录
        recordSee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CapacityVericationProject_Info.this,CapacityverificationRecord_Info.class);
                intent.putExtra("chuandi",capacityVerificationProject);
                startActivity(intent);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog=new AlertDialog.Builder(CapacityVericationProject_Info.this);
                dialog.setTitle("确定删除此项目吗？");
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
                            }
                        }).start();
                        finish();
                    }
                });
                dialog.show();
            }
        });

        tobeRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CapacityVericationProject_Info.this,CapacityVerificationRecord_Add.class);
                intent.putExtra("chuandi",capacityVerificationProject);
                startActivity(intent);
            }
        });

    }

    public void postDelete(){
        OkHttpClient okHttpClient=new OkHttpClient();
        RequestBody requestBody=new FormBody.Builder().add("id",Long.toString(capacityVerificationProject.getProjectId())).build();
        Request request = new Request.Builder()
                .url("http://119.23.38.100:8080/cma/CapacityVerification/deleteOneProject")//url的地址
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CapacityVericationProject_Info.this, "删除失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CapacityVericationProject_Info.this, "删除成功！", Toast.LENGTH_SHORT).show();
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
                    String temp="http://119.23.38.100:8080/cma/CapacityVerification/getOneProject?id="+id;
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            // 指定访问的服务器地址，后续在这里修改
                            .url(temp)
                            .build();
                    Response response = client.newCall(request).execute();;
                    String responseData = response.body().string();
                    parseJSONWithGSON2(responseData);
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

    private void  parseJSONWithGSON2(String responseData){
        try{
            JSONObject object=new JSONObject(responseData);
            String array=object.getString("data");
            Log.d("请求array：",array);
            if(array.equals("null"))
            {

            }else
            {
                Gson gson=new Gson();
                capacityVerificationProject=gson.fromJson(array,new TypeToken<CapacityVerificationProject>(){}.getType());
            }

        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public void  initViews() {
        nameText.setText(capacityVerificationProject.getName());
        noteText.setText(capacityVerificationProject.getNote());
        methodText.setText(capacityVerificationProject.getMethod());
        if(capacityVerificationProject.getState()==0)
            stateText.setText("未执行");
        else
            stateText.setText("已执行");
        recordLayout.setVisibility(View.VISIBLE);
        doLayout.setVisibility(View.VISIBLE);
        deleteButton.setVisibility(View.VISIBLE);
        if(capacityVerificationProject.getState()==0)
        {
            recordLayout.setVisibility(View.GONE);
        }
        else
        {
            doLayout.setVisibility(View.GONE);
            deleteButton.setVisibility(View.GONE);
        }
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


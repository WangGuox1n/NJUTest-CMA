package com.example.cma.ui.staff_management;


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
import com.example.cma.model.staff_management.StaffAuthorization;
import com.example.cma.model.staff_management.StaffManagement;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class StaffAuthorization_See extends AppCompatActivity {
    Toolbar toolbar;
    EditText name;
    EditText department;
    EditText position;
    EditText content;
    EditText name2;
    TextView time;
    Button deleteButton;
    Button modifyButton;

    StaffAuthorization tempstaffAuthorization;
    StaffAuthorization staffAuthorization;
    List<StaffManagement> list=new ArrayList<StaffManagement>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_authorization__see);

        deleteButton = (Button) findViewById(R.id.delete_button);

        //设置toolbar
        toolbar = (Toolbar) findViewById(R.id.mToolbar4);
        setSupportActionBar(toolbar);

        //设置Toolbar左边显示一个返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }




        Intent intent=getIntent();
        tempstaffAuthorization=(StaffAuthorization) intent.getSerializableExtra("chuandi");

        initData(tempstaffAuthorization.getAuthorizationId());
        //initViews();

        deleteData();

        modifyButton=(Button)findViewById(R.id.modify_button);
        modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(StaffAuthorization_See.this,StaffAuthorization_Modify.class);
                intent.putExtra("chuandi",staffAuthorization);
                startActivity(intent);
            }
        });

    }

    public void initData(final long id){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String temp="http://119.23.38.100:8080/cma/StaffAuthorization/getOne?authorizationId="+id;
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            // 指定访问的服务器地址，后续在这里修改
                            .url(temp)
                            .build();
                    Response response = client.newCall(request).execute();;
                    String responseData = response.body().string();
                    Log.d("请求回复：",responseData);
                    Log.d("请求回复：",Long.toString(id));
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
        // JSONArray array=new JSONArray();
        try{
            //Log.d("responseData:",responseData);
            JSONObject object=new JSONObject(responseData);
            String array=object.getString("data");
            Log.d("请求array：",array);
            if(array.equals("null"))
            {


            }else
            {
                Gson gson=new Gson();
                staffAuthorization=gson.fromJson(array,new TypeToken<StaffAuthorization>(){}.getType());
                // Log.d("staff name:",staffAuthorization.getName());
            }

        }catch (Exception e){
            e.printStackTrace();
        }


    }


    public void  initViews() {

        name = (EditText) findViewById(R.id.edit_text1);
        department = (EditText) findViewById(R.id.edit_text2);
        position = (EditText) findViewById(R.id.edit_text3);
        content=(EditText)findViewById(R.id.edit_text4);
        time=(TextView)findViewById(R.id.select_time);


        name2 = (EditText) findViewById(R.id.edit_text5_1);

        name.setText(staffAuthorization.getName());
        department.setText(staffAuthorization.getDepartment());
        position.setText(staffAuthorization.getPosition());
        name2.setText(staffAuthorization.getAuthorizerName());
        content.setText(staffAuthorization.getContent());
        time.setText(staffAuthorization.getAuthorizerDate());


        name.setKeyListener(null);
        department.setKeyListener(null);
        position.setKeyListener(null);
        name2.setKeyListener(null);
        content.setKeyListener(null);

    }


    public void deleteData(){

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog=new AlertDialog.Builder(StaffAuthorization_See.this);
                dialog.setTitle("确定删除此人的档案吗？");
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
    }

    public void postDelete(){
        OkHttpClient okHttpClient=new OkHttpClient();
        RequestBody requestBody=new FormBody.Builder().add("authorizationId",Long.toString(staffAuthorization.getAuthorizationId())).build();
        Request request = new Request.Builder()
                .url("http://119.23.38.100:8080/cma/StaffAuthorization/deleteOne")//url的地址
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(StaffAuthorization_See.this, "删除失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(StaffAuthorization_See.this, "删除成功！", Toast.LENGTH_SHORT).show();
                    }
                });

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

    @Override
    protected void onResume() {
        super.onResume();
        initData(tempstaffAuthorization.getAuthorizationId());
    }

}

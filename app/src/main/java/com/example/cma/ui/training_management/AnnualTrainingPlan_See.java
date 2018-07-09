package com.example.cma.ui.training_management;


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
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.cma.R;
import com.example.cma.model.staff_management.Result;
import com.example.cma.model.training_management.AnnualTrainingPlan;
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
public class AnnualTrainingPlan_See extends AppCompatActivity {

    Toolbar toolbar;
    String id;
    AnnualTrainingPlan temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.annual_training_plan__see);
        Intent intent = getIntent();
        id = (String) intent.getSerializableExtra("id");
        //staff=(AnnualPlan) intent.getSerializableExtra("chuandi");

        initStaff(id);

        ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);

        //在Activity代码中使用Toolbar对象替换ActionBar
        toolbar = (Toolbar) findViewById(R.id.mToolbar4);
        setSupportActionBar(toolbar);

        //设置Toolbar左边显示一个返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        toolbar.findViewById(R.id.edit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AnnualTrainingPlan_See.this,AnnualTrainingPlan_Modify.class);
                intent.putExtra("chuandi",temp);
                startActivity(intent);


            }
        });
        Button button=(Button)findViewById(R.id.delete_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog=new AlertDialog.Builder(AnnualTrainingPlan_See.this);
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
                                postDelete(temp.getPlanId());
                            }
                        }).start();


                        finish();
                    }
                });
                dialog.show();
            }
        });
        TextView dangan_see=(TextView)findViewById(R.id.edit_button);
        dangan_see.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AnnualTrainingPlan_See.this,AnnualTrainingPlan_Modify.class);
                intent.putExtra("chuandi",temp);
                startActivity(intent);
            }
        });

    }


    private void postDelete(long id){
        OkHttpClient okHttpClient=new OkHttpClient();
        RequestBody requestBody=new FormBody.Builder().add("planId",Long.toString(id)).build();
        Request request = new Request.Builder()
                .url("http://119.23.38.100:8080/cma/AnnualTrainingPlan/deleteOne")//url的地址
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AnnualTrainingPlan_See.this, "删除失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.d("androixx.cn", result);

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        Toast.makeText(AnnualTrainingPlan_See.this, "删除成功！", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }


    public void initView(){


        EditText editText1=(EditText) findViewById(R.id.edit_text1);
        editText1.setText(String.valueOf(temp.getPlanId()));
        editText1.setKeyListener(null);

        EditText editText2=(EditText)findViewById(R.id.edit_text2);
        editText2.setText(temp.getTrainProject());
        editText2.setKeyListener(null);

        EditText editText3=(EditText)findViewById(R.id.edit_text3);
        editText3.setText(temp.getPeople());
        editText3.setKeyListener(null);

        EditText editText4=(EditText)findViewById(R.id.edit_text4);
        editText4.setText(temp.getMethod());
        editText4.setKeyListener(null);

        EditText editText5=(EditText)findViewById(R.id.edit_text5);
        editText5.setText(String.valueOf(temp.getTrainingTime()));
        editText5.setKeyListener(null);

        EditText editText6=(EditText)findViewById(R.id.edit_text6);
        editText6.setText(temp.getTrainProject());
        editText6.setKeyListener(null);

        EditText editText7=(EditText)findViewById(R.id.edit_text7);
        editText7.setText(temp.getPeople());
        editText7.setKeyListener(null);

        EditText editText8=(EditText)findViewById(R.id.edit_text8);
        editText8.setText(temp.getMethod());
        editText8.setKeyListener(null);





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




    public void initStaff(String id){
        final String k=id;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String temp="http://119.23.38.100:8080/cma/AnnualTrainingPlan/getOne?planId="+k;
                    Log.d("地址是！！！！！！",temp);
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            // 指定访问的服务器地址，后续在这里修改
                            .url(temp)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.d("!!!!!!",responseData);
                    parseJSONWithGSON2(responseData);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // 在这里进行UI操作，将结果显示到界面上
                            initView();
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
            Gson gson = new Gson();
            Result<AnnualTrainingPlan> userListResult = gson.fromJson(responseData,new TypeToken<Result<AnnualTrainingPlan>>(){}.getType());
            //Result<Array<AnnualPlan>> userListResult = gson.fromJson(jsonData,new TypeToken<Result<List<AnnualPlan>>>(){}.getType());
            temp= userListResult.data;

        }catch (Exception e){
            e.printStackTrace();
        }


    }
    @Override
    protected void onResume() {
        super.onResume();
        initStaff(id);
    }
}






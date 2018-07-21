package com.example.cma.ui.staff_management;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.cma.R;
import com.example.cma.model.staff_management.Result;
import com.example.cma.model.staff_management.StaffTraining;
import com.example.cma.model.staff_management.TrainingResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class StaffTraining_result_See extends AppCompatActivity {

    String trainingid;
    String id;
    String staffname;
    String program;
    String Date;
    String presenter;
    String programContent;
    TrainingResult dangAns;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_training_result__see);

        Intent intent=getIntent();
        dangAns=(TrainingResult) intent.getSerializableExtra("chuan");
        trainingid=Long.toString(dangAns.getTrainingId());
        id=(String) intent.getSerializableExtra("id");
        staffname=(String) intent.getSerializableExtra("staffname");
        final  StaffTraining temp=(StaffTraining)intent.getSerializableExtra("training");
        program=temp.getProgram();
        Date=temp.getTrainingDate();
        presenter=temp.getPresenter();
        programContent=temp.getContent();
        Log.d("11111",dangAns.getProgram());
        Log.d("22222",dangAns.getResult());
        toolbar = (Toolbar) findViewById(R.id.mToolbar3);
        setSupportActionBar(toolbar);
        Button button=(Button)findViewById(R.id.delete_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog=new AlertDialog.Builder(StaffTraining_result_See.this);
                dialog.setTitle("确定删除此人的考核结果吗？");
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
        //设置Toolbar左边显示一个返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        final TextView Text1=(TextView) findViewById(R.id.text1);
        Text1.setText(staffname);

        final TextView Text2=(TextView)findViewById(R.id.text2);
        Text2.setText(program);

        final TextView Text3=(TextView)findViewById(R.id.text3);
        Text3.setText(Date);

        final TextView Text4=(TextView)findViewById(R.id.text4);
        Text4.setText(presenter);

        final TextView Text5=(TextView)findViewById(R.id.text5);
        Text5.setText(programContent);

        final TextView Text6=(TextView)findViewById(R.id.text6);
        Text6.setText(dangAns.getResult());


        toolbar.findViewById(R.id.save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StaffTraining_result_See.this, StaffTraining_result_modify.class);

                intent.putExtra("chuan", dangAns);
                intent.putExtra("id", id);
                intent.putExtra("staffname", staffname);
                intent.putExtra("training", temp);
                refresh();
                startActivity(intent);


            }


        });
    }
    private void refresh(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("点击获取的","here is json11");
                    OkHttpClient client = new OkHttpClient();
                    String path="http://119.23.38.100:8080/cma/StaffTraining/getOne";
                    String url_path = path +"?id=" +id+"&trainingId=" +trainingid;
                    Request request = new Request.Builder()
                            // 指定访问的服务器地址，后续在这里修改
                            //.url("http://10.0.2.2:3000/stars")
                            .url(url_path)
                            .build();
                    Response response = client.newCall(request).execute();
                    Log.d("点击获取的","here is json22");
                    String responseData = response.body().string();
                    Log.d("点击获取的","here is json44");
                    Log.d("获得的数据:",responseData);
                    parseJSONWithGSON(responseData);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void parseJSONWithGSON(String jsonData) {

        System.out.print(jsonData);
        Gson gson = new Gson();
        //Log.d("点击获取得数据","jsonData");
        //dangAns = gson.fromJson(jsonData, new TypeToken<List<StaffTraining>>() {}.getType());
        Log.d("更新！！！！！！！！！！！","！！！！！");
        Result<TrainingResult> userListResult = gson.fromJson(jsonData,new TypeToken<Result<TrainingResult>>(){}.getType());
        //Result<Array<StaffTraining>> userListResult = gson.fromJson(jsonData,new TypeToken<Result<List<StaffTraining>>>(){}.getType());
        TrainingResult temp=userListResult.data;
        showResponse(temp);



    }

    private void showResponse(final TrainingResult temp) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {



                final TextView text6 = (TextView) findViewById(R.id.text6);
                text6.setText(temp.getResult());


            }
        });
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
    private void postDelete(){

        OkHttpClient okHttpClient=new OkHttpClient();
        RequestBody requestBody1=new FormBody.Builder()
                .add("id",id)
                .add("trainingId",trainingid).build();

        Request request = new Request.Builder()
                .url("http://119.23.38.100:8080/cma/StaffTraining/deleteTrainingPeople")//url的地址
                .post(requestBody1)
                .build();

        //同步上传
        //异步post
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(StaffTraining_result_See.this, "删除失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(StaffTraining_result_See.this, "删除成功！", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }
    protected void onResume(){
        super.onResume();
        refresh();
    }
}



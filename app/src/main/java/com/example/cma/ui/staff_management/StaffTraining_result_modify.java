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
import android.widget.EditText;
import android.widget.Toast;

import com.example.cma.R;
import com.example.cma.model.staff_management.StaffTraining;
import com.example.cma.model.staff_management.TrainingResult;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class StaffTraining_result_modify extends AppCompatActivity {

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
        setContentView(R.layout.staff_training_result_modify);

        Intent intent=getIntent();
        dangAns=(TrainingResult) intent.getSerializableExtra("chuan");
        trainingid=Long.toString(dangAns.getTrainingId());
        id=(String) intent.getSerializableExtra("id");
        staffname=(String) intent.getSerializableExtra("staffname");
        StaffTraining temp=(StaffTraining)intent.getSerializableExtra("training");
        program=temp.getProgram();
        Date=temp.getTrainingDate();
        presenter=temp.getPresenter();
        programContent=temp.getContent();
        Log.d("11111",dangAns.getProgram());
        Log.d("22222",dangAns.getResult());
        toolbar = (Toolbar) findViewById(R.id.mToolbar3);
        setSupportActionBar(toolbar);

        //设置Toolbar左边显示一个返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        final EditText editText1=(EditText) findViewById(R.id.edit_text3_1);
        editText1.setText(staffname);

        final EditText editText2=(EditText)findViewById(R.id.edit_text3_2);
        editText2.setText(program);

        final EditText editText3=(EditText)findViewById(R.id.edit_text3_3);
        editText3.setText(Date);

        final EditText editText4=(EditText)findViewById(R.id.edit_text3_4);
        editText4.setText(presenter);

        final EditText editText5=(EditText)findViewById(R.id.edit_text3_5);
        editText5.setText(programContent);

        final EditText editText6=(EditText)findViewById(R.id.edit_text3_6);
        editText6.setText(dangAns.getResult());


        ShowCursor(editText6);


        toolbar.findViewById(R.id.save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(StaffTraining_result_modify.this, "保存成功！", Toast.LENGTH_SHORT).show();
                //需要保存数据到数据库 TODO


                final String s2=editText6.getText().toString();


                if((s2==null||s2.equals("")))
                    Toast.makeText(StaffTraining_result_modify.this,"请全部填满",Toast.LENGTH_SHORT).show();
                else
                {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            postSave(id,trainingid,s2);
                        }
                    }).start();
                }

                finish();
            }
        });

    }


    private void postSave(String s1,String s2,String s3){
        Gson gson=new Gson();

        //String result="{\"name\":\""+preName+"\",\"staffFile\":"+jsonStr+"}";
        OkHttpClient okHttpClient=new OkHttpClient();
        Log.d("传输的数据",s1+s2+s3);
        RequestBody requestBody1= new FormBody.Builder().add("id",s1)
                .add("trainingId",s2)
                .add("result",s3)
                .build();

        Request request = new Request.Builder()
                .url("http://119.23.38.100:8080/cma/StaffTraining/modifyResult")//url的地址
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
                        Toast.makeText(StaffTraining_result_modify.this, "保存失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String c=response.toString();
                        Log.d("查看返回信息",c);
                        Toast.makeText(StaffTraining_result_modify.this, "保存成功！", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }
    private void ShowCursor(final EditText editText){
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setCursorVisible(true);
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

}


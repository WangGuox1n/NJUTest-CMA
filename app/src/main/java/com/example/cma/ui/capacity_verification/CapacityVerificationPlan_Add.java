package com.example.cma.ui.capacity_verification;

import android.content.DialogInterface;
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
import android.widget.Toast;

import com.example.cma.R;
import com.example.cma.utils.ViewUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CapacityVerificationPlan_Add extends AppCompatActivity {


    Toolbar toolbar;
    EditText name;
    EditText organizer;
    EditText year;
    EditText note;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.capacity_verification_plan__add);
        //在Activity代码中使用Toolbar对象替换ActionBar
        toolbar = (Toolbar) findViewById(R.id.mToolbar4);
        setSupportActionBar(toolbar);

        //设置Toolbar左边显示一个返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        name=(EditText)findViewById(R.id.name_text);
        organizer=(EditText)findViewById(R.id.organizer_text);
        year=(EditText)findViewById(R.id.year_text);
        note=(EditText)findViewById(R.id.note_text);

        ViewUtil.ShowCursor(name);
        ViewUtil.ShowCursor(organizer);
        ViewUtil.ShowCursor(year);
        ViewUtil.ShowCursor(note);

        Button button=(Button)findViewById(R.id.save_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean isfull=true;
                final String s1=name.getText().toString();
                final String s2=organizer.getText().toString();
                final String s3=year.getText().toString();
                final String s4=note.getText().toString();

                if((s1==null||s1.equals("")) || (s2==null||s2.equals(""))||(s4==null||s4.equals("")) ||(s3==null||s3.equals(""))  )
                    isfull=false;
                if(isfull==true){

                    AlertDialog.Builder dialog=new AlertDialog.Builder(CapacityVerificationPlan_Add.this);
                    dialog.setTitle("确定提交吗？");
                    dialog.setMessage("确保信息准确！");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {


                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    postJson(s1,s2,s3,s4);
                                }
                            }).start();

                            finish();
                        }
                    });
                    dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(CapacityVerificationPlan_Add.this,"你仍旧可以修改！",Toast.LENGTH_SHORT).show();
                        }
                    });
                    dialog.show();
                }
                else{
                    Toast.makeText(CapacityVerificationPlan_Add.this,"请全部填满",Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

    private void postJson(String s1,String s2,String s3,String s4){

        OkHttpClient okHttpClient=new OkHttpClient();
        RequestBody requestBody=new FormBody.Builder()
                .add("name",s1)
                .add("organizer",s2)
                .add("year",s3)
                .add("note",s4)
                .build();
        Request request = new Request.Builder()
                .url("http://119.23.38.100:8080/cma/CapacityVerification/addOne")//url的地址
                .post(requestBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("androixx.cn", "失败！！！！");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CapacityVerificationPlan_Add.this, "上传失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.d("androixx.cn", result);
                Log.d("androixx.cn", "成功！！！！");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CapacityVerificationPlan_Add.this, "上传成功！", Toast.LENGTH_SHORT).show();
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


}

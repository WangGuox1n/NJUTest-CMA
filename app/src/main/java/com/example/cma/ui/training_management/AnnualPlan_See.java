package com.example.cma.ui.training_management;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import com.example.cma.model.training_management.AnnualPlan;
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
public class AnnualPlan_See extends AppCompatActivity {

    Toolbar toolbar;
    String year;
    AnnualPlan temp;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.annual_plan__see);
        Intent intent = getIntent();
        year = (String) intent.getSerializableExtra("chuandi");
        //staff=(AnnualPlan) intent.getSerializableExtra("chuandi");

        initStaff(year);
        ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);

        button=(Button)findViewById(R.id.approve_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AnnualPlan_See.this,AnnualPlan_Approve.class);
                intent.putExtra("year",year);
                startActivity(intent);
            }
        });

        //在Activity代码中使用Toolbar对象替换ActionBar
        toolbar = (Toolbar) findViewById(R.id.mToolbar4);
        setSupportActionBar(toolbar);

        //设置Toolbar左边显示一个返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        TextView dangan_see=(TextView)findViewById(R.id.dangan_view);
        dangan_see.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AnnualPlan_See.this,AnnualTrainingPlan_Main.class);
                intent.putExtra("year",year);
                startActivity(intent);
            }
        });

    }
    public void initView(){


        EditText editText1=(EditText) findViewById(R.id.edit_text1);
        editText1.setText(String.valueOf(temp.getYear()));
        editText1.setKeyListener(null);

        EditText editText2=(EditText)findViewById(R.id.edit_text2);
        editText2.setText(temp.getAuthor());
        editText2.setKeyListener(null);

        EditText editText3=(EditText)findViewById(R.id.edit_text3);
        editText3.setText(temp.getCreateDate());
        editText3.setKeyListener(null);

        EditText editText4=(EditText)findViewById(R.id.edit_text4);
        editText4.setText(temp.getApprover());
        editText4.setKeyListener(null);

        EditText editText5=(EditText)findViewById(R.id.edit_text5);
        editText5.setText(temp.getApproveDate());
        editText5.setKeyListener(null);

        if(temp.getApprover()!=null) {
            button.setTextColor(Color.rgb(90, 90, 90));
            button.setClickable(false);
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




    public void initStaff(String year){
        final String id=year;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String temp="http://119.23.38.100:8080/cma/AnnualTrainingPlan/getAnnualPlan?year="+id;
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
            Result<AnnualPlan> userListResult = gson.fromJson(responseData,new TypeToken<Result<AnnualPlan>>(){}.getType());
            //Result<Array<AnnualPlan>> userListResult = gson.fromJson(jsonData,new TypeToken<Result<List<AnnualPlan>>>(){}.getType());
            temp= userListResult.data;

        }catch (Exception e){
            e.printStackTrace();
        }


    }
    @Override
    protected void onResume() {
        super.onResume();
        initStaff(year);
    }
}






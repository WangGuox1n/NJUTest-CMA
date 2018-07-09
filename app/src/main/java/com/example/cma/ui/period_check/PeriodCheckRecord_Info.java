package com.example.cma.ui.period_check;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.cma.R;
import com.example.cma.model.period_check.IntermediateChecksPlan;
import com.example.cma.model.period_check.IntermediateChecksRecord;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PeriodCheckRecord_Info extends AppCompatActivity {
    Toolbar toolbar;
    EditText object;//核查对象
    TextView checkDate;//核查时间
    EditText processRecord;//核查过程记录
    EditText processRecordPerson;//核查过程记录人
    TextView processRecordDate;//核查过程记录时间
    EditText resultRecord;//核查结论记录
    EditText resultRecordPerson;//核查结论记录人
    TextView resultRecordDate;//核查结论记录时间
    EditText note;//备注
    Button deleteButton;
    Button modifyButton;

    IntermediateChecksPlan tempcheckesPlan;//从前面传过来的是计划，根据计划编号获取记录信息
    IntermediateChecksRecord checksRecord;
    List<IntermediateChecksRecord> list=new ArrayList<IntermediateChecksRecord>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.period_check_record__info);
        deleteButton = (Button) findViewById(R.id.delete_button);

        //设置toolbar
        toolbar = (Toolbar) findViewById(R.id.mToolbar4);
        setSupportActionBar(toolbar);

        //设置Toolbar左边显示一个返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!= null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }



        Intent intent=getIntent();
        tempcheckesPlan=(IntermediateChecksPlan) intent.getSerializableExtra("chuandi");


        TextView time1=(TextView)findViewById(R.id.check_date1);
        time1.setText(tempcheckesPlan.getCheckDate());
        TextView text1=(TextView) findViewById(R.id.edit_text4);
        text1.setText(tempcheckesPlan.getContent());


        initData(tempcheckesPlan.getPlanId());

        deleteData();

        modifyButton=(Button)findViewById(R.id.modify_button);
        modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(PeriodCheckRecord_Info.this,PeriodCheckRecord_Modify.class);
                intent.putExtra("chuandi",checksRecord);
                startActivity(intent);
            }
        });

    }

    public void initData(final long id){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String temp="http://119.23.38.100:8080/cma/IntermediateChecksRecord/getOneByPlanId?planId="+id;
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
                checksRecord=gson.fromJson(array,new TypeToken<IntermediateChecksRecord>(){}.getType());
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public void  initViews() {

        object = (EditText) findViewById(R.id.object_text);
        checkDate=(TextView) findViewById(R.id.check_date);
        processRecord=(EditText)findViewById(R.id.process_record);
        processRecordPerson=(EditText)findViewById(R.id.process_person);
        processRecordDate=(TextView)findViewById(R.id.process_date);
        resultRecord=(EditText)findViewById(R.id.result_record);
        resultRecordPerson=(EditText)findViewById(R.id.result_person);
        resultRecordDate=(TextView)findViewById(R.id.result_date);
        note=(EditText)findViewById(R.id.note_text);


        object.setText(checksRecord.getObject());
        checkDate.setText(checksRecord.getCheckDate());
        processRecord.setText(checksRecord.getProcessRecord());
        processRecordPerson.setText(checksRecord.getProcessRecordPerson());
        processRecordDate.setText(checksRecord.getProcessRecordDate());
        resultRecord.setText(checksRecord.getResultRecord());
        resultRecordPerson.setText(checksRecord.getResultRecordPerson());
        resultRecordDate.setText(checksRecord.getResultRecordDate());
        note.setText(checksRecord.getNote());


        object.setKeyListener(null);
        processRecord.setKeyListener(null);
        processRecordPerson.setKeyListener(null);
        resultRecord.setKeyListener(null);
        resultRecordPerson.setKeyListener(null);
        note.setKeyListener(null);

    }


    public void deleteData(){

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog=new AlertDialog.Builder(PeriodCheckRecord_Info.this);
                dialog.setTitle("确定删除该记录信息吗？");
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
                                postJson2();
                            }
                        }).start();


                        finish();
                    }
                });
                dialog.show();
            }
        });
    }

    private void postJson2(){

        OkHttpClient okHttpClient=new OkHttpClient();
        //创建一个请求对象
        MediaType JSON=MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody=new FormBody.Builder()
                .add("planId",Long.toString(checksRecord.getPlanId()))
                .add("state",Long.toString(0))
                .build();

        final Request request = new Request.Builder()
                .url("http://119.23.38.100:8080/cma/IntermediateChecksPlan/modifyOne")//url的地址
                .post(requestBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("androixx.cn", "失败！！！！");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(PeriodCheckRecord_Info.this, "保存失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("androixx.cn", "成功！！！！");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(PeriodCheckRecord_Info.this, "保存成功！", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }
    public void postDelete(){
        OkHttpClient okHttpClient=new OkHttpClient();
        RequestBody requestBody=new FormBody.Builder().add("recordId",Long.toString(checksRecord.getRecordId())).build();
        Request request = new Request.Builder()
                .url("http://119.23.38.100:8080/cma/IntermediateChecksRecord/deleteOne")//url的地址
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(PeriodCheckRecord_Info.this, "删除失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(PeriodCheckRecord_Info.this, "删除成功！", Toast.LENGTH_SHORT).show();
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
        initData(tempcheckesPlan.getPlanId());
    }

}



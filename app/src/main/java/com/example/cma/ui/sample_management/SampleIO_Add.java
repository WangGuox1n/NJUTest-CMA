package com.example.cma.ui.sample_management;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cma.R;
import com.example.cma.model.sample_management.SampleReceive;
import com.example.cma.utils.HttpUtil;
import com.example.cma.utils.ToastUtil;
import com.example.cma.utils.ViewUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.feezu.liuli.timeselector.TimeSelector;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SampleIO_Add extends AppCompatActivity implements View.OnClickListener,Spinner.OnItemSelectedListener{
    private List<SampleReceive> wholeList = new ArrayList<>();
    private SampleReceive sampleReceive;
    private Spinner spinner;
    private List<SampleReceive> sample_list = new ArrayList<>();
    private List<String> data_list = new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    private int sampleState;
    private TextView samplenumber_text;
    private TextView samplename_text;
    private TextView sampleamount_text;
    private TextView samplestate_text;
    private EditText sender_text;
    private EditText senderdate_text;
    private EditText receiver_text;
    private EditText obtainer_text;
    private EditText obtaindate_text;
    private EditText note_text;
    private Button submitButton;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_io__add);
        initView();
        getAll();
        getDataFromServer();
        LinearLayout linearLayout1 = (LinearLayout) findViewById(R.id.top7);
        final TextView editTextTime1 = (TextView) findViewById(R.id.edit_text7);
        editTextTime1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
                final String now = simpleDateFormat.format(new Date());
                //editText.setText();
                TimeSelector timeSelector = new TimeSelector(SampleIO_Add.this, new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        editTextTime1.setText(time.split(" ")[0]);
                    }
                }, "2000-01-01 00:00", now);
                timeSelector.setIsLoop(true);//设置不循环,true循环
                timeSelector.setTitle("请选择日期");
                //        timeSelector.setMode(TimeSelector.MODE.YMDHM);//显示 年月日时分（默认）
                timeSelector.setMode(TimeSelector.MODE.YMD);//只显示 年月日
                timeSelector.show();
            }
        });

        linearLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
                final String now = simpleDateFormat.format(new Date());
                //editText.setText();
                TimeSelector timeSelector = new TimeSelector(SampleIO_Add.this, new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        editTextTime1.setText(time.split(" ")[0]);
                    }
                }, "2000-01-01 00:00", now);
                timeSelector.setIsLoop(true);//设置不循环,true循环

                //        timeSelector.setMode(TimeSelector.MODE.YMDHM);//显示 年月日时分（默认）
                timeSelector.setMode(TimeSelector.MODE.YMD);//只显示 年月日
                timeSelector.show();
            }
        });


        LinearLayout linearLayout2 = (LinearLayout) findViewById(R.id.top9);
        final TextView editTextTime2 = (TextView) findViewById(R.id.edit_text9);
        editTextTime2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
                final String now = simpleDateFormat.format(new Date());
                //editText.setText();
                TimeSelector timeSelector = new TimeSelector(SampleIO_Add.this, new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        editTextTime2.setText(time.split(" ")[0]);
                    }
                }, "2000-01-01 00:00", now);
                timeSelector.setIsLoop(true);//设置不循环,true循环
                timeSelector.setTitle("请选择日期");
                //        timeSelector.setMode(TimeSelector.MODE.YMDHM);//显示 年月日时分（默认）
                timeSelector.setMode(TimeSelector.MODE.YMD);//只显示 年月日
                timeSelector.show();
            }
        });

        linearLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
                final String now = simpleDateFormat.format(new Date());
                //editText.setText();
                TimeSelector timeSelector = new TimeSelector(SampleIO_Add.this, new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        editTextTime2.setText(time.split(" ")[0]);
                    }
                }, "2000-01-01 00:00", now);
                timeSelector.setIsLoop(true);//设置不循环,true循环

                //        timeSelector.setMode(TimeSelector.MODE.YMDHM);//显示 年月日时分（默认）
                timeSelector.setMode(TimeSelector.MODE.YMD);//只显示 年月日
                timeSelector.show();
            }
        });
    }
    public void initView(){
        spinner = (Spinner) findViewById(R.id.spinner);
        samplenumber_text = (TextView)findViewById(R.id.text_view1_1);
        samplename_text = (TextView)findViewById(R.id.text_view2_1);
        sampleamount_text = (TextView)findViewById(R.id.text_view3_1);
        samplestate_text = (TextView)findViewById(R.id.text_view4_1);
        sender_text = (EditText)findViewById(R.id.edit_text5);
        senderdate_text = (EditText)findViewById(R.id.edit_text6);
        receiver_text = (EditText)findViewById(R.id.edit_text7);
        obtainer_text = (EditText)findViewById(R.id.edit_text8);
        obtaindate_text = (EditText)findViewById(R.id.edit_text9);
        note_text = (EditText)findViewById(R.id.edit_text10);

        submitButton = (Button)findViewById(R.id.submit_button);
        toolbar = (Toolbar) findViewById(R.id.mToolbar4);

        ViewUtil.ShowCursor(sender_text);
        ViewUtil.ShowCursor(senderdate_text);
        ViewUtil.ShowCursor(receiver_text);
        ViewUtil.ShowCursor(obtainer_text);
        ViewUtil.ShowCursor(obtaindate_text);
        ViewUtil.ShowCursor(note_text);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        //设置监听
        submitButton.setOnClickListener(this);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submit_button:
                onSave();
                break;
            default:break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        int i = 1;
        for(SampleReceive sample : sample_list) {
            String selectedItem = arg0.getSelectedItem().toString();
            if(selectedItem.equals(i +". "+sample.getSampleName())){
                for(SampleReceive info : wholeList){
                    if(info.getSampleId() == sample.getSampleId()){
                        Log.d("xuanzhong+info",sample.getSampleId()+"+"+info.getSampleId());
                        samplenumber_text.setText(info.getSampleNumber());
                        samplename_text.setText(info.getSampleName());
                        sampleamount_text.setText(String.valueOf(info.getSampleAmount()));
                        samplestate_text.setText(info.StateToString());
                    }
                }
                sampleReceive=sample ;
                break;
            }
            i++;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void initDataList(){
        data_list.clear();
        int i = 1;
        for(SampleReceive sample : sample_list){
            data_list.add(i+". "+sample.getSampleName());
            i++;
        }
        Log.d("data_list",""+i+sample_list.size());
    }

    public void getDataFromServer(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpUtil.sendOkHttpRequest("http://119.23.38.100:8080/cma/SampleReceive/getAll",new okhttp3.Callback(){
                    @Override
                    public void onResponse(Call call, Response response)throws IOException {
                        String responseData = response.body().string();
                        Log.d("responseData:",responseData);
                        parseJSONWithGSON(responseData);
                        initDataList();
                        showResponse();
                    }
                    @Override
                    public void onFailure(Call call,IOException e){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(SampleIO_Add.this, "请求数据失败！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        }).start();
    }

    private void parseJSONWithGSON(String jsondata){
        JSONArray array = new JSONArray();
        try {
            JSONObject object = new JSONObject(jsondata);//最外层的JSONObject对象
            array = object.getJSONArray("data");
        }catch (Exception e){
            e.printStackTrace();
        }
        if(array.equals("null")){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(SampleIO_Add.this, "无可添加的内容", Toast.LENGTH_LONG).show();
                }
            });
        }
        Gson gson = new Gson();
        List<SampleReceive> newList = gson.fromJson(array.toString(),new TypeToken<List<SampleReceive>>(){}.getType());
        sample_list.clear();
        sample_list.addAll(newList);
    }

    private void showResponse() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //适配器
                adapter= new ArrayAdapter<String>(SampleIO_Add.this, android.R.layout.simple_spinner_item, data_list);
                //设置样式
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //加载适配器
                spinner.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });
    }

    //监听返回按钮的点击事件，比如可以返回上级Activity
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackConfirm();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(!sender_text.getText().toString().isEmpty()||!senderdate_text.getText().toString().isEmpty()||!receiver_text.getText().toString().isEmpty()
                ||!obtainer_text.getText().toString().isEmpty()||!obtaindate_text.getText().toString().isEmpty()||!note_text.getText().toString().isEmpty()) {
            AlertDialog.Builder dialog=new AlertDialog.Builder(SampleIO_Add.this);
            dialog.setTitle("内容尚未保存");
            dialog.setMessage("是否退出？");
            dialog.setCancelable(true);
            dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            dialog.show();
        }else
            super.onBackPressed();
    }

    public void getAll(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpUtil.sendOkHttpRequest("http://119.23.38.100:8080/cma/SampleReceive/getAll",new okhttp3.Callback(){
                    @Override
                    public void onResponse(Call call, Response response)throws IOException {
                        String responseData = response.body().string();
                        Log.d("getAll:",responseData);
                        parseAll(responseData);
                    }
                    @Override
                    public void onFailure(Call call,IOException e){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(SampleIO_Add.this, "请求数据失败！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        }).start();
    }

    private void parseAll(String jsondata){
        JSONArray array = new JSONArray();
        try {
            JSONObject object = new JSONObject(jsondata);//最外层的JSONObject对象
            array = object.getJSONArray("data");
        }catch (Exception e){
            e.printStackTrace();
        }
        if(array.equals("null")){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(SampleIO_Add.this, "无可添加内容", Toast.LENGTH_LONG).show();
                }
            });
        }
        Gson gson = new Gson();
        wholeList  = gson.fromJson(array.toString(),new TypeToken<List<SampleReceive>>(){}.getType());
    }

    public void onSave(){
        if(sample_list.size() == 0){
            ToastUtil.showShort(SampleIO_Add.this,"没有可添加内容");
            return;
        }
        if(sender_text.getText().toString().isEmpty()||senderdate_text.getText().toString().isEmpty()||receiver_text.getText().toString().isEmpty()
                ||obtainer_text.getText().toString().isEmpty()||obtaindate_text.getText().toString().isEmpty()||note_text.getText().toString().isEmpty()) {
            Toast.makeText(SampleIO_Add.this, "请填写完整！", Toast.LENGTH_LONG).show();
            return;
        }

        AlertDialog.Builder dialog=new AlertDialog.Builder(SampleIO_Add.this);
        dialog.setMessage("确定提交？");
        dialog.setCancelable(false);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        postDataToServer();
                    }
                }).start();

            }
        });
        dialog.show();
    }

    public void postDataToServer(){
        TextView Text1 = (TextView) findViewById(R.id.text_view1_1);
        final String s1 = Text1.getText().toString();
        TextView Text2 = (TextView) findViewById(R.id.text_view2_1);
        final String s2 = Text2.getText().toString();
        TextView Text3 = (TextView) findViewById(R.id.text_view3_1);
        final String s3 = Text3.getText().toString();
        TextView Text4 = (TextView) findViewById(R.id.text_view4_1);
        final String s4 = Text4.getText().toString();
        EditText editText5 = (EditText) findViewById(R.id.edit_text5);
        final String s5 = editText5.getText().toString();
        EditText editText6 = (EditText) findViewById(R.id.edit_text6);
        final String s6 = editText6.getText().toString();
        EditText editText7 = (EditText) findViewById(R.id.edit_text7);
        final String s7 = editText7.getText().toString();
        EditText editText8 = (EditText) findViewById(R.id.edit_text8);
        final String s8 = editText8.getText().toString();
        EditText editText9 = (EditText) findViewById(R.id.edit_text9);
        final String s9 = editText9.getText().toString();
        EditText editText10 = (EditText) findViewById(R.id.edit_text10);
        final String s10 = editText10.getText().toString();

        if(s4=="待处理"){
            sampleState=0;
        }
        else if(s4=="待测"){
            sampleState=1;
        }
        else if(s4=="测毕"){
            sampleState=2;
        }
        else if(s4=="已处理"){
            sampleState=3;
        }
        OkHttpClient okHttpClient=new OkHttpClient();
        //创建一个请求对象
        MediaType JSON=MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody=new FormBody.Builder()
                .add("sampleNumber",s1)
                .add("sampleName",s2)
                .add("sampleAmount",String.valueOf(s3))
                .add("sampleState",String.valueOf(sampleState))
                .add("sender",s5)
                .add("receiver",s6)
                .add("sendDate",s7)
                .add("obtainer",s8)
                .add("obtainDate",s9)
                .add("note",s10)
                .build();
        Log.d("addoneup",s1+s2+String.valueOf(s3)+" "+String.valueOf(sampleState)+s5+s6+s7+s8+s9+s10);

        Request request = new Request.Builder()
                .url("http://119.23.38.100:8080/cma/SampleIo/addOne")//url的地址
                .post(requestBody)
                .build();
        //  Log.d("Add"," Here here133");


        //异步上传
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("androixx.cn", "失败！！！！");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SampleIO_Add.this, "上传失败！", Toast.LENGTH_SHORT).show();
                        finish();
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
                        Toast.makeText(SampleIO_Add.this, "上传成功！", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

            }
        });
    }

    public void onBackConfirm(){
        if(!sender_text.getText().toString().isEmpty()||!senderdate_text.getText().toString().isEmpty()||!receiver_text.getText().toString().isEmpty()
                ||!obtainer_text.getText().toString().isEmpty()||!obtaindate_text.getText().toString().isEmpty()||!note_text.getText().toString().isEmpty()) {
            AlertDialog.Builder dialog=new AlertDialog.Builder(SampleIO_Add.this);
            dialog.setTitle("内容尚未保存");
            dialog.setMessage("是否退出？");
            dialog.setCancelable(true);
            dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            dialog.show();
        }else
            finish();
    }
}

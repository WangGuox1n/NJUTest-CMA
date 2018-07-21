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
import android.view.MotionEvent;
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
import com.example.cma.model.staff_management.StaffAuthorization;
import com.example.cma.model.staff_management.StaffManagement;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.feezu.liuli.timeselector.TimeSelector;
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
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class StaffAuthorization_Modify extends AppCompatActivity  implements AdapterView.OnItemSelectedListener{
    Toolbar toolbar;
    Spinner spinner1;
    Spinner spinner2;
    EditText name;
    EditText department;
    EditText position;
    EditText name2;
    Button saveButton;
  //  int pos1;
    //int pos2;
    long id1;
    long id2;
    StaffAuthorization staffAuthorization;
    List<StaffManagement> list=new ArrayList<StaffManagement>();
    List<String> stringList=new ArrayList<String>();
    ArrayAdapter adapter1=null;
    ArrayAdapter adapter2=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_authorization__modify);

        initViews();

        Intent intent=getIntent();
        staffAuthorization=(StaffAuthorization) intent.getSerializableExtra("chuandi");
        EditText contentText=(EditText)findViewById(R.id.edit_text4);
        contentText.setText(staffAuthorization.getContent());
        TextView timeText=(TextView)findViewById(R.id.select_time);
        timeText.setText(staffAuthorization.getAuthorizerDate());
        name.setText(staffAuthorization.getName());
        department.setText(staffAuthorization.getDepartment());
        position.setText(staffAuthorization.getPosition());
        name2.setText(staffAuthorization.getAuthorizerName());
        id1=staffAuthorization.getId();
        id2=staffAuthorization.getAuthorizerId();


        setData();//这已经把list给初始化好了,并且初始化spinner。

        saveData();
    }


    public void  initViews(){
        //设置toolbar
        toolbar = (Toolbar) findViewById(R.id.mToolbar4);
        setSupportActionBar(toolbar);

        //设置Toolbar左边显示一个返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        name=(EditText)findViewById(R.id.edit_text1);
        department=(EditText)findViewById(R.id.edit_text2);
        position=(EditText)findViewById(R.id.edit_text3);
        name2=(EditText)findViewById(R.id.edit_text5_1);
        saveButton=(Button)findViewById(R.id.save_button);

        name.setKeyListener(null);
        department.setKeyListener(null);
        position.setKeyListener(null);
        name2.setKeyListener(null);

        spinner1=(Spinner)findViewById(R.id.spinner1);
        spinner2=(Spinner)findViewById(R.id.spinner2);

        //设置光标
        ShowCursor((EditText)findViewById(R.id.edit_text4));


        //设置时间
        LinearLayout linearLayout=(LinearLayout)findViewById(R.id.top6);
        final TextView editText=(TextView) findViewById(R.id.select_time);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
                final String now=simpleDateFormat.format(new Date());
                //editText.setText();
                TimeSelector timeSelector = new TimeSelector(StaffAuthorization_Modify.this,new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        // Toast.makeText(StaffTraining_Add.this, time, Toast.LENGTH_SHORT).show();
                        editText.setText(time.split(" ")[0]);
                    }
                }, "2000-01-01 00:00", now);
                timeSelector.setIsLoop(false);//设置不循环,true循环
                timeSelector.setTitle("请选择日期");
                //        timeSelector.setMode(TimeSelector.MODE.YMDHM);//显示 年月日时分（默认）
                timeSelector.setMode(TimeSelector.MODE.YMD);//只显示 年月日
                timeSelector.show();
            }
        });

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
                final String now=simpleDateFormat.format(new Date());
                //editText.setText();
                TimeSelector timeSelector = new TimeSelector(StaffAuthorization_Modify.this,new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        // Toast.makeText(StaffTraining_Add.this, time, Toast.LENGTH_SHORT).show();
                        editText.setText(time.split(" ")[0]);
                    }
                }, "2000-01-01 00:00", now);
                timeSelector.setIsLoop(false);//设置不循环,true循环

                //        timeSelector.setMode(TimeSelector.MODE.YMDHM);//显示 年月日时分（默认）
                timeSelector.setMode(TimeSelector.MODE.YMD);//只显示 年月日
                timeSelector.show();
            }
        });
    }

    public void setData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("点击获取的", "here is json11");
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            // 指定访问的服务器地址，后续在这里修改
                            .url("http://119.23.38.100:8080/cma/StaffManagement/getAll")
                            //.url("http://192.168.200.111/get_staff.json")
                            .build();
                    Response response = client.newCall(request).execute();
                    Log.d("点击获取的", "here is json22");
                    String responseData = response.body().string();
                    Log.d("点击获取的", "here is json44");
                    Log.d("获得的数据:", responseData);
                    parseJSONWithGSON(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void parseJSONWithGSON(String responseData){
        try{
            JSONObject object=new JSONObject(responseData);
            String array=object.getString("data");
            if(array.equals("null"))
            {

            }else
            {
                Gson gson=new Gson();
                list.clear();
                list=gson.fromJson(array,new TypeToken<List<StaffManagement>>(){}.getType());

              /*  int i=-1;
                for(StaffManagement t:list){
                    i++;
                    if(t.getId()==staffAuthorization.getId())
                    {
                        Log.d("name1",t.getName());
                        Log.d("chuandi id:",Long.toString(staffAuthorization.getId()));
                        pos1=i;
                    }
                    if(t.getId()==staffAuthorization.getAuthorizerId())
                    {
                        Log.d("name",t.getName());
                        Log.d("chuandi authorizerId:",Long.toString(staffAuthorization.getAuthorizerId()));
                        pos2=i;
                    }
                }*/

                setSpinner1();
                setSpinner2();


            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setSpinner1(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                stringList.clear();
                int i=1;
                for(StaffManagement temp:list){
                    stringList.add(i+" "+temp.getName());
                    i++;
                }
                adapter1=new ArrayAdapter(StaffAuthorization_Modify.this,android.R.layout.simple_list_item_1,stringList);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner1.setAdapter(adapter1);
               // spinner1.setSelection(pos1,true);
                spinner1.setOnItemSelectedListener(StaffAuthorization_Modify.this);
            }
        });
    }

    public void setSpinner2(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                stringList.clear();
                int i=1;
                for(StaffManagement temp:list){
                    stringList.add(i+" "+temp.getName());
                    i++;
                }
                adapter2=new ArrayAdapter(StaffAuthorization_Modify.this,android.R.layout.simple_list_item_1,stringList);
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(adapter2);
                //spinner2.setSelection(pos2,true);
                spinner2.setOnItemSelectedListener(StaffAuthorization_Modify.this);
            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        switch (parent.getId()){
            case R.id.spinner1:
                String q=(String)adapter1.getItem(pos);
                String []qq=q.split(" ");
                int qqq=Integer.valueOf(qq[0]);
                name.setText(list.get(qqq-1).getName());
                department.setText(list.get(qqq-1).getDepartment());
                position.setText(list.get(qqq-1).getPosition());
                id1=list.get(qqq-1).getId();
                break;
            case R.id.spinner2:
                String str = (String) adapter2.getItem(pos);
                String []ar=str.split(" ");
                int aaa=Integer.valueOf(ar[0]);
                EditText editText= (EditText) findViewById(R.id.edit_text5_1);
                editText.setText(list.get(aaa-1).getName());
                id2=list.get(aaa-1).getId();
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void saveData(){
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isFull()==false)
                    Toast.makeText(StaffAuthorization_Modify.this,"请全部填满",Toast.LENGTH_SHORT).show();
                else{
                    AlertDialog.Builder dialog=new AlertDialog.Builder(StaffAuthorization_Modify.this);
                    dialog.setTitle("确定提交吗？");
                    dialog.setMessage("确保信息准确！");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    //postJson(s1,gender,s3,s4,s5,s6,s7,s8,s9,Integer.parseInt(s10));
                                    TextView time=(TextView)findViewById(R.id.select_time);
                                    String s1=time.getText().toString();
                                    EditText content=(EditText)findViewById(R.id.edit_text4);
                                    String s2=content.getText().toString();
                                    postData(id1,id2,s2,s1);
                                }
                            }).start();

                            finish();
                        }
                    });
                    dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(StaffAuthorization_Modify.this,"你仍旧可以修改！",Toast.LENGTH_SHORT).show();
                        }
                    });
                    dialog.show();
                }
            }
        });
    }

    //判断是否填满
    public Boolean isFull(){
        TextView time=(TextView)findViewById(R.id.select_time);
        String s1=time.getText().toString();
        EditText content=(EditText)findViewById(R.id.edit_text4);
        String s2=content.getText().toString();
        if((s1==null||s1.equals("")) || (s2==null||s2.equals("")))
            return false;
        return true;


    }

    public void postData(long id,long authorizerId,String content,String authorizerDate){
        OkHttpClient okHttpClient=new OkHttpClient();
        //创建一个请求对象
        RequestBody requestBody=new FormBody.Builder()
                .add("authorizationId",Long.toString(staffAuthorization.getAuthorizationId()))
                .add("id",Long.toString(id))
                .add("authorizerId",Long.toString(authorizerId))
                .add("content",content)
                .add("authorizerDate",authorizerDate)
                .build();
        Request request = new Request.Builder()
                .url("http://119.23.38.100:8080/cma/StaffAuthorization/modifyOne")//url的地址
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
                        Toast.makeText(StaffAuthorization_Modify.this, "上传失败！", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(StaffAuthorization_Modify.this, "上传成功！", Toast.LENGTH_SHORT).show();
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


    //设置光标可见
    private void ShowCursor(final EditText editText){
        editText.setOnTouchListener(new View.OnTouchListener() {
            int touch_flag=0;
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                touch_flag++;
                if(touch_flag==2){
                    touch_flag=0;
                    editText.setCursorVisible(true);
                }
                return false;
            }
        });

    }
}

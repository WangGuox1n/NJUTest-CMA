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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cma.R;
import com.example.cma.model.capacity_verification.CapacityVerificationRecord;
import com.example.cma.model.equipment_management.Equipment;
import com.example.cma.model.staff_management.StaffManagement;
import com.example.cma.utils.ViewUtil;
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

public class CapacityVerificationRecord_Modify extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    CapacityVerificationRecord capacityVerificationRecord;
    Toolbar toolbar;

    LinearLayout dateLayout;
    TextView dateText;
    EditText methodText;
    EditText equipmentName;
    EditText equipmentId;
    EditText experimenterText;
    EditText resultText;
    EditText resultDeal;
    EditText noteText;
    Button submitButton;
    Spinner spinner1;//设备
    Spinner spinner2;//实验人员
    List<StaffManagement> list=new ArrayList<StaffManagement>();//用于人员 spinner2
    List<String> stringList=new ArrayList<String>();
    ArrayAdapter adapter2=null;


    List<Equipment> equipmentList=new ArrayList<Equipment>();//spinner1
    List<String> equipmentString=new ArrayList<String>();
    ArrayAdapter adapter1=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.capacity_verification_record__modify);

        toolbar = (Toolbar) findViewById(R.id.mToolbar);
        setSupportActionBar(toolbar);
        //设置Toolbar左边显示一个返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent=getIntent();
        capacityVerificationRecord=(CapacityVerificationRecord) intent.getSerializableExtra("chuandi");

        dateLayout=(LinearLayout)findViewById(R.id.date_layout);
        dateText=(TextView)findViewById(R.id.date_text);
        methodText=(EditText)findViewById(R.id.method_text);
        equipmentName=(EditText)findViewById(R.id.equipment_name);
        equipmentId=(EditText)findViewById(R.id.equipment_id);
        experimenterText=(EditText)findViewById(R.id.experimenter);
        resultText=(EditText)findViewById(R.id.result_text);
        resultDeal=(EditText)findViewById(R.id.result_deal);
        noteText=(EditText)findViewById(R.id.note_text);
        submitButton=(Button)findViewById(R.id.submit_button);
        spinner1=(Spinner)findViewById(R.id.spinner1);
        spinner2=(Spinner)findViewById(R.id.spinner2);

        dateText.setText(capacityVerificationRecord.getDate());
        methodText.setText(capacityVerificationRecord.getMethodId());
        equipmentName.setText(capacityVerificationRecord.getEquipmentName());
        equipmentId.setText(capacityVerificationRecord.getEquipmentId());
        experimenterText.setText(capacityVerificationRecord.getExperimenter());
        resultText.setText(capacityVerificationRecord.getResult());
        resultDeal.setText(capacityVerificationRecord.getResultDeal());
        noteText.setText(capacityVerificationRecord.getNote());

        equipmentName.setKeyListener(null);
        equipmentId.setKeyListener(null);
        experimenterText.setKeyListener(null);

        ViewUtil.ShowCursor(methodText);
        ViewUtil.ShowCursor(resultText);
        ViewUtil.ShowCursor(resultDeal);
        ViewUtil.ShowCursor(noteText);


        //设置时间
        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
                final String now=simpleDateFormat.format(new Date());
                //editText.setText();
                TimeSelector timeSelector = new TimeSelector(CapacityVerificationRecord_Modify.this,new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        // Toast.makeText(StaffTraining_Add.this, time, Toast.LENGTH_SHORT).show();
                        dateText.setText(time.split(" ")[0]);
                    }
                }, "2000-01-01 00:00", now);
                timeSelector.setIsLoop(false);//设置不循环,true循环
                timeSelector.setTitle("请选择日期");
                //        timeSelector.setMode(TimeSelector.MODE.YMDHM);//显示 年月日时分（默认）
                timeSelector.setMode(TimeSelector.MODE.YMD);//只显示 年月日
                timeSelector.show();
            }
        });

        dateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
                final String now=simpleDateFormat.format(new Date());
                //editText.setText();
                TimeSelector timeSelector = new TimeSelector(CapacityVerificationRecord_Modify.this,new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        // Toast.makeText(StaffTraining_Add.this, time, Toast.LENGTH_SHORT).show();
                        dateText.setText(time.split(" ")[0]);
                    }
                }, "2000-01-01 00:00", now);
                timeSelector.setIsLoop(false);//设置不循环,true循环
                timeSelector.setMode(TimeSelector.MODE.YMD);//只显示 年月日
                timeSelector.show();
            }
        });

        setEquipmentData();
        setStaffData();
        saveData();

    }

    public void saveData(){
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean isFull=true;
                final String dateString=dateText.getText().toString();
                final String methodString=methodText.getText().toString();
                final String equipmentNameString=equipmentName.getText().toString();
                final String equipmentIDString=equipmentId.getText().toString();
                final String experimenterString=experimenterText.getText().toString();
                final String resultString=resultText.getText().toString();
                final String resultDealString=resultDeal.getText().toString();
                final String noteString=noteText.getText().toString();
                if((dateString==null||dateString.equals("")) || (methodString==null||methodString.equals(""))||(equipmentNameString==null||equipmentNameString.equals("")) || (equipmentIDString==null||equipmentIDString.equals("")) || (resultString==null||resultString.equals("")) || (resultDealString==null||resultDealString.equals("")) ||(noteString==null||noteString.equals(""))||(experimenterString==null||experimenterString.equals("")))
                    isFull=false;
                if(isFull==false)
                    Toast.makeText(CapacityVerificationRecord_Modify.this,"请全部填满",Toast.LENGTH_SHORT).show();
                else{
                    AlertDialog.Builder dialog=new AlertDialog.Builder(CapacityVerificationRecord_Modify.this);
                    dialog.setTitle("确定提交吗？");
                    dialog.setMessage("确保信息准确！");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    postData(dateString,methodString,equipmentNameString, equipmentIDString,experimenterString,resultString,resultDealString,noteString);
                                }
                            }).start();

                            finish();
                        }
                    });
                    dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(CapacityVerificationRecord_Modify.this,"你仍旧可以修改！",Toast.LENGTH_SHORT).show();
                        }
                    });
                    dialog.show();
                }
            }
        });
    }


    public void postData(String s1,String s2,String s3,String s4,String s5,String s6,String s7,String s8){
        OkHttpClient okHttpClient=new OkHttpClient();
        //创建一个请求对象
        RequestBody requestBody=new FormBody.Builder()
                .add("id",Long.toString(capacityVerificationRecord.getRecordId()))
                .add("date",s1)
                .add("methodId",s2)
                .add("equipmentName",s3)
                .add("equipmentId",s4)
                .add("experimenter",s5)
                .add("result",s6)
                .add("resultDeal",s7)
                .add("note",s8)
                .build();
        Request request = new Request.Builder()
                .url("http://119.23.38.100:8080/cma/CapacityVerification/modifyOneRecord")//url的地址
                .post(requestBody)
                .build();
        //异步上传
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("androixx.cn", "失败！！！！");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CapacityVerificationRecord_Modify.this, "上传失败！", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(CapacityVerificationRecord_Modify.this, "上传成功！", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }



    public void setEquipmentData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            // 指定访问的服务器地址，后续在这里修改
                            .url("http://119.23.38.100:8080/cma/Equipment/getAll")
                            .build();
                    Response response = client.newCall(request).execute();String responseData = response.body().string();
                    parseJSONWithGSON(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void setStaffData(){
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
                    parseJSONWithGSON2(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void parseJSONWithGSON(String responseData)
    {
        try{
            JSONObject object=new JSONObject(responseData);
            String array=object.getString("data");
            if(array.equals("null"))
            {

            }else
            {
                Gson gson=new Gson();
                equipmentList.clear();
                equipmentList=gson.fromJson(array,new TypeToken<List<Equipment>>(){}.getType());
                setSpinner1();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void parseJSONWithGSON2(String responseData){
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
                //  setSpinner1();
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
                equipmentString.clear();
                int i=1;
                for(Equipment temp:equipmentList){
                    //stringList.add("ID:"+temp.getId()+" "+temp.getName());
                    equipmentString.add(i+" "+temp.getName());
                    i++;
                }
                adapter1=new ArrayAdapter(CapacityVerificationRecord_Modify.this,android.R.layout.simple_list_item_1,equipmentString);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner1.setAdapter(adapter1);
                spinner1.setOnItemSelectedListener(CapacityVerificationRecord_Modify.this);
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
                    // stringList.add("ID:"+temp.getId()+" "+temp.getName());
                    stringList.add(i+" "+temp.getName());
                    i++;
                }
                adapter2=new ArrayAdapter(CapacityVerificationRecord_Modify.this,android.R.layout.simple_list_item_1,stringList);
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(adapter2);
                spinner2.setOnItemSelectedListener(CapacityVerificationRecord_Modify.this);
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
                equipmentName.setText(equipmentList.get(qqq-1).getName());
                equipmentId.setText(equipmentList.get(qqq-1).getEquipmentNumber());
                break;
            case R.id.spinner2:
                String string = (String) adapter2.getItem(pos);
                String []t=string.split(" ");
                int p=Integer.valueOf(t[0]);
                experimenterText.setText(list.get(p-1).getName());
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
       // experimenterText.setText(list.get(0).getName());
       // equipmentName.setText(equipmentList.get(0).getName());
       // equipmentId.setText(equipmentList.get(0).getEquipmentNumber());
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

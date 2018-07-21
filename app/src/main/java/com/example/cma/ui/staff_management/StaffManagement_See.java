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
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cma.R;
import com.example.cma.model.staff_management.StaffFile;
import com.example.cma.model.staff_management.StaffManagement;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class StaffManagement_See extends AppCompatActivity {

    Toolbar toolbar;
    StaffManagement staff;
    int gender;
    StaffManagement stafftemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_management__see);
        Intent intent=getIntent();
        stafftemp=(StaffManagement) intent.getSerializableExtra("chuandi");
        //staff=(StaffManagement) intent.getSerializableExtra("chuandi");

        initStaff(stafftemp.getId());

        ScrollView scrollView=(ScrollView)findViewById(R.id.scrollView);

        //在Activity代码中使用Toolbar对象替换ActionBar
        toolbar = (Toolbar) findViewById(R.id.mToolbar4);
        setSupportActionBar(toolbar);

        //设置Toolbar左边显示一个返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }



        Button button=(Button)findViewById(R.id.delete_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog=new AlertDialog.Builder(StaffManagement_See.this);
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
                                postDelete(stafftemp.getId());
                            }
                        }).start();


                        finish();
                    }
                });
                dialog.show();
            }
        });


        //对 编辑 按钮监听
        toolbar.findViewById(R.id.edit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(StaffManagement_See.this,StaffManagement_Modify.class);
                intent.putExtra("chuandi",staff);
                startActivity(intent);


            }
        });

        //对查看档案监听
        TextView dangan_see=(TextView)findViewById(R.id.dangan_view);
        dangan_see.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String temp="http://119.23.38.100:8080/cma/StaffFile/getOne?id="+staff.getId();
                            OkHttpClient client = new OkHttpClient();
                            Request request = new Request.Builder()
                                    // 指定访问的服务器地址，后续在这里修改
                                    .url(temp)
                                    .build();
                            Response response = client.newCall(request).execute();;
                            String responseData = response.body().string();
                            Log.d("请求回复：",responseData);
                            parseJSONWithGSON(responseData);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }



        });

        TextView shouquan_see=(TextView)findViewById(R.id.shouqaun_view);
        shouquan_see.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent=new Intent(StaffManagement_See.this,StaffAuthorization_Main.class);
               intent.putExtra("id",stafftemp.getId());
               startActivity(intent);
            }
        });

        TextView qualication_info=(TextView)findViewById(R.id.zizhi_view);
        qualication_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(StaffManagement_See.this, StaffQualification_Main.class);
                intent.putExtra("id",stafftemp.getId());
                startActivity(intent);
            }
        });

        TextView training_see=(TextView)findViewById(R.id.training_view);
        training_see.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(StaffManagement_See.this,StaffTraining_result_main.class);
                String a=String.valueOf(stafftemp.getId());
                String b=stafftemp.getName();
                Log.d("!!!!!!!",a);
                Log.d("!!!!!!!!!!!!",b);
                intent.putExtra("chuandi",a);
                intent.putExtra("chuandi1",b);
                startActivity(intent);
            }
        });


    }

    public void disableRadioGroup(RadioGroup testRadioGroup) {
        for (int i = 0; i < testRadioGroup.getChildCount(); i++) {
            testRadioGroup.getChildAt(i).setClickable(false);
        }
    }

    public void initView(){
        EditText editText1=(EditText) findViewById(R.id.edit_text1);
        editText1.setText(staff.getName());
        editText1.setKeyListener(null);
/*
        RadioGroup rg=(RadioGroup)findViewById(R.id.rg);
        if(staff.getGender()==0)
        {
            rg.check(R.id.male);
        }else
        {
            rg.check(R.id.femle);
        }
*/
        gender=staff.getGender();

        TextView sex=(TextView)findViewById(R.id.sex);
        if(staff.getGender()==0)
        {
            sex.setText("男");
        }
        else{
            sex.setText("女");
        }

        EditText editText3=(EditText)findViewById(R.id.edit_text3);
        editText3.setText(staff.getDepartment());
        editText3.setKeyListener(null);

        EditText editText4=(EditText)findViewById(R.id.edit_text4);
        editText4.setText(staff.getPosition());
        editText4.setKeyListener(null);

        EditText editText5=(EditText)findViewById(R.id.edit_text5);
        editText5.setText(staff.getTitle());
        editText5.setKeyListener(null);

        EditText editText6=(EditText)findViewById(R.id.edit_text6);
        editText6.setText(staff.getDegree());
        editText6.setKeyListener(null);

        EditText editText7=(EditText)findViewById(R.id.edit_text7);
        editText7.setText(staff.getGraduationSchool());
        editText7.setKeyListener(null);

        EditText editText8=(EditText)findViewById(R.id.edit_text8);
        editText8.setText(staff.getGraduationMajor());
        editText8.setKeyListener(null);

        TextView textView9=(TextView)findViewById(R.id.select_time9);
        textView9.setText(staff.getGraduationDate());
        textView9.setKeyListener(null);

        EditText editText10=(EditText)findViewById(R.id.edit_text10);
        editText10.setText(Integer.toString(staff.getWorkingYears()));
        editText10.setKeyListener(null);

        //disableRadioGroup(rg);

    }

    private void  parseJSONWithGSON(String responseData){
        // JSONArray array=new JSONArray();
        try{
            //  Log.d("responseData:",responseData);
            JSONObject object=new JSONObject(responseData);
            String array=object.getString("data");
            Log.d("请求array：",array);
            if(array.equals("null"))
            {
                Log.d("请求回复：","222");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(StaffManagement_See.this,"档案不存在",Toast.LENGTH_SHORT).show();
                    }
                });

            }else
            {
                List<StaffFile> list = new Gson().fromJson(array, new TypeToken<List<StaffFile>>() {}.getType());
                if (list.size() == 1){
                    StaffFile staffFile = list.get(0);
                    Intent intent=new Intent(StaffManagement_See.this,StaffFile_Info.class);
                    intent.putExtra("StaffFile",staffFile);
                    startActivity(intent);
                }

            }

        }catch (Exception e){
            e.printStackTrace();
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


    private void postDelete(long id){
        OkHttpClient okHttpClient=new OkHttpClient();
        RequestBody requestBody=new FormBody.Builder().add("id",Long.toString(id)).build();
        final Request request = new Request.Builder()
                .url("http://119.23.38.100:8080/cma/StaffManagement/deleteOne")//url的地址
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(StaffManagement_See.this, "删除失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                Log.d("androixx.cn", result);

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {


                        try {
                            Log.d("??response:",result);
                            if(result.contains("Error")==true)
                            {
                                Toast.makeText(StaffManagement_See.this, "    数据库不许删除\n此人存在其他相关数据！", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(StaffManagement_See.this, "删除成功！", Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e)
                        {

                        }

                    }
                });

            }
        });
    }


    public void initStaff(final long id){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String temp="http://119.23.38.100:8080/cma/StaffManagement/getOne?id="+id;
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            // 指定访问的服务器地址，后续在这里修改
                            .url(temp)
                            .build();
                    Response response = client.newCall(request).execute();;
                    String responseData = response.body().string();
                    //Log.d("请求回复：",responseData);
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
            //Log.d("responseData:",responseData);
            JSONObject object=new JSONObject(responseData);
            String array=object.getString("data");
            Log.d("请求array：",array);
            if(array.equals("null"))
            {


            }else
            {
                Gson gson=new Gson();
                staff=gson.fromJson(array,new TypeToken<StaffManagement>(){}.getType());
                Log.d("staff name:",staff.getName());
            }

        }catch (Exception e){
            e.printStackTrace();
        }


    }
    @Override
    protected void onResume() {
        super.onResume();
        initStaff(stafftemp.getId());
    }
}





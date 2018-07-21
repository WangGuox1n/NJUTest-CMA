package com.example.cma.ui.testing_institution;

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
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.cma.R;
import com.example.cma.model.testing_institution.TestingInstitutionResource;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TestingInstitutionResource_Modify extends AppCompatActivity {
    Toolbar toolbar;
    TestingInstitutionResource testingInstitutionResource;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testing_institution_resource__modify);
        Intent intent=getIntent();
        testingInstitutionResource=(TestingInstitutionResource) intent.getSerializableExtra("test");

        //默认键盘不弹出
        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ScrollView scrollView=(ScrollView)findViewById(R.id.scrollView);

        //在Activity代码中使用Toolbar对象替换ActionBar
        toolbar = (Toolbar) findViewById(R.id.mToolbar4);
        setSupportActionBar(toolbar);

        //设置Toolbar左边显示一个返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        final EditText editText1=(EditText) findViewById(R.id.edit_text1);
        final EditText editText2=(EditText) findViewById(R.id.edit_text2);
        final EditText editText3=(EditText) findViewById(R.id.edit_text3);
        final EditText editText4=(EditText) findViewById(R.id.edit_text4);
        final EditText editText5=(EditText) findViewById(R.id.edit_text5);
        final EditText editText6=(EditText) findViewById(R.id.edit_text6);
        final EditText editText7=(EditText) findViewById(R.id.edit_text7);
        final EditText editText8=(EditText) findViewById(R.id.edit_text8);
        final EditText editText9=(EditText) findViewById(R.id.edit_text9);
        final EditText editText10=(EditText) findViewById(R.id.edit_text10);
        final EditText editText11=(EditText) findViewById(R.id.edit_text11);

        editText1.setText(String.valueOf(testingInstitutionResource.getTotalNumber()));
        editText2.setText(String.valueOf(testingInstitutionResource.getSeniorProfessionalTitle()));
        editText3.setText(String.valueOf(testingInstitutionResource.getIntermediateProfessionalTitle()));
        editText4.setText(String.valueOf(testingInstitutionResource.getPrimaryProfessionalTitle()));
        editText5.setText(String.valueOf(testingInstitutionResource.getFixedAssets()));
        editText6.setText(String.valueOf(testingInstitutionResource.getEquipmentNumber()));
        editText7.setText(String.valueOf(testingInstitutionResource.getFloorSpace()));
        editText8.setText(String.valueOf(testingInstitutionResource.getStableArea()));
        editText9.setText(String.valueOf(testingInstitutionResource.getOutdoorsArea()));
        editText10.setText(testingInstitutionResource.getNameAndAddress());
        editText11.setText(testingInstitutionResource.getNewPlace());

        ShowCursor(editText1);
        ShowCursor(editText2);
        ShowCursor(editText3);
        ShowCursor(editText4);
        ShowCursor(editText5);
        ShowCursor(editText6);
        ShowCursor(editText7);
        ShowCursor(editText8);
        ShowCursor(editText9);
        ShowCursor(editText10);
        ShowCursor(editText11);

        findViewById(R.id.save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean isfull=true;
                //EditText editText1=(EditText)findViewById(R.id.edit_text1);
                final String s1=editText1.getText().toString();
                //EditText editText2=(EditText)findViewById(R.id.edit_text2);
                final String s2=editText2.getText().toString();
                //EditText editText3=(EditText)findViewById(R.id.edit_text3);
                final String s3=editText3.getText().toString();
                //EditText editText4=(EditText)findViewById(R.id.edit_text4);
                final String s4=editText4.getText().toString();
                //EditText editText5=(EditText)findViewById(R.id.edit_text5);
                final String s5=editText5.getText().toString();
                //EditText editText6=(EditText)findViewById(R.id.edit_text6);
                final String s6=editText6.getText().toString();
                //EditText editText7=(EditText)findViewById(R.id.edit_text7);
                final String s7=editText7.getText().toString();
                //EditText editText8=(EditText)findViewById(R.id.edit_text8);
                final String s8=editText8.getText().toString();
                //EditText editText9=(EditText)findViewById(R.id.edit_text9);
                final String s9=editText9.getText().toString();
                //EditText editText10=(EditText)findViewById(R.id.edit_text10);
                final String s10=editText10.getText().toString();
                //EditText editText11=(EditText)findViewById(R.id.edit_text11);
                final String s11=editText11.getText().toString();


                if((s1==null||s1.equals("")) ||(s2==null||s2.equals("")) || (s3==null||s3.equals("")) || (s4==null||s4.equals("")) ||(s5==null||s5.equals("")) || (s6==null||s6.equals(""))
                        ||(s7==null||s7.equals("")) ||(s8==null||s8.equals(""))||(s9==null||s9.equals(""))||(s10==null||s10.equals("")))
                    isfull=false;
                if(isfull==true){
                    //Log.d("select_time:",s9);
                    //Log.d("选择的性别：",Integer.toString(gender));
                    AlertDialog.Builder dialog=new AlertDialog.Builder(TestingInstitutionResource_Modify.this);
                    dialog.setTitle("确定提交吗？");
                    dialog.setMessage("确保信息准确！");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {


                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    postJson(s1,s2,s3,s4,s5,s6,s7,s8,s9,s10,s11);
                                }
                            }).start();

                            finish();
                        }
                    });
                    dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(TestingInstitutionResource_Modify.this,"你仍旧可以修改！",Toast.LENGTH_SHORT).show();
                        }
                    });
                    dialog.show();
                }
                else{
                    Toast.makeText(TestingInstitutionResource_Modify.this,"请全部填满",Toast.LENGTH_SHORT).show();

                }

            }
        });
    }

    private void postJson(String s1,String s2,String s3,String s4,String s5,String s6,String s7,String s8,String s9,String s10,String s11){

        OkHttpClient okHttpClient=new OkHttpClient();
        //创建一个请求对象
        MediaType JSON=MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody=new FormBody.Builder()
                .add("totalNumber",String.valueOf(s1))
                .add("seniorProfessionalTitle",String.valueOf(s2))
                .add("intermediateProfessionalTitle",String.valueOf(s3))
                .add("primaryProfessionalTitle",String.valueOf(s4))
                .add("fixedAssets",String.valueOf(s5))
                .add("equipmentNumber",String.valueOf(s6))
                .add("floorSpace",String.valueOf(s7))
                .add("stableArea",String.valueOf(s8))
                .add("outdoorsArea",String.valueOf(s9))
                .add("nameAndAddress",s10)
                .add("newPlace",s11)
                .build();

        Request request = new Request.Builder()
                .url("http://119.23.38.100:8080/cma/TestingInstitutionResource/modify")//url的地址
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
                        Toast.makeText(TestingInstitutionResource_Modify.this, "上传失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                JSONObject object = new JSONObject();
                int code = 0;
                String msg = "";
                try {
                    object = new JSONObject(result);
                    code = object.getInt("code");
                    msg = object.getString("msg");
                }catch (JSONException e){
                    e.printStackTrace();
                }
                Log.d("androixx.cn", result);
                //Log.d("androixx.cn", "成功！！！！");
                if(code==200) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(TestingInstitutionResource_Modify.this, "修改成功！", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(TestingInstitutionResource_Modify.this, "数据填写错误，请按格式填写！", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                finish();

            }
        });
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

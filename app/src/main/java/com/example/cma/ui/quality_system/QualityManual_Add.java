package com.example.cma.ui.quality_system;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cma.R;
import com.example.cma.utils.FileUtil;

import org.feezu.liuli.timeselector.TimeSelector;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class QualityManual_Add extends AppCompatActivity implements View.OnClickListener{
    Toolbar toolbar;
    Spinner spinner1;
    EditText filename;
    EditText fileId;
    EditText person;
    EditText content;
    Button saveButton;
    String version_text;
    String state_text;
    TextView time_text;
    File file;
    TextView file_text;
    String function;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quality_manual__add);
        Intent intent=getIntent();
        function=intent.getStringExtra("function");
        initViews();
    }

    public void  initViews(){
        //设置toolbar
        toolbar = (Toolbar) findViewById(R.id.mToolbar);
        setSupportActionBar(toolbar);

        //设置Toolbar左边显示一个返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        filename=(EditText)findViewById(R.id.name_text);
        fileId=(EditText)findViewById(R.id.id_text);
        person=(EditText)findViewById(R.id.person_text);
        content=(EditText)findViewById(R.id.content_text);
        file_text=(TextView)findViewById(R.id.file_text);
        file_text.setOnClickListener(QualityManual_Add.this);
        saveButton=(Button)findViewById(R.id.submit_button);
        saveButton.setOnClickListener(QualityManual_Add.this);



       Intent intent=getIntent();

       version_text=intent.getStringExtra("version");
       state_text=intent.getStringExtra("state");


        //设置光标
        ShowCursor(filename);
        ShowCursor(fileId);
        ShowCursor(person);
        ShowCursor(content);




        //设置时间
     //   LinearLayout linearLayout=(LinearLayout)findViewById(R.id.top6);
        time_text=(TextView) findViewById(R.id.time_text);
        time_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
                final String now=simpleDateFormat.format(new Date());
                //editText.setText();
                TimeSelector timeSelector = new TimeSelector(QualityManual_Add.this,new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        // Toast.makeText(StaffTraining_Add.this, time, Toast.LENGTH_SHORT).show();
                        time_text.setText(time.split(" ")[0]);
                    }
                }, "2000-01-01 00:00", now);
                timeSelector.setIsLoop(false);//设置不循环,true循环
                timeSelector.setTitle("请选择日期");
                //        timeSelector.setMode(TimeSelector.MODE.YMDHM);//显示 年月日时分（默认）
                timeSelector.setMode(TimeSelector.MODE.YMD);//只显示 年月日
                timeSelector.show();
            }
        });
    }

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

    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.file_text:
                Log.d("添加","!!!!!!!!");
                FileUtil.getInstance().localStorage(this);
                break;
            case R.id.submit_button:
                saveData();
                break;
            default:
                break;
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FileUtil.GET_FILE:
                if (resultCode == RESULT_OK) {
                    addAttachment(data);
                }
                break;
            default:
                break;
        }
    }
    public void addAttachment(Intent data) {
        Uri uri = data.getData();
        file = FileUtil.getInstance().getFile(this, uri);
        if (!file.exists()) {

            Log.d("失败！！！", "!!!");
            return;
        }
    }

    void saveData() {
        final String s1 = filename.getText().toString();
        final String s2 = fileId.getText().toString();
        final String s3 = person.getText().toString();
        final String s4 = content.getText().toString();
        final String s5 = time_text.getText().toString();
        String requestUrl;
        OkHttpClient okHttpClient = new OkHttpClient();
        MultipartBody.Builder builder = new MultipartBody.Builder();
        //设置类型
        builder.setType(MultipartBody.FORM);
        final ProgressDialog progressDialog=new ProgressDialog(QualityManual_Add.this);
        progressDialog.setTitle("正在上传中");
        progressDialog.setMessage("请稍等....");
        progressDialog.setCancelable(true);
        progressDialog.show();
        //追加参数
        //历史版本的生成方式是通过修改  version=0,state=0; modify
        //历史版本的生成是
        if (state_text.equals("0") && version_text.equals("1")) {
            builder.addFormDataPart("fileId", s2);
            builder.addFormDataPart("fileName", s1);
            builder.addFormDataPart("modifyTime", s5);
            builder.addFormDataPart("modifier", s3);
            builder.addFormDataPart("modifyContent", s4);
            builder.addFormDataPart("file", file.getName(), RequestBody.create(null, file));
            requestUrl="http://119.23.38.100:8080/cma/"+function+"/modify";
        } else {
            builder.addFormDataPart("fileId", s2);
            builder.addFormDataPart("fileName", s1);
            builder.addFormDataPart("state", state_text);
            builder.addFormDataPart("current", version_text);
            builder.addFormDataPart("modifyTime", s5);
            builder.addFormDataPart("modifier", s3);
            builder.addFormDataPart("modifyContent", s4);
            builder.addFormDataPart("file", file.getName(), RequestBody.create(null, file));
            requestUrl="http://119.23.38.100:8080/cma/"+function+"/upload";
        }
        //创建RequestBody
        RequestBody body = builder.build();
        //创建Request
        final Request request = new Request.Builder().url(requestUrl).post(body).build();
        //单独设置参数 比如读取超时时间
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override

            public void onResponse(Call call, Response response)throws IOException {
                String responseData = response.body().string();
                Log.d("onSave:",responseData);
                int code = 0;
                String msg = "";
                try {
                    JSONObject object = new JSONObject(responseData);
                    code = object.getInt("code");
                    msg = object.getString("msg");
                }catch (JSONException e){
                    e.printStackTrace();
                }
                if(code == 200 && msg.equals("成功")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            Toast.makeText(QualityManual_Add.this, "提交成功！", Toast.LENGTH_SHORT).show();
                        }
                    });
                    finish();
                }
            }
            @Override
            public void onFailure(Call call,IOException e){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        Toast.makeText(QualityManual_Add.this, "提交失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

}

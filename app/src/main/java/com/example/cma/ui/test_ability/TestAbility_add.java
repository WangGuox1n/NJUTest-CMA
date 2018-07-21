package com.example.cma.ui.test_ability;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cma.R;
import com.example.cma.utils.FileUtil;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TestAbility_add extends AppCompatActivity  implements View.OnClickListener  {
    Toolbar toolbar;
    TextView attachment_text;
    EditText editText1;
    File file;
    class Attachment{
        File file;
        RelativeLayout layout;
        TextView name;
        TextView size;
    }

    Attachment attachment=new Attachment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_ability_add);
        toolbar = (Toolbar) findViewById(R.id.mToolbar4);
        setSupportActionBar(toolbar);

        //设置Toolbar左边显示一个返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        attachment.name = (TextView) findViewById(R.id.attachment_name);
        attachment.size = (TextView) findViewById(R.id.attachment_size);
        attachment.layout = (RelativeLayout)findViewById(R.id.attachment_layout);
        attachment.layout.setVisibility(View.GONE);
        ((Button)findViewById(R.id.attachment_delete)).setOnClickListener(this);
        editText1=(EditText) findViewById(R.id.edit_text1);
        ShowCursor(editText1);
        attachment_text=(TextView)findViewById(R.id.attachment_text);
        attachment_text.setOnClickListener(this);
        Button button2=(Button)findViewById(R.id.save_button);
        button2.setOnClickListener(this);

    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.attachment_text:
                FileUtil.getInstance().localStorage(this);
                break;
            case R.id.save_button:
                OnSave();
                break;
            case R.id.attachment_delete:
                removeAttachment();
                break;
        }

    }
    public void removeAttachment(){
        attachment.file = null;
        attachment.layout.setVisibility(View.GONE);
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
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String fileSize = FileUtil.FormatFileSize(file);
                attachment.file = file;
                attachment.name.setText(file.getName());
                attachment.size.setText(fileSize);
                attachment.layout.setVisibility(View.VISIBLE);


            }
        });

    }
    public void OnSave()
    {

        final String s1=editText1.getText().toString();
        if((s1==null||s1.equals(""))||!file.exists())
            Toast.makeText(TestAbility_add.this,"请全部填满",Toast.LENGTH_SHORT).show();
        else
        {
              postSave(s1);

        }
    }

    private void postSave(String year){
        //String result="{\"name\":\""+preName+"\",\"staffFile\":"+jsonStr+"}";
        final ProgressDialog progressDialog=new ProgressDialog(TestAbility_add.this);
        progressDialog.setTitle("正在上传中");
        progressDialog.setMessage("请稍等....");
        progressDialog.setCancelable(true);
        progressDialog.show();
        OkHttpClient okHttpClient=new OkHttpClient();

            MultipartBody.Builder builder = new MultipartBody.Builder();
            //设置类型
            builder.setType(MultipartBody.FORM);
            //追加参数
             String name=file.getName();

            builder.addFormDataPart("year", year);
             builder.addFormDataPart("fileName", name);
            builder.addFormDataPart("file", file.getName(), RequestBody.create(null, file));


            //创建RequestBody
            RequestBody body = builder.build();

            final Request request = new Request.Builder().url("http://119.23.38.100:8080/cma/TestAbility/addOne").post(body).build();

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
                                Toast.makeText(TestAbility_add.this, "提交成功！", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(TestAbility_add.this, "提交失败！", Toast.LENGTH_SHORT).show();
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




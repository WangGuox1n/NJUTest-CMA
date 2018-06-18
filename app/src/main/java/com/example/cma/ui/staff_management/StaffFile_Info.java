package com.example.cma.ui.staff_management;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.StringSignature;
import com.example.cma.R;
import com.example.cma.model.staff_management.StaffFile;
import com.example.cma.model.staff_management.StaffManagement;
import com.example.cma.utils.HttpUtil;
import com.example.cma.utils.ToastUtil;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class StaffFile_Info extends AppCompatActivity implements View.OnClickListener{

    private StaffManagement staff = new StaffManagement();
    private StaffFile staffFile;
    private TextView name_text;
    private TextView id_text;
    private TextView location_text;
    private Button deleteButton;
    private Button editButton;
    private Toolbar toolbar;
    private ImageView file_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_file_info);

        initView();
        Intent intent = getIntent();
        staffFile = (StaffFile)intent.getSerializableExtra("StaffFile");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(staffFile == null){
            ToastUtil.showShort(StaffFile_Info.this,"数据传送失败！");
            return;
        }
        getStaffFile();  //重新获取是为了从编辑页面返回后刷新
        getImage();
    }

    public void initView(){
        name_text = (TextView)findViewById(R.id.name_text);
        id_text = (TextView)findViewById(R.id.id_text);
        location_text = (TextView)findViewById(R.id.location_text);
        editButton = (Button)findViewById(R.id.edit_button);
        deleteButton = (Button)findViewById(R.id.delete_button);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        file_image = (ImageView)findViewById(R.id.file_image);
        setSupportActionBar(toolbar);

        //设置Toolbar左边显示一个返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        editButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
    }

    public void setText(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                name_text.setText(staffFile.getName());
                id_text.setText(staffFile.getFileId());
                location_text.setText(staffFile.getFileLocation());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edit_button:
                Intent intent=new Intent(StaffFile_Info.this,StaffFile_Modify.class);
                intent.putExtra("StaffFile", staffFile);
                startActivity(intent);
                break;
            case R.id.delete_button:  //点击删除，弹出弹窗
                onDeleteComfirm();
                break;
            default:break;
        }
    }

    //是否确认删除的对话框
    public void onDeleteComfirm(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(StaffFile_Info.this);
        dialog.setMessage("确定删除？");
        dialog.setCancelable(false);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onDelete();
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Do nothing
            }
        });
        dialog.show();
    }

    public void onDelete(){
        String address = "http://119.23.38.100:8080/cma/StaffFile/deleteOne";
        RequestBody requestBody=new FormBody.Builder().add("id",""+staffFile.getId()).build();
        HttpUtil.sendOkHttpWithRequestBody(address,requestBody,new okhttp3.Callback(){
            @Override
            public void onResponse(Call call, Response response)throws IOException {
                String responseData = response.body().string();
                Log.d("StaffFile onDelete:",responseData);
                JSONObject object = new JSONObject();
                int code = 0;
                String msg = "";
                try {
                    object = new JSONObject(responseData);
                    code = object.getInt("code");
                    msg = object.getString("msg");
                }catch (JSONException e){
                    e.printStackTrace();
                }
                if(code == 200 && msg.equals("成功")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(StaffFile_Info.this, "删除成功！", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(StaffFile_Info.this, "删除失败！", Toast.LENGTH_SHORT).show();
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

    public void getStaffFile(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String address = "http://119.23.38.100:8080/cma/StaffFile/getOne?id=" + staffFile.getId();
                HttpUtil.sendOkHttpRequest(address,new okhttp3.Callback(){
                    @Override
                    public void onResponse(Call call, Response response)throws IOException {
                        String responseData = response.body().string();
                        parseJSONWithGSON(responseData);
                        setText();
                    }
                    @Override
                    public void onFailure(Call call,IOException e){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(StaffFile_Info.this, "获取数据失败！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        }).start();
    }

    public void parseJSONWithGSON(String jsondata){
        String staffData = "";
        try {
            JSONObject jsonObject = new JSONObject(jsondata);//最外层的JSONObject对象
            staffData = jsonObject.getString("data");
        }catch (Exception e){
            e.printStackTrace();
        }
        if(staffData.equals("null")){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(StaffFile_Info.this, "获取数据失败", Toast.LENGTH_LONG).show();
                }
            });
        }else{
            staffFile = new Gson().fromJson(staffData,StaffFile.class);
        }
    }

    public void getImage(){
        Glide.with(this)
                .load("http://119.23.38.100:8080/cma/StaffFile/getImage?id=" + staffFile.getId())
                .error(R.drawable.invalid_image)
                .placeholder(R.drawable.loading)
                .animate(android.R.anim.slide_in_left)
                .signature(new StringSignature(UUID.randomUUID().toString()))
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        /*runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showLong(StaffFile_Info.this,"档案扫描件加载失败！");
                            }
                        });*/
                        return false;
                    }
                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(file_image);
    }
}


package com.example.cma.ui.staff_management;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.cma.R;
import com.example.cma.model.staff_management.StaffManagement;
import com.example.cma.model.staff_management.StaffQualification;
import com.example.cma.utils.HttpUtil;
import com.example.cma.utils.ToastUtil;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class StaffQualification_Info extends AppCompatActivity implements View.OnClickListener{

    private StaffManagement staff = new StaffManagement();
    private StaffQualification staffQualification;
    private TextView name_text;
    private TextView department_text;
    private TextView position_text;
    private TextView id_text;
    private TextView location_text;
    private Button deleteButton;
    private Button editButton;
    private Toolbar toolbar;
    private ImageView file_image;
    Bitmap bitmap;

    private String name;
    private String department;
    private String position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_qualification__info);

        initView();
        Intent intent = getIntent();
        staffQualification = (StaffQualification) intent.getSerializableExtra("StaffQualification");
        //Log.d("sq",staffQualification.getQualificationId()+"");
        name=staffQualification.getName();
        department=staffQualification.getDepartment();
        position=staffQualification.getPosition();
        //setText();
        //getImage();
    }

    protected void onResume() {
        super.onResume();
        if(staffQualification == null){
            ToastUtil.showShort(StaffQualification_Info.this,"数据传送失败！");
            return;
        }
        getStaffQualification(staffQualification.getQualificationId());  //重新获取是为了从编辑页面返回后刷新
        getStaff(staffQualification.getId());
        getImage();
    }

    public void initView(){
        name_text = (TextView)findViewById(R.id.name_text);
        department_text = (TextView)findViewById(R.id.department_text);
        position_text = (TextView)findViewById(R.id.position_text);
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
                name_text.setText(staffQualification.getName());
                department_text.setText(staffQualification.getDepartment());
                position_text.setText(staffQualification.getPosition());
                //name_text.setText(name);
                //department_text.setText(department);
                //position_text.setText(position);
                id_text.setText(String.valueOf(staffQualification.getQualificationId()));
                //Log.d("sqid",String.valueOf(staffQualification.getQualificationId()));
                location_text.setText(staffQualification.getQualificationName());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edit_button:
                Intent intent=new Intent(StaffQualification_Info.this,StaffQualification_Modify.class);
                intent.putExtra("StaffQualification", staffQualification);
                //Log.d("sq_modify_data",staffQualification.getName());
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
        AlertDialog.Builder dialog = new AlertDialog.Builder(StaffQualification_Info.this);
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
        String address = "http://119.23.38.100:8080/cma/StaffQualification/deleteOne";
        RequestBody requestBody=new FormBody.Builder().add("qualificationId",""+staffQualification.getQualificationId()).build();
        HttpUtil.sendOkHttpWithRequestBody(address,requestBody,new okhttp3.Callback(){
            @Override
            public void onResponse(Call call, Response response)throws IOException {
                String responseData = response.body().string();
                Log.d("StaffQ onDelete:",responseData);
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
                            Toast.makeText(StaffQualification_Info.this, "删除成功！", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(StaffQualification_Info.this, "删除失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


    public void getStaff(long id){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String address = "http://119.23.38.100:8080/cma/StaffManagement/getOne?id=" + staffQualification.getId();
                HttpUtil.sendOkHttpRequest(address,new okhttp3.Callback(){
                    @Override
                    public void onResponse(Call call, Response response)throws IOException {
                        String responseData = response.body().string();
                        Log.d("SQgetStaff:",responseData);
                        parseJSONWithGSON(responseData,staff);
                        setText();
                    }
                    @Override
                    public void onFailure(Call call,IOException e){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(StaffQualification_Info.this, "获取数据失败！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        }).start();
    }

    public void getStaffQualification(long id){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String address = "http://119.23.38.100:8080/cma/StaffQualification/getOne?qualificationId=" + staffQualification.getQualificationId();
                HttpUtil.sendOkHttpRequest(address,new okhttp3.Callback(){
                    @Override
                    public void onResponse(Call call, Response response)throws IOException {
                        String responseData = response.body().string();
                        Log.d("newSQ",responseData);
                        parseJSONWithGSON(responseData,staffQualification);
                        //setText();
                    }
                    @Override
                    public void onFailure(Call call,IOException e){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(StaffQualification_Info.this, "获取数据失败！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        }).start();
    }

    public void parseJSONWithGSON(String jsondata,Object object){
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
                    Toast.makeText(StaffQualification_Info.this, "获取数据失败", Toast.LENGTH_LONG).show();
                }
            });
        }
        Gson gson = new Gson();
        if(object instanceof StaffManagement){
            staff = gson.fromJson(staffData,StaffManagement.class);
            Log.d("staffData",staff.getDepartment());
            Log.d("staffData",staff.getPosition());
            staffQualification.setDepartment(staff.getDepartment());
            staffQualification.setPosition(staff.getPosition());
        }else if(object instanceof StaffQualification){
            staffQualification = gson.fromJson(staffData,StaffQualification.class);
        }
    }

    public void getImage(){
        String address = "http://119.23.38.100:8080/cma/StaffQualification/getImage?qualificationId=" + staffQualification.getQualificationId();
        Log.d("address",address);
        Glide.with(this)
                .load(address)
                .error(R.drawable.invalid_image)   //图片加载失败时，将image_invalid放进去
                .animate(android.R.anim.slide_in_left)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showLong(StaffQualification_Info.this,"档案扫描件加载失败！");
                            }
                        });
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

package com.example.cma.ui.staff_management;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.signature.StringSignature;
import com.example.cma.R;
import com.example.cma.model.staff_management.StaffFile;
import com.example.cma.model.staff_management.StaffManagement;
import com.example.cma.model.staff_management.StaffQualification;
import com.example.cma.ui.training_management.TrainingApplication_Modify;
import com.example.cma.utils.HttpUtil;
import com.example.cma.utils.PhotoUtil;
import com.example.cma.utils.ViewUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class StaffQualification_Modify extends AppCompatActivity implements View.OnClickListener{
    private StaffQualification staffQualification;
    private TextView name_text;
    private TextView department_text;
    private TextView position_text;
    private TextView id_text;
    private EditText location_text;
    private Button saveButton;
    private Toolbar toolbar;
    private ImageView fileImage;
    private File outputImage;
    private boolean isFileChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_qualification__modify);

        initView();
        Intent intent = getIntent();
        staffQualification = (StaffQualification) intent.getSerializableExtra("StaffQualification");
        setText();
    }

    public void initView(){
        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        name_text = (TextView)findViewById(R.id.name_text);
        department_text = (TextView)findViewById(R.id.department_text);
        position_text = (TextView)findViewById(R.id.position_text);
        id_text = (TextView) findViewById(R.id.id_text);
        location_text = (EditText)findViewById(R.id.location_text);
        saveButton = (Button)findViewById(R.id.save_button);
        fileImage = (ImageView)findViewById(R.id.file_image);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //设置Toolbar左边显示一个返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //设置监听

        ViewUtil.ShowCursor(location_text);
        fileImage.setOnClickListener(this);
        saveButton.setOnClickListener(this);
    }

    public void setText(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                name_text.setText(staffQualification.getName());
                department_text.setText(staffQualification.getDepartment());
                position_text.setText(staffQualification.getPosition());
                id_text.setText(staffQualification.getQualificationId()+"");
                location_text.setText(staffQualification.getQualificationName());
            }
        });
        SimpleTarget target = new SimpleTarget<Bitmap>(300,200) {
            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                fileImage.setImageBitmap(bitmap);
            }
        };

        Glide.with(this)
                .load("http://119.23.38.100:8080/cma/StaffQualification/getImage?qualificationId="+staffQualification.getQualificationId())
                .asBitmap()
                .signature(new StringSignature(UUID.randomUUID().toString()))
                .error(R.drawable.add_image)
                .placeholder(R.drawable.loading)
                .animate(android.R.anim.slide_in_left)
                .into(target);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.save_button:
                if(location_text.getText().toString().isEmpty()) {
                    Toast.makeText(StaffQualification_Modify.this, "请填写完整！", Toast.LENGTH_LONG).show();
                    return;
                }
                AlertDialog.Builder dialog=new AlertDialog.Builder(StaffQualification_Modify.this);
                dialog.setTitle("确定提交吗？");
                dialog.setMessage("确保信息准确！");
                dialog.setCancelable(false);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                onSave();
                            }
                        }).start();

                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(StaffQualification_Modify.this,"你仍旧可以修改！",Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show();

                break;
            case  R.id.file_image:
                PhotoUtil.getInstance().showPopupWindow(StaffQualification_Modify.this);
                break;
            default:break;
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

    public void onSave(){
        String address = "\thttp://119.23.38.100:8080/cma/StaffQualification/modifyOne";
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        requestBody.addFormDataPart("qualificationId", ""+staffQualification.getQualificationId());
        requestBody.addFormDataPart("qualificationName", location_text.getText().toString());
        //requestBody.addFormDataPart("fileLocation", location_text.getText().toString());
        if(isFileChanged){
            RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), outputImage);
            requestBody.addFormDataPart("qualificationImage", outputImage.getName(), fileBody);
            Log.d("changePhotoUtil",outputImage.getPath());
            Log.d("PhotoUtil",outputImage.getName());
        }

        HttpUtil.sendOkHttpWithMultipartBody(address,requestBody.build(),new okhttp3.Callback(){
            @Override
            public void onResponse(Call call, Response response)throws IOException {
                String responseData = response.body().string();
                Log.d("onSavewithFile:",responseData);
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
                            Toast.makeText(StaffQualification_Modify.this, "修改成功！", Toast.LENGTH_SHORT).show();
                        }
                    });
                    finish();
                }
                else if(code == 210 && msg.equals("离任人员不能修改")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(StaffQualification_Modify.this, "该人员已离任，不能修改!", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(StaffQualification_Modify.this, "修改失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        switch (requestCode){
            case PhotoUtil.TAKE_PHOTO://take_photo
                if(resultCode==RESULT_OK)
                {
                    outputImage = PhotoUtil.getInstance().getOutputImage();
                    Bitmap bitmap = BitmapFactory.decodeFile(outputImage.getPath());
                    fileImage.setImageBitmap(bitmap);
                    isFileChanged = true;
                }
                break;
            case PhotoUtil.CHOOSE_PHOTO:
                if(resultCode==RESULT_OK) {
                    if(PhotoUtil.getInstance().selectPicture(data)){
                        Bitmap bitmap = PhotoUtil.getInstance().getBitmap();
                        fileImage.setImageBitmap(bitmap);
                        outputImage = PhotoUtil.getInstance().getOutputImage();
                        isFileChanged = true;
                    }
                }
            default:
                break;
        }
    }
}


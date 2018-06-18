package com.example.cma.ui.staff_management;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.StringSignature;
import com.example.cma.R;
import com.example.cma.model.staff_management.StaffFile;
import com.example.cma.model.staff_management.StaffManagement;
import com.example.cma.utils.HttpUtil;
import com.example.cma.utils.PhotoUtil;
import com.example.cma.utils.ToastUtil;
import com.example.cma.utils.ViewUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class StaffFile_Modify extends AppCompatActivity implements View.OnClickListener{
    private StaffFile staffFile;
    private TextView name_text;
    private EditText id_text;
    private EditText location_text;
    private Button saveButton;
    private Toolbar toolbar;
    private ImageView fileImage;
    private File outputImage;
    private boolean isFileChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_file_modify);

        initView();
        Intent intent = getIntent();
        staffFile = (StaffFile)intent.getSerializableExtra("StaffFile");
        setText();
    }

    public void initView(){
        name_text = (TextView)findViewById(R.id.name_text);
        id_text = (EditText)findViewById(R.id.id_text);
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
        ViewUtil.ShowCursor(id_text);
        ViewUtil.ShowCursor(location_text);
        fileImage.setOnClickListener(this);
        saveButton.setOnClickListener(this);
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

        SimpleTarget target = new SimpleTarget<Bitmap>(300,200) {
            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                fileImage.setImageBitmap(bitmap);
            }
        };

        Glide.with(this)
                .load("http://119.23.38.100:8080/cma/StaffFile/getImage?id="+staffFile.getId())
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
                onSave();
                break;
            case  R.id.file_image:
                PhotoUtil.getInstance().showPopupWindow(StaffFile_Modify.this);
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
        if(id_text.getText().toString().isEmpty()||
                location_text.getText().toString().isEmpty()) {
            Toast.makeText(StaffFile_Modify.this, "请填写完整！", Toast.LENGTH_LONG).show();
            return;
        }

        String address = "\thttp://119.23.38.100:8080/cma/StaffFile/modifyOne";
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        requestBody.addFormDataPart("id", ""+staffFile.getId());
        requestBody.addFormDataPart("fileId", id_text.getText().toString());
        requestBody.addFormDataPart("fileLocation", location_text.getText().toString());
        if(isFileChanged){
            RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), outputImage);
            requestBody.addFormDataPart("fileImage", outputImage.getName(), fileBody);
        }

        HttpUtil.sendOkHttpWithMultipartBody(address,requestBody.build(),new okhttp3.Callback(){
            @Override
            public void onResponse(Call call, Response response)throws IOException {
                String responseData = response.body().string();
                Log.d("onSavewithFile:",responseData);
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
                            Toast.makeText(StaffFile_Modify.this, "修改成功！", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(StaffFile_Modify.this, "修改失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        switch (requestCode){
            case PhotoUtil.TAKE_PHOTO:
                if(resultCode==RESULT_OK) {
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

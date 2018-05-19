package com.example.uicustomview;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PersonnelFile_Add extends AppCompatActivity implements View.OnClickListener{

    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    private static final int CAMERA_PERMISSIONS_REQUEST_CODE = 0x03;
    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 0x04;
    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/photo.jpg");
    private File fileCropUri = new File(Environment.getExternalStorageDirectory().getPath() + "/crop_photo.jpg");
    private Uri imageUri;
    private Uri cropImageUri;

    private PersonnelFile personnelFile;
    private EditText add_name;
    private EditText add_department;
    private EditText add_position;
    private EditText add_id;
    private EditText add_location;
    private TextView add_fileImage;
    private ImageView fileImage;
    private Button saveButton;
    private Button backButton;
    private PopupWindow mPopWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personnel_file__add);
        ViewUtils.inject(this);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        personnelFile = new PersonnelFile();
        findView();
    }

    private void findView(){
        add_name = (EditText)findViewById(R.id.add_name);
        add_name.setOnClickListener(this);
        add_department = (EditText)findViewById(R.id.add_department);
        add_department.setOnClickListener(this);
        add_position = (EditText)findViewById(R.id.add_position);
        add_position.setOnClickListener(this);
        add_id = (EditText)findViewById(R.id.add_id);
        add_id.setOnClickListener(this);
        add_location = (EditText)findViewById(R.id.add_location);
        add_location.setOnClickListener(this);

        add_fileImage = (TextView)findViewById(R.id.add_fileImage);
        add_fileImage.setOnClickListener(this);
        add_fileImage.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        fileImage = (ImageView)findViewById(R.id.fileImage);

        backButton = (Button)findViewById(R.id.add_title_back);
        saveButton = (Button)findViewById(R.id.add_title_save);
        backButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);
    }

    private boolean isEditTextChange(){
        //TODO 添加界面 没有填写任何东西，也返回true
        /*if(!add_name.getText().toString().isEmpty()) {
            if(!add_name.getText().toString().equals(personnelFile.getName()))
                return true;
        }*/
        if(!add_name.getText().toString().equals(personnelFile.getName()))
            return true;
        if(!add_department.getText().toString().isEmpty()) {
            if(!add_department.getText().toString().equals(personnelFile.getDepartment()))
            return true;
        }
        if(!add_position.getText().toString().isEmpty()) {
            if(!add_position.getText().toString().equals(personnelFile.getPosition()))
            return true;
        }
        if(!add_id.getText().toString().isEmpty()) {
            if(!add_id.getText().toString().equals(personnelFile.getId()))
            return true;
        }
        if(!add_location.getText().toString().isEmpty()) {
            if(!add_location.getText().toString().equals(personnelFile.getLocation()))
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        Log.d("PersonnelFile_Modified","PersonnelFile");
        switch (v.getId()){
            case R.id.add_name:
                add_name.setCursorVisible(true);break;
            case R.id.add_department:
                add_department.setCursorVisible(true);break;
            case R.id.add_position:
                add_position.setCursorVisible(true);break;
            case R.id.add_id:
                add_id.setCursorVisible(true);break;
            case R.id.add_location:
                add_location.setCursorVisible(true);break;
            case R.id.add_title_back:
                exitActivity();break;
            case R.id.add_title_save:
                saveChange();break;
            case R.id.add_fileImage:
                showPopupWindow();
                break;
            case R.id.pop_camare:
                autoObtainCameraPermission();break;
            case R.id.pop_local:
                autoObtainStoragePermission();break;
            case R.id.pop_cancel:
                mPopWindow.dismiss();break;
            default:break;
        }
    }

    //TODO 点击保存后，若再次更改，应该还要弹出提示
    public void exitActivity(){

        if(isEditTextChange()){
            //TODO 内容已经更改，需要传到数据库
            AlertDialog.Builder dialog = new AlertDialog.Builder(PersonnelFile_Add.this);
            dialog.setMessage("修改尚未保存，确定返回吗？");
            dialog.setCancelable(false);
            dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
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
        else
            finish();
    }

    //保存修改
    private void saveChange(){
        if(add_name.getText().toString().isEmpty()
                ||add_department.getText().toString().isEmpty()
                ||add_position.getText().toString().isEmpty()
                ||add_id.getText().toString().isEmpty()
                ||add_location.getText().toString().isEmpty())
            Toast.makeText(PersonnelFile_Add.this,"尚未填写完整！",Toast.LENGTH_SHORT).show();
        else {
            personnelFile.setName(add_name.getText().toString());
            personnelFile.setDepartment(add_department.getText().toString());
            personnelFile.setPosition(add_position.getText().toString());
            personnelFile.setId(add_id.getText().toString());
            personnelFile.setLocation(add_location.getText().toString());
            doPost();
        }
    }

    public void doPost(){
        String address = "http://119.23.38.100:8080/cma/StaffFile/addStaff";
        //拿到body的构建器
        FormBody.Builder builder = new FormBody.Builder();
        //添加参数
        builder.add("name", personnelFile.getName())
                .add("department", personnelFile.getDepartment())
                .add("position", personnelFile.getPosition())
                .add("id", personnelFile.getId())
                .add("location", personnelFile.getLocation())
                .add("fileImage", personnelFile.getFileImage());
        //拿到requestBody
        RequestBody requestBody = builder.build();

        HttpUtil.sendOkHttpWithRequestBody(address,requestBody,new okhttp3.Callback(){
            @Override
            public void onResponse(Call call, Response response)throws IOException {
                String responseData = response.body().string();
                ToastUtils.showShort(PersonnelFile_Add.this,"保存成功！");
                ToastUtils.showShort(PersonnelFile_Add.this,responseData);
            }
            @Override
            public void onFailure(Call call,IOException e){
                ToastUtils.showShort(PersonnelFile_Add.this,"保存失败");
            }
        });

    }

    /**
     * 自动获取相机权限
     */
    private void autoObtainCameraPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                ToastUtils.showShort(this, "您已经拒绝过一次");
            }
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_PERMISSIONS_REQUEST_CODE);
        } else {//有权限直接调用系统相机拍照
            if (hasSdcard()) {
                imageUri = Uri.fromFile(fileUri);
                //通过FileProvider创建一个content类型的Uri
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    imageUri = FileProvider.getUriForFile(PersonnelFile_Add.this, "com.wgx.fileprovider", fileUri);
                }
                PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
            } else {
                ToastUtils.showShort(this, "设备没有SD卡！");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            //调用系统相机申请拍照权限回调
            case CAMERA_PERMISSIONS_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (hasSdcard()) {
                        imageUri = Uri.fromFile(fileUri);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            imageUri = FileProvider.getUriForFile(PersonnelFile_Add.this, "com.wgx.fileprovider", fileUri);//通过FileProvider创建一个content类型的Uri
                        PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
                    } else {
                        ToastUtils.showShort(this, "设备没有SD卡！");
                    }
                } else {

                    ToastUtils.showShort(this, "请允许打开相机！！");
                }
                break;


            }
            //调用系统相册申请Sdcard权限回调
            case STORAGE_PERMISSIONS_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
                } else {

                    ToastUtils.showShort(this, "请允许打操作SDCard！！");
                }
                break;
            default:
        }
    }

    private static final int OUTPUT_X = 480;
    private static final int OUTPUT_Y = 480;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                //拍照完成回调
                case CODE_CAMERA_REQUEST:
                    cropImageUri = Uri.fromFile(fileCropUri);
                    ToastUtils.showShort(this, "拍照完成！");
                    PhotoUtils.cropImageUri(this, imageUri, cropImageUri, 1, 1, OUTPUT_X, OUTPUT_Y, CODE_RESULT_REQUEST);
                    break;
                //访问相册完成回调
                case CODE_GALLERY_REQUEST:
                    if (hasSdcard()) {
                        cropImageUri = Uri.fromFile(fileCropUri);
                        Uri newUri = Uri.parse(PhotoUtils.getPath(this, data.getData()));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            newUri = FileProvider.getUriForFile(this, "com.wgx.fileprovider", new File(newUri.getPath()));
                        }
                        PhotoUtils.cropImageUri(this, newUri, cropImageUri, 1, 1, OUTPUT_X, OUTPUT_Y, CODE_RESULT_REQUEST);
                    } else {
                        ToastUtils.showShort(this, "设备没有SD卡！");
                    }
                    break;
                case CODE_RESULT_REQUEST:
                    Bitmap bitmap = PhotoUtils.getBitmapFromUri(cropImageUri, this);
                    if (bitmap != null) {
                        ToastUtils.showShort(this, "成功！");
                        showImages(bitmap);
                    }
                    break;
                default:
            }
        }
    }

    /**
     * 自动获取sdk权限
     */

    private void autoObtainStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSIONS_REQUEST_CODE);
        } else {
            PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
        }

    }

    private void showImages(Bitmap bitmap) {
        fileImage.setImageBitmap(bitmap);
        //每次选取照片结束后dismiss弹窗
        mPopWindow.dismiss();
    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    private void showPopupWindow() {
        //设置contentView
        View contentView = LayoutInflater.from(PersonnelFile_Add.this).inflate(R.layout.popupwindow, null);
        mPopWindow = new PopupWindow(contentView,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        mPopWindow.setContentView(contentView);
        //设置各个控件的点击响应
        TextView tv1 = (TextView)contentView.findViewById(R.id.pop_camare);
        TextView tv2 = (TextView)contentView.findViewById(R.id.pop_local);
        TextView tv3 = (TextView)contentView.findViewById(R.id.pop_cancel);
        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        tv3.setOnClickListener(this);

        //显示PopupWindow
        View rootview = LayoutInflater.from(PersonnelFile_Add.this).inflate(R.layout.activity_personnel_file__add, null);
        mPopWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
    }
}

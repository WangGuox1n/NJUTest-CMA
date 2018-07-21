package com.example.cma.ui.staff_management;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cma.R;
import com.example.cma.model.staff_management.StaffFile;
import com.example.cma.utils.HttpUtil;
import com.example.cma.utils.PhotoUtil;
import com.example.cma.utils.ToastUtil;
import com.example.cma.utils.ViewUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class StaffFile_Modify extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "StaffFile_Modify";
    private StaffFile staffFile;
    private TextView name_text;
    private EditText id_text;
    private EditText location_text;
    private Button saveButton;
    private ImageView fileImage;
    private File outputImage;
    private boolean isFileChanged;
    private ProgressDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_file_modify);

        initView();
        Intent intent = getIntent();
        staffFile = (StaffFile) intent.getSerializableExtra("StaffFile");
        setText();
    }

    public void initView() {
        name_text = findViewById(R.id.name_text);
        id_text = findViewById(R.id.id_text);
        location_text = findViewById(R.id.location_text);
        fileImage = findViewById(R.id.file_image);
        fileImage.setOnClickListener(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        ViewUtil.getInstance().setSupportActionBar(this, toolbar);
        ViewUtil.ShowCursor(id_text);
        ViewUtil.ShowCursor(location_text);
        saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(this);
    }

    public void setText() {
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
        switch (v.getId()) {
            case R.id.save_button:
                saveButton.setEnabled(false);
                onSave();
                break;
            case R.id.file_image:
                PhotoUtil.getInstance().showPopupWindow(StaffFile_Modify.this);
                break;
            default:
                break;
        }
    }

    //监听返回按钮的点击事件，比如可以返回上级Activity
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:{
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void onSave() {
        if (id_text.getText().toString().isEmpty() ||
                location_text.getText().toString().isEmpty()) {
            Toast.makeText(StaffFile_Modify.this, "请填写完整！", Toast.LENGTH_LONG).show();
            return;
        }

        String address = "\thttp://119.23.38.100:8080/cma/StaffFile/modifyOne";
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        requestBody.addFormDataPart("id", "" + staffFile.getId());
        requestBody.addFormDataPart("fileId", id_text.getText().toString());
        requestBody.addFormDataPart("fileLocation", location_text.getText().toString());
        if (isFileChanged) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
            final String now = simpleDateFormat.format(new Date());
            String filename = staffFile.getName() + "_档案扫描件_" + now + ".jpg";
            RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), outputImage);
            requestBody.addFormDataPart("fileImage", filename, fileBody);
            staffFile.setFileImage(filename);
            showLoadingDialog();
        }

        HttpUtil.sendOkHttpWithMultipartBody(address, requestBody.build(), new okhttp3.Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                Log.d(TAG, responseData);
                int code = 0;
                String msg = "";
                try {
                    JSONObject object = new JSONObject(responseData);
                    code = object.getInt("code");
                    msg = object.getString("msg");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (code == 200 && msg.equals("成功")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showShort(StaffFile_Modify.this, "修改成功");
                            if(isFileChanged)
                                loadingDialog.dismiss();
                        }
                    });
                    Intent intent = new Intent();
                    intent.putExtra("newFileName",staffFile.getFileImage());
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        saveButton.setEnabled(true);
                        ToastUtil.showShort(StaffFile_Modify.this, "修改失败");
                        if(isFileChanged)
                            loadingDialog.dismiss();
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PhotoUtil.TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    outputImage = PhotoUtil.getInstance().getOutputImage();
                    Bitmap bitmap = BitmapFactory.decodeFile(outputImage.getPath());
                    fileImage.setImageBitmap(bitmap);
                    isFileChanged = true;
                }
                break;
            case PhotoUtil.CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    if (PhotoUtil.getInstance().selectPicture(data)) {
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

    public void showLoadingDialog(){
        loadingDialog = new ProgressDialog(StaffFile_Modify.this);
        loadingDialog.setTitle("正在上传中");
        loadingDialog.setMessage("请稍等......");
        loadingDialog.setCancelable(true);
        loadingDialog.show();
    }
}

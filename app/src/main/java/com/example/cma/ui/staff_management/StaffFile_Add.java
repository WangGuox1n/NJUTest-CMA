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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cma.R;
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
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;


public class StaffFile_Add extends AppCompatActivity implements View.OnClickListener,Spinner.OnItemSelectedListener{

    private List<StaffManagement> wholeList = new ArrayList<>();
    private StaffManagement staffManagement;
    private Spinner spinner;
    private List<StaffManagement> staff_list = new ArrayList<>();
    private List<String> data_list = new ArrayList<String>();
    private ArrayAdapter<String> adapter;

    private TextView name_text;
    private TextView department_text;
    private TextView position_text;
    private EditText id_text;
    private EditText location_text;
    private Button submitButton;
    private Toolbar toolbar;

    private ImageView fileImage;
    private File outputImage;
    private boolean isPhotoSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_file_add);
        initView();
        getAll();
        getDataFromServer();
    }

    public void initView(){
        spinner = (Spinner) findViewById(R.id.spinner);
        name_text = (TextView)findViewById(R.id.name_text);
        department_text = (TextView)findViewById(R.id.department_text);
        position_text = (TextView)findViewById(R.id.position_text);
        id_text = (EditText)findViewById(R.id.id_text);
        location_text = (EditText)findViewById(R.id.location_text);
        submitButton = (Button)findViewById(R.id.submit_button);
        toolbar = (Toolbar) findViewById(R.id.mToolbar4);
        fileImage=(ImageView)findViewById(R.id.file_image);
        fileImage.setOnClickListener(this);

        ViewUtil.ShowCursor(id_text);
        ViewUtil.ShowCursor(location_text);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        //设置监听
        submitButton.setOnClickListener(this);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submit_button:
                onSave();
                break;
            case R.id.file_image:
                PhotoUtil.getInstance().showPopupWindow(this);
                break;
            default:break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        int i = 1;
        for(StaffManagement staff : staff_list) {
            String selectedItem = arg0.getSelectedItem().toString();
            if(selectedItem.equals(i +". "+staff.getName())){
                for(StaffManagement info : wholeList){
                    if(info.getId() == staff.getId()){
                        name_text.setText(info.getName());
                        department_text.setText(info.getDepartment());
                        position_text.setText(info.getPosition());
                    }
                }
                staffManagement = staff;
                break;
            }
            i++;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void initDataList(){
        data_list.clear();
        int i = 1;
        for(StaffManagement staff : staff_list){
            data_list.add(i+". "+staff.getName());
            i++;
        }
        Log.d("data_list",""+i+staff_list.size());
    }

    //向后端发送请求，返回所有没有档案的人员
    public void getDataFromServer(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpUtil.sendOkHttpRequest("http://119.23.38.100:8080/cma/StaffManagement/getNoFile",new okhttp3.Callback(){
                    @Override
                    public void onResponse(Call call, Response response)throws IOException {
                        String responseData = response.body().string();
                        Log.d("responseData:",responseData);
                        parseJSONWithGSON(responseData);
                        initDataList();
                        showResponse();
                    }
                    @Override
                    public void onFailure(Call call,IOException e){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(StaffFile_Add.this, "请求数据失败！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        }).start();
    }

    private void parseJSONWithGSON(String jsondata){
        JSONArray array = new JSONArray();
        try {
            JSONObject object = new JSONObject(jsondata);//最外层的JSONObject对象
            array = object.getJSONArray("data");
        }catch (Exception e){
            e.printStackTrace();
        }
        if(array.equals("null")){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(StaffFile_Add.this, "无可添加档案的人员", Toast.LENGTH_LONG).show();
                }
            });
        }
        Gson gson = new Gson();
        List<StaffManagement> newList = gson.fromJson(array.toString(),new TypeToken<List<StaffManagement>>(){}.getType());
        staff_list.clear();
        staff_list.addAll(newList);
    }

    private void showResponse() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //适配器
                adapter= new ArrayAdapter<String>(StaffFile_Add.this, android.R.layout.simple_spinner_item, data_list);
                //设置样式
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //加载适配器
                spinner.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });
    }

    //监听返回按钮的点击事件，比如可以返回上级Activity
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackConfirm();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(!id_text.getText().toString().isEmpty()||
                !location_text.getText().toString().isEmpty()||isPhotoSelect) {
            AlertDialog.Builder dialog=new AlertDialog.Builder(StaffFile_Add.this);
            dialog.setTitle("内容尚未保存");
            dialog.setMessage("是否退出？");
            dialog.setCancelable(true);
            dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            dialog.show();
        }else
            super.onBackPressed();
    }

    public void getAll(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpUtil.sendOkHttpRequest("http://119.23.38.100:8080/cma/StaffManagement/getAll",new okhttp3.Callback(){
                    @Override
                    public void onResponse(Call call, Response response)throws IOException {
                        String responseData = response.body().string();
                        Log.d("getAll:",responseData);
                        parseAll(responseData);
                    }
                    @Override
                    public void onFailure(Call call,IOException e){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(StaffFile_Add.this, "请求数据失败！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        }).start();
    }

    private void parseAll(String jsondata){
        JSONArray array = new JSONArray();
        try {
            JSONObject object = new JSONObject(jsondata);//最外层的JSONObject对象
            array = object.getJSONArray("data");
        }catch (Exception e){
            e.printStackTrace();
        }
        if(array.equals("null")){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(StaffFile_Add.this, "无可添加档案的人员", Toast.LENGTH_LONG).show();
                }
            });
        }
        Gson gson = new Gson();
        wholeList  = gson.fromJson(array.toString(),new TypeToken<List<StaffManagement>>(){}.getType());
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        switch (requestCode){
            case PhotoUtil.TAKE_PHOTO:
                if(resultCode==RESULT_OK)
                {
                    outputImage = PhotoUtil.getInstance().getOutputImage();
                    Bitmap bitmap = BitmapFactory.decodeFile(outputImage.getPath());
                    fileImage.setImageBitmap(bitmap);
                    isPhotoSelect = true;
                }
                break;
            case PhotoUtil.CHOOSE_PHOTO:
                if(resultCode==RESULT_OK)
                {
                    if(PhotoUtil.getInstance().selectPicture(data)){
                        Bitmap bitmap = PhotoUtil.getInstance().getBitmap();
                        fileImage.setImageBitmap(bitmap);
                        outputImage = PhotoUtil.getInstance().getOutputImage();
                        isPhotoSelect = true;
                    }
                }
                break;
            default:
                break;
        }
    }

    public void onSave(){
        if(staff_list.size() == 0){
            ToastUtil.showShort(StaffFile_Add.this,"没有可添加档案的人员");
            return;
        }
        if(id_text.getText().toString().isEmpty()||
                location_text.getText().toString().isEmpty()) {
            Toast.makeText(StaffFile_Add.this, "请填写完整！", Toast.LENGTH_LONG).show();
            return;
        }
        if(outputImage == null){
            Toast.makeText(StaffFile_Add.this, "请选择档案图片！", Toast.LENGTH_LONG).show();
            return;
        }

        AlertDialog.Builder dialog=new AlertDialog.Builder(StaffFile_Add.this);
        dialog.setMessage("确定提交？");
        dialog.setCancelable(false);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        postDataToServer();
                    }
                }).start();
            }
        });
        dialog.show();
    }

    public void postDataToServer(){
        String address = "\thttp://119.23.38.100:8080/cma/StaffFile/addOne";
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        requestBody.addFormDataPart("id", ""+staffManagement.getId());
        requestBody.addFormDataPart("fileId", id_text.getText().toString());
        requestBody.addFormDataPart("fileLocation", location_text.getText().toString());

        RequestBody body = RequestBody.create(MediaType.parse("image/*"), outputImage);
        requestBody.addFormDataPart("fileImage", outputImage.getName(), body);

        HttpUtil.sendOkHttpWithMultipartBody(address,requestBody.build(),new okhttp3.Callback(){
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
                            Toast.makeText(StaffFile_Add.this, "提交成功！", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(StaffFile_Add.this, "提交失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void onBackConfirm(){
        if(!id_text.getText().toString().isEmpty()||
                !location_text.getText().toString().isEmpty()||isPhotoSelect) {
            AlertDialog.Builder dialog=new AlertDialog.Builder(StaffFile_Add.this);
            dialog.setTitle("内容尚未保存");
            dialog.setMessage("是否退出？");
            dialog.setCancelable(true);
            dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            dialog.show();
        }else
            finish();
    }
}

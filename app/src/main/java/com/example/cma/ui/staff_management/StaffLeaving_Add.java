package com.example.cma.ui.staff_management;

import android.support.v7.app.ActionBar;
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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cma.R;
import com.example.cma.model.staff_management.StaffManagement;
import com.example.cma.utils.HttpUtil;
import com.example.cma.utils.ViewUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.feezu.liuli.timeselector.TimeSelector;

public class StaffLeaving_Add extends AppCompatActivity implements View.OnClickListener,Spinner.OnItemSelectedListener{

    private List<StaffManagement> whole_stafflist = new ArrayList<>();
    private StaffManagement staffManagement = new StaffManagement();
    private Spinner spinner;
    private List<StaffManagement> staff_list = new ArrayList<>();
    private List<String> data_list = new ArrayList<String>();;
    private ArrayAdapter<String> adapter;

    private TextView name_text;
    private TextView department_text;
    private TextView position_text;
    private TextView date_text;
    private Button submitButton;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_leaving_add);
        initView();
        getDataFromServer();
    }

    public void initView(){
        spinner = (Spinner) findViewById(R.id.spinner);
        name_text = (TextView)findViewById(R.id.name_text);
        department_text = (TextView)findViewById(R.id.department_text);
        position_text = (TextView)findViewById(R.id.position_text);
        date_text = (TextView)findViewById(R.id.date_text);

        submitButton = (Button)findViewById(R.id.submit_button);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        //设置监听
        submitButton.setOnClickListener(this);
        spinner.setOnItemSelectedListener(this);

        LinearLayout dateLayout=(LinearLayout)findViewById(R.id.layout5);
        date_text.setOnClickListener(this);
        dateLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submit_button:
                onSave();
                break;
            case R.id.date_text:
                ViewUtil.getInstance().selectDate(StaffLeaving_Add.this,date_text);
                break;
            case R.id.layout5:
                ViewUtil.getInstance().selectDate(StaffLeaving_Add.this,date_text);
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
                staffManagement = staff;
                name_text.setText(staff.getName());
                department_text.setText(staff.getDepartment());
                position_text.setText(staff.getPosition());
                break;
            }
            i++;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void initDataList(){
        int i = 1;
        for(StaffManagement staff : staff_list){
            data_list.add(i+". "+staff.getName());
            i++;
        }
        Log.d("data_list",""+i+staff_list.size());
    }

    //向后端发送请求，返回所有人员记录
    public void getDataFromServer(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpUtil.sendOkHttpRequest("http://119.23.38.100:8080/cma/StaffManagement/getAll",new okhttp3.Callback(){
                    @Override
                    public void onResponse(Call call, Response response)throws IOException {
                        String responseData = response.body().string();
                        Log.d("StaffLeaving_Add Data:",responseData);
                        parseJSONWithGSON(responseData);
                        initDataList();
                        showResponse();
                    }
                    @Override
                    public void onFailure(Call call,IOException e){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(StaffLeaving_Add.this, "请求数据失败！", Toast.LENGTH_SHORT).show();
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
                adapter= new ArrayAdapter<String>(StaffLeaving_Add.this, android.R.layout.simple_spinner_item, data_list);
                //设置样式
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //加载适配器
                spinner.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void onSave(){
        //保存前先判断
        if(date_text.getText().toString().isEmpty()) {
            Toast.makeText(StaffLeaving_Add.this, "请填写离休日期！", Toast.LENGTH_LONG).show();
            return;
        }
        postSave();
    }

    public void postSave(){
        String address = "\thttp://119.23.38.100:8080/cma/StaffLeaving/addOne";
        //拿到body的构建器
        FormBody.Builder builder = new FormBody.Builder();

        builder.add("id",""+staffManagement.getId())
                .add("leavingDate", date_text.getText().toString());
        RequestBody requestBody = builder.build();

        HttpUtil.sendOkHttpWithRequestBody(address,requestBody,new okhttp3.Callback(){
            @Override
            public void onResponse(Call call, Response response)throws IOException {
                String responseData = response.body().string();
                Log.d("StaffLeaving_Add:",responseData);
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
                            Toast.makeText(StaffLeaving_Add.this, "提交成功！", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(StaffLeaving_Add.this, "提交失败！", Toast.LENGTH_SHORT).show();
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
}

package com.example.cma.ui.self_inspection;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.cma.R;
import com.example.cma.adapter.self_inspection.SelfInspectionAdapter;
import com.example.cma.model.self_inspection.SelfInspection;
import com.example.cma.utils.AddressUtil;
import com.example.cma.utils.HttpUtil;
import com.example.cma.utils.ToastUtil;
import com.example.cma.utils.ViewUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SelfInspection_Main extends AppCompatActivity implements SearchView.OnQueryTextListener,View.OnClickListener{

    private static final String TAG = "SelfInspection_Main";
    //data
    private List<SelfInspection> list= new ArrayList<>();

    //View
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private SearchView searchView;
    private FloatingActionButton addButton;
    private SelfInspectionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.self_inspection_main);
        initView();
        adapter = new SelfInspectionAdapter(list);
        recyclerView.setAdapter(adapter);
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

    @Override
    protected void onResume() {
        super.onResume();
        getDataFromServer();
    }

    //初始化所有控件
    public void initView(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView =(RecyclerView) findViewById(R.id.recycler_view);
        searchView =(SearchView)findViewById(R.id.searchview);
        addButton = (FloatingActionButton)findViewById(R.id.add_button);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        ViewUtil.getInstance().setSupportActionBar(this,toolbar);

        //默认不弹出键盘
        searchView.setFocusable(false);
        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(false);
        //listView可筛选
        addButton.setOnClickListener(this);
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.filter(newText);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        adapter.filter(query);
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_button:
                showAddDialog();
                break;
            default:break;
        }
    }

    public void showAddDialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(SelfInspection_Main.this);
        final View dialogView = LayoutInflater.from(SelfInspection_Main.this)
                .inflate(R.layout.self_inspection_add,null);
        builder.setTitle("新增自查管理文档集");
        builder.setView(dialogView);
        builder.setCancelable(false);
        final AlertDialog editDialog = builder.create();
        editDialog.show();

        //取得 dialog中的值
        Button submitButton=(Button) dialogView.findViewById(R.id.submit_button);
        Button cancelButton=(Button) dialogView.findViewById(R.id.cancel_button);
        final EditText name_text = dialogView.findViewById(R.id.name_text);
        final TextView time_text = dialogView.findViewById(R.id.time_text);
        time_text.setOnClickListener(this);

        time_text.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ViewUtil.getInstance().selectDate(SelfInspection_Main.this,time_text);
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name_text.getText().toString().isEmpty()||time_text.getText().toString().isEmpty()){
                    ToastUtil.showShort(SelfInspection_Main.this,"请填写完整");
                }else{
                    postSave(name_text.getText().toString(),time_text.getText().toString());
                    editDialog.dismiss();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!name_text.getText().toString().isEmpty()||!time_text.getText().toString().isEmpty()){
                    AlertDialog.Builder dialog=new AlertDialog.Builder(SelfInspection_Main.this);
                    dialog.setTitle("内容尚未保存");
                    dialog.setMessage("是否退出？");
                    dialog.setCancelable(true);
                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            editDialog.dismiss();
                        }
                    });
                    dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    dialog.show();
                }else{
                    editDialog.dismiss();
                }
            }
        });
    }

    public void postSave(String name,String date){
        String address = AddressUtil.getAddress(AddressUtil.SelfInspection_addOne);
        RequestBody requestBody = new FormBody.Builder()
                .add("name",name)
                .add("date",date)
                .build();

        HttpUtil.sendOkHttpWithRequestBody(address,requestBody,new okhttp3.Callback(){
            @Override
            public void onResponse(Call call, Response response)throws IOException {
                String responseData = response.body().string();
                Log.d(TAG,responseData);
                int code = 0;
                String msg = "";
                try {
                    JSONObject object = new JSONObject(responseData);
                    code = object.getInt("code");
                    msg = object.getString("msg");
                }catch (JSONException e){
                    ToastUtil.showShort(SelfInspection_Main.this,"网络连接错误，添加失败");
                    e.printStackTrace();
                }
                if(code == 200 && msg.equals("成功")) {
                    ToastUtil.showShort(SelfInspection_Main.this,"添加成功");
                    getDataFromServer();
                }
            }
            @Override
            public void onFailure(Call call,IOException e){
                ToastUtil.showShort(SelfInspection_Main.this,"添加失败");
            }
        });
    }

    public void getDataFromServer(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String address = AddressUtil.getAddress(AddressUtil.SelfInspection_getAll);
                Log.d(TAG,address);
                HttpUtil.sendOkHttpRequest(address,new okhttp3.Callback(){
                    @Override
                    public void onResponse(Call call, Response response)throws IOException {
                        String responseData = response.body().string();
                        Log.d(TAG,responseData);
                        parseJSONWithGSON(responseData);
                        //showResponse();
                    }
                    @Override
                    public void onFailure(Call call,IOException e){
                        ToastUtil.showShort(SelfInspection_Main.this, "请求数据失败");
                    }
                });
            }
        }).start();
    }

    private void parseJSONWithGSON(String jsonData){
        JSONArray array = new JSONArray();
        try {
            JSONObject object = new JSONObject(jsonData);
            array = object.getJSONArray("data");
        }catch (Exception e){
            e.printStackTrace();
        }
        if(array.length()==0){
            ToastUtil.showShort(SelfInspection_Main.this, "自查记录为空");
        }
        Gson gson = new Gson();
        List<SelfInspection> newList = gson.fromJson(array.toString(),new TypeToken<List<SelfInspection>>(){}.getType());
        list.clear();
        list.addAll(newList);
        Collections.reverse(list);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.myNotifyDataSetChanged();
            }
        });
    }

    private void showResponse() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Collections.reverse(list);
                adapter = new SelfInspectionAdapter(list);
                recyclerView.setAdapter(adapter);
            }
        });
    }
}

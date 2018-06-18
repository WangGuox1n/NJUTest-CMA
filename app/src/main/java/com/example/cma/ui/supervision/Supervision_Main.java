package com.example.cma.ui.supervision;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.cma.R;
import com.example.cma.adapter.supervision.SupervisionAdapter;
import com.example.cma.model.supervision.Supervision;
import com.example.cma.utils.AddressUtil;
import com.example.cma.utils.HttpUtil;
import com.example.cma.utils.ToastUtil;
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

public class Supervision_Main extends AppCompatActivity implements SearchView.OnQueryTextListener,View.OnClickListener,AdapterView.OnItemClickListener{

    //data
    private List<Supervision> list= new ArrayList<>();;

    //View
    private Toolbar toolbar;
    private ListView listView;
    private SearchView searchView;
    private FloatingActionButton addButton;
    private SupervisionAdapter adapter;

    EditText author_text;
    EditText remark_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.supervision_main);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDataFromServer();
    }

    //初始化所有控件
    public void initView(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        listView =(ListView)findViewById(R.id.list_view);
        searchView =(SearchView)findViewById(R.id.searchview);
        addButton = (FloatingActionButton)findViewById(R.id.add_button);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setSupportActionBar(toolbar);
        //默认不弹出键盘
        searchView.setFocusable(false);
        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(false);
        //listView可筛选
        listView.setTextFilterEnabled(true);
        listView.setOnItemClickListener(this);
        addButton.setOnClickListener(this);
    }

    //监听searchView中文本的改变
    @Override
    public boolean onQueryTextChange(String newText) {
        ListAdapter adapter=listView.getAdapter();
        if(adapter instanceof Filterable){
            Filter filter=((Filterable)adapter).getFilter();
            if(newText.isEmpty()){
                filter.filter(null);
            }else{
                filter.filter(newText);
            }
        }
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        // TODO Auto-generated method stub
        ListAdapter listAdapter=listView.getAdapter();
        if(listAdapter instanceof Filterable){
            Filter filter=((Filterable)listAdapter).getFilter();
            if(query.isEmpty()){
                filter.filter(null);
            }else{
                filter.filter(query);
            }
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_button:{
                showAddDialog();
                break;
            }
            default:break;
        }
    }

    //listView 的Item点击事件,跳转到编辑页面
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent=new Intent(Supervision_Main.this,Supervision_Info.class);
        Supervision supervision = (Supervision)listView.getItemAtPosition(position);
        intent.putExtra("Supervision", supervision);
        startActivity(intent);
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

    //向后端发送请求，返回所有人员记录
    public void getDataFromServer(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String address = AddressUtil.Supervision_getAll();
                HttpUtil.sendOkHttpRequest(address,new okhttp3.Callback(){
                    @Override
                    public void onResponse(Call call, Response response)throws IOException {
                        String responseData = response.body().string();
                        Log.d("Supervision_Main",responseData);
                        parseJSONWithGSON(responseData);
                        showResponse();
                    }
                    @Override
                    public void onFailure(Call call,IOException e){
                        ToastUtil.showShort(Supervision_Main.this, "请求数据失败！");
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
            ToastUtil.showLong(Supervision_Main.this, "监督记录为空！");
        }
        Gson gson = new Gson();
        List<Supervision> newList = gson.fromJson(array.toString(),new TypeToken<List<Supervision>>(){}.getType());
        list.clear();
        list.addAll(newList);
    }

    private void showResponse() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Collections.reverse(list);
                adapter = new SupervisionAdapter(Supervision_Main.this, R.layout.supervision_main_listitem,list);
                listView.setAdapter(adapter);
            }
        });
    }

    public void showAddDialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(Supervision_Main.this);
        final View dialogView = LayoutInflater.from(Supervision_Main.this)
                .inflate(R.layout.supervision_add,null);
        builder.setTitle("监督·添加");
        builder.setView(dialogView);
        builder.setCancelable(false);
        final AlertDialog editDialog = builder.create();
        editDialog.show();

        //取得 dialog中的值
        Button submitButton=(Button) dialogView.findViewById(R.id.submit_button);
        Button cancelButton=(Button) dialogView.findViewById(R.id.cancel_button);
        author_text = (EditText)  dialogView.findViewById(R.id.author_text);
        remark_text = (EditText)  dialogView.findViewById(R.id.remark_text);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(author_text.getText().toString().isEmpty()||remark_text.getText().toString().isEmpty()){
                    ToastUtil.showShort(Supervision_Main.this,"请填写完整！");
                }else{
                    postSave();
                    editDialog.dismiss();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!author_text.getText().toString().isEmpty()||!remark_text.getText().toString().isEmpty()){
                    AlertDialog.Builder dialog=new AlertDialog.Builder(Supervision_Main.this);
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

    public void postSave(){
        String address = AddressUtil.Supervision_addOne();
        RequestBody requestBody = new FormBody.Builder()
                .add("author",author_text.getText().toString())
                .add("remark",remark_text.getText().toString())
                .build();

        HttpUtil.sendOkHttpWithRequestBody(address,requestBody,new okhttp3.Callback(){
            @Override
            public void onResponse(Call call, Response response)throws IOException {
                String responseData = response.body().string();
                Log.d("SupervisionRecord_Add:",responseData);
                int code = 0;
                String msg = "";
                try {
                    JSONObject object = new JSONObject(responseData);
                    code = object.getInt("code");
                    msg = object.getString("msg");
                }catch (JSONException e){
                    ToastUtil.showShort(Supervision_Main.this,"网络连接错误，提交失败！");
                    e.printStackTrace();
                }
                if(code == 200 && msg.equals("成功")) {
                    ToastUtil.showShort(Supervision_Main.this,"添加成功！");
                    getDataFromServer();
                }
            }
            @Override
            public void onFailure(Call call,IOException e){
                ToastUtil.showShort(Supervision_Main.this,"添加失败！");
            }
        });
    }
}

package com.example.cma.ui.quality_system;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cma.R;
import com.example.cma.adapter.quality_system.QualityManualAdapter;
import com.example.cma.adapter.quality_system.QualityManualChangeAdapter;
import com.example.cma.model.quality_system.QualityManual;
import com.example.cma.model.staff_management.Result;
import com.example.cma.utils.FileUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class QualityManualChange_Main extends AppCompatActivity {

    static public List<QualityManual> dangAns=new ArrayList<QualityManual>();

    private QualityManualChangeAdapter adapter;
    private ListView listView;
    private SearchView searchView;
    private QualityManual temp;
    String function;
    private ButtonOnClick buttonOnClick = new ButtonOnClick(1);
    Toolbar toolbar;
    private int k;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quality_manual_change__main);
        Intent intent=getIntent();
        function=intent.getStringExtra("function");
        initView();
        initDangan();
        //在Activity代码中使用Toolbar对象替换ActionBar
        toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        //设置Toolbar左边显示一个返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        //设置搜索文本监听
        listView.setTextFilterEnabled(true);
        searchView.setFocusable(false);
        //当点击搜索按钮时触发
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //当点击搜索按钮触发该方法进行回调
            @Override
            public boolean onQueryTextSubmit(String s) {
                ListAdapter listAdapter=listView.getAdapter();
                if(listAdapter instanceof Filterable){
                    Filter filter=((Filterable)listAdapter).getFilter();
                    if(s==null||s.length()==0){
                        filter.filter(null);
                    }else{
                        filter.filter(s);
                    }
                }
                return false;
            }

            //当搜索内容改变时触发该方法进行回调
            @Override
            public boolean onQueryTextChange(String s) {
                ListAdapter adapter=listView.getAdapter();
                if(adapter instanceof Filterable){
                    Filter filter=((Filterable)adapter).getFilter();
                    if(s==null||s.length()==0){
                        filter.filter(null);
                    }else{
                        filter.filter(s);
                    }
                }
                return true;
            }
        });

        showResponse();//对于从数据库获取的数据重新开UI线程否则出错。

        //选择listview中的一个元素
        registerForContextMenu(listView);
        //添加按钮

    }
    private class ButtonOnClick implements DialogInterface.OnClickListener
    {
        private int index; // 表示选项的索引
        public ButtonOnClick(int index) {
            this.index = index;
        }
        @Override
        public void onClick(DialogInterface dialog,int which)
        {

            // which表示单击的按钮索引，所有的选项索引都是大于0，按钮索引都是小于0的。

            if (which >= 0)
            {
           //如果单击的是列表项，将当前列表项的索引保存在index中。
                index = which;
            }
            else
            {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    Log.d("修改的状态","111111111111111");
                    changeState(index+1);
                }else if (which == DialogInterface.BUTTON_NEGATIVE)
                {

                }
            }
        }
    }


    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        temp = (QualityManual)listView.getItemAtPosition(((AdapterView.AdapterContextMenuInfo) menuInfo).position);//获取listview的item对象
        Log.d("点击的对象",temp.getFileName());
        getMenuInflater().inflate(R.menu.context_menu, menu);
    }
    public boolean onContextItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_item3) {
           // showSingleChoiceButton();
            Log.d("点击了第三个按钮","!!!!!!1");
            AlertDialog.Builder builder = new AlertDialog.Builder(QualityManualChange_Main.this);
            builder.setTitle("请选择审核状态");
            final String[] sex = {"不允许", "批准通过"};
            builder.setSingleChoiceItems(sex, 1,buttonOnClick);
            builder.setPositiveButton("确定",buttonOnClick);
            builder.setNegativeButton("取消",buttonOnClick);
            builder.show();

        } else if (item.getItemId() == R.id.menu_item2) {
            AlertDialog.Builder builder = new AlertDialog.Builder(QualityManualChange_Main.this);
            builder.setTitle("确定删除吗");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    postDelete();
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {

                }
            });
            builder.show();



        }
        else
        {
            Intent intent=new Intent(QualityManualChange_Main.this,QualityManual_See.class);
            intent.putExtra("temp",temp);
            intent.putExtra("function",function);
            startActivity(intent);
        }


        return super.onContextItemSelected(item);
    }

    public void postDelete()
    {
        String id=String.valueOf(temp.getId());
        OkHttpClient okHttpClient=new OkHttpClient();
        RequestBody requestBody1=new FormBody.Builder()
                .add("id",id)
                .build();
        Request request = new Request.Builder()
                .url("http://119.23.38.100:8080/cma/"+function+"/delete")//url的地址
                .post(requestBody1)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(QualityManualChange_Main.this, "删除失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                initDangan();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(QualityManualChange_Main.this, "删除成功！", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }
    public void changeState(int State)
    {
        String id=String.valueOf(temp.getId());
        String newstate=String.valueOf(State);
        Log.d("文档状态！！！！！！！！！",newstate);
        OkHttpClient okHttpClient=new OkHttpClient();
        RequestBody requestBody1=new FormBody.Builder()
                .add("id",id)
                .add("state",newstate)
                .build();
        Request request = new Request.Builder()
                .url("http://119.23.38.100:8080/cma/"+function+"/approve")//url的地址
                .post(requestBody1)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(QualityManualChange_Main.this, "修改失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                initDangan();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(QualityManualChange_Main.this, "修改成功！", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }
    //这里是获取json数据
    private void sendRequestWithOkHttp(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String path="http://119.23.38.100:8080/cma/"+function+"/getApprove";
                    Log.d("地址是！！！！",path);
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()

                            .url(path)
                            .build();
                    Response response = client.newCall(request).execute();
                    Log.d("点击获取的","here is json22");
                    String responseData = response.body().string();
                    Log.d("点击获取的","here is json44");
                    Log.d("获得的数据:",responseData);
                    parseJSONWithGSON(responseData);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void parseJSONWithGSON(String jsonData) {

        System.out.print(jsonData);
        Gson gson = new Gson();

        Log.d("点击获取的","here is json33");
        Result<List<QualityManual>> userListResult = gson.fromJson(jsonData,new TypeToken<Result<List<QualityManual>>>(){}.getType());
        //Result<Array<QualityManual>> userListResult = gson.fromJson(jsonData,new TypeToken<Result<List<QualityManual>>>(){}.getType());
        dangAns= userListResult.data;
        showResponse();
    }

    private void showResponse() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 在这里进行UI操作，将结果显示到界面上
                adapter=new QualityManualChangeAdapter(QualityManualChange_Main.this,R.layout.quality_manual_change_one,dangAns);
                listView.setAdapter(adapter);
            }
        });
    }


    private void initDangan(){
        dangAns.clear();
        Log.d("点击获取的","init");
        sendRequestWithOkHttp();



    }

    private void initView(){
        listView=(ListView)findViewById(R.id.list_view);
        searchView=(SearchView)findViewById(R.id.searchview);
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


    @Override
    protected void onResume(){
        super.onResume();
        initDangan();
    }
}



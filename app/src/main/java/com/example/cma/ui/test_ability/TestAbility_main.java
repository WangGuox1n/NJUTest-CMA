package com.example.cma.ui.test_ability;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.cma.R;
import com.example.cma.adapter.test_ability.TestAbilityAdapter;
import com.example.cma.model.staff_management.Result;
import com.example.cma.model.test_ability.TestAbility;
import com.example.cma.utils.DownloadUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TestAbility_main extends AppCompatActivity {

    static public List<TestAbility> dangAns=new ArrayList<TestAbility>();

    private TestAbilityAdapter adapter;
    private ListView listView;
    Toolbar toolbar;
    TestAbility temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_ability_main);
        initView();
        initDangan();
        //在Activity代码中使用Toolbar对象替换ActionBar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //设置Toolbar左边显示一个返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        showResponse();//对于从数据库获取的数据重新开UI线程否则出错。



        //选择listview中的一个元素
        registerForContextMenu(listView);
        //添加按钮
        Button button=(Button)findViewById(R.id.add_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  Intent intent=new Intent(TestAbility_main.this,TestAbility_add.class);
                   startActivity(intent);
                   onResume();
                   showResponse();

            }
        });



    }
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        temp = (TestAbility)listView.getItemAtPosition(((AdapterView.AdapterContextMenuInfo) menuInfo).position);//获取listview的item对象
        getMenuInflater().inflate(R.menu.context_menu_2, menu);
    }
    public boolean onContextItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_item2) {
            final ProgressDialog progressDialog=new ProgressDialog(TestAbility_main.this);
            progressDialog.setTitle("正在下载中");
            progressDialog.setMessage("请稍等....");
            progressDialog.setCancelable(true);
            progressDialog.show();
            String year=String.valueOf(temp.getYear());
            String filename=temp.getFile();
            String path="http://119.23.38.100:8080/cma/TestAbility/getAnnex?year="+year;
            String fileUrl = Environment.getExternalStorageDirectory().getAbsolutePath()+"/CMA/检验检测能力";
            DownloadUtil.getInstance().download(path,fileUrl,filename,new DownloadUtil.OnDownloadListener() {
                @Override
                public void onDownloadSuccess(String path) {
                    progressDialog.dismiss();
                    Toast.makeText(TestAbility_main.this, "下载成功！", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onDownloading(int progress) {

                }

                @Override
                public void onDownloadFailed() {
                    Toast.makeText(TestAbility_main.this, "下载失败！", Toast.LENGTH_SHORT).show();

                }
            });
        }
        else
        {
            Intent intent = new Intent(TestAbility_main.this, TestAbilityOne_main.class);
            String year=String.valueOf(temp.getYear());
            intent.putExtra("year",year);
            startActivity(intent);
        }


        return super.onContextItemSelected(item);
    }

    //这里是获取json数据
    private void sendRequestWithOkHttp(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("点击获取的","here is json11");
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()

                            .url("http://119.23.38.100:8080/cma/TestAbility/getAll")
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
        //Log.d("点击获取得数据","jsonData");
        //dangAns = gson.fromJson(jsonData, new TypeToken<List<TestAbility>>() {}.getType());
        Log.d("点击获取的","here is json33");
        Result<List<TestAbility>> userListResult = gson.fromJson(jsonData,new TypeToken<Result<List<TestAbility>>>(){}.getType());
        //Result<Array<TestAbility>> userListResult = gson.fromJson(jsonData,new TypeToken<Result<List<TestAbility>>>(){}.getType());
        dangAns= userListResult.data;
        showResponse();
    }

    private void showResponse() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 在这里进行UI操作，将结果显示到界面上
                adapter=new TestAbilityAdapter(TestAbility_main.this,R.layout.test_ability_item,dangAns);
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



package com.example.cma.ui.staff_management;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.cma.R;
import com.example.cma.adapter.staff_management.StaffFileAdapter;
import com.example.cma.model.staff_management.StaffFile;
import com.example.cma.utils.HttpUtil;
import com.example.cma.utils.ToastUtil;
import com.example.cma.utils.ViewUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class StaffFile_Main extends AppCompatActivity implements SearchView.OnQueryTextListener, View.OnClickListener, AdapterView.OnItemClickListener {

    //data
    private List<StaffFile> list = new ArrayList<>();

    //View
    private ListView listView;
    private StaffFileAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_file_main);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDataFromServer();
    }

    //初始化所有控件
    public void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        SearchView searchView = findViewById(R.id.searchView);
        FloatingActionButton addButton = findViewById(R.id.add_button);

        listView = findViewById(R.id.list_view);
        ViewUtil.getInstance().setSupportActionBar(this, toolbar);

        //默认不弹出键盘
        searchView.setFocusable(false);
        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(false);
        addButton.setOnClickListener(this);

        //listView可筛选
        listView.setTextFilterEnabled(true);
        listView.setOnItemClickListener(this);
    }

    //监听searchView中文本的改变
    @Override
    public boolean onQueryTextChange(String newText) {
        ListAdapter adapter = listView.getAdapter();
        if (adapter instanceof Filterable) {
            Filter filter = ((Filterable) adapter).getFilter();
            if (newText.isEmpty()) {
                filter.filter(null);
            } else {
                filter.filter(newText);
            }
        }
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter instanceof Filterable) {
            Filter filter = ((Filterable) listAdapter).getFilter();
            if (query.isEmpty()) {
                filter.filter(null);
            } else {
                filter.filter(query);
            }
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_button: {
                Intent intent = new Intent(StaffFile_Main.this, StaffFile_Add.class);
                startActivity(intent);
                break;
            }
            default:
                break;
        }
    }

    //listView 的Item点击事件,跳转到编辑页面
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(StaffFile_Main.this, StaffFile_Info.class);
        StaffFile staff = (StaffFile) listView.getItemAtPosition(position);
        intent.putExtra("StaffFile", staff);
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
    public void getDataFromServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpUtil.sendOkHttpRequest("http://119.23.38.100:8080/cma/StaffFile/getAll", new okhttp3.Callback() {
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseData = response.body().string();
                        Log.d("responseData:", responseData);
                        parseJSONWithGSON(responseData);
                        showResponse();
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(StaffFile_Main.this, "请求数据失败！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        }).start();
    }

    private void parseJSONWithGSON(String jsonData) {
        JSONArray array = new JSONArray();
        try {
            JSONObject object = new JSONObject(jsonData);//最外层的JSONObject对象
            array = object.getJSONArray("data");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (array.length() == 0) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtil.showShort(StaffFile_Main.this, "档案记录为空");
                }
            });
        }
        Gson gson = new Gson();
        List<StaffFile> newList = gson.fromJson(array.toString(), new TypeToken<List<StaffFile>>() {
        }.getType());
        list.clear();
        list.addAll(newList);
    }

    private void showResponse() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Collections.reverse(list);
                adapter = new StaffFileAdapter(StaffFile_Main.this, R.layout.staff_management_listview, list);
                listView.setAdapter(adapter);
            }
        });
    }
}

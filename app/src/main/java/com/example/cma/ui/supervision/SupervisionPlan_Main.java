package com.example.cma.ui.supervision;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.example.cma.R;
import com.example.cma.adapter.supervision.SupervisionPlanAdapter;
import com.example.cma.model.supervision.Supervision;
import com.example.cma.model.supervision.SupervisionPlan;
import com.example.cma.utils.AddressUtil;
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

public class SupervisionPlan_Main extends AppCompatActivity implements SearchView.OnQueryTextListener, View.OnClickListener {
    private static final String TAG = "SupervisionPlan_Main";
    //data
    private List<SupervisionPlan> list = new ArrayList<>();
    private Supervision supervision;

    //View
    private RecyclerView recyclerView;
    private SupervisionPlanAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.supervision_plan_main);
        supervision = (Supervision) getIntent().getSerializableExtra("Supervision");
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (supervision == null) {
            ToastUtil.showShort(SupervisionPlan_Main.this, "数据传送失败");
            return;
        }
        getDataFromServer();
    }

    //初始化所有控件
    public void initView() {
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //增加分割线
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        Toolbar toolbar = findViewById(R.id.toolbar);
        ViewUtil.getInstance().setSupportActionBar(this, toolbar);
        //默认不弹出键盘
        SearchView searchView = findViewById(R.id.searchView);
        searchView.setFocusable(false);
        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(false);

        FloatingActionButton add_button = findViewById(R.id.add_button);
        add_button.setOnClickListener(this);
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
        switch (v.getId()) {
            case R.id.add_button: {
                if (supervision.getSituation() == 2) {
                    ToastUtil.showShort(SupervisionPlan_Main.this, "此监督已执行完毕，无法添加监督计划");
                    return;
                }

                Intent intent = new Intent(SupervisionPlan_Main.this, SupervisionPlan_Add.class);
                intent.putExtra("Supervision", supervision);
                startActivity(intent);
                break;
            }
            default:
                break;
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

    //向后端发送请求，返回所有人员记录
    public void getDataFromServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String address = AddressUtil.getAddress(AddressUtil.SupervisionPlan_getAll)+supervision.getId();
                HttpUtil.sendOkHttpRequest(address, new okhttp3.Callback() {
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseData = response.body().string();
                        Log.d(TAG, responseData);
                        parseJSONWithGSON(responseData);
                        showResponse();
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        ToastUtil.showShort(SupervisionPlan_Main.this, "请求数据失败");
                    }
                });
            }
        }).start();
    }

    private void parseJSONWithGSON(String jsonData) {
        JSONArray array = new JSONArray();
        try {
            JSONObject object = new JSONObject(jsonData);
            array = object.getJSONArray("data");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (array.length() == 0) {
            ToastUtil.showShort(SupervisionPlan_Main.this, "监督计划为空");
        }
        Gson gson = new Gson();
        List<SupervisionPlan> newList = gson.fromJson(array.toString(), new TypeToken<List<SupervisionPlan>>() {
        }.getType());
        list.clear();
        list.addAll(newList);
    }

    private void showResponse() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Collections.reverse(list);
                adapter = new SupervisionPlanAdapter(list, supervision);
                recyclerView.setAdapter(adapter);
            }
        });
    }
}

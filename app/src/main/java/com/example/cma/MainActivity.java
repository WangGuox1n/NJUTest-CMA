package com.example.cma;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.cma.ui.staff_management.StaffFile_Main;
import com.example.cma.ui.staff_management.StaffFile_Modify;
import com.example.cma.ui.staff_management.StaffLeaving_Main;
import com.example.cma.ui.staff_management.StaffManagement_Main;
import com.example.cma.ui.staff_management.Staff_Entry;
import com.example.cma.ui.supervision.Supervision_Main;
import com.example.cma.utils.HttpUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private GridView gridView;
    private List<Map<String, Object>> dataList;
    private SimpleAdapter adapter;
    Toolbar toolbar;
    boolean isRequireCheck = true;
    public static final int PERMISSIONS_GRANTED = 0;
    public static final int PERMISSIONS_DENIED = 1;

    public static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 2;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //在Activity代码中使用Toolbar对象替换ActionBar
        toolbar = (Toolbar) findViewById(R.id.mToolbar);
        setSupportActionBar(toolbar);

        gridView = (GridView) findViewById(R.id.gridview);
        //初始化数据
        initData();
        String[] from={"img","text"};
        int[] to={R.id.img,R.id.text};
        adapter=new SimpleAdapter(this, dataList, R.layout.activity_main_itemview, from, to);

        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                switch (arg2){
                    case 5:{  //监督
                        Intent intent=new Intent(MainActivity.this,Supervision_Main.class);
                        startActivity(intent);
                        break;
                    }
                    case 11:{ //人员管理
                        Intent intent=new Intent(MainActivity.this,Staff_Entry.class);
                        startActivity(intent);
                    }break;
                    default:break;
                }
                /*AlertDialog.Builder builder= new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("提示").setMessage(dataList.get(arg2).get("text").toString()).create().show();*/
            }
        });



        // 权限获取
        if(lacksPermissions()) {
            requestPermissions(new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
        }


    }

    void initData() {
        //图标
        int icno[] = { R.drawable.pyq,R.drawable.pyq,R.drawable.pyq, R.drawable.pyq,
                R.drawable.pyq,R.drawable.pyq, R.drawable.pyq, R.drawable.pyq,
                R.drawable.pyq,R.drawable.pyq, R.drawable.pyq, R.drawable.pyq,
                R.drawable.pyq,R.drawable.pyq, R.drawable.pyq, R.drawable.pyq,
                R.drawable.pyq, R.drawable.pyq, R.drawable.pyq, R.drawable.pyq };
        //图标下的文字
        String name[]={"用户管理","质量体系","管理评审", "内审管理",
                "自查管理","监督管理","期间核查","档案管理",
                "培训管理","样品管理","设备管理","人员管理",
                "授权签字","检测能力","能力验证","标准管理",
                "外部评审","检测机构","客户意见","系统管理"};
        dataList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i <name.length; i++) {
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("img", icno[i]);
            map.put("text",name[i]);
            dataList.add(map);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean lacksPermissions(){
        return checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS && hasAllPermissionsGranted(grantResults)) {
            isRequireCheck = true;
            setResult(PERMISSIONS_GRANTED);
        } else {
            isRequireCheck = false;
            setResult(PERMISSIONS_DENIED);
            finish();
        }
    }

    private boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }
}

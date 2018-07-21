package com.example.cma;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.example.cma.ui.capacity_verification.CapacityVerificationPlan_Main;
import com.example.cma.ui.equipment_management.Equipment_Management_Entry;
import com.example.cma.ui.external_review.ExternalReviewManagement_Main;
import com.example.cma.ui.internal_audit.InternalAuditManagementMain;
import com.example.cma.ui.manage_review.ManageReview_Main;
import com.example.cma.ui.period_check.PeriodCheck_Main;
import com.example.cma.ui.quality_system.QualitySystem_Main;
import com.example.cma.ui.sample_management.SampleManagement_Entey;
import com.example.cma.ui.self_inspection.SelfInspection_Main;
import com.example.cma.ui.staff_management.Staff_Entry;
import com.example.cma.ui.standard_management.StandardManagement_Main;
import com.example.cma.ui.supervision.Supervision_Main;
import com.example.cma.ui.test_ability.TestAbility_main;
import com.example.cma.ui.testing_institution.TestingInstitution_Entry;
import com.example.cma.ui.training_management.TrainingApplication_Entry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        String[] from = {"img", "text"};
        int[] to = {R.id.img, R.id.text};
        adapter = new SimpleAdapter(this, dataList, R.layout.activity_main_itemview, from, to);

        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                switch (arg2) {
                    case 0: { //质量体系
                        Intent intent = new Intent(MainActivity.this, QualitySystem_Main.class);
                        startActivity(intent);
                        break;
                    }
                    case 1: { //管理评审
                        Intent intent = new Intent(MainActivity.this, ManageReview_Main.class);
                        startActivity(intent);
                        break;
                    }
                    case 2: {//内审管理
                        Intent intent = new Intent(MainActivity.this, InternalAuditManagementMain.class);
                        startActivity(intent);
                        break;
                    }
                    case 3: {//自查管理
                        Intent intent = new Intent(MainActivity.this, SelfInspection_Main.class);
                        startActivity(intent);
                        break;
                    }
                    case 4: {  //监督
                        Intent intent = new Intent(MainActivity.this, Supervision_Main.class);
                        startActivity(intent);
                        break;
                    }
                    case 5: { //期间核查
                        Intent intent = new Intent(MainActivity.this, PeriodCheck_Main.class);
                        startActivity(intent);
                        break;
                    }
                    case 6: { //培训管理
                        Intent intent = new Intent(MainActivity.this, TrainingApplication_Entry.class);
                        startActivity(intent);
                    }
                    break;
                    case 7: { //样品管理
                        Intent intent = new Intent(MainActivity.this, SampleManagement_Entey.class);
                        startActivity(intent);
                        break;
                    }
                    case 8: { //设备管理
                        Intent intent = new Intent(MainActivity.this, Equipment_Management_Entry.class);
                        startActivity(intent);
                        break;
                    }
                    case 9: { //人员管理
                        Intent intent = new Intent(MainActivity.this, Staff_Entry.class);
                        startActivity(intent);
                        break;
                    }
                    case 10: { //检测能力
                        Intent intent = new Intent(MainActivity.this, TestAbility_main.class);
                        startActivity(intent);
                        break;
                    }
                    case 11: { //能力验证
                        Intent intent=new Intent(MainActivity.this, CapacityVerificationPlan_Main.class);
                        startActivity(intent);
                        break;
                    }
                    case 12: { //标准管理
                        Intent intent=new Intent(MainActivity.this, StandardManagement_Main.class);
                        startActivity(intent);
                        break;
                    }
                    case 13: { //外部评审
                        Intent intent = new Intent(MainActivity.this, ExternalReviewManagement_Main.class);
                        startActivity(intent);
                        break;
                    }
                    case 14: { //检测机构
                        Intent intent = new Intent(MainActivity.this, TestingInstitution_Entry.class);
                        startActivity(intent);
                        break;
                    }
                    default:
                        break;
                }
            }
        });

        // 权限获取
        if (lacksPermissions()) {
            requestPermissions(new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
        }
    }

    void initData() {
        //图标
        int icon[] = {R.mipmap.zltx, R.mipmap.glps, R.mipmap.ns, R.mipmap.zcgl,
                R.mipmap.jdgl, R.mipmap.qjhc, R.mipmap.pxgl, R.mipmap.ypgl,
                R.mipmap.sbgl, R.mipmap.rygl, R.mipmap.jcnl, R.mipmap.nlyz,
                R.mipmap.bzgl, R.mipmap.wbps, R.mipmap.jcjg};
        //图标下的文字
        String name[] = {"质量体系", "管理评审", "内审管理",
                "自查管理", "监督管理", "期间核查",
                "培训管理", "样品管理", "设备管理",
                "人员管理", "检测能力", "能力验证",
                "标准管理", "外部评审", "检测机构"};
        dataList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < name.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("img", icon[i]);
            map.put("text", name[i]);
            dataList.add(map);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean lacksPermissions() {
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

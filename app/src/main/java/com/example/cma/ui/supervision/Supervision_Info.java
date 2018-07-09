package com.example.cma.ui.supervision;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cma.R;
import com.example.cma.model.supervision.Supervision;
import com.example.cma.utils.AddressUtil;
import com.example.cma.utils.HttpUtil;
import com.example.cma.utils.ToastUtil;
import com.example.cma.utils.ViewUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Supervision_Info extends AppCompatActivity implements View.OnClickListener{

    private Supervision supervision;
    private TextView situation_text;
    private TextView author_text;
    private TextView createDate_text;
    private TextView approver_text;
    private TextView approveDate_text;
    private TextView remark_text;
    private TextView plan_button;
    private Button deleteButton;
    private Button editButton;
    private Button approveButton;
    private Toolbar toolbar;
    LinearLayout approve_layout;
    LinearLayout approve_button_layout;

    private EditText dialog_approver_text;
    private EditText dialog_remark_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.supervision_info);
        Intent intent = getIntent();
        supervision = (Supervision)intent.getSerializableExtra("Supervision");
        if(supervision == null){
            ToastUtil.showShort(Supervision_Info.this,"数据传送失败！");
            return;
        }
        initView();
        setText();
    }

    public void initView(){
        situation_text = (TextView)findViewById(R.id.situation_text);
        author_text = (TextView)findViewById(R.id.author_text);
        createDate_text = (TextView)findViewById(R.id.createDate_text);
        approver_text = (TextView)findViewById(R.id.approver_text);
        approveDate_text = (TextView)findViewById(R.id.approveDate_text);
        remark_text = (TextView)findViewById(R.id.remark_text);
        plan_button = (TextView)findViewById(R.id.plan_text);
        plan_button.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        editButton = (Button)findViewById(R.id.edit_button);
        deleteButton = (Button)findViewById(R.id.delete_button);
        approveButton = (Button)findViewById(R.id.approve_button);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        approve_layout = (LinearLayout)findViewById(R.id.approve_layout);
        approve_button_layout = (LinearLayout)findViewById(R.id.approve_button_layout);
        setSupportActionBar(toolbar);

        //设置Toolbar左边显示一个返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        plan_button.setOnClickListener(this);
        editButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
        approveButton.setOnClickListener(this);

        if(supervision.getSituation()==0){
            approve_layout.setVisibility(View.GONE);
        }else if(supervision.getSituation()==1){
            approveButton.setText("执行确认");
        }else
            approve_button_layout.setVisibility(View.GONE);
    }

    public void setText(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                situation_text.setText(supervision.SituationToString());
                author_text.setText(supervision.getAuthor());
                createDate_text.setText(supervision.getCreateDate());
                approver_text.setText(supervision.getApprover());
                approveDate_text.setText(supervision.getApproveDate());
                remark_text.setText(supervision.getRemark());
            }
        });
    }

    public void refreshLayout(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(supervision.getSituation()==0){
                    approve_layout.setVisibility(View.GONE);
                }else if(supervision.getSituation()==1){
                    approveButton.setText("执行确认");
                    approve_layout.setVisibility(View.VISIBLE);
                    approver_text.setText(supervision.getApprover());
                    approveDate_text.setText(supervision.getApproveDate());
                    situation_text.setText(supervision.SituationToString());
                }else {
                    approve_button_layout.setVisibility(View.GONE);
                    situation_text.setText(supervision.SituationToString());
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edit_button:
                showModifyDialog();
                break;
            case R.id.delete_button:  //点击删除，弹出弹窗
                onDeleteConfirm();
                break;
            case R.id.approve_button:
                if(supervision.getSituation()==0)
                    showApproveDialog();
                else if(supervision.getSituation()==1)
                    showExecuteDialog();
                break;
            case R.id.plan_text:
                Intent intent=new Intent(Supervision_Info.this,SupervisionPlan_Main.class);
                intent.putExtra("Supervision", supervision);
                startActivity(intent);
            default:break;
        }
    }

    //是否确认删除的对话框
    public void onDeleteConfirm(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(Supervision_Info.this);
        dialog.setMessage("确定删除？");
        dialog.setCancelable(false);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onDelete();
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Do nothing
            }
        });
        dialog.show();
    }

    public void onDelete(){
        String address = AddressUtil.Supervision_deleteOne();
        RequestBody requestBody=new FormBody.Builder().add("id",""+supervision.getId()).build();
        HttpUtil.sendOkHttpWithRequestBody(address,requestBody,new okhttp3.Callback(){
            @Override
            public void onResponse(Call call, Response response)throws IOException {
                String responseData = response.body().string();
                Log.d("Supervision onDelete:",responseData);
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
                    ToastUtil.showShort(Supervision_Info.this, "删除成功！");
                    finish();
                }
            }
            @Override
            public void onFailure(Call call,IOException e){
                ToastUtil.showShort(Supervision_Info.this, "删除失败！");
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

    public void showApproveDialog(){
        final AlertDialog.Builder editDialog = new AlertDialog.Builder(Supervision_Info.this);
        final View dialogView = LayoutInflater.from(Supervision_Info.this)
                .inflate(R.layout.supervision_approver,null);
        editDialog.setTitle("监督·批准");
        editDialog.setView(dialogView);
        editDialog.setCancelable(false);
        dialog_approver_text = (EditText)  dialogView.findViewById(R.id.dialog_approver_text);

        editDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(dialog_approver_text.getText().toString().isEmpty()){
                    ToastUtil.showShort(Supervision_Info.this,"请填写批准人姓名");
                    return;
                }
                postApprove();
            }
        });
        editDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        editDialog.create().show();
    }

    public void postApprove(){
        String address = AddressUtil.Supervision_approveOne();
        RequestBody requestBody = new FormBody.Builder()
                .add("id",""+supervision.getId())
                .add("approver",dialog_approver_text.getText().toString())
                .build();

        HttpUtil.sendOkHttpWithRequestBody(address,requestBody,new okhttp3.Callback(){
            @Override
            public void onResponse(Call call, Response response)throws IOException {
                String responseData = response.body().string();
                Log.d("Supervision_approveOne:",responseData);
                int code = 0;
                String msg = "";
                try {
                    JSONObject object = new JSONObject(responseData);
                    code = object.getInt("code");
                    msg = object.getString("msg");
                }catch (JSONException e){
                    ToastUtil.showShort(Supervision_Info.this,"网络连接错误，提交失败！");
                    e.printStackTrace();
                }
                if(code == 200 && msg.equals("成功")) {
                    ToastUtil.showShort(Supervision_Info.this,"批准成功！");
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String dateString = simpleDateFormat.format(new Date());
                    supervision.setApproveDate(dateString);
                    supervision.setApprover(dialog_approver_text.getText().toString());
                    supervision.setSituation(1);
                    refreshLayout();
                }
            }
            @Override
            public void onFailure(Call call,IOException e){
                ToastUtil.showShort(Supervision_Info.this,"提交失败！");
            }
        });
    }

    public void showModifyDialog(){
        final AlertDialog.Builder editDialog = new AlertDialog.Builder(Supervision_Info.this);
        dialog_remark_text = new EditText(Supervision_Info.this);
        dialog_remark_text.setMaxLines(10);
        dialog_remark_text.setCursorVisible(false);
        ViewUtil.ShowCursor(dialog_remark_text);
        dialog_remark_text.setText(supervision.getRemark());

        editDialog.setTitle("监督 · 修改备注");
        editDialog.setView(dialog_remark_text);
        editDialog.setCancelable(false);

        editDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(dialog_remark_text.getText().toString().isEmpty()){
                    ToastUtil.showShort(Supervision_Info.this,"请填写完整！");
                    return;
                }
                postModify();
            }
        });
        editDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        editDialog.create().show();
    }

    public void postModify(){
        String address = AddressUtil.Supervision_modifyOne();
        RequestBody requestBody = new FormBody.Builder()
                .add("id",""+supervision.getId())
                .add("remark",dialog_remark_text.getText().toString())
                .build();

        HttpUtil.sendOkHttpWithRequestBody(address,requestBody,new okhttp3.Callback(){
            @Override
            public void onResponse(Call call, Response response)throws IOException {
                String responseData = response.body().string();
                Log.d("Supervision_modifyOne:",responseData);
                int code = 0;
                String msg = "";
                try {
                    JSONObject object = new JSONObject(responseData);
                    code = object.getInt("code");
                    msg = object.getString("msg");
                }catch (JSONException e){
                    ToastUtil.showShort(Supervision_Info.this,"网络连接错误，提交失败！");
                    e.printStackTrace();
                }
                if(code == 200 && msg.equals("成功")) {
                    ToastUtil.showShort(Supervision_Info.this,"修改成功！");
                    supervision.setRemark(dialog_remark_text.getText().toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            remark_text.setText(dialog_remark_text.getText().toString());
                        }
                    });
                }
            }
            @Override
            public void onFailure(Call call,IOException e){
                ToastUtil.showShort(Supervision_Info.this,"修改失败！");
            }
        });
    }

    public void showExecuteDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(Supervision_Info.this);
        dialog.setMessage("确定此监督执行完毕？");
        dialog.setCancelable(false);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                postExecute();
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Do nothing
            }
        });
        dialog.show();
    }

    public void postExecute(){
        final String address = AddressUtil.Supervision_executeOne();
        RequestBody requestBody = new FormBody.Builder()
                .add("id",""+supervision.getId())
                .build();

        HttpUtil.sendOkHttpWithRequestBody(address,requestBody,new okhttp3.Callback(){
            @Override
            public void onResponse(Call call, Response response)throws IOException {
                String responseData = response.body().string();
                Log.d("postExecute:",responseData);
                int code = 0;
                String msg = "";
                try {
                    JSONObject object = new JSONObject(responseData);
                    code = object.getInt("code");
                    msg = object.getString("msg");
                    if(code == 200 && msg.equals("成功")) {
                        ToastUtil.showShort(Supervision_Info.this,"执行确认成功！");
                        supervision.setSituation(2);
                        refreshLayout();
                    }
                }catch (JSONException e){
                    ToastUtil.showShort(Supervision_Info.this,"网络连接错误，提交失败！");
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call call,IOException e){
                ToastUtil.showShort(Supervision_Info.this,"操作失败！");
            }
        });
    }
}

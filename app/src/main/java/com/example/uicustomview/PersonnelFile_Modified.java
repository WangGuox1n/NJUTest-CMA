package com.example.uicustomview;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PersonnelFile_Modified extends AppCompatActivity implements View.OnClickListener{

    private PersonnelFile personnelFile;
    private EditText modified_name;
    private EditText modified_department;
    private EditText modified_position;
    private EditText modified_id;
    private EditText modified_location;
    private LinearLayout linearLayout;
    private Button saveButton;
    private Button backButton;
    private Button deleteButton;
    private PopupWindow mPopWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personnel_file__modified);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        findView();

        Intent intent = getIntent();
        personnelFile = (PersonnelFile)intent.getSerializableExtra("PersonnelFile");

        setText();
    }

    private void findView(){
        modified_name = (EditText)findViewById(R.id.modified_name);
        modified_name.setOnClickListener(this);
        modified_department = (EditText)findViewById(R.id.modified_department);
        modified_department.setOnClickListener(this);
        modified_position = (EditText)findViewById(R.id.modified_position);
        modified_position.setOnClickListener(this);
        modified_id = (EditText)findViewById(R.id.modified_id);
        modified_id.setOnClickListener(this);
        modified_location = (EditText)findViewById(R.id.modified_location);
        modified_location.setOnClickListener(this);

        linearLayout = (LinearLayout)findViewById(R.id.titlelayout);
        backButton = (Button)findViewById(R.id.title_back);
        saveButton = (Button)findViewById(R.id.title_save);
        deleteButton = (Button)findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(this);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PersonnelFile_Modified.this, "保存成功！", Toast.LENGTH_SHORT).show();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEditTextChange()){
                    //TODO 内容已经更改，需要传到数据库
                    AlertDialog.Builder dialog = new AlertDialog.Builder(PersonnelFile_Modified.this);
                    dialog.setMessage("修改尚未保存，确定返回吗？");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
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
                else
                    finish();
            }
        });
    }

    private boolean isEditTextChange(){
        if(!personnelFile.getName().equals(modified_name.getText().toString()))
            return true;
        if(!personnelFile.getDepartment().equals(modified_department.getText().toString()))
            return true;
        if(!personnelFile.getPosition().equals(modified_position.getText().toString()))
            return true;
        if(!personnelFile.getId().equals(modified_id.getText().toString()))
            return true;
        if(!personnelFile.getLocation().equals(modified_location.getText().toString()))
            return true;
        return false;
    }

    private void setText(){
        modified_name.setText(personnelFile.getName());
        modified_department.setText(personnelFile.getDepartment());
        modified_position.setText(personnelFile.getPosition());
        modified_id.setText(personnelFile.getId());
        modified_location.setText(personnelFile.getLocation());
    }

    @Override
    public void onClick(View v) {
        Log.d("PersonnelFile_Modified","PersonnelFile");
        switch (v.getId()){
            case R.id.modified_name:
                Toast.makeText(PersonnelFile_Modified.this, "点击了！", Toast.LENGTH_SHORT).show();
                modified_name.setCursorVisible(true);break;
            case R.id.modified_department:
                modified_department.setCursorVisible(true);break;
            case R.id.modified_position:
                modified_position.setCursorVisible(true);break;
            case R.id.modified_id:
                modified_id.setCursorVisible(true);break;
            case R.id.modified_location:
                modified_location.setCursorVisible(true);break;
            case R.id.title_save:
                Log.d("PersonnelFile_Modified","PersonnelFile_Modified");
                //Toast.makeText(PersonnelFile_Modified.this, "保存成功！", Toast.LENGTH_SHORT).show();
                postData();
                break;
            case R.id.title_back:
                finish();
                break;
            case R.id.delete_button:  //点击删除，弹出弹窗
                showPopupWindow();break;
            case R.id.title_cancel:  //取消删除，dismiss 弹窗
                mPopWindow.dismiss();
            case R.id.delete_comfirm:
                //TODO 从数据库中删除此记录
            default:break;
        }
    }

    // 传送数据给后端
    public void postData(){}

    private void showPopupWindow() {
        //设置contentView
        View contentView = LayoutInflater.from(PersonnelFile_Modified.this).inflate(R.layout.popupwindow_delete, null);
        mPopWindow = new PopupWindow(contentView,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        mPopWindow.setContentView(contentView);
        //设置各个控件的点击响应
        TextView tv1 = (TextView)contentView.findViewById(R.id.title_cancel);
        Button bt2 = (Button)contentView.findViewById(R.id.delete_comfirm);
        tv1.setOnClickListener(this);
        bt2.setOnClickListener(this);

        //让背景变暗
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f; //0.0-1.0
        getWindow().setAttributes(lp);
        mPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            //在弹窗消失后恢复背景
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });

        //显示PopupWindow
        View rootview = LayoutInflater.from(PersonnelFile_Modified.this).inflate(R.layout.activity_personnel_file__modified, null);
        mPopWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
    }
}

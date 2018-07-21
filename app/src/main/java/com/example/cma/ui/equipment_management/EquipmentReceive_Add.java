package com.example.cma.ui.equipment_management;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.cma.R;
import com.example.cma.ui.staff_management.StaffFile_Add;
import com.example.cma.utils.AddressUtil;
import com.example.cma.utils.FileUtil;
import com.example.cma.utils.HttpUtil;
import com.example.cma.utils.ToastUtil;
import com.example.cma.utils.ViewUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EquipmentReceive_Add extends AppCompatActivity implements View.OnClickListener {

    private EditText name_text;
    private EditText model_text;
    private EditText manufacturer_text;
    private EditText receiveSituation_text;
    private EditText recipient_text;
    private TextView receiveDate_text;
    private EditText equipmentSituation_text;
    private EditText acceptance_text;
    private EditText acceptancePerson_text;
    private TextView acceptanceDate_text;
    private Button submit_button;

    private List<AttachmentLayout> attachmentList = new ArrayList<>(5);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.equipment_receive_add);
        initView();
    }

    public void initView() {
        name_text = findViewById(R.id.name_text);
        model_text = findViewById(R.id.model_text);
        manufacturer_text = findViewById(R.id.manufacturer_text);
        receiveSituation_text = findViewById(R.id.receiveSituation_text);
        recipient_text = findViewById(R.id.recipient_text);
        receiveDate_text = findViewById(R.id.receiveDate_text);
        equipmentSituation_text = findViewById(R.id.equipmentSituation_text);
        acceptance_text = findViewById(R.id.acceptance_text);
        acceptancePerson_text = findViewById(R.id.acceptancePerson_text);
        acceptanceDate_text = findViewById(R.id.acceptanceDate_text);

        Toolbar toolbar = findViewById(R.id.toolbar);
        ViewUtil.getInstance().setSupportActionBar(this, toolbar);
        submit_button = findViewById(R.id.submit_button);
        submit_button.setOnClickListener(this);
        findViewById(R.id.attachment_text).setOnClickListener(this);

        receiveDate_text.setOnClickListener(this);
        acceptanceDate_text.setOnClickListener(this);

        for (int i = 0; i < 5; i++) {
            attachmentList.add(new AttachmentLayout());
            attachmentList.get(i).file = null;
        }
        attachmentList.get(0).layout = findViewById(R.id.attachment_layout1);
        attachmentList.get(1).layout = findViewById(R.id.attachment_layout2);
        attachmentList.get(2).layout = findViewById(R.id.attachment_layout3);
        attachmentList.get(3).layout = findViewById(R.id.attachment_layout4);
        attachmentList.get(4).layout = findViewById(R.id.attachment_layout5);
        attachmentList.get(0).name = findViewById(R.id.attachment_name1);
        attachmentList.get(1).name = findViewById(R.id.attachment_name2);
        attachmentList.get(2).name = findViewById(R.id.attachment_name3);
        attachmentList.get(3).name = findViewById(R.id.attachment_name4);
        attachmentList.get(4).name = findViewById(R.id.attachment_name5);
        attachmentList.get(0).size = findViewById(R.id.attachment_size1);
        attachmentList.get(1).size = findViewById(R.id.attachment_size2);
        attachmentList.get(2).size = findViewById(R.id.attachment_size3);
        attachmentList.get(3).size = findViewById(R.id.attachment_size4);
        attachmentList.get(4).size = findViewById(R.id.attachment_size5);
        for (AttachmentLayout attachment : attachmentList)
            attachment.layout.setVisibility(View.GONE);

        findViewById(R.id.attachment_delete1).setOnClickListener(this);
        findViewById(R.id.attachment_delete2).setOnClickListener(this);
        findViewById(R.id.attachment_delete3).setOnClickListener(this);
        findViewById(R.id.attachment_delete4).setOnClickListener(this);
        findViewById(R.id.attachment_delete5).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_button:
                submit_button.setEnabled(false);
                onSave();
                break;
            case R.id.receiveDate_text:
                ViewUtil.getInstance().selectDate(EquipmentReceive_Add.this, receiveDate_text);
                break;
            case R.id.acceptanceDate_text:
                ViewUtil.getInstance().selectDate(EquipmentReceive_Add.this, acceptanceDate_text);
                break;
            case R.id.attachment_text:
                if (isFullAttachment()) {
                    ToastUtil.showShort(this, "无法添加超过5个附件");
                    break;
                }
                FileUtil.getInstance().localStorage(this);
                break;
            case R.id.attachment_delete1:
                removeAttachment(0);
                break;
            case R.id.attachment_delete2:
                removeAttachment(1);
                break;
            case R.id.attachment_delete3:
                removeAttachment(2);
                break;
            case R.id.attachment_delete4:
                removeAttachment(3);
                break;
            case R.id.attachment_delete5:
                removeAttachment(4);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FileUtil.GET_FILE:
                if (resultCode == RESULT_OK) {
                    addAttachment(data);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackConfirm(true);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        onBackConfirm(false);
    }

    public void onSave() {
        if (name_text.getText().toString().isEmpty() ||
                model_text.getText().toString().isEmpty() ||
                manufacturer_text.getText().toString().isEmpty() ||
                receiveSituation_text.getText().toString().isEmpty() ||
                recipient_text.getText().toString().isEmpty() ||
                receiveDate_text.getText().toString().isEmpty() ||
                equipmentSituation_text.getText().toString().isEmpty() ||
                acceptance_text.getText().toString().isEmpty() ||
                acceptancePerson_text.getText().toString().isEmpty() ||
                acceptanceDate_text.getText().toString().isEmpty()) {
            ToastUtil.showShort(EquipmentReceive_Add.this, "请填写完整");
            return;
        }
        postSave();
    }

    public void postSave() {
        String address = AddressUtil.getAddress(AddressUtil.EquipmentReceive_addOne);

        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        requestBody.addFormDataPart("name", name_text.getText().toString())
                .addFormDataPart("model", model_text.getText().toString())
                .addFormDataPart("manufacturer", manufacturer_text.getText().toString())
                .addFormDataPart("receiveSituation", receiveSituation_text.getText().toString())
                .addFormDataPart("recipient", recipient_text.getText().toString())
                .addFormDataPart("receiveDate", receiveDate_text.getText().toString())
                .addFormDataPart("equipmentSituation", equipmentSituation_text.getText().toString())
                .addFormDataPart("acceptance", acceptance_text.getText().toString())
                .addFormDataPart("acceptancePerson", acceptancePerson_text.getText().toString())
                .addFormDataPart("acceptanceDate", acceptanceDate_text.getText().toString());
        for (AttachmentLayout attachment : attachmentList) {
            if (attachment.file != null) {
                RequestBody fileBody = RequestBody.create(MediaType.parse("*/*"), attachment.file);
                requestBody.addFormDataPart("attachment", attachment.file.getName(), fileBody);
            }
        }
        final ProgressDialog loadingDialog = new ProgressDialog(EquipmentReceive_Add.this);
        loadingDialog.setTitle("正在上传中");
        loadingDialog.setMessage("请稍等......");
        loadingDialog.setCancelable(true);
        /*
        * 不添加附件的时候，不需要显示正在上传的dialog
        * */
        if(attachmentList.size()>0)
            loadingDialog.show();
        HttpUtil.sendOkHttpWithMultipartBody(address, requestBody.build(), new okhttp3.Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                Log.d("EquipmentReceive_Add:", responseData);
                int code = 0;
                String msg = "";
                try {
                    JSONObject object = new JSONObject(responseData);
                    code = object.getInt("code");
                    msg = object.getString("msg");
                } catch (JSONException e) {
                    if(attachmentList.size()>0)
                        loadingDialog.dismiss();
                    e.printStackTrace();
                }
                if (code == 200 && msg.equals("成功")) {
                    if(attachmentList.size()>0)
                        loadingDialog.dismiss();
                    ToastUtil.showShort(EquipmentReceive_Add.this, "添加成功");
                    finish();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                if(attachmentList.size()>0)
                    loadingDialog.dismiss();
                submit_button.setEnabled(true);
                ToastUtil.showShort(EquipmentReceive_Add.this, "添加失败");
            }
        });
    }

    public void onBackConfirm(boolean flag) {
        if (!name_text.getText().toString().isEmpty() ||
                !model_text.getText().toString().isEmpty() ||
                !manufacturer_text.getText().toString().isEmpty() ||
                !receiveSituation_text.getText().toString().isEmpty() ||
                !recipient_text.getText().toString().isEmpty() ||
                !receiveDate_text.getText().toString().isEmpty() ||
                !equipmentSituation_text.getText().toString().isEmpty() ||
                !acceptance_text.getText().toString().isEmpty() ||
                !acceptancePerson_text.getText().toString().isEmpty() ||
                !acceptanceDate_text.getText().toString().isEmpty()) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(EquipmentReceive_Add.this);
            dialog.setTitle("内容尚未保存");
            dialog.setMessage("是否退出？");
            dialog.setCancelable(true);
            dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            dialog.show();
        } else if (flag)
            finish();
        else
            super.onBackPressed();
    }

    /*
    * 添加一个附件，并使附件的layout显示出来
    * */
    public void addAttachment(Intent data) {
        Uri uri = data.getData();
        final File file = FileUtil.getInstance().getFile(this, uri);
        if (!file.exists())
            return;
        long fileS = FileUtil.getFileSizes(file);
        if (fileS > 10485760) { //附件不能大于10MB
            ToastUtil.showShort(this, "无法上传超过10MB的附件");
            return;
        }


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String formatFileSize = FileUtil.FormatFileSize(file);
                for (AttachmentLayout attachment : attachmentList) {
                    if (attachment.file == null) {
                        attachment.file = file;
                        attachment.name.setText(file.getName());
                        attachment.size.setText(formatFileSize);
                        attachment.layout.setVisibility(View.VISIBLE);
                        break;
                    }
                }
            }
        });
    }

    /*
    * 删除附件，并使该附件的layout消失
    * */
    public void removeAttachment(final int index) {
        attachmentList.get(index).file = null;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                attachmentList.get(index).layout.setVisibility(View.GONE);
            }
        });
    }

    /*
    * 判断附件数是否等于5
    * */
    public Boolean isFullAttachment() {
        for (AttachmentLayout attachment : attachmentList)
            if (attachment.file == null)
                return false;
        return true;
    }
}

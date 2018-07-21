package com.example.cma.ui.equipment_management;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cma.R;
import com.example.cma.model.equipment_management.Attachment;
import com.example.cma.model.equipment_management.EquipmentReceive;
import com.example.cma.utils.AddressUtil;
import com.example.cma.utils.FileUtil;
import com.example.cma.utils.HttpUtil;
import com.example.cma.utils.ToastUtil;
import com.example.cma.utils.ViewUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EquipmentReceive_Modify extends AppCompatActivity implements View.OnClickListener {

    private EquipmentReceive equipmentReceive;
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

    private List<Attachment> attachments = new ArrayList<>();
    private List<AttachmentLayout> attachmentLayouts = new ArrayList<>(5);
    private List<String> deleteAttachmentId = new ArrayList<>();

    private boolean saveSuccess = false;
    private int needToAdd = 5;     //计算需要添加的文件
    private int attachmentAddSuccess = 0; //添加成功的文件数
    private int attachmentDelSuccess = 0; //删除成功的文件数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.equipment_receive_modify);
        initView();
        Intent intent = getIntent();
        equipmentReceive = (EquipmentReceive) intent.getSerializableExtra("EquipmentReceive");
        setText();
        getAttachments();
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

        findViewById(R.id.submit_button).setOnClickListener(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        ViewUtil.getInstance().setSupportActionBar(this, toolbar);

        LinearLayout receiveDate_layout = findViewById(R.id.receiveDate_layout);
        receiveDate_text.setOnClickListener(this);
        receiveDate_layout.setOnClickListener(this);

        LinearLayout acceptanceDate_layout = findViewById(R.id.acceptanceDate_layout);
        acceptanceDate_text.setOnClickListener(this);
        acceptanceDate_layout.setOnClickListener(this);

        (findViewById(R.id.attachment_text)).setOnClickListener(this);

        for (int i = 0; i < 5; i++) {
            attachmentLayouts.add(new AttachmentLayout());
            attachmentLayouts.get(i).file = null;
        }

        attachmentLayouts.get(0).layout = findViewById(R.id.attachment_layout1);
        attachmentLayouts.get(1).layout = findViewById(R.id.attachment_layout2);
        attachmentLayouts.get(2).layout = findViewById(R.id.attachment_layout3);
        attachmentLayouts.get(3).layout = findViewById(R.id.attachment_layout4);
        attachmentLayouts.get(4).layout = findViewById(R.id.attachment_layout5);
        attachmentLayouts.get(0).name = findViewById(R.id.attachment_name1);
        attachmentLayouts.get(1).name = findViewById(R.id.attachment_name2);
        attachmentLayouts.get(2).name = findViewById(R.id.attachment_name3);
        attachmentLayouts.get(3).name = findViewById(R.id.attachment_name4);
        attachmentLayouts.get(4).name = findViewById(R.id.attachment_name5);
        attachmentLayouts.get(0).size = findViewById(R.id.attachment_size1);
        attachmentLayouts.get(1).size = findViewById(R.id.attachment_size2);
        attachmentLayouts.get(2).size = findViewById(R.id.attachment_size3);
        attachmentLayouts.get(3).size = findViewById(R.id.attachment_size4);
        attachmentLayouts.get(4).size = findViewById(R.id.attachment_size5);
        for (AttachmentLayout attachment : attachmentLayouts) {
            attachment.layout.setVisibility(View.GONE);
            attachment.size.setVisibility(View.INVISIBLE);
        }

        findViewById(R.id.attachment_delete1).setOnClickListener(this);
        findViewById(R.id.attachment_delete2).setOnClickListener(this);
        findViewById(R.id.attachment_delete3).setOnClickListener(this);
        findViewById(R.id.attachment_delete4).setOnClickListener(this);
        findViewById(R.id.attachment_delete5).setOnClickListener(this);
    }

    public void setText() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                name_text.setText(equipmentReceive.getName());
                model_text.setText(equipmentReceive.getModel());
                manufacturer_text.setText(equipmentReceive.getManufacturer());
                receiveSituation_text.setText(equipmentReceive.getReceiveSituation());
                recipient_text.setText(equipmentReceive.getRecipient());
                receiveDate_text.setText(equipmentReceive.getReceiveDate());
                equipmentSituation_text.setText(equipmentReceive.getEquipmentSituation());
                acceptance_text.setText(equipmentReceive.getAcceptance());
                acceptancePerson_text.setText(equipmentReceive.getAcceptancePerson());
                acceptanceDate_text.setText(equipmentReceive.getAcceptanceDate());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_button:
                onSave();
                break;
            case R.id.receiveDate_layout:
            case R.id.receiveDate_text:
                ViewUtil.getInstance().selectDate(EquipmentReceive_Modify.this, receiveDate_text);
                break;
            case R.id.acceptanceDate_layout:
            case R.id.acceptanceDate_text:
                ViewUtil.getInstance().selectDate(EquipmentReceive_Modify.this, acceptanceDate_text);
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
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
            ToastUtil.showShort(EquipmentReceive_Modify.this, "请填写完整");
            return;
        }
        postSave();
        postAttachmentAdd();
        postAttachmentDel();
    }

    public void checkFinish() {
        if (saveSuccess && attachmentAddSuccess == needToAdd && attachmentDelSuccess == deleteAttachmentId.size()) {
            ToastUtil.showShort(EquipmentReceive_Modify.this, "修改成功");
            finish();
        }
    }

    public void postSave() {
        String address = AddressUtil.getAddress(AddressUtil.EquipmentReceive_modifyOne);
        RequestBody requestBody = new FormBody.Builder()
                .add("id", equipmentReceive.getId() + "")
                .add("name", name_text.getText().toString())
                .add("model", model_text.getText().toString())
                .add("manufacturer", manufacturer_text.getText().toString())
                .add("receiveSituation", receiveSituation_text.getText().toString())
                .add("recipient", recipient_text.getText().toString())
                .add("receiveDate", receiveDate_text.getText().toString())
                .add("equipmentSituation", equipmentSituation_text.getText().toString())
                .add("acceptance", acceptance_text.getText().toString())
                .add("acceptancePerson", acceptancePerson_text.getText().toString())
                .add("acceptanceDate", acceptanceDate_text.getText().toString())
                .build();

        HttpUtil.sendOkHttpWithRequestBody(address, requestBody, new okhttp3.Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                Log.d("EquipmentReceiveModify:", responseData);
                int code = 0;
                String msg = "";
                try {
                    JSONObject object = new JSONObject(responseData);
                    code = object.getInt("code");
                    msg = object.getString("msg");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (code == 200 && msg.equals("成功")) {
                    saveSuccess = true;
                    checkFinish();
                }
                if (code == 210) {
                    ToastUtil.showShort(EquipmentReceive_Modify.this, "修改信息失败");
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showShort(EquipmentReceive_Modify.this, "修改信息失败");
            }
        });
    }

    public void postAttachmentAdd() {
        for (AttachmentLayout attachment : attachmentLayouts) {
            if (attachment.file == null) {
                needToAdd--; //需要添加的文件减1
                continue;
            }

            MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
            RequestBody fileBody = RequestBody.create(MediaType.parse("*/*"), attachment.file);
            requestBody.addFormDataPart("id", equipmentReceive.getId() + "")
                    .addFormDataPart("attachment", attachment.file.getName(), fileBody);

            String address = "http://119.23.38.100:8080/cma/EquipmentReceive/addAttachment";
            HttpUtil.sendOkHttpWithMultipartBody(address, requestBody.build(), new okhttp3.Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseData = response.body().string();
                    Log.d("AttachmentAdd:", responseData);
                    int code = 0;
                    String msg = "";
                    try {
                        JSONObject object = new JSONObject(responseData);
                        code = object.getInt("code");
                        msg = object.getString("msg");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (code == 200 && msg.equals("成功")) {
                        attachmentAddSuccess++;
                        checkFinish();
                    }
                    if (code == 210){
                        ToastUtil.showShort(EquipmentReceive_Modify.this,"无法添加重复文件");
                    }
                }

                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showShort(EquipmentReceive_Modify.this, "附件上传失败");
                        }
                    });
                }
            });
        }
    }

    public void postAttachmentDel() {
        for (String attachmentId : deleteAttachmentId) {
            String address = "http://119.23.38.100:8080/cma/EquipmentReceive/deleteAttachment";
            RequestBody requestBody = new FormBody.Builder().add("attachmentId", attachmentId).build();
            HttpUtil.sendOkHttpWithRequestBody(address, requestBody, new okhttp3.Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseData = response.body().string();
                    Log.d("AttachmentDel", responseData);
                    int code = 0;
                    String msg = "";
                    try {
                        JSONObject object = new JSONObject(responseData);
                        code = object.getInt("code");
                        msg = object.getString("msg");
                    } catch (JSONException e) {
                        e.printStackTrace();
                        ToastUtil.showShort(EquipmentReceive_Modify.this, "附件删除失败");
                    }
                    if (code == 200 && msg.equals("成功")) {
                        attachmentDelSuccess++;
                        checkFinish();
                    }
                }

                @Override
                public void onFailure(Call call, IOException e) {
                    ToastUtil.showShort(EquipmentReceive_Modify.this, "附件删除失败");
                }
            });
        }
    }

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
                for (AttachmentLayout attachment : attachmentLayouts) {
                    if (!attachment.isUse) {
                        attachment.isUse = true;
                        attachment.file = file;
                        attachment.name.setText(file.getName());
                        attachment.size.setText(formatFileSize);
                        attachment.layout.setVisibility(View.VISIBLE);
                        attachment.size.setVisibility(View.VISIBLE);
                        break;
                    }
                }
            }
        });
    }

    public void removeAttachment(final int index) {
        attachmentLayouts.get(index).file = null;

        //若要删除的附件是后端已存在的，则需要保存该附件的id.
        if (attachmentLayouts.get(index).file == null) {
            for (Attachment attachment : attachments) {
                String attachmentName = attachmentLayouts.get(index).name.getText().toString();
                if (attachment.getName().equals(attachmentName))
                    deleteAttachmentId.add(attachment.getAttachmentId() + "");
            }
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                attachmentLayouts.get(index).layout.setVisibility(View.GONE);
                attachmentLayouts.get(index).isUse = false;
            }
        });
    }

    public Boolean isFullAttachment() {
        for (AttachmentLayout attachment : attachmentLayouts)
            if (!attachment.isUse)
                return false;
        return true;
    }

    public void getAttachments() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String address = "http://119.23.38.100:8080/cma/EquipmentReceive/getAllAttachmentNameById?id=" + equipmentReceive.getId();
                HttpUtil.sendOkHttpRequest(address, new okhttp3.Callback() {
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseData = response.body().string();
                        Log.d("EquipmentReceive_Modify", responseData);
                        parseJSONWithGSON(responseData);
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        ToastUtil.showShort(EquipmentReceive_Modify.this, "请求数据失败");
                    }
                });
            }
        }).start();
    }

    private void parseJSONWithGSON(String jsonData) {
        String info = "";
        try {
            JSONObject object = new JSONObject(jsonData);
            info = object.getString("data");
        } catch (Exception e) {
            e.printStackTrace();
        }
        attachments = new Gson().fromJson(info, new TypeToken<List<Attachment>>() {
        }.getType());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < attachments.size(); i++) {
                    if (i >= attachmentLayouts.size())
                        break;
                    attachmentLayouts.get(i).name.setText(attachments.get(i).getName());
                    attachmentLayouts.get(i).layout.setVisibility(View.VISIBLE);
                    attachmentLayouts.get(i).isUse = true;
                }
            }
        });
    }
}

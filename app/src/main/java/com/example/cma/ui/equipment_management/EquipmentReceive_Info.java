package com.example.cma.ui.equipment_management;

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
import android.widget.TextView;

import com.example.cma.R;
import com.example.cma.model.equipment_management.Attachment;
import com.example.cma.model.equipment_management.EquipmentReceive;
import com.example.cma.utils.AddressUtil;
import com.example.cma.utils.DownloadHelper;
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
import okhttp3.RequestBody;
import okhttp3.Response;

public class EquipmentReceive_Info extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "EquipmentReceive_Info";
    private EquipmentReceive equipmentReceive;
    private TextView name_text;
    private TextView model_text;
    private TextView manufacturer_text;
    private TextView receiveSituation_text;
    private TextView recipient_text;
    private TextView receiveDate_text;
    private TextView equipmentSituation_text;
    private TextView acceptance_text;
    private TextView acceptancePerson_text;
    private TextView acceptanceDate_text;


    private List<Attachment> attachments = new ArrayList<>();
    private List<AttachmentLayout> attachmentLayouts = new ArrayList<>(5);

    private Button attachment_download1;
    private Button attachment_download2;
    private Button attachment_download3;
    private Button attachment_download4;
    private Button attachment_download5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.equipment_receive_info);
        initView();
        Intent intent = getIntent();
        equipmentReceive = (EquipmentReceive) intent.getSerializableExtra("EquipmentReceive");
        setText();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getEquipmentReceiveInfo();
        getAttachment();
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

        findViewById(R.id.delete_button).setOnClickListener(this);
        findViewById(R.id.edit_button).setOnClickListener(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        ViewUtil.getInstance().setSupportActionBar(this, toolbar);

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
            attachment.size.setVisibility(View.GONE);
        }

        attachment_download1 = findViewById(R.id.attachment_download1);
        attachment_download2 = findViewById(R.id.attachment_download2);
        attachment_download3 = findViewById(R.id.attachment_download3);
        attachment_download4 = findViewById(R.id.attachment_download4);
        attachment_download5 = findViewById(R.id.attachment_download5);
        attachment_download1.setOnClickListener(this);
        attachment_download2.setOnClickListener(this);
        attachment_download3.setOnClickListener(this);
        attachment_download4.setOnClickListener(this);
        attachment_download5.setOnClickListener(this);
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
            case R.id.edit_button: {
                Intent intent = new Intent(EquipmentReceive_Info.this, EquipmentReceive_Modify.class);
                intent.putExtra("EquipmentReceive", equipmentReceive);
                startActivity(intent);
                break;
            }
            case R.id.delete_button:
                onDeleteConfirm();
                break;
            case R.id.attachment_download1:
                downloadAttachment(0);
                break;
            case R.id.attachment_download2:
                downloadAttachment(1);
                break;
            case R.id.attachment_download3:
                downloadAttachment(2);
                break;
            case R.id.attachment_download4:
                downloadAttachment(3);
                break;
            case R.id.attachment_download5:
                downloadAttachment(4);
                break;
            default:
                break;
        }
    }

    public void onDeleteConfirm() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(EquipmentReceive_Info.this);
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

    public void onDelete() {
        String address = AddressUtil.getAddress(AddressUtil.EquipmentReceive_deleteOne);
        RequestBody requestBody = new FormBody.Builder().add("id", "" + equipmentReceive.getId()).build();
        HttpUtil.sendOkHttpWithRequestBody(address, requestBody, new okhttp3.Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                Log.d(TAG, responseData);
                int code = 0;
                String msg = "";
                try {
                    JSONObject object = new JSONObject(responseData);
                    code = object.getInt("code");
                    msg = object.getString("msg");
                } catch (JSONException e) {
                    e.printStackTrace();
                    ToastUtil.showShort(EquipmentReceive_Info.this, "删除失败");
                }
                if (code == 200 && msg.equals("成功")) {
                    ToastUtil.showShort(EquipmentReceive_Info.this, "删除成功");
                    finish();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showShort(EquipmentReceive_Info.this, "删除失败");
            }
        });
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

    public void getEquipmentReceiveInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String address = AddressUtil.getAddress(AddressUtil.EquipmentReceive_getOne) + equipmentReceive.getId();
                HttpUtil.sendOkHttpRequest(address, new okhttp3.Callback() {
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseData = response.body().string();
                        Log.d(TAG, responseData);
                        parseJSONWithGSON(responseData, true);
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        ToastUtil.showShort(EquipmentReceive_Info.this, "请求数据失败");
                    }
                });
            }
        }).start();
    }

    public void getAttachment() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String address = "http://119.23.38.100:8080/cma/EquipmentReceive/getAllAttachmentNameById?id=" + equipmentReceive.getId();
                HttpUtil.sendOkHttpRequest(address, new okhttp3.Callback() {
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseData = response.body().string();
                        Log.d(TAG, responseData);
                        parseJSONWithGSON(responseData, false);
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        ToastUtil.showShort(EquipmentReceive_Info.this, "请求数据失败");
                    }
                });
            }
        }).start();
    }

    /**
     * @param flag 用于判断分析的是EquipmentReceive还是Attachment
     */
    private void parseJSONWithGSON(String jsonData, boolean flag) {
        String info = "";
        try {
            JSONObject object = new JSONObject(jsonData);//最外层的JSONObject对象
            info = object.getString("data");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        if (flag) {
            equipmentReceive = gson.fromJson(info, EquipmentReceive.class);
            if (equipmentReceive != null)
                setText();
        } else {
            attachments = gson.fromJson(info, new TypeToken<List<Attachment>>() {
            }.getType());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 5; i++) {
                        if (i < attachments.size()) {
                            attachmentLayouts.get(i).name.setText(attachments.get(i).getName());
                            attachmentLayouts.get(i).layout.setVisibility(View.VISIBLE);
                        } else
                            attachmentLayouts.get(i).layout.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    public void downloadAttachment(final int index) {


        final String address = "http://119.23.38.100:8080/cma/EquipmentReceive/getOneAttachment?attachmentId=" + attachments.get(index).getAttachmentId();
        attachmentLayouts.get(index).size.setVisibility(View.VISIBLE);

        DownloadHelper.Builder builder = new DownloadHelper.Builder(EquipmentReceive_Info.this).title(attachments.get(index).getName())
                .description("设备附件下载")
                .downloadUrl(address)
                .fileSaveName(attachments.get(index).getName())
                .fileSavePath("CMA/设备附件")
                .notifyVisible(true)
                .fileType(DownloadHelper.FileType.NORMAL)
                .onProgressListener(new DownloadHelper.OnDownloadProgressListener() {
                    @Override
                    public void onProgress(int downloadedSize, int totalSize) {
                        if (downloadedSize == totalSize) {  //下载完成
                            showSuccess(index);
                        }
                    }

                    @Override
                    public void onSuccess(Uri fileUri) {
                        showSuccess(index);
                    }

                    @Override
                    public void onFail() {
                    }

                    @Override
                    public void fileAlreadyExits(File file) {
                        showSuccess(index);
                    }
                });
        DownloadHelper downloadHelper = builder.build();
        downloadHelper.start();
    }

    /**
     * 显示下载成功
     *
     * @param index 附件列表list中的下标
     */
    public void showSuccess(final int index) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                attachmentLayouts.get(index).size.setText("下载成功");
                download_btn_setEnable(index);
            }
        });
    }

    public void download_btn_setEnable(int index) {
        switch (index) {
            case 0:
                attachment_download1.setEnabled(false);
                break;
            case 1:
                attachment_download2.setEnabled(false);
                break;
            case 2:
                attachment_download3.setEnabled(false);
                break;
            case 3:
                attachment_download4.setEnabled(false);
                break;
            case 4:
                attachment_download5.setEnabled(false);
                break;
            default:
                break;
        }
    }
}

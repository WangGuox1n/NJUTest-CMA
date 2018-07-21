package com.example.cma.adapter.self_inspection;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cma.R;
import com.example.cma.model.self_inspection.SelfInspection_File;
import com.example.cma.ui.self_inspection.SelfInspection_FileList;
import com.example.cma.utils.AddressUtil;
import com.example.cma.utils.HttpUtil;
import com.example.cma.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by 王国新 on 2018/7/13.
 */

public class SelfInspectionFileAdapter extends RecyclerView.Adapter<SelfInspectionFileAdapter.ViewHolder> {
    private static final String TAG = "SelfInspectFileAdapter";

    private Context mContext;
    private List<SelfInspection_File> mFileList = new ArrayList<>();
    private List<SelfInspection_File> rawList = new ArrayList<>();

    public SelfInspectionFileAdapter(List<SelfInspection_File> SelfInspectionFileList) {
        mFileList.addAll(SelfInspectionFileList);
        rawList = SelfInspectionFileList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View item;
        TextView fileName;
        TextView file;

        public ViewHolder(final View view) {
            super(view);
            this.item = view;
            fileName = view.findViewById(R.id.item_fileName);
            file = view.findViewById(R.id.item_file);
            final MenuItem.OnMenuItemClickListener listener = new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {         //设置每个菜单的点击动作
                    int index = (int) view.getTag();
                    switch (item.getItemId()) {
                        case 1: {
                            ((SelfInspection_FileList) mContext).downloadFile(index);
                            return true;
                        }
                        case 2: {
                            ((SelfInspection_FileList) mContext).showModifyDialog(index);
                            return true;
                        }
                        case 3: {
                            showDeleteDialog(index);
                            return true;
                        }
                        default:
                            return true;
                    }
                }
            };
            item.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                @Override
                public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                    MenuItem download = menu.add(Menu.NONE, 1, 1, "下载");
                    MenuItem edit = menu.add(Menu.NONE, 2, 2, "编辑");
                    MenuItem delete = menu.add(Menu.NONE, 3, 3, "删除");
                    download.setOnMenuItemClickListener(listener);
                    edit.setOnMenuItemClickListener(listener);
                    delete.setOnMenuItemClickListener(listener);
                }
            });
        }
    }

    @Override
    public SelfInspectionFileAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.self_inspection_file_listitem, parent, false);
        final SelfInspectionFileAdapter.ViewHolder holder = new SelfInspectionFileAdapter.ViewHolder(view);
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showShort(mContext,"长按可进行操作");
            }
        });


        return new SelfInspectionFileAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SelfInspectionFileAdapter.ViewHolder holder, int position) {
        SelfInspection_File file = mFileList.get(position);
        holder.fileName.setText(file.getFileName());
        holder.file.setText(file.getFile());
        //需要把position传给holder
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mFileList.size();
    }

    public void filter(String text) {
        mFileList.clear();
        if (text.isEmpty()) {
            mFileList.addAll(rawList);
        } else {
            text = text.toLowerCase();
            for (SelfInspection_File selfInspection : rawList) {
                if (selfInspection.getFileName().contains(text)||
                        selfInspection.getFile().contains(text)) {
                    mFileList.add(selfInspection);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void myNotifyDataSetChanged() {
        mFileList.clear();
        mFileList.addAll(rawList);
        notifyDataSetChanged();
    }

    private void showDeleteDialog(final int index) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        dialog.setMessage("确定删除？");
        dialog.setCancelable(false);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                postDelete(index);
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


    public void postDelete(final int index) {
        String address = AddressUtil.getAddress(AddressUtil.SelfInspection_deleteOneFile);
        RequestBody requestBody = new FormBody.Builder()
                .add("fileId", mFileList.get(index).getId() + "")
                .build();
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
                    ToastUtil.showShort(mContext, "网络连接错误，删除失败");
                    e.printStackTrace();
                }
                if (code == 200 && msg.equals("成功")) {
                    ToastUtil.showShort(mContext, "删除成功");
                    rawList.remove(index);
                    ((AppCompatActivity) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            myNotifyDataSetChanged();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showShort(mContext, "删除失败");
            }
        });
    }

}

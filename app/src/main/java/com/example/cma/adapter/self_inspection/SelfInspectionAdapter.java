package com.example.cma.adapter.self_inspection;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.cma.model.self_inspection.SelfInspection;
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

public class SelfInspectionAdapter extends RecyclerView.Adapter<SelfInspectionAdapter.ViewHolder> {
    private static final String TAG = "SelfInspectionAdapter";

    private Context mContext;
    private List<SelfInspection> mSelfInspectionList = new ArrayList<>();
    private List<SelfInspection> rawList = new ArrayList<>();

    public SelfInspectionAdapter(List<SelfInspection> SelfInspectionList) {
        mSelfInspectionList.addAll(SelfInspectionList);
        rawList = SelfInspectionList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View item;
        TextView name;
        TextView date;

        public ViewHolder(final View view) {
            super(view);
            this.item = view;
            name = view.findViewById(R.id.item_name);
            date = view.findViewById(R.id.item_date);
            final MenuItem.OnMenuItemClickListener listener = new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {         //设置每个菜单的点击动作
                    switch (item.getItemId()) {
                        case 1:
                            int index = (int) view.getTag();
                            deleteConfirm(index);
                            return true;
                        default:
                            return true;
                    }
                }
            };
            item.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                @Override
                public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                    //MenuInflater inflater = ((AppCompatActivity)mContext).getMenuInflater();
                    MenuItem delete = menu.add(Menu.NONE, 1, 1, "删除");
                    delete.setOnMenuItemClickListener(listener); //响应点击事件
                }
            });
        }
    }

    @Override
    public SelfInspectionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.self_inspection_main_listitem, parent, false);
        final SelfInspectionAdapter.ViewHolder holder = new SelfInspectionAdapter.ViewHolder(view);
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                Intent intent = new Intent(mContext, SelfInspection_FileList.class);
                intent.putExtra("SelfInspection", mSelfInspectionList.get(position));
                mContext.startActivity(intent);
            }
        });

        return new SelfInspectionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SelfInspectionAdapter.ViewHolder holder, int position) {
        SelfInspection inspection = mSelfInspectionList.get(position);
        holder.name.setText(inspection.getName());
        holder.date.setText(inspection.getDate());
        //需要把position传给holder
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mSelfInspectionList.size();
    }

    public void filter(String text) {
        mSelfInspectionList.clear();
        if (text.isEmpty()) {
            mSelfInspectionList.addAll(rawList);
        } else {
            text = text.toLowerCase();
            for (SelfInspection selfInspection : rawList) {
                if (selfInspection.getName().contains(text)||
                        selfInspection.getDate().contains(text)) {
                    mSelfInspectionList.add(selfInspection);
                }
            }
        }
        notifyDataSetChanged();
    }

    /*
    * mSelfInspectionList 和 rawList不是同一个对象，
    * 因此rawList改变时，也要刷新用于展示的 mSelfInspectionList
    * */
    public void myNotifyDataSetChanged() {
        mSelfInspectionList.clear();
        mSelfInspectionList.addAll(rawList);
        notifyDataSetChanged();
    }

    public void deleteConfirm(final int index) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        dialog.setTitle("删除自查文档集?");
        dialog.setMessage("确定删除？");
        dialog.setCancelable(true);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                postDelete(index);
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        dialog.show();
    }

    public void postDelete(final int index) {
        String address = AddressUtil.getAddress(AddressUtil.SelfInspection_deleteOne);
        RequestBody requestBody = new FormBody.Builder()
                .add("id", rawList.get(index).getId() + "")
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

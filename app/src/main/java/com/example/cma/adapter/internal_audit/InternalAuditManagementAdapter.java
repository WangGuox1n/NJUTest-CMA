package com.example.cma.adapter.internal_audit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cma.R;
import com.example.cma.model.internal_audit.InternalAuditManagement;

import java.util.List;

/**
 * Created by admin on 2018/7/11.
 */

public class InternalAuditManagementAdapter extends ArrayAdapter<InternalAuditManagement> {
    private int resourceId;
    private List<InternalAuditManagement> list;
    private List<InternalAuditManagement> listdata;//为list整个数据做备份

    //上下文，listview子项布局id,数据
    public InternalAuditManagementAdapter(Context context, int textViewResourceId, List<InternalAuditManagement> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
        list=objects;
        listdata=objects;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public InternalAuditManagement getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        InternalAuditManagement internalAuditManagement= getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        TextView date=(TextView)view.findViewById(R.id.date);
        date.setText(internalAuditManagement.getYear()+"年内审文档集合("+internalAuditManagement.getDate()+")");
        ImageView imageView=(ImageView)view.findViewById(R.id.file_image);
        imageView.setImageResource(R.drawable.filefolder);
        return view;
    }


}


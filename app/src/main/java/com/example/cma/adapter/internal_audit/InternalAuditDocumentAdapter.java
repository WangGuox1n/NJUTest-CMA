package com.example.cma.adapter.internal_audit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cma.R;
import com.example.cma.model.internal_audit.InternalAuditDocument;

import java.util.List;

/**
 * Created by admin on 2018/7/12.
 */

public class InternalAuditDocumentAdapter extends ArrayAdapter<InternalAuditDocument> {

    private int resourceId;
    private List<InternalAuditDocument> list;
    private List<InternalAuditDocument> listdata;//为list整个数据做备份

    //上下文，listview子项布局id,数据
    public InternalAuditDocumentAdapter(Context context, int textViewResourceId, List<InternalAuditDocument> objects){
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
    public InternalAuditDocument getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        InternalAuditDocument internalAuditDocument= getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
       TextView filename=(TextView)view.findViewById(R.id.file_name);
       filename.setText(internalAuditDocument.getYear()+"年"+internalAuditDocument.getFileName()+"文档");

        ImageView imageView=(ImageView)view.findViewById(R.id.file_image);
        imageView.setImageResource(R.drawable.file);
        return view;
    }


}


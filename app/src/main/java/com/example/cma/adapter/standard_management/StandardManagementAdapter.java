package com.example.cma.adapter.standard_management;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cma.R;
import com.example.cma.model.standard_management.StandardManagement;

import java.util.List;

/**
 * Created by admin on 2018/7/15.
 */

public class StandardManagementAdapter extends ArrayAdapter<StandardManagement> {

    private int resourceId;
    private List<StandardManagement> list;
    private List<StandardManagement> listdata;//为list整个数据做备份

    //上下文，listview子项布局id,数据
    public StandardManagementAdapter(Context context, int textViewResourceId, List<StandardManagement> objects){
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
    public StandardManagement getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        StandardManagement standardManagement= getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        TextView filename=(TextView)view.findViewById(R.id.file_name);
        filename.setText(standardManagement.getFileName());

        ImageView imageView=(ImageView)view.findViewById(R.id.file_image);
        imageView.setImageResource(R.drawable.file);
        return view;
    }


}



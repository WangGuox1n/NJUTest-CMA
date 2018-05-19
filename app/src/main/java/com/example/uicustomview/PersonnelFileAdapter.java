package com.example.uicustomview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by 王国新 on 2018/5/17.
 */

public class PersonnelFileAdapter extends ArrayAdapter<PersonnelFile> {
    private int resourceId;
    private MyFilter mFilter;
    private List<PersonnelFile> list;   //用于展示的数据
    private List<PersonnelFile> rawList;//原始数据


    public PersonnelFileAdapter(Context context,int textViewResourceId, List<PersonnelFile> objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
        list = objects;
        rawList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PersonnelFile personnelFile = getItem(position);

        View view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);

        TextView name=(TextView) view.findViewById(R.id.item_name);
        name.setText(personnelFile.getName());

        TextView department=(TextView) view.findViewById(R.id.item_department);
        department.setText(personnelFile.getDepartment());

        TextView post=(TextView)view.findViewById(R.id.item_position);
        post.setText(personnelFile.getPosition());
        return view;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public PersonnelFile getItem(int position) {
        //list用于展示
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Filter getFilter() {
        if (null == mFilter) {
            mFilter = new MyFilter();
        }
        return mFilter;
    }

    // 自定义Filter类
    class MyFilter extends Filter {
        @Override
        // 该方法在子线程中执行
        // 自定义过滤规则
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            //String filterString = constraint.toString().trim().toLowerCase();

            List<PersonnelFile> filterList = new ArrayList<>();
            // 如果搜索框内容为空，就恢复原始数据
            if (TextUtils.isEmpty(constraint)) {
                filterList = rawList;
            } else {
                // 过滤出新数据
                for (PersonnelFile pf : list) {
                    if(pf.getName().contains(constraint)||
                            pf.getDepartment().contains(constraint)||
                            pf.getPosition().contains(constraint))
                        filterList.add(pf);
                }
            }

            results.values = filterList;
            results.count = filterList.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            list = (List<PersonnelFile>) results.values;

            if (results.count > 0) {
                notifyDataSetChanged();  // 通知数据发生了改变
            } else {
                notifyDataSetInvalidated(); // 通知数据失效
            }
        }
    }
}

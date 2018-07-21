package com.example.cma.adapter.staff_management;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.example.cma.R;
import com.example.cma.model.staff_management.StaffManagement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2018/5/6.
 */

public class StaffManagementAdapter extends ArrayAdapter<StaffManagement> {

    private int resourceId;
    private List<StaffManagement> list;
    private List<StaffManagement> listdata;//为list整个数据做备份
    private MyFilter myFilter;

    //上下文，listview子项布局id,数据
    public StaffManagementAdapter(Context context, int textViewResourceId, List<StaffManagement> objects){
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
    public StaffManagement getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        StaffManagement dangAn= getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        TextView departmetText=(TextView) view.findViewById(R.id.item_department);
        departmetText.setText(dangAn.getDepartment());
        TextView nameText=(TextView) view.findViewById(R.id.item_name);
        nameText.setText(dangAn.getName());
        TextView positionText=(TextView)view.findViewById(R.id.item_position);
        positionText.setText(dangAn.getPosition());
        return view;
    }


    //定义过滤器定义过滤规则
    class MyFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<StaffManagement> lists;
            if (TextUtils.isEmpty(constraint)) {//当过滤的关键字为空的时候，我们则显示所有的数据
                lists = listdata;
            } else {//否则把符合条件的数据对象添加到集合中
                lists = new ArrayList<>();
                for (StaffManagement d : listdata) {
                    if (d.getDepartment().contains(constraint) || d.getName().contains(constraint) || d.getPosition().contains(constraint)) {
                        lists.add(d);
                        Log.d("StaffFileAdapter","name is"+d.getName());
                    }
                }
            }

            results.values = lists; //将得到的集合保存到FilterResults的value变量中
            results.count = lists.size();//将集合的大小保存到FilterResults的count变量中

            // for(DangAn d:lists){
            //   Log.d("StaffFileAdapter","name is"+d.getName());
            //}
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            list=(List<StaffManagement>)results.values;
            // for(DangAn d:list){
            //  Log.d("DanganAdapterPP","name is"+d.getName());
            // }
            if(results.count>0)
            {
                notifyDataSetChanged();//通知数据发生了改变
            }else {
                notifyDataSetInvalidated();//通知数据失效
            }
        }
    }



    //让getFilter()方法，在方法里面实例化刚刚创建的过滤器
    @Override
    public Filter getFilter() {
        if (myFilter ==null){
            myFilter = new MyFilter();
        }
        return myFilter;
    }

}
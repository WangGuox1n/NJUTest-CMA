package com.example.cma.adapter.capacity_verification;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.example.cma.R;
import com.example.cma.model.capacity_verification.CapacityVerificationProject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2018/7/17.
 */

public class CapacityVerificationProjectAdapter extends ArrayAdapter<CapacityVerificationProject> {
    private int resourceId;
    private List<CapacityVerificationProject> list;
    private List<CapacityVerificationProject> listdata;//为list整个数据做备份
    private MyFilter myFilter;

    //上下文，listview子项布局id,数据
    public CapacityVerificationProjectAdapter(Context context, int textViewResourceId, List<CapacityVerificationProject> objects){
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
    public CapacityVerificationProject getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        CapacityVerificationProject project= getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        TextView nameText=(TextView)view.findViewById(R.id.name_item);
        nameText.setText(project.getName());
        TextView methodText=(TextView)view.findViewById(R.id.method_item);
        methodText.setText(project.getMethod());
        TextView situationText=(TextView)view.findViewById(R.id.item_situation);
        if(project.getState()==0)
            situationText.setText("未执行");
        else
            situationText.setText("已执行");
        return view;
    }


    //定义过滤器定义过滤规则
    class MyFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<CapacityVerificationProject> lists;
            if (TextUtils.isEmpty(constraint)) {//当过滤的关键字为空的时候，我们则显示所有的数据
                lists = listdata;
            } else {//否则把符合条件的数据对象添加到集合中
                lists = new ArrayList<>();
                for (CapacityVerificationProject d : listdata) {
                    String temp;
                    if(d.getState()==0)
                        temp="未执行";
                    else
                        temp="已执行";
                    if (d.getName().contains(constraint) || d.getMethod().contains(constraint)|| temp.contains(constraint)) {
                        lists.add(d);
                    }
                }
            }

            results.values = lists; //将得到的集合保存到FilterResults的value变量中
            results.count = lists.size();//将集合的大小保存到FilterResults的count变量中
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            list=(List<CapacityVerificationProject>)results.values;
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



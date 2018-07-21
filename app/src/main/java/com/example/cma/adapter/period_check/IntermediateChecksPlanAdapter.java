package com.example.cma.adapter.period_check;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.example.cma.R;
import com.example.cma.model.period_check.IntermediateChecksPlan;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2018/6/17.
 */

public class IntermediateChecksPlanAdapter extends ArrayAdapter<IntermediateChecksPlan> {
    private int resourceId;
    private List<IntermediateChecksPlan> list;
    private List<IntermediateChecksPlan> listdata;//为list整个数据做备份
    private MyFilter myFilter;

    //上下文，listview子项布局id,数据
    public IntermediateChecksPlanAdapter(Context context, int textViewResourceId, List<IntermediateChecksPlan> objects){
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
    public IntermediateChecksPlan getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        IntermediateChecksPlan plan= getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        TextView objectText=(TextView)view.findViewById(R.id.item_object);
        objectText.setText(plan.getObject());
        TextView personText=(TextView)view.findViewById(R.id.item_chargePerson);
        personText.setText(plan.getPersonInCharge());
        TextView situationText=(TextView)view.findViewById(R.id.item_situation);
        if(plan.getState()==0)
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
            List<IntermediateChecksPlan> lists;
            if (TextUtils.isEmpty(constraint)) {//当过滤的关键字为空的时候，我们则显示所有的数据
                lists = listdata;
            } else {//否则把符合条件的数据对象添加到集合中
                lists = new ArrayList<>();
                for (IntermediateChecksPlan d : listdata) {
                    String temp;
                    if(d.getState()==0)
                        temp="未执行";
                    else
                        temp="已执行";
                    if (d.getObject().contains(constraint) || d.getPersonInCharge().contains(constraint)|| temp.contains(constraint)) {
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
            list=(List<IntermediateChecksPlan>)results.values;
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


package com.example.cma.adapter.training_management;


import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.example.cma.R;
import com.example.cma.model.training_management.AnnualPlan;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by new on 2018/6/17.
 */

public class AnnualPlanAdapter  extends ArrayAdapter<AnnualPlan> {
    private int resourceId;
    private AnnualPlanAdapter.MyFilter mFilter;
    private List<AnnualPlan> list;   //用于展示的数据
    private List<AnnualPlan> rawList;//原始数据


    public AnnualPlanAdapter(Context context, int textViewResourceId, List<AnnualPlan> objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
        list = objects;
        rawList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AnnualPlan temp = getItem(position);
        View view;
        AnnualPlanAdapter.ViewHolder viewHolder;

        if(null == convertView){
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder = new AnnualPlanAdapter.ViewHolder();
            viewHolder.year =(TextView) view.findViewById(R.id.item_year);
            viewHolder.author=(TextView) view.findViewById(R.id.item_author);
            viewHolder.createDate=(TextView)view.findViewById(R.id.item_create_time);
            view.setTag(viewHolder);  //ViewHolder存在View中
        }else{
            view = convertView;
            viewHolder = (AnnualPlanAdapter.ViewHolder) view.getTag();
        }

        viewHolder.year.setText(String.valueOf(temp.getYear()));
        viewHolder.author.setText(temp.getAuthor());

        viewHolder.createDate.setText(temp.getCreateDate());
        return view;
    }

    class ViewHolder{
        TextView year;
        TextView author;
        TextView createDate;


    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public AnnualPlan getItem(int position) {
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
            mFilter = new AnnualPlanAdapter.MyFilter();
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

            List<AnnualPlan> filterList;
            // 如果搜索框内容为空，就恢复原始数据
            if (TextUtils.isEmpty(constraint)) {
                filterList = rawList;
            } else {
                // 过滤出新数据
                filterList = new ArrayList<>();
                for (AnnualPlan staff : list) {
                    if(String.valueOf(staff.getYear()).contains(constraint)||staff.getAuthor().contains(constraint))
                        filterList.add(staff);
                }
            }
            results.values = filterList;
            results.count = filterList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            list = (List<AnnualPlan>) results.values;

            if (results.count > 0) {
                notifyDataSetChanged();  // 通知数据发生了改变
            } else {
                notifyDataSetInvalidated(); // 通知数据失效
            }
        }
    }
}



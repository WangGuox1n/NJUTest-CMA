package com.example.cma.adapter.supervision;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.example.cma.R;
import com.example.cma.model.supervision.SupervisionPlan;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 王国新 on 2018/6/10.
 */

public class SupervisionPlanAdapter extends ArrayAdapter<SupervisionPlan> {
    private int resourceId;
    private SupervisionPlanAdapter.MyFilter mFilter;
    private List<SupervisionPlan> list;   //用于展示的数据
    private List<SupervisionPlan> rawList;//原始数据

    public SupervisionPlanAdapter(Context context, int textViewResourceId, List<SupervisionPlan> objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
        list = objects;
        rawList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SupervisionPlan supervisionPlan = getItem(position);
        View view;
        SupervisionPlanAdapter.ViewHolder viewHolder;

        if(null == convertView){
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder = new SupervisionPlanAdapter.ViewHolder();
            viewHolder.content =(TextView) view.findViewById(R.id.item_content);
            viewHolder.object=(TextView) view.findViewById(R.id.item_object);
            viewHolder.dateFrequency=(TextView)view.findViewById(R.id.item_dateFrequency);
            view.setTag(viewHolder);  //ViewHolder存在View中
        }else{
            view = convertView;
            viewHolder = (SupervisionPlanAdapter.ViewHolder) view.getTag();
        }

        viewHolder.content.setText(supervisionPlan.getContent());
        viewHolder.object.setText(supervisionPlan.getObject());
        viewHolder.dateFrequency.setText(supervisionPlan.getDateFrequency());
        return view;
    }

    class ViewHolder{
        TextView content;
        TextView object;
        TextView dateFrequency;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public SupervisionPlan getItem(int position) {
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
            mFilter = new SupervisionPlanAdapter.MyFilter();
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

            List<SupervisionPlan> filterList;
            // 如果搜索框内容为空，就恢复原始数据
            if (TextUtils.isEmpty(constraint)) {
                filterList = rawList;
            } else {
                // 过滤出新数据
                filterList = new ArrayList<>();
                for (SupervisionPlan supervisionPlan : rawList) {


                    if(supervisionPlan.getContent().contains(constraint)||
                            supervisionPlan.getObject().contains(constraint)||
                            supervisionPlan.getDateFrequency().contains(constraint))
                        filterList.add(supervisionPlan);
                }
            }
            results.values = filterList;
            results.count = filterList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            list = (List<SupervisionPlan>) results.values;

            if (results.count > 0) {
                notifyDataSetChanged();  // 通知数据发生了改变
            } else {
                notifyDataSetInvalidated(); // 通知数据失效
            }
        }
    }
}

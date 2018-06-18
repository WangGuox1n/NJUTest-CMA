package com.example.cma.adapter.staff_management;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.example.cma.R;
import com.example.cma.model.staff_management.StaffLeaving;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 王国新 on 2018/5/27.
 */

public class StaffLeavingAdapter extends ArrayAdapter<StaffLeaving> {
    private int resourceId;
    private StaffLeavingAdapter.MyFilter mFilter;
    private List<StaffLeaving> list;   //用于展示的数据
    private List<StaffLeaving> rawList;//原始数据

    public StaffLeavingAdapter(Context context, int textViewResourceId, List<StaffLeaving> objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
        list = objects;
        rawList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        StaffLeaving staff = getItem(position);
        View view;
        StaffLeavingAdapter.ViewHolder viewHolder;

        if(null == convertView){
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder = new StaffLeavingAdapter.ViewHolder();
            viewHolder.name =(TextView) view.findViewById(R.id.item_name);
            viewHolder.department=(TextView) view.findViewById(R.id.item_department);
            viewHolder.position=(TextView)view.findViewById(R.id.item_position);
            viewHolder.leavingDate=(TextView)view.findViewById(R.id.item_date);
            view.setTag(viewHolder);  //ViewHolder存在View中
        }else{
            view = convertView;
            viewHolder = (StaffLeavingAdapter.ViewHolder) view.getTag();
        }

        viewHolder.name.setText(staff.getName());
        viewHolder.department.setText(staff.getDepartment());
        viewHolder.position.setText(staff.getPosition());
        viewHolder.leavingDate.setText(staff.getLeavingDate());
        return view;
    }

    class ViewHolder{
        TextView name;
        TextView department;
        TextView position;
        TextView leavingDate;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public StaffLeaving getItem(int position) {
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
            mFilter = new StaffLeavingAdapter.MyFilter();
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

            List<StaffLeaving> filterList;
            // 如果搜索框内容为空，就恢复原始数据
            if (TextUtils.isEmpty(constraint)) {
                filterList = rawList;
            } else {
                // 过滤出新数据
                filterList = new ArrayList<>();
                for (StaffLeaving staff : rawList) {
                    if(staff.getName().contains(constraint)||
                            staff.getDepartment().contains(constraint)||
                            staff.getPosition().contains(constraint))
                        filterList.add(staff);
                }
            }
            results.values = filterList;
            results.count = filterList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            list = (List<StaffLeaving>) results.values;

            if (results.count > 0) {
                notifyDataSetChanged();  // 通知数据发生了改变
            } else {
                notifyDataSetInvalidated(); // 通知数据失效
            }
        }
    }
}

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
import com.example.cma.model.staff_management.StaffQualification;

import java.util.ArrayList;
import java.util.List;

public class StaffQualificationAdapter extends ArrayAdapter<StaffQualification> {
    private int resourceId;
    private  MyFilter mFilter;
    private List<StaffQualification> list;   //用于展示的数据
    private List<StaffQualification> rawList;//原始数据


    public StaffQualificationAdapter(Context context, int textViewResourceId, List<StaffQualification> objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
        list = objects;
        rawList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        StaffQualification staffQualification = getItem(position);
        View view;
        StaffQualificationAdapter.ViewHolder viewHolder;

        if(null == convertView){
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder = new StaffQualificationAdapter.ViewHolder();
            viewHolder.name =(TextView) view.findViewById(R.id.item_name);
            viewHolder.department=(TextView) view.findViewById(R.id.item_department);
            viewHolder.qualificationName=(TextView)view.findViewById(R.id.qualificationName);
            view.setTag(viewHolder);  //ViewHolder存在View中
        }else{
            view = convertView;
            viewHolder = (StaffQualificationAdapter.ViewHolder) view.getTag();
        }

        viewHolder.name.setText(staffQualification.getName());
        viewHolder.department.setText(staffQualification.getDepartment());
        viewHolder.qualificationName.setText(staffQualification.getQualificationName());
        return view;
    }

    class ViewHolder{
        TextView name;
        TextView department;
        TextView qualificationName;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public StaffQualification getItem(int position) {
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
            mFilter = new StaffQualificationAdapter.MyFilter();
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

            List<StaffQualification> filterList;
            // 如果搜索框内容为空，就恢复原始数据
            if (TextUtils.isEmpty(constraint)) {
                filterList = rawList;
            } else {
                // 过滤出新数据
                filterList = new ArrayList<>();
                for (StaffQualification staff : rawList) {
                    if(staff.getName().contains(constraint)||
                            staff.getDepartment().contains(constraint)||
                            staff.getQualificationName().contains(constraint))
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
            list = (List<StaffQualification>) results.values;

            if (results.count > 0) {
                notifyDataSetChanged();  // 通知数据发生了改变
            } else {
                notifyDataSetInvalidated(); // 通知数据失效
            }
        }
    }
}

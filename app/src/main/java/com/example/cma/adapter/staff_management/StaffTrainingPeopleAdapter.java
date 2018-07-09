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
import com.example.cma.model.staff_management.StaffTrainingPeople;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by new on 2018/6/16.
 */

public class StaffTrainingPeopleAdapter extends ArrayAdapter<StaffTrainingPeople> {
    private int resourceId;
    private StaffTrainingPeopleAdapter.MyFilter mFilter;
    private List<StaffTrainingPeople> list;   //用于展示的数据
    private List<StaffTrainingPeople> rawList;//原始数据


    public StaffTrainingPeopleAdapter(Context context, int textViewResourceId, List<StaffTrainingPeople> objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
        list = objects;
        rawList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        StaffTrainingPeople StaffTrainingPeople = getItem(position);
        View view;
        StaffTrainingPeopleAdapter.ViewHolder viewHolder;

        if(null == convertView){
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder = new StaffTrainingPeopleAdapter.ViewHolder();
            viewHolder.id=(TextView) view.findViewById(R.id.item_number);
            viewHolder.name=(TextView) view.findViewById(R.id.item_name);
            view.setTag(viewHolder);  //ViewHolder存在View中
        }else{
            view = convertView;
            viewHolder = (StaffTrainingPeopleAdapter.ViewHolder) view.getTag();
        }

        viewHolder.id.setText(String.valueOf(StaffTrainingPeople.getId()));
        viewHolder.name.setText(StaffTrainingPeople.getName());
        return view;
    }

    class ViewHolder{
        TextView id;
        TextView name;


    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public StaffTrainingPeople getItem(int position) {
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
            mFilter = new StaffTrainingPeopleAdapter.MyFilter();
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

            List<StaffTrainingPeople> filterList;
            // 如果搜索框内容为空，就恢复原始数据
            if (TextUtils.isEmpty(constraint)) {
                filterList = rawList;
            } else {
                // 过滤出新数据
                filterList = new ArrayList<>();
                for (StaffTrainingPeople staff : list) {
                    if(staff.getName().contains(constraint))

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
            list = (List<StaffTrainingPeople>) results.values;

            if (results.count > 0) {
                notifyDataSetChanged();  // 通知数据发生了改变
            } else {
                notifyDataSetInvalidated(); // 通知数据失效
            }
        }
    }
}

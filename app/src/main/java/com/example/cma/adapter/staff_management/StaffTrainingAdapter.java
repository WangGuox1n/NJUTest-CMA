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
import com.example.cma.model.staff_management.StaffTraining;

import java.util.ArrayList;
import java.util.List;
public class StaffTrainingAdapter extends ArrayAdapter<StaffTraining> {
    private int resourceId;
    private StaffTrainingAdapter.MyFilter mFilter;
    private List<StaffTraining> list;   //用于展示的数据
    private List<StaffTraining> rawList;//原始数据


    public StaffTrainingAdapter(Context context, int textViewResourceId, List<StaffTraining> objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
        list = objects;
        rawList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        StaffTraining StaffTraining = getItem(position);
        View view;
        StaffTrainingAdapter.ViewHolder viewHolder;

        if(null == convertView){
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder = new StaffTrainingAdapter.ViewHolder();
            viewHolder.place =(TextView) view.findViewById(R.id.item_place);
            viewHolder.program=(TextView) view.findViewById(R.id.item_program);
            viewHolder.presenter=(TextView)view.findViewById(R.id.item_presenter);
            view.setTag(viewHolder);  //ViewHolder存在View中
        }else{
            view = convertView;
            viewHolder = (StaffTrainingAdapter.ViewHolder) view.getTag();
        }

        viewHolder.program.setText(StaffTraining.getProgram());
        viewHolder.place.setText(StaffTraining.getPlace());
        viewHolder.presenter.setText(StaffTraining.getPresenter());
        return view;
    }

    class ViewHolder{
        TextView program;
        TextView place;
        TextView presenter;

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public StaffTraining getItem(int position) {
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
            mFilter = new StaffTrainingAdapter.MyFilter();
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

            List<StaffTraining> filterList;
            // 如果搜索框内容为空，就恢复原始数据
            if (TextUtils.isEmpty(constraint)) {
                filterList = rawList;
            } else {
                // 过滤出新数据
                filterList = new ArrayList<>();
                for (StaffTraining staff : rawList) {
                    if(staff.getProgram().contains(constraint)||
                            staff.getPlace().contains(constraint)||staff.getPresenter().contains(constraint))

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
            list = (List<StaffTraining>) results.values;

            if (results.count > 0) {
                notifyDataSetChanged();  // 通知数据发生了改变
            } else {
                notifyDataSetInvalidated(); // 通知数据失效
            }
        }
    }
}

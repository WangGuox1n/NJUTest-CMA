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
import com.example.cma.model.training_management.TrainingApplication;

import java.util.ArrayList;
import java.util.List;

public class TrainingApplicationAdapter extends ArrayAdapter<TrainingApplication>{

    private int resourceId;
    private TrainingApplicationAdapter.MyFilter mFilter;
    private List<TrainingApplication> list;   //用于展示的数据
    private List<TrainingApplication> rawList;//原始数据

    public TrainingApplicationAdapter(Context context, int textViewResourceId, List<TrainingApplication> objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
        list = objects;
        rawList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TrainingApplication trainingApplication = getItem(position);
        View view;
        TrainingApplicationAdapter.ViewHolder viewHolder;

        if(null == convertView){
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder = new TrainingApplicationAdapter.ViewHolder();
            viewHolder.name =(TextView) view.findViewById(R.id.item_name);
            viewHolder.department=(TextView) view.findViewById(R.id.item_department);
            viewHolder.situation=(TextView)view.findViewById(R.id.item_situation);
            view.setTag(viewHolder);  //ViewHolder存在View中
        }else{
            view = convertView;
            viewHolder = (TrainingApplicationAdapter.ViewHolder) view.getTag();
        }

        viewHolder.name.setText(trainingApplication.getName());
        viewHolder.department.setText(trainingApplication.getDepartment());
        viewHolder.situation.setText(trainingApplication.SituationToString());
        return view;
    }

    class ViewHolder{
        TextView name;
        TextView department;
        TextView situation;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public TrainingApplication getItem(int position) {
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
            mFilter = new TrainingApplicationAdapter.MyFilter();
        }
        return mFilter;
    }

    // 自定义Filter类
    public class MyFilter extends Filter {
        @Override
        // 该方法在子线程中执行
        // 自定义过滤规则
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            List<TrainingApplication> filterList;
            // 如果搜索框内容为空，就恢复原始数据
            if (TextUtils.isEmpty(constraint)) {
                filterList = rawList;
            } else {
                // 过滤出新数据
                filterList = new ArrayList<>();
                for (TrainingApplication trainingApplication : rawList) {


                    if(trainingApplication.getName().contains(constraint)||
                            trainingApplication.getDepartment().contains(constraint)||
                            trainingApplication.SituationToString().contains(constraint))
                        filterList.add(trainingApplication);
                }
            }
            results.values = filterList;
            results.count = filterList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            list = (List<TrainingApplication>) results.values;

            if (results.count > 0) {
                notifyDataSetChanged();  // 通知数据发生了改变
            } else {
                notifyDataSetInvalidated(); // 通知数据失效
            }
        }
    }
}

package com.example.cma.adapter.sample_management;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.example.cma.R;
import com.example.cma.model.sample_management.SampleIO;

import java.util.ArrayList;
import java.util.List;

public class SampleIOAdapter extends ArrayAdapter<SampleIO> {
    private int resourceId;
    private SampleIOAdapter.MyFilter mFilter;
    private List<SampleIO> list;   //用于展示的数据
    private List<SampleIO> rawList;//原始数据

    public SampleIOAdapter(Context context, int textViewResourceId, List<SampleIO> objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
        list = objects;
        rawList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SampleIO sampleIO = getItem(position);
        View view;
        SampleIOAdapter.ViewHolder viewHolder;

        if(null == convertView){
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder = new SampleIOAdapter.ViewHolder();

            viewHolder.samplename =(TextView) view.findViewById(R.id.item_name);
            viewHolder.sender=(TextView) view.findViewById(R.id.item_sender);
            viewHolder.receiver=(TextView) view.findViewById(R.id.item_receiver);
            viewHolder.sampleState=(TextView)view.findViewById(R.id.item_situation);
            view.setTag(viewHolder);  //ViewHolder存在View中
        }else{
            view = convertView;
            viewHolder = (SampleIOAdapter.ViewHolder) view.getTag();
        }

        viewHolder.samplename.setText(sampleIO.getSampleName());
        viewHolder.sender.setText(sampleIO.getSender());
        viewHolder.receiver.setText(sampleIO.getReceiver());
        viewHolder.sampleState.setText(sampleIO.StateToString());
        return view;
    }

    class ViewHolder{
        TextView samplename;
        TextView sender;
        TextView receiver;
        TextView sampleState;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public SampleIO getItem(int position) {
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
            mFilter = new SampleIOAdapter.MyFilter();
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

            List<SampleIO> filterList;
            // 如果搜索框内容为空，就恢复原始数据
            if (TextUtils.isEmpty(constraint)) {
                filterList = rawList;
            } else {
                // 过滤出新数据
                filterList = new ArrayList<>();
                for (SampleIO staff : rawList) {
                    if(staff.getSampleName().contains(constraint)||
                            staff.getSender().contains(constraint)||
                            staff.getReceiver().contains(constraint)||
                            staff.StateToString().contains(constraint))
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
            list = (List<SampleIO>) results.values;

            if (results.count > 0) {
                notifyDataSetChanged();  // 通知数据发生了改变
            } else {
                notifyDataSetInvalidated(); // 通知数据失效
            }
        }
    }
}

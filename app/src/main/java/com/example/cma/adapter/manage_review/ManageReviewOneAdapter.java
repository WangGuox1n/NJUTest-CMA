package com.example.cma.adapter.manage_review;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.example.cma.R;
import com.example.cma.model.manage_review.ManageReviewOne;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by new on 2018/7/12.
 */

public class ManageReviewOneAdapter  extends ArrayAdapter<ManageReviewOne> {
    private int resourceId;
    private ManageReviewOneAdapter.MyFilter mFilter;
    private List<ManageReviewOne> list;   //用于展示的数据
    private List<ManageReviewOne> rawList;//原始数据
    private Context context;
    public ManageReviewOneAdapter(Context context, int textViewResourceId, List<ManageReviewOne> objects){
        super(context,textViewResourceId,objects);
        this.context=context;
        resourceId = textViewResourceId;
        list = objects;
        rawList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ManageReviewOne temp = getItem(position);
        View view;
        ManageReviewOneAdapter.ViewHolder viewHolder;

        if(null == convertView){
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder = new ManageReviewOneAdapter.ViewHolder();
            viewHolder.filename =(TextView) view.findViewById(R.id.file_name);
            view.setTag(viewHolder);  //ViewHolder存在View中
        }else{
            view = convertView;
            viewHolder = (ManageReviewOneAdapter.ViewHolder) view.getTag();
        }

        viewHolder.filename.setText(temp.getFileName());


        return view;
    }

    class ViewHolder{
        TextView filename;




    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public ManageReviewOne getItem(int position) {
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
            mFilter = new ManageReviewOneAdapter.MyFilter();
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

            List<ManageReviewOne> filterList;
            // 如果搜索框内容为空，就恢复原始数据
            if (TextUtils.isEmpty(constraint)) {
                filterList = rawList;
            } else {
                // 过滤出新数据
                filterList = new ArrayList<>();
                for (ManageReviewOne staff : list) {
                    if(staff.getFileName().contains(constraint))
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
            list = (List<ManageReviewOne>) results.values;

            if (results.count > 0) {
                notifyDataSetChanged();  // 通知数据发生了改变
            } else {
                notifyDataSetInvalidated(); // 通知数据失效
            }
        }
    }
}




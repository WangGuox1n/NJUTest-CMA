package com.example.cma.adapter.external_review;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cma.R;
import com.example.cma.model.external_review.ExternalReviewManagement;

import java.util.ArrayList;
import java.util.List;

public class ExternalReviewManagementAdapter extends ArrayAdapter<ExternalReviewManagement> {
    private int resourceId;
    private ExternalReviewManagementAdapter.MyFilter mFilter;
    private List<ExternalReviewManagement> list;   //用于展示的数据
    private List<ExternalReviewManagement> rawList;//原始数据

    public ExternalReviewManagementAdapter(Context context, int textViewResourceId, List<ExternalReviewManagement> objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
        list = objects;
        rawList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /*ExternalReviewManagement externalReviewManagement = getItem(position);
        View view;
        ExternalReviewManagementAdapter.ViewHolder viewHolder;

        if(null == convertView){
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder = new ExternalReviewManagementAdapter.ViewHolder();
            viewHolder.date =(TextView) view.findViewById(R.id.item_name);
            view.setTag(viewHolder);  //ViewHolder存在View中
        }else{
            view = convertView;
            viewHolder = (ExternalReviewManagementAdapter.ViewHolder) view.getTag();
        }

        viewHolder.date.setText(externalReviewManagement.getDate());
        ImageView imageView=(ImageView)view.findViewById(R.id.file_image);
        imageView.setImageResource(R.drawable.filefolder);
        return view;*/

        ExternalReviewManagement externalReviewManagement= getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        TextView date=(TextView)view.findViewById(R.id.date);
        date.setText(externalReviewManagement.getYear()+"外审文档集合("+externalReviewManagement.getDate()+")");
        ImageView imageView=(ImageView)view.findViewById(R.id.file_image);
        imageView.setImageResource(R.drawable.filefolder);
        return view;
    }

    class ViewHolder{
        TextView date;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public ExternalReviewManagement getItem(int position) {
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
            mFilter = new ExternalReviewManagementAdapter.MyFilter();
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

            List<ExternalReviewManagement> filterList;
            // 如果搜索框内容为空，就恢复原始数据
            if (TextUtils.isEmpty(constraint)) {
                filterList = rawList;
            } else {
                // 过滤出新数据
                filterList = new ArrayList<>();
                for (ExternalReviewManagement staff : rawList) {
                    if(staff.getDate().contains(constraint))
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
            list = (List<ExternalReviewManagement>) results.values;

            if (results.count > 0) {
                notifyDataSetChanged();  // 通知数据发生了改变
            } else {
                notifyDataSetInvalidated(); // 通知数据失效
            }
        }
    }
}

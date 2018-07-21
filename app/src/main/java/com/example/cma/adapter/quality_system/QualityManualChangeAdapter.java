package com.example.cma.adapter.quality_system;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cma.R;
import com.example.cma.model.quality_system.QualityManual;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by new on 2018/7/18.
 */

public class QualityManualChangeAdapter extends ArrayAdapter<QualityManual>{
    private int resourceId;
    private QualityManualChangeAdapter.MyFilter mFilter;
    private List<QualityManual> list;   //用于展示的数据
    private List<QualityManual> rawList;//原始数据
    private Context context;

    public QualityManualChangeAdapter(Context context, int textViewResourceId, List<QualityManual> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        resourceId = textViewResourceId;
        list = objects;
        rawList = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        QualityManual temp = getItem(position);
        View view;
        QualityManualChangeAdapter.ViewHolder viewHolder;

        if (null == convertView) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new QualityManualChangeAdapter.ViewHolder();
            viewHolder.filename = (TextView) view.findViewById(R.id.file_name);
            viewHolder.state = (TextView) view.findViewById(R.id.state);
            view.setTag(viewHolder);  //ViewHolder存在View中
        } else {
            view = convertView;
            viewHolder = (QualityManualChangeAdapter.ViewHolder) view.getTag();
        }

        viewHolder.filename.setText(temp.getFileName());
        String state = String.valueOf(temp.getState());
        if (state.equals("1")) {
            viewHolder.state.setText("不允许");
        }
        if (state.equals("2")) {
            viewHolder.state.setText("批准通过");
        }
        if (state.equals("0")) {
            viewHolder.state.setText("未批准");
        }


        return view;
    }

    class ViewHolder {
        TextView filename;
        TextView state;


    }



    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public QualityManual getItem(int position) {
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
            mFilter = new QualityManualChangeAdapter.MyFilter();
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

            List<QualityManual> filterList;
            // 如果搜索框内容为空，就恢复原始数据
            if (TextUtils.isEmpty(constraint)) {
                filterList = rawList;
            } else {
                // 过滤出新数据
                filterList = new ArrayList<>();
                for (QualityManual staff : list) {
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
            list = (List<QualityManual>) results.values;

            if (results.count > 0) {
                notifyDataSetChanged();  // 通知数据发生了改变
            } else {
                notifyDataSetInvalidated(); // 通知数据失效
            }
        }
    }
}


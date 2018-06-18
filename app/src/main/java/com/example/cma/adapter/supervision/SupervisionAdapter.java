package com.example.cma.adapter.supervision;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.example.cma.R;
import com.example.cma.model.supervision.Supervision;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 王国新 on 2018/6/9.
 */

public class SupervisionAdapter extends ArrayAdapter<Supervision> {
    private int resourceId;
    private SupervisionAdapter.MyFilter mFilter;
    private List<Supervision> list;   //用于展示的数据
    private List<Supervision> rawList;//原始数据

    public SupervisionAdapter(Context context, int textViewResourceId, List<Supervision> objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
        list = objects;
        rawList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Supervision supervision = getItem(position);
        View view;
        SupervisionAdapter.ViewHolder viewHolder;

        if(null == convertView){
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder = new SupervisionAdapter.ViewHolder();
            viewHolder.author =(TextView) view.findViewById(R.id.item_author);
            viewHolder.createDate=(TextView) view.findViewById(R.id.item_creadteDate);
            viewHolder.situation=(TextView)view.findViewById(R.id.item_situation);
            view.setTag(viewHolder);  //ViewHolder存在View中
        }else{
            view = convertView;
            viewHolder = (SupervisionAdapter.ViewHolder) view.getTag();
        }

        viewHolder.author.setText(supervision.getAuthor());
        viewHolder.createDate.setText(supervision.getCreateDate());
        viewHolder.situation.setText(supervision.SituationToString());
        if(supervision.getSituation()==0)
            viewHolder.situation.setTextColor(Color.GRAY);
        else if(supervision.getSituation()==2)
            viewHolder.situation.setTextColor(Color.RED);
        return view;
    }

    class ViewHolder{
        TextView author;
        TextView createDate;
        TextView situation;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Supervision getItem(int position) {
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
            mFilter = new SupervisionAdapter.MyFilter();
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

            List<Supervision> filterList;
            // 如果搜索框内容为空，就恢复原始数据
            if (TextUtils.isEmpty(constraint)) {
                filterList = rawList;
            } else {
                // 过滤出新数据
                filterList = new ArrayList<>();
                for (Supervision supervision : rawList) {


                    if(supervision.getAuthor().contains(constraint)||
                            supervision.getCreateDate().contains(constraint)||
                            supervision.SituationToString().contains(constraint))
                        filterList.add(supervision);
                }
            }
            results.values = filterList;
            results.count = filterList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            list = (List<Supervision>) results.values;

            if (results.count > 0) {
                notifyDataSetChanged();  // 通知数据发生了改变
            } else {
                notifyDataSetInvalidated(); // 通知数据失效
            }
        }
    }
}

package com.example.cma.adapter.manage_review;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.TextView;

import com.example.cma.R;
import com.example.cma.model.manage_review.ManageReview;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by new on 2018/7/12.
 */

public class ManageReviewAdapter extends ArrayAdapter<ManageReview> {
    private int resourceId;
    private ManageReviewAdapter.MyFilter mFilter;
    private List<ManageReview> list;   //用于展示的数据
    private List<ManageReview> rawList;//原始数据
    private View view;
    private Context context;

    public ManageReviewAdapter(Context context, int textViewResourceId, List<ManageReview> objects){
        super(context,textViewResourceId,objects);
        this.context=context;
        resourceId = textViewResourceId;
        list = objects;
        rawList = objects;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ManageReview temp = getItem(position);
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.manage_review_item,
                    null);
            holder = new ViewHolder();
            holder.year = (TextView) convertView.findViewById(R.id.item_year);
            holder.createDate=(TextView)convertView.findViewById(R.id.item_create_time);
            holder.button=(Button) convertView.findViewById(R.id.bt);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.year.setText(String.valueOf(temp.getYear())); // 显示数据
        holder.createDate.setText(temp.getDate());
        return convertView;
    }



    class ViewHolder{
        TextView year;
        TextView createDate;
        Button button;


    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public ManageReview getItem(int position) {
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
            mFilter = new ManageReviewAdapter.MyFilter();
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

            List<ManageReview> filterList;
            // 如果搜索框内容为空，就恢复原始数据
            if (TextUtils.isEmpty(constraint)) {
                filterList = rawList;
            } else {
                // 过滤出新数据
                filterList = new ArrayList<>();
                for (ManageReview staff : rawList) {
                    if(String.valueOf(staff.getYear()).contains(constraint))
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
            list = (List<ManageReview>) results.values;

            if (results.count > 0) {
                notifyDataSetChanged();  // 通知数据发生了改变
            } else {
                notifyDataSetInvalidated(); // 通知数据失效
            }
        }
    }
}



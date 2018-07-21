package com.example.cma.adapter.test_ability;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cma.R;
import com.example.cma.model.test_ability.TestAbility;
import com.example.cma.model.test_ability.TestAbilityOne;
import com.google.gson.Gson;

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
 * Created by new on 2018/7/16.
 */

public class TestAbilityOneAdapter extends ArrayAdapter<TestAbilityOne> {
    private int resourceId;
    private float downX; // 点下时候获取的x坐标
    private float upX; // 手指离开时候的x坐标
    private Button button; // 用于执行删除的button
    private Animation animation; // 删除时候的动画
    private TestAbilityOneAdapter.MyFilter mFilter;
    private List<TestAbilityOne> list;   //用于展示的数据
    private List<TestAbilityOne> rawList;//原始数据
    private Context context;
    private  View view;
    public TestAbilityOneAdapter(Context context, int textViewResourceId, List<TestAbilityOne> objects){
        super(context,textViewResourceId,objects);
        this.context=context;
        resourceId = textViewResourceId;
        list = objects;
        rawList = objects;
        animation = AnimationUtils.loadAnimation(context, R.anim.push_out);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        TestAbilityOne temp = getItem(position);
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.test_ability_one_item,
                    null);
            holder = new ViewHolder();
            holder.production = (TextView) convertView.findViewById(R.id.item_production_one);
            holder.ability=(TextView)convertView.findViewById(R.id.item_ability_one);
            holder.reference=(TextView)convertView.findViewById(R.id.item_reference_one);
            holder.button=(Button) convertView.findViewById(R.id.bt);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        convertView.setOnTouchListener(new OnTouchListener() { // 为每个item设置setOnTouchListener事件

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final ViewHolder holder = (ViewHolder) v.getTag(); // 获取滑动时候相应的ViewHolder，以便获取button按钮
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: // 手指按下
                        downX = event.getX(); // 获取手指x坐标
                        if (button != null) {
                            button.setVisibility(View.GONE); // 隐藏显示出来的button
                        }
                        break;
                    case MotionEvent.ACTION_UP: // 手指离开
                        upX = event.getX(); // 获取x坐标值
                        break;
                }

                if (holder.button != null) {
                    if (Math.abs(downX - upX) > 80 && (upX < downX)) { //向左滑动，删除item
                        holder.button.setVisibility(View.VISIBLE); // 显示删除button
                        button = holder.button; // 赋值给全局button，一会儿用
                        view = v; // 得到itemview，在上面加动画
                        return true; // 终止事件
                    }

                    if(Math.abs(downX - upX) > 80 && (upX > downX)) {//撤销删除操作
                        if(holder.button.getVisibility() == View.VISIBLE) {//此时Button可见
                            holder.button.setVisibility(View.GONE);
                        }
                        return true; // 终止事件
                    }


                    return false; // 释放事件，使onitemClick可以执行
                }
                return false;
            }

        });

        holder.button.setOnClickListener(new View.OnClickListener() { // 为button绑定事件

            @Override
            public void onClick(View v) {

                if (button != null) {

                    button.setVisibility(View.GONE); // 点击删除按钮后，影藏按钮
                    deleteItem(view, position); // 删除数据，加动画
                }

            }
        });
        holder.production.setText(temp.getProductionName()); // 显示数据
        holder.ability.setText(temp.getAbility());
        holder.reference.setText(temp.getReferrence());
        return convertView;
    }
    public void deleteItem(View view, final int position) {
        view.startAnimation(animation); // 给view设置动画
        animation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) { // 动画执行完毕
                TestAbilityOne temp = getItem(position);
                String id=String.valueOf(temp.getId());
                postDelete(id);
                list.remove(position); // 把数据源里面相应数据删除

                notifyDataSetChanged();

            }
        });

    }
    class ViewHolder{
        TextView production;
        TextView ability;
        TextView reference;
        Button button;
    }
    private void postDelete(String id) {

        OkHttpClient okHttpClient = new OkHttpClient();

        RequestBody requestBody1 = new FormBody.Builder().add("id", id).build();

        Request request = new Request.Builder()
                .url("http://119.23.38.100:8080/cma/TestAbility/deleteOneItem")//url的地址
                .post(requestBody1)
                .build();

        //同步上传
        //异步post
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {


            }


        @Override
        public void onResponse (Call call, Response response) throws IOException {


        }

        });
    }



    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public TestAbilityOne getItem(int position) {
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
            mFilter = new TestAbilityOneAdapter.MyFilter();
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

            List<TestAbilityOne> filterList;
            // 如果搜索框内容为空，就恢复原始数据
            if (TextUtils.isEmpty(constraint)) {
                filterList = rawList;
            } else {
                // 过滤出新数据
                filterList = new ArrayList<>();
                for (TestAbilityOne staff : list) {
                    if(staff.getProductionName().contains(constraint)||staff.getReferrence().contains(constraint)||staff.getAbility().contains(constraint))
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
            list = (List<TestAbilityOne>) results.values;

            if (results.count > 0) {
                notifyDataSetChanged();  // 通知数据发生了改变
            } else {
                notifyDataSetInvalidated(); // 通知数据失效
            }
        }
    }
}





package com.example.cma.adapter.test_ability;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.cma.R;
import com.example.cma.model.test_ability.TestAbility;

import java.util.List;

/**
 * Created by new on 2018/7/16.
 */

public class TestAbilityAdapter extends ArrayAdapter<TestAbility> {
    private int resourceId;
    private List<TestAbility> list;   //用于展示的数据
    private List<TestAbility> rawList;//原始数据
    private Context context;
    ProgressDialog progressDialog;
    public TestAbilityAdapter(Context context, int textViewResourceId, List<TestAbility> objects){
        super(context,textViewResourceId,objects);
        this.context=context;
        resourceId = textViewResourceId;
        list = objects;
        rawList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TestAbility temp = getItem(position);
        View view;
        TestAbilityAdapter.ViewHolder viewHolder;
         final String year=String.valueOf(temp.getYear());
        if(null == convertView){
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder = new TestAbilityAdapter.ViewHolder();
            viewHolder.year =(TextView) view.findViewById(R.id.item_year);
            //viewHolder.createDate=(TextView)view.findViewById(R.id.item_create_time);
            view.setTag(viewHolder);  //ViewHolder存在View中
        }else{
            view = convertView;
            viewHolder = (TestAbilityAdapter.ViewHolder) view.getTag();
        }
        viewHolder.year.setText(String.valueOf(temp.getYear()));
        return view;
    }

    class ViewHolder{
        TextView  year;


    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public TestAbility getItem(int position) {
        //list用于展示
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }




}



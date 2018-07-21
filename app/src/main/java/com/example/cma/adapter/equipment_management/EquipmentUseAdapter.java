package com.example.cma.adapter.equipment_management;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cma.R;
import com.example.cma.model.equipment_management.EquipmentUse;
import com.example.cma.ui.equipment_management.EquipmentUse_Info;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 王国新 on 2018/6/24.
 */

public class EquipmentUseAdapter extends RecyclerView.Adapter<EquipmentUseAdapter.ViewHolder>{
    private static final String TAG = "EquipmentUseAdapter";

    private Context mContext;
    private List<EquipmentUse> mList = new ArrayList<>();
    private List<EquipmentUse> rawList = new ArrayList<>();

    public EquipmentUseAdapter(List<EquipmentUse> EquipmentUseList) {
        mList.addAll(EquipmentUseList);
        rawList.addAll(EquipmentUseList);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        View item;
        TextView name;
        TextView testProject;
        TextView useDate;

        public ViewHolder(View view) {
            super(view);
            this.item = view;
            name =  view.findViewById(R.id.item_name);
            testProject =  view.findViewById(R.id.item_testProject);
            useDate =  view.findViewById(R.id.item_useDate);
        }
    }

    @Override
    public EquipmentUseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.equipment_use_listitem, parent, false);
        final EquipmentUseAdapter.ViewHolder holder = new EquipmentUseAdapter.ViewHolder(view);
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int)v.getTag();
                Intent intent=new Intent(mContext,EquipmentUse_Info.class);
                intent.putExtra("EquipmentUse", mList.get(position));
                mContext.startActivity(intent);
            }
        });

        return new EquipmentUseAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EquipmentUseAdapter.ViewHolder holder, int position) {
        EquipmentUse equipmentUse = mList.get(position);
        holder.name.setText(equipmentUse.getName());
        holder.testProject.setText(equipmentUse.getTestProject());
        holder.useDate.setText(equipmentUse.getUseDate());

        //需要把position传给holder
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void filter(String text) {
        mList.clear();
        if(text.isEmpty()){
            mList.addAll(rawList);
        } else{
            text = text.toLowerCase();
            for(EquipmentUse equipmentUse: rawList){
                if(equipmentUse.getName().contains(text) ||
                        equipmentUse.getTestProject().contains(text)||
                        equipmentUse.getUseDate().contains(text)){
                    mList.add(equipmentUse);
                }
            }
        }
        notifyDataSetChanged();
    }
}

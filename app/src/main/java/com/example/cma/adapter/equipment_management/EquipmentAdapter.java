package com.example.cma.adapter.equipment_management;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cma.R;
import com.example.cma.model.equipment_management.Equipment;
import com.example.cma.ui.equipment_management.Equipment_Info;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 王国新 on 2018/6/23.
 */

public class EquipmentAdapter extends RecyclerView.Adapter<EquipmentAdapter.ViewHolder>{
    private static final String TAG = "EquipmentAdapter";

    private Context mContext;
    private List<Equipment> mEquipmentList = new ArrayList<>();
    private List<Equipment> rawList = new ArrayList<>();

    public EquipmentAdapter(List<Equipment> EquipmentList) {
        mEquipmentList.addAll(EquipmentList);
        rawList.addAll(EquipmentList);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        View item;
        TextView name;
        TextView model;
        TextView equipment_number;
        TextView state;

        public ViewHolder(View view) {
            super(view);
            this.item = view;
            name = (TextView) view.findViewById(R.id.item_name);
            model = (TextView) view.findViewById(R.id.item_model);
            equipment_number = (TextView) view.findViewById(R.id.item_number);
            state = (TextView) view.findViewById(R.id.item_state);
        }
    }

    @Override
    public EquipmentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.equipment_main_listitem, parent, false);
        final EquipmentAdapter.ViewHolder holder = new EquipmentAdapter.ViewHolder(view);
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int)v.getTag();
                Intent intent=new Intent(mContext,Equipment_Info.class);
                intent.putExtra("Equipment", mEquipmentList.get(position));
                mContext.startActivity(intent);
            }
        });

        return new EquipmentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EquipmentAdapter.ViewHolder holder, int position) {
        Equipment equipment = mEquipmentList.get(position);
        holder.name.setText(equipment.getName());
        holder.model.setText(equipment.getModel());
        holder.equipment_number.setText(equipment.getEquipmentNumber());
        holder.state.setText(equipment.stateToString());
        if(equipment.getState()==1)
            holder.state.setTextColor(Color.GRAY);
        //需要把position传给holder
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mEquipmentList.size();
    }

    public void filter(String text) {
        mEquipmentList.clear();
        if(text.isEmpty()){
            mEquipmentList.addAll(rawList);
        } else{
            text = text.toLowerCase();
            for(Equipment equipment: rawList){
                if(equipment.getName().contains(text) ||
                        equipment.getModel().contains(text)||
                        equipment.getEquipmentNumber().contains(text)||
                        equipment.stateToString().contains(text)){
                    mEquipmentList.add(equipment);
                }
            }
        }
        notifyDataSetChanged();
    }
}


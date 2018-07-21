package com.example.cma.adapter.equipment_management;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cma.R;
import com.example.cma.model.equipment_management.EquipmentReceive;
import com.example.cma.ui.equipment_management.EquipmentReceive_Info;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 王国新 on 2018/6/23.
 */

public class EquipmentReceiveAdapter extends RecyclerView.Adapter<EquipmentReceiveAdapter.ViewHolder>{
    private static final String TAG = "EquipmentReceiveAdapter";

    private Context mContext;
    private List<EquipmentReceive> mList = new ArrayList<>();
    private List<EquipmentReceive> rawList = new ArrayList<>();

    public EquipmentReceiveAdapter(List<EquipmentReceive> EquipmentReceiveList) {
        mList.addAll(EquipmentReceiveList);
        rawList.addAll(EquipmentReceiveList);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        View item;
        TextView name;
        TextView model;
        TextView receiveDate;

        public ViewHolder(View view) {
            super(view);
            this.item = view;
            name = (TextView) view.findViewById(R.id.item_name);
            model = (TextView) view.findViewById(R.id.item_model);
            receiveDate = (TextView) view.findViewById(R.id.item_receiveDate);
        }
    }

    @Override
    public EquipmentReceiveAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.equipment_receive_listitem, parent, false);
        final EquipmentReceiveAdapter.ViewHolder holder = new EquipmentReceiveAdapter.ViewHolder(view);
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int)v.getTag();
                Intent intent=new Intent(mContext,EquipmentReceive_Info.class);
                intent.putExtra("EquipmentReceive", mList.get(position));
                mContext.startActivity(intent);
            }
        });

        return new EquipmentReceiveAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EquipmentReceiveAdapter.ViewHolder holder, int position) {
        EquipmentReceive equipmentReceive = mList.get(position);
        holder.name.setText(equipmentReceive.getName());
        holder.model.setText(equipmentReceive.getModel());
        holder.receiveDate.setText(equipmentReceive.getReceiveDate());

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
            for(EquipmentReceive equipmentReceive: rawList){
                if(equipmentReceive.getName().contains(text) ||
                        equipmentReceive.getModel().contains(text)||
                        equipmentReceive.getReceiveDate().contains(text)){
                    mList.add(equipmentReceive);
                }
            }
        }
        notifyDataSetChanged();
    }
}

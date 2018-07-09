package com.example.cma.adapter.equipment_management;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cma.R;
import com.example.cma.model.equipment_management.EquipmentMaintenance;
import com.example.cma.ui.equipment_management.EquipmentMaintenance_Info;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 王国新 on 2018/6/24.
 */

public class EquipmentMaintenanceAdapter extends RecyclerView.Adapter<EquipmentMaintenanceAdapter.ViewHolder> {
    private static final String TAG = "EquipmentMaintenanceAdapter";

    private Context mContext;
    private List<EquipmentMaintenance> mList = new ArrayList<>();
    private List<EquipmentMaintenance> rawList = new ArrayList<>();

    public EquipmentMaintenanceAdapter(List<EquipmentMaintenance> equipmentMaintenanceList) {
        mList.addAll(equipmentMaintenanceList);
        rawList.addAll(equipmentMaintenanceList);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View item;
        TextView name;
        TextView equipmentNumber;
        TextView maintenanceDate;

        public ViewHolder(View view) {
            super(view);
            this.item = view;
            name = (TextView) view.findViewById(R.id.item_name);
            equipmentNumber = (TextView) view.findViewById(R.id.item_equipmentNumber);
            maintenanceDate = (TextView) view.findViewById(R.id.item_maintenanceDate);
        }

    }

    @Override
    public EquipmentMaintenanceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.equipment_maintenance_listitem, parent, false);
        final EquipmentMaintenanceAdapter.ViewHolder holder = new EquipmentMaintenanceAdapter.ViewHolder(view);
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                Intent intent = new Intent(mContext, EquipmentMaintenance_Info.class);
                intent.putExtra("EquipmentMaintenance", mList.get(position));
                mContext.startActivity(intent);
            }
        });

        return new EquipmentMaintenanceAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EquipmentMaintenanceAdapter.ViewHolder holder, int position) {
        EquipmentMaintenance equipmentMaintenance = mList.get(position);
        holder.name.setText(equipmentMaintenance.getName());
        holder.equipmentNumber.setText(equipmentMaintenance.getEquipmentNumber());
        holder.maintenanceDate.setText(equipmentMaintenance.getMaintenanceDate());

        //需要把position传给holder
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void filter(String text) {
        mList.clear();
        if (text.isEmpty()) {
            mList.addAll(rawList);
        } else {
            text = text.toLowerCase();
            for (EquipmentMaintenance equipmentMaintenance : rawList) {
                if (equipmentMaintenance.getName().contains(text) ||
                        equipmentMaintenance.getEquipmentNumber().contains(text) ||
                        equipmentMaintenance.getMaintenanceDate().contains(text)) {
                    mList.add(equipmentMaintenance);
                }
            }
        }
        notifyDataSetChanged();
    }
}
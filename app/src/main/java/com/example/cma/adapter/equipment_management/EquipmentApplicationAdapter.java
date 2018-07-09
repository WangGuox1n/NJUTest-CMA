package com.example.cma.adapter.equipment_management;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cma.R;
import com.example.cma.model.equipment_management.EquipmentApplication;
import com.example.cma.ui.equipment_management.EquipmentApplication_Info;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 王国新 on 2018/6/24.
 */

public class EquipmentApplicationAdapter extends RecyclerView.Adapter<EquipmentApplicationAdapter.ViewHolder>{
    private static final String TAG = "EquipmentApplicationAdapter";

    private Context mContext;
    private List<EquipmentApplication> mList = new ArrayList<>();
    private List<EquipmentApplication> rawList = new ArrayList<>();

    public EquipmentApplicationAdapter(List<EquipmentApplication> EquipmentApplicationList) {
        mList.addAll(EquipmentApplicationList);
        rawList.addAll(EquipmentApplicationList);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        View item;
        TextView applicant;
        TextView equipmentNumber;
        TextView applicationDate;

        public ViewHolder(View view) {
            super(view);
            this.item = view;
            applicant = (TextView) view.findViewById(R.id.item_applicant);
            equipmentNumber = (TextView) view.findViewById(R.id.item_equipmentNumber);
            applicationDate = (TextView) view.findViewById(R.id.item_applicationDate);
        }
    }

    @Override
    public EquipmentApplicationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.equipment_application_listitem, parent, false);
        final EquipmentApplicationAdapter.ViewHolder holder = new EquipmentApplicationAdapter.ViewHolder(view);
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int)v.getTag();
                Intent intent=new Intent(mContext,EquipmentApplication_Info.class);
                intent.putExtra("EquipmentApplication", mList.get(position));
                mContext.startActivity(intent);
            }
        });

        return new EquipmentApplicationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EquipmentApplicationAdapter.ViewHolder holder, int position) {
        EquipmentApplication equipmentApplication = mList.get(position);
        holder.applicant.setText(equipmentApplication.getApplicant());
        holder.equipmentNumber.setText(equipmentApplication.getEquipmentNumber());
        holder.applicationDate.setText(equipmentApplication.getApplicationDate());

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
            for(EquipmentApplication equipmentApplication: rawList){
                if(equipmentApplication.getApplicant().contains(text) ||
                        equipmentApplication.getEquipmentNumber().contains(text)||
                        equipmentApplication.getApplicationDate().contains(text)){
                    mList.add(equipmentApplication);
                }
            }
        }
        notifyDataSetChanged();
    }
}

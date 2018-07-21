package com.example.cma.adapter.supervision;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cma.R;
import com.example.cma.model.supervision.Supervision;
import com.example.cma.model.supervision.SupervisionPlan;
import com.example.cma.ui.supervision.SupervisionPlan_Info;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 王国新 on 2018/6/10.
 */

public class SupervisionPlanAdapter extends RecyclerView.Adapter<SupervisionPlanAdapter.ViewHolder> {

    private static final String TAG = "SupervisionPlanAdapter";

    private Context mContext;
    private Supervision supervision;
    private List<SupervisionPlan> mSupervisionPlanList = new ArrayList<>();
    private List<SupervisionPlan> rawList = new ArrayList<>();

    public SupervisionPlanAdapter(List<SupervisionPlan> SupervisionPlanList, Supervision supervision) {
        mSupervisionPlanList.addAll(SupervisionPlanList);
        rawList.addAll(SupervisionPlanList);
        this.supervision = supervision;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View item;
        TextView content;
        TextView object;
        TextView dateFrequency;

        public ViewHolder(View view) {
            super(view);
            this.item = view;
            content = (TextView) view.findViewById(R.id.item_content);
            object = (TextView) view.findViewById(R.id.item_object);
            dateFrequency = (TextView) view.findViewById(R.id.item_dateFrequency);
        }

    }

    @Override
    public SupervisionPlanAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.supervision_plan_main_listitem, parent, false);
        final SupervisionPlanAdapter.ViewHolder holder = new SupervisionPlanAdapter.ViewHolder(view);
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                Intent intent = new Intent(mContext, SupervisionPlan_Info.class);
                intent.putExtra("Supervision", supervision);
                intent.putExtra("SupervisionPlan", mSupervisionPlanList.get(position));
                mContext.startActivity(intent);
            }
        });

        return new SupervisionPlanAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SupervisionPlanAdapter.ViewHolder holder, int position) {
        SupervisionPlan supervisionPlan = mSupervisionPlanList.get(position);
        holder.content.setText(supervisionPlan.getContent());
        holder.object.setText(supervisionPlan.getObject());
        holder.dateFrequency.setText(supervisionPlan.getDateFrequency());
        //需要把position传给holder
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mSupervisionPlanList.size();
    }

    public void filter(String text) {
        mSupervisionPlanList.clear();
        if (text.isEmpty()) {
            mSupervisionPlanList.addAll(rawList);
        } else {
            text = text.toLowerCase();
            Log.d(TAG, text);
            for (SupervisionPlan item : rawList) {
                if (item.getContent().contains(text) ||
                        item.getObject().contains(text) ||
                        item.getDateFrequency().contains(text)) {
                    mSupervisionPlanList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }
}

package com.example.cma.adapter.supervision;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cma.R;
import com.example.cma.model.supervision.Supervision;
import com.example.cma.ui.supervision.Supervision_Info;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 王国新 on 2018/6/9.
 */

public class SupervisionAdapter extends RecyclerView.Adapter<SupervisionAdapter.ViewHolder>{
    private static final String TAG = "SupervisionAdapter";

    private Context mContext;
    private List<Supervision> mSupervisionList = new ArrayList<>();
    private List<Supervision> rawList = new ArrayList<>();

    public SupervisionAdapter(List<Supervision> SupervisionList) {
        mSupervisionList.addAll(SupervisionList);
        rawList.addAll(SupervisionList);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        View item;
        TextView author;
        TextView createDate;
        TextView situation;

        public ViewHolder(View view) {
            super(view);
            this.item = view;
            author = (TextView) view.findViewById(R.id.item_author);
            createDate = (TextView) view.findViewById(R.id.item_createDate);
            situation = (TextView) view.findViewById(R.id.item_situation);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.supervision_main_listitem, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int)v.getTag();
                Intent intent=new Intent(mContext,Supervision_Info.class);
                intent.putExtra("Supervision", mSupervisionList.get(position));
                mContext.startActivity(intent);
            }
        });

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Supervision supervision = mSupervisionList.get(position);
        holder.author.setText(supervision.getAuthor());
        holder.createDate.setText(supervision.getCreateDate());
        holder.situation.setText(supervision.SituationToString());
        if(supervision.getSituation()==0)
            holder.situation.setTextColor(Color.GRAY);
        /*else if(supervision.getSituation()==2)
            holder.situation.setTextColor(Color.MAGENTA);*/
        //需要把position传给holder
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mSupervisionList.size();
    }

    public void filter(String text) {
        mSupervisionList.clear();
        if(text.isEmpty()){
            mSupervisionList.addAll(rawList);
        } else{
            text = text.toLowerCase();
            Log.d(TAG,text);
            for(Supervision item: rawList){
                Log.d(TAG,item.getAuthor() +" " +item.getCreateDate());
                if(item.getAuthor().contains(text) ||
                        item.getCreateDate().contains(text)||
                        item.SituationToString().contains(text)){
                    mSupervisionList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }
}

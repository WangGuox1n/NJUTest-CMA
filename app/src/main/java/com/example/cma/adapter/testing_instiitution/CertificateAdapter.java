package com.example.cma.adapter.testing_instiitution;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cma.R;
import com.example.cma.model.testing_institution.Certificate;

import java.util.List;

public class CertificateAdapter extends ArrayAdapter<Certificate> {
    private int resourceId;
    private List<Certificate> list;
    private List<Certificate> listdata;//为list整个数据做备份

    //上下文，listview子项布局id,数据
    public CertificateAdapter(Context context, int textViewResourceId, List<Certificate> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
        list=objects;
        listdata=objects;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Certificate getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Certificate certificate= getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        TextView filename=(TextView)view.findViewById(R.id.file_name);
        filename.setText(certificate.getFileName());
        ImageView imageView=(ImageView)view.findViewById(R.id.file_image);
        imageView.setImageResource(R.drawable.file);
        return view;
    }
}

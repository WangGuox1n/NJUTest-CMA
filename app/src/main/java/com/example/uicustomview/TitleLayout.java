package com.example.uicustomview;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by 王国新 on 2018/5/9.
 */

public class TitleLayout extends LinearLayout{
    public TitleLayout(Context context, AttributeSet arrts){
        super(context,arrts);
        LayoutInflater.from(context).inflate(R.layout.title,this);
        Button titleBack = (Button)findViewById(R.id.title_back);
        Button titleEdit = (Button)findViewById(R.id.title_save);
        titleBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity)getContext()).finish();
            }
        });

        titleEdit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}

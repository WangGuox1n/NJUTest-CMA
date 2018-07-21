package com.example.cma.utils;

/**
 * Created by admin on 2018/5/31.
 */

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;

import com.example.cma.R;

public class ModelPopup extends PopupWindow implements android.view.View.OnClickListener {

    private OnPopListener listener;
    private View pop;

    /**
     *
     * @param context
     * @param listener 接口回调
     * @param isShowMd 可以控制按钮数量
     */
    public ModelPopup(Context context, OnPopListener listener, boolean isShowMd) {
        super(context);
        this.listener = listener;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        pop = inflater.inflate(R.layout.pop_menu, null);
        this.setContentView(pop);
        this.setWidth(LayoutParams.MATCH_PARENT);
        this.setHeight(LayoutParams.WRAP_CONTENT);
        Button btn_1 = (Button) pop.findViewById(R.id.btn_1);
        Button btn_2 = (Button) pop.findViewById(R.id.btn_2);
        Button btn_3 = (Button) pop.findViewById(R.id.btn_3);
        if (!isShowMd) {
            btn_3.setVisibility(View.GONE);
        }
        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);
        btn_3.setOnClickListener(this);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 点击外面的控件也可以使得PopUpWindow dismiss
        this.setOutsideTouchable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.PopupAnimation);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);

        this.setBackgroundDrawable(dw);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn_1:
                listener.onBtn1();
                break;
            case R.id.btn_2:
                listener.onBtn2();
                break;
            case R.id.btn_3:
                listener.onBtn3();
                break;
        }
        dismiss();
    }
}


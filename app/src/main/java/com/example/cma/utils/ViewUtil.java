package com.example.cma.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.cma.ui.staff_management.StaffLeaving_Add;

import org.feezu.liuli.timeselector.TimeSelector;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by 王国新 on 2018/6/3.
 *
 * 对某些View的操作，可复用的代码
 */

public class ViewUtil {
    private AppCompatActivity mActivty;
    private Context mContext;

    private static ViewUtil viewUtil = null;

    private ViewUtil(){}

    public static ViewUtil getInstance(){
        if(null == viewUtil)
            viewUtil = new ViewUtil();
        return viewUtil;
    }

    //设置光标可见
    public static void ShowCursor(final EditText editText){
        editText.setOnTouchListener(new View.OnTouchListener() {
            int touch_flag=0;
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                touch_flag++;
                if(touch_flag==2){
                    touch_flag=0;
                    editText.setCursorVisible(true);
                }
                return false;
            }
        });
    }

    //选择日期，context为当前Activity, TextView用于显示日期
    public void selectDate(Context context,final TextView date_text){
        this.mContext = context;
        this.mActivty = (AppCompatActivity)mContext;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        final String now = simpleDateFormat.format(new Date());
        TimeSelector timeSelector = new TimeSelector(mContext, new TimeSelector.ResultHandler() {
            @Override
            public void handle(String time) {
                date_text.setText(time.split(" ")[0]);
            }
        }, "2000-01-01 00:00", now);
        timeSelector.setIsLoop(false);//设置不循环,true循环
        timeSelector.setTitle("请选择日期");
        timeSelector.setMode(TimeSelector.MODE.YMD);//只显示年月日
        timeSelector.show();
    }

    /**
     * 得到资源文件中图片的Uri
     * @param context 上下文对象
     * @param id 资源id
     * @return Uri
     */

    public Uri getUriFromDrawableRes(Context context, int id) {
        Resources resources = context.getResources();
        String path = ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + resources.getResourcePackageName(id) + "/"
                + resources.getResourceTypeName(id) + "/"
                + resources.getResourceEntryName(id);
        return Uri.parse(path);
    }
}

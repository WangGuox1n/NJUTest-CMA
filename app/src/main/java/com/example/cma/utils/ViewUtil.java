package com.example.cma.utils;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.cma.R;

import org.feezu.liuli.timeselector.TimeSelector;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by 王国新 on 2018/6/3.
 * <p>
 * 对某些View的操作，可复用的代码
 */

public class ViewUtil {
    private AppCompatActivity mActivity;
    private Context mContext;

    private static ViewUtil viewUtil = null;

    private ViewUtil() {
    }

    public static ViewUtil getInstance() {
        if (null == viewUtil)
            viewUtil = new ViewUtil();
        return viewUtil;
    }

    /**
     * 设置EditText的光标可见
     *
     * @param editText EditText组件
     */
    public static void ShowCursor(final EditText editText) {
        editText.setOnTouchListener(new View.OnTouchListener() {
            int touch_flag = 0;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                touch_flag++;
                if (touch_flag == 2) {
                    touch_flag = 0;
                    editText.setCursorVisible(true);
                }
                return false;
            }
        });
    }

    /**
     * 选择日期
     *
     * @param context   上下文对象
     * @param date_text 用于显示日期的 TextView
     */
    public void selectDate(Context context, final TextView date_text) {
        this.mContext = context;
        this.mActivity = (AppCompatActivity) mContext;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        final String now = simpleDateFormat.format(new Date());
        TimeSelector timeSelector = new TimeSelector(mContext, new TimeSelector.ResultHandler() {
            @Override
            public void handle(String time) {
                date_text.setText(time.split(" ")[0]);
            }
        }, "2000-01-01 00:00", now);
        timeSelector.setIsLoop(true);//设置不循环,true循环
        timeSelector.setTitle("请选择日期");
        timeSelector.setMode(TimeSelector.MODE.YMD);//只显示年月日
        timeSelector.show();
    }

    public void selectTime(Context context, final TextView time_text) {
        new TimePickerDialog(context, AlertDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String h, m;
                if (hourOfDay < 10)
                    h = "0" + hourOfDay;
                else
                    h = "" + hourOfDay;
                if (minute < 10)
                    m = "0" + minute;
                else
                    m = "" + minute;

                String time = h + ":" + m + ":00";
                time_text.setText(time);
            }
        }, 0, 0, true).show();
    }

    /**
     * 设置自定义的Toolbar,并加上返回按钮
     *
     * @param context 上下文对象
     * @param toolbar 所要显示的toolbar
     */
    public void setSupportActionBar(final Context context, final Toolbar toolbar) {
        this.mContext = context;
        this.mActivity = (AppCompatActivity) mContext;
        mActivity.setSupportActionBar(toolbar);
        ActionBar actionBar = mActivity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mActivity.setSupportActionBar(toolbar);
    }

    public void showPostLoadingDialog(final Context context){
        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
        final View dialogView = LayoutInflater.from(context)
                .inflate(R.layout.post_file_loading, null);
        builder.setTitle("记录上传中");
        builder.setView(dialogView);
        builder.setCancelable(false);
        final android.support.v7.app.AlertDialog editDialog = builder.create();
        editDialog.show();
    }

    /**
     * 得到资源文件中图片的Uri
     *
     * @param context 上下文对象
     * @param id      资源id
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

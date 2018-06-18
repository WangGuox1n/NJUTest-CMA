package com.example.cma.utils;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by 王国新 on 2018/6/3.
 */

public class ToastUtil {
    public static void showShort(final Context context,final String message){
        AppCompatActivity mActivty = (AppCompatActivity)context;
        mActivty.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
            }
        });
        //Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }

    public static void showLong(final Context context, final String message) {
        AppCompatActivity mActivty = (AppCompatActivity)context;
        mActivty.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context,message,Toast.LENGTH_LONG).show();
            }
        });
        //Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}

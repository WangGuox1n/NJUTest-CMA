package com.example.cma.utils;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by 王国新 on 2018/5/29.
 */

public class HttpUtil {
    //get
    public static void sendOkHttpRequest(String address,okhttp3.Callback callback){
        /*if(!isNetworkAvailable()){

        }*/

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);
    }

    //post
    public static void sendOkHttpWithRequestBody(String address, RequestBody requestBody, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    private static boolean isNetworkAvailable(){
        return false;
    }

    //post
    public static void sendOkHttpWithMultipartBody(String address, MultipartBody requestBody, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }
}
package com.example.cma.utils;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by 王国新 on 2018/5/29.
 */

public class HttpUtil {

    /**
     * 向服务器发送 GET 请求
     *
     * @param address 地址
     * @param callback 回调函数
     * */
    public static void sendOkHttpRequest(String address,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);
    }

    /**
     * 向服务器发送 POST请求
     *
     * @param address 地址
     * @param requestBody 上传的内容
     * @param callback 回调函数
     * */
    public static void sendOkHttpWithRequestBody(String address, RequestBody requestBody, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    /**
     * 向服务器发送 POST请求
     *
     * @param address 地址
     * @param requestBody 上传的内容，形式为Multipart
     * @param callback 回调函数
     * */
    public static void sendOkHttpWithMultipartBody(String address, MultipartBody requestBody, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }
}
package com.example.android.camera.utils;

import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtil {

    public static final String TAG = "http";
    public static final String HOST = "http://192.168.1.105:81/api/";

    /**
     * bitmap转为base64
     * @param bitmap
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap) {
        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
                result = result.replaceAll("\n","");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static void faceCheck(){
        String url = HOST + "faceCheck";
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
//                .post(RequestBody.create())//默认就是GET请求，可以不写
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: ");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, "onResponse: " + response.body().string());
            }
        });
    }

    public static Call doPostForm(String url, Map<String,String> params){
//        String url = HOST + "faceCheck";
        FormBody.Builder formBody = new FormBody.Builder();
        if(params!=null){
            Set<Map.Entry<String, String>> entrySet = params.entrySet();
            for(Map.Entry<String, String> entry:entrySet) {
                formBody.add(entry.getKey(),entry.getValue());
            }
        }
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = formBody.build();
        final Request request = new Request.Builder()
                .url(url)
                .post(requestBody)//默认就是GET请求，可以不写
                .build();
        Call call = okHttpClient.newCall(request);
        return call;
    }

    public static void main(String[] args) {
        Call call = doPostForm("",null);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: ");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, "onResponse: " + response.body().string());
            }
        });
    }

}

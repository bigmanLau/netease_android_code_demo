package com.example.netmanager;

import android.os.Handler;
import android.os.Looper;

import com.alibaba.fastjson.JSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class JsonCallBackListener<T> implements CallbackListener {

    public Class<T> responsClass;
    private IJsonDataListener iJsonDataListener;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    public JsonCallBackListener(Class<T> responsClass, IJsonDataListener jsonDataListener) {
        this.responsClass = responsClass;
        this.iJsonDataListener = jsonDataListener;
    }

    @Override
    public void onSuccess(InputStream inputStream) {
        String response = getContent(inputStream);
        final T clazz = JSON.parseObject(response, responsClass);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                iJsonDataListener.onSuccess(clazz);
            }
        });
    }

    private String getContent(InputStream inputStream) {
        String content = null;
        try {


            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
                System.out.println("Error=" + e.toString());
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    System.out.println("Error=" + e.toString());
                }
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }

    @Override
    public void onFailure() {

    }
}

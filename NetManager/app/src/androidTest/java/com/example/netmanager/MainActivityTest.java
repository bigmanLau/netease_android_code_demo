package com.example.netmanager;

import android.util.Log;

import org.junit.Test;

import static org.junit.Assert.*;

public class MainActivityTest {

    @Test
    public void sendRequest() {
        String url="http://v.juhe.cn/historyWeather/citys?province_id=2&key=bb52107206585ab074f5e59a8c73875b";
        NeHttp.sendJsonRequest(url, null, ResponseClass.class, new IJsonDataListener<ResponseClass>() {
            @Override
            public void onSuccess(ResponseClass clazz) {
                Log.e("==========",clazz.toString());
            }
        });
    }
}
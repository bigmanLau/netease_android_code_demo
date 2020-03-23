package com.example.netmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
   private String url="http://v.juhe.cn/historyWeather/citys?province_id=2&key=bb52107206585ab074f5e59a8c73875b";
   private String url2="http://v.321.cn/3414231/citys?province_id=2&key=bb52107206585ab074f5e59a8c73875b";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         sendRequest();
    }

    public void sendRequest(){
        NeHttp.sendJsonRequest(url2, null, ResponseClass.class, new IJsonDataListener<ResponseClass>() {
            @Override
            public void onSuccess(ResponseClass clazz) {
                Log.e("==========",clazz.toString());
            }
        });
    }


}

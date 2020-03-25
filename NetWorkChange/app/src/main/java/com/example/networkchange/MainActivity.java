package com.example.networkchange;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NetWorkManager.getDefault().register(this);
    }

    @Network(netType = NetType.AUTO)
    public void network(NetType netType) {
        switch (netType) {
            case WIFI:
                Log.e("bigman", "wifi");

                break;
            case CMNET:
            case CMWAP:
                Log.e("bigman", netType.name());
                break;
            case NONE:
                Log.e("bigman", "没有网络");
                break;
        }
    }
}

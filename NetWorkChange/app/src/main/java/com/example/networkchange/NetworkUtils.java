package com.example.networkchange;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class NetworkUtils {

    private NetworkUtils() {
        throw new IllegalStateException("you can't instantiate me!");
    }


    /**
     * 获取当前网络类型
     *
     * @return 0：没有网络 1：WIFI网络 2：WAP网络 3：NET网络
     */
    @SuppressLint("MissingPermission")
    public static NetType getNetworkType(Context context) {
        NetType netType = NetType.NONE;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            String extraInfo = networkInfo.getExtraInfo();
            if (extraInfo != null && !extraInfo.isEmpty()) {
                if (extraInfo.equalsIgnoreCase("cmnet")) {
//                    netType = NetType.CMNET;
                } else {
                    netType = NetType.CMWAP;
                }
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = NetType.WIFI;
        }
        return netType;
    }

    @SuppressLint("MissingPermission")
    public static boolean netIsConnected(Context context) {
        ConnectivityManager connectMgr = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        //手机网络连接状态
        NetworkInfo mobNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        //WIFI连接状态
        NetworkInfo wifiNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if ((mobNetInfo==null||!mobNetInfo.isConnected()) && (wifiNetInfo==null||!wifiNetInfo.isConnected())) {
            //当前无可用的网络
            return false;
        }
        return true;
    }

    @SuppressLint("MissingPermission")
    public static boolean isWifiOpen(Context context) {
        ConnectivityManager connectMgr = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        //手机网络连接状态
        NetworkInfo mobNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        //WIFI连接状态
        NetworkInfo wifiNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if ( (wifiNetInfo==null||!wifiNetInfo.isConnected())) {
            //当前无可用的网络
            return false;
        }
        return true;
    }
}




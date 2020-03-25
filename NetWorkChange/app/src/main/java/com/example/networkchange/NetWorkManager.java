package com.example.networkchange;

import android.app.Application;
import android.content.IntentFilter;

public class NetWorkManager {
    private static volatile NetWorkManager instance;
    private NetStateReceiver receiver;
    private Application application;

    public static NetWorkManager getDefault() {
        if(instance==null){
            synchronized (NetWorkManager.class){
                if (instance==null){
                    instance=new NetWorkManager();
                }
            }
        }
        return instance;
    }

    private NetWorkManager(){
         receiver = new NetStateReceiver();
    }

    public Application getApplication(){
        if (application==null){
            throw new RuntimeException("....");
        }
        return  application;
    }

    public void init(Application application){
        this.application=application;
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        application.registerReceiver(receiver,intentFilter);
    }

    public void register(Object object) {
        receiver.register(object);
    }
}

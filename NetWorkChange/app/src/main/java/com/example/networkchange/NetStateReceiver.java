package com.example.networkchange;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NetStateReceiver extends BroadcastReceiver {
    private NetType netType;
    private Map<Object, List<MethodManager>> map;

    public NetStateReceiver() {
        netType = NetType.NONE;
        map = new HashMap<>();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null||intent.getAction()==null) {
            Log.e("bigman","异常....");
            return;
        }
        if (intent.getAction().equalsIgnoreCase("android.net.conn.CONNECTIVITY_CHANGE")){
            Log.e("bigman","网络发生了变化....");
            Application application = NetWorkManager.getDefault().getApplication();
            netType=NetworkUtils.getNetworkType(application);
            if(NetworkUtils.netIsConnected(application)){
                Log.e("bigman","网络连接成功....");
            }else{
                Log.e("bigman","网络连接失败....");
            }
            post(netType);
        }
    }

    public  void post(NetType netType){
       if (map.isEmpty())return;
        Set<Object> set = map.keySet();
        for (Object getter : set) {
            List<MethodManager> methodManagerList = map.get(getter);
            if (methodManagerList!=null){
                for (MethodManager methodManager : methodManagerList) {
                    if (methodManager.getType().isAssignableFrom(netType.getClass())) {
                       switch (methodManager.getNetType()){
                           case AUTO:
                               invoke(methodManager,getter,netType);
                               break;
                           case WIFI:
                               if (netType==NetType.WIFI||netType==netType.NONE){
                                   invoke(methodManager,getter,netType);
                               }
                               break;
                           case CMNET:
                           case CMWAP:
                               if (netType==NetType.CMNET||netType==NetType.CMWAP||netType==netType.NONE){
                                   invoke(methodManager,getter,netType);
                               }
                               break;

                       }
                    }
                }
            }
        }
    }

    private void invoke(MethodManager methodManager, Object getter, NetType netType) {
        try {
            methodManager.getMethod().invoke(getter,netType);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void register(Object object) {
        List<MethodManager> methodManagerList = map
                .get(object);
        if (methodManagerList == null) {
            methodManagerList = findAnnotationMethod(object);
            map.put(object, methodManagerList);
        }
    }

    private List<MethodManager> findAnnotationMethod(Object object) {
        List<MethodManager> list = new ArrayList<>();
        Class<?> aClass = object.getClass();
        Method[] methods = aClass.getMethods();
        for (Method method : methods) {
            Network annotation = method.getAnnotation(Network.class);
            if (annotation == null) {
                continue;
            }
            //获取方法返回类型
//            method.getGenericReturnType();

            //获取方法的参数
            Class<?>[] parameterTypes = method.getParameterTypes();

            if (parameterTypes.length != 1) {
                throw new RuntimeException("参数只能有一个");
            }
            MethodManager methodManager = new MethodManager(parameterTypes[0],annotation.netType(),method);
            list.add(methodManager);

        }
        return list;
    }
}

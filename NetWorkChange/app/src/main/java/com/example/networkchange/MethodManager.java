package com.example.networkchange;

import java.lang.reflect.Method;

class MethodManager {
    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    private Class<?> type;//参数类型

    public NetType getNetType() {
        return netType;
    }

    public void setNetType(NetType netType) {
        this.netType = netType;
    }

    private NetType netType;//网络类型
    private Method method;
    public  MethodManager(Class<?> type,NetType netType,Method method){
        this.type=type;
        this.netType=netType;
        this.method=method;
//        method.invoke();
    }
}

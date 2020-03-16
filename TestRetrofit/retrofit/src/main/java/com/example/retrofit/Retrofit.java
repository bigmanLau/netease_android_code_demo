package com.example.retrofit;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

public class Retrofit {
    private HttpUrl baseUrl;

    private Call.Factory callFactory;
    private  final Map<Method,ServiceMethod> serviceMethodCache=new HashMap<>();

    public HttpUrl baseUrl() {
        return baseUrl;
    }

    public Call.Factory callFactory() {
        return callFactory;
    }

    private Retrofit(Builder builder) {
        this.baseUrl = builder.baseUrl;
        this.callFactory = builder.callFactory;
    }
    public <T> T create(Class<T> service){
        return (T) Proxy.newProxyInstance(service.getClassLoader(), new Class[]{service}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                ServiceMethod serviceMethod=loadServiceMethod(method);
                return new OkHttpCall(serviceMethod,args);
            }
        });
    }

    private ServiceMethod loadServiceMethod(Method method) {
        //懒汉
        ServiceMethod serviceMethod = serviceMethodCache.get(method);
        if(serviceMethod!=null)return serviceMethod;
        //线程安全锁
        synchronized (serviceMethodCache){
            serviceMethod=serviceMethodCache.get(method);
            if(serviceMethod==null){
                serviceMethod=new ServiceMethod.Builder(this,method).build();
                serviceMethodCache.put(method,serviceMethod);
            }
        }
        return serviceMethod;
    }

    public static class Builder {
        private HttpUrl baseUrl;
        private Call.Factory callFactory;

        public Builder baseUrl(String baseUrl) {
            if (baseUrl.isEmpty()) {
                throw new NullPointerException("baseUrl==null");
            }
            this.baseUrl = HttpUrl.parse(baseUrl);
            return this;
        }

        public Builder baseUrl(HttpUrl baseUrl) {
            if (baseUrl == null) {
                throw new NullPointerException("baseUrl==null");
            }
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder callFactory(Call.Factory callFactory) {
            this.callFactory = callFactory;
            return this;
        }

        public Retrofit build() {
            if (this.baseUrl == null) {
                throw new IllegalStateException("BaseUrl required");
            }
            if (this.callFactory == null) {
                callFactory = new OkHttpClient();
            }
            return new Retrofit(this);
        }

    }
}

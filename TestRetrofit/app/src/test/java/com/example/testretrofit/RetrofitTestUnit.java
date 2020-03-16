package com.example.testretrofit;

import com.example.retrofit.GET;
import com.example.retrofit.Query;
import com.example.retrofit.Retrofit;

import org.junit.Test;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.security.Key;
import java.util.Arrays;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class RetrofitTestUnit {
    private final static String IP="144.34.161.97";
    private final static String KEY="aa205eeb45aa76c6afe3c52151b52160";
    private final static String BASE_URl="http://apis.juhe.cn/";
    interface  HOST{
        @GET("/ip/ipNew")
        Call get(@Query("ip")String ip, @Query("key")String key);
    }

    @Test
    public void testRetrofit(){
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(BASE_URl)
                .build();
        HOST host=retrofit.create(HOST.class);
        Call call=host.get(IP,KEY);
        try {
            Response response = call.execute();
            if (response!=null&&response.body()!=null){
                System.out.println(response.body().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void proxy(){
        HOST host = (HOST) Proxy.newProxyInstance(HOST.class.getClassLoader(), new Class[]{HOST.class}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println("方法名称" + method.getName());
                GET get = method.getAnnotation(GET.class);
                System.out.println("获取到Get注解值" + get.value());
                Annotation[][] parameterAnnotations = method.getParameterAnnotations();
                for (Annotation[] parameterAnnotation : parameterAnnotations) {
                    System.out.println("获取方法参数的 注解" + Arrays.toString(parameterAnnotation));
                }
                System.out.println("获取方法 的参数值" + Arrays.toString(args));
                return null;
            }
        });
        host.get(IP, KEY);
    }
}

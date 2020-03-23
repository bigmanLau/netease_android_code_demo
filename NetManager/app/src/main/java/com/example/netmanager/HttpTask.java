package com.example.netmanager;

import com.alibaba.fastjson.JSON;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class HttpTask<T> implements Runnable, Delayed {
    private IHttpRequest httpRequest;

    public HttpTask(String url, T requestData, IHttpRequest request, CallbackListener listener) {
        this.httpRequest = request;
        request.setUrl(url);
        request.setListener(listener);
        String content = JSON.toJSONString(requestData);
        try {
            request.setData(content.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            httpRequest.execute();
        } catch (Exception e) {
            ThreadPoolManager.getInstance().addDelayTask(this);
        }
    }

    public long getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(long delayTime) {
        this.delayTime = System.currentTimeMillis() + delayTime;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    private long delayTime;
    private int retryCount;

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(this.delayTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        return 0;
    }
}

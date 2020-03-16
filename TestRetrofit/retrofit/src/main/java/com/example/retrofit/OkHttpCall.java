package com.example.retrofit;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import okio.Timeout;

public class OkHttpCall implements Call {
    private ServiceMethod serviceMethod;
    private Object[] args;
    private okhttp3.Call rawCall;
    public  OkHttpCall(ServiceMethod serviceMethod,Object[] args){
        this.serviceMethod=serviceMethod;
        this.args=args;
        this.rawCall=serviceMethod.toCall(args);
    }
    @Override
    public Request request() {
        return rawCall.request();
    }

    @Override
    public Response execute() throws IOException {
        return rawCall.execute();

    }

    @Override
    public void enqueue(Callback responseCallback) {
rawCall.enqueue(responseCallback);
    }

    @Override
    public void cancel() {
        rawCall.cancel();
    }

    @Override
    public boolean isExecuted() {
        return rawCall.isExecuted();
    }

    @Override
    public boolean isCanceled() {
        return rawCall.isCanceled();
    }

    @Override
    public Timeout timeout() {
        return rawCall.timeout();
    }

    @Override
    public Call clone() {
        return new OkHttpCall(serviceMethod,args);
    }
}

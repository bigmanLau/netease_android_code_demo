package com.example.netmanager;

import java.net.MalformedURLException;

public interface IHttpRequest {
    void setUrl(String url);
    void setData(byte[] data);
    void setListener(CallbackListener listener);
    void execute() throws MalformedURLException;
}

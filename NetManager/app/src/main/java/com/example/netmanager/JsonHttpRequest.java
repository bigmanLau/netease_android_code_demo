package com.example.netmanager;

import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class JsonHttpRequest implements IHttpRequest {
    private String url;
    private byte[] data;
    private CallbackListener callbackListener;
    private HttpURLConnection urlConn;

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public void setListener(CallbackListener listener) {
        this.callbackListener = listener;
    }

    @Override
    public void execute() throws MalformedURLException {
        //访问网络
        URL url = null;
        try {
            url = new URL(this.url); // 打开一个HttpURLConnection连接
            urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setUseCaches(false);
            urlConn.setInstanceFollowRedirects(true);
            urlConn.setReadTimeout(3000);
            urlConn.setDoInput(true);
            urlConn.setDoOutput(true);
            urlConn.setRequestMethod("POST");
            urlConn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            urlConn.connect();

            OutputStream out = urlConn.getOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(out);
            bos.write(data);
            bos.flush();
            out.close();
            bos.close();
            if (urlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in = urlConn.getInputStream();
                callbackListener.onSuccess(in);
            } else {
                throw new RuntimeException("请求失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("请求失败");
        } finally {

            urlConn.disconnect();

        }
    }
}

package com.example.netmanager;

public class NeHttp{
    public static<T,M> void sendJsonRequest(String url,T requestData,Class<M> reponse,IJsonDataListener listener){
      IHttpRequest request=new JsonHttpRequest();
      CallbackListener callbackListener=new JsonCallBackListener<>(reponse,listener);
      HttpTask ht=new HttpTask(url,requestData,request,callbackListener);
       ThreadPoolManager.getInstance().addTask(ht);
    }
}

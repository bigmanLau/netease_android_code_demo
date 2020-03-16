package com.example.retrofit;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.RequestBody;

class RequestBuilder {
    private final String  method;
    private final HttpUrl baseUrl;
    private String relativeUrl;
    private HttpUrl.Builder urlBuilder;
    private FormBody.Builder formBuilder;
    private final Request.Builder requestBuilder;
    public RequestBuilder(String httpMethod, HttpUrl baseUrl, String relativeUrl, boolean hasBody) {
        this.method=httpMethod;
        this.baseUrl=baseUrl;
        this.relativeUrl=relativeUrl;
        requestBuilder=new Request.Builder();
        if(hasBody)formBuilder=new FormBody.Builder();
    }

     void addQueryParam(String name, String value) {
        if (relativeUrl!=null){
            urlBuilder=baseUrl.newBuilder(relativeUrl);
            relativeUrl=null;
        }
        urlBuilder.addQueryParameter(name,value);
    }

     void addFormField(String name, String value) {
        formBuilder.add(name,value);
    }

     Request build() {
         HttpUrl url;
         if(urlBuilder!=null){
             url=urlBuilder.build();
         }else{
             url=baseUrl.resolve(relativeUrl);
             if (url==null){
                 throw new  IllegalArgumentException("Malformed URL.Base:"+baseUrl+",Relative:"+relativeUrl);
             }
         }
         RequestBody body=null;
         if (formBuilder!=null){
             body=formBuilder.build();
         }
         return requestBuilder.url(url)
                 .method(method,body)
                 .build();
     }
}

package com.example.retrofit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import okhttp3.Call;
import okhttp3.HttpUrl;

public class ServiceMethod {
    private final Call.Factory callFactory;
    private final HttpUrl baseUrl;
    private final String httpMethod;
    private final String relativeUrl;
    private final ParameterHandler[] parameterHandlers;
    private final boolean hasBody;
    public ServiceMethod(Builder builder) {
        this.callFactory=builder.retrofit.callFactory();
        this.baseUrl=builder.retrofit.baseUrl();
        this.httpMethod=builder.httpMethod;
        this.relativeUrl=builder.relativeUrl;
        this.parameterHandlers=builder.parameterHandlers;
        this.hasBody=builder.hasBody;
    }
    okhttp3.Call toCall(Object... args){
        RequestBuilder requestBuilder=new RequestBuilder(httpMethod,baseUrl,relativeUrl,hasBody);
        ParameterHandler[] handlers=this.parameterHandlers;
      int argumentCount=args!=null?args.length:0;
      if (argumentCount!=handlers.length){
          //方法真实的参数个数不等于收集的参数个数
          throw new IllegalArgumentException("");
      }
      ///循环凭借每个参数名加参数值
        for (int i = 0; i < argumentCount; i++) {
            handlers[i].apply(requestBuilder,args[i].toString());
        }
        //发起真正的请求
        return callFactory.newCall(requestBuilder.build());
 }

    public static class Builder {
        final Retrofit retrofit;
        final Method method;
        final Annotation[] methodAnnotations;
        final Annotation[][] parameterAnnotationsArray;
        private ParameterHandler[] parameterHandlers;
        private String httpMethod;
        private String relativeUrl;
        private boolean hasBody;

        public Builder(Retrofit retrofit, Method method) {
            this.retrofit = retrofit;
            this.method = method;
            //方法注解
            this.methodAnnotations = method.getAnnotations();
            //参数注解
            this.parameterAnnotationsArray = method.getParameterAnnotations();
        }

        ServiceMethod build() {
            for (Annotation methodAnnotation : methodAnnotations) {
                //解析方法注解其实这里就是把信息存起来
                parseMethodAnnotation(methodAnnotation);
            }
            //定义方法参数的数组长度
            int parameterLength = parameterAnnotationsArray.length;
            parameterHandlers = new ParameterHandler[parameterLength];
            for (int i = 0; i < parameterLength; i++) {
                //获取方法的参数的所有注解
                Annotation[] annotations = parameterAnnotationsArray[i];
                if (annotations == null) {
                    throw new NullPointerException("不合规矩");
                }
                //解析参数注解值 和参数值
                parameterHandlers[i] = parseParameter(i, annotations);
            }
            return new ServiceMethod(this);
        }

        //解析参数注解值 和参数值
        private ParameterHandler parseParameter(int i, Annotation[] annotations) {
            ParameterHandler result = null;
            for (Annotation annotation : annotations) {
                ParameterHandler annotationAction = parseParameterAnnotation(annotation);
                if(annotation==null){
                    continue;
                }
                result=annotationAction;
            }
            if(result==null){
                throw new IllegalArgumentException("没有Retrofit注解的支持");
            }
            return result;
        }

        private ParameterHandler parseParameterAnnotation(Annotation annotation) {
            if (annotation instanceof Query) {
                Query query = (Query) annotation;
                String name = query.value();
                return new ParameterHandler.Query(name);
            } else if (annotation instanceof Field) {
                Field field = (Field) annotation;
                String name = field.value();
                return new ParameterHandler.Field(name);
            }
            return null;
        }

        private void parseMethodAnnotation(Annotation methodAnnotation) {
            if (methodAnnotation instanceof GET) {
                parseHttpMethodAndPath("GET", ((GET) (methodAnnotation)).value(), false);
            } else if (methodAnnotation instanceof POST) {
                parseHttpMethodAndPath("POST", ((POST) (methodAnnotation)).value(), true);
            }
        }

        //收集信息
        private void parseHttpMethodAndPath(String httpMethod, String value, boolean hasBody) {
            this.httpMethod = httpMethod;
            this.relativeUrl = value;
            this.hasBody = hasBody;
        }
    }


}

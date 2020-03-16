package com.example.retrofit;

/**
 * 用来保存参数注解值，参数值，用于拼接最后的请求
 */
abstract class ParameterHandler {
    abstract  void apply(RequestBuilder builder,String  value);

    public static class Query extends ParameterHandler {
//        参数名
        private String name;
        public Query(String name) {
            if(name.isEmpty()){
                throw new NullPointerException("name 空");
            }
            this.name=name;
        }

        @Override
        void apply(RequestBuilder builder, String value) {
//            参数值
          if (value==null)return;
          builder.addQueryParam(name,value);
        }
    }

    public static class Field extends ParameterHandler {
        private String name;
        public Field(String name) {
            if(name.isEmpty()){
                throw new NullPointerException("name 空");
            }
            this.name=name;
        }

        @Override
        void apply(RequestBuilder builder, String value) {
            if (value==null)return;
            builder.addFormField(name,value);
        }
    }
}

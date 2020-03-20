package com.example.library;

public class ButterKnife {
    //target.tv=target.findViewById(R.id.tv)
    public static void bind(Object object) {
     String  className=object.getClass().getName()+"$$ViewBinder";

     //类加载
        try {
            Class<?> clazz = Class.forName(className);
            ViewBinder viewBinder = (ViewBinder) clazz.newInstance();
            viewBinder.bind(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

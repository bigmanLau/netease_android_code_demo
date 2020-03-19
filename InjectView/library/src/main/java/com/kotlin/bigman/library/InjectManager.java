package com.kotlin.bigman.library;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;


import com.kotlin.bigman.library.annotation.ContentView;
import com.kotlin.bigman.library.annotation.EventBase;
import com.kotlin.bigman.library.annotation.InjectView;

import com.kotlin.bigman.library.annotation.OnItemClick;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class InjectManager {
    public static  void  inject(Activity activity){
        injectLayout(activity);
        injectViews(activity);
        injectEvents(activity);
        injectListOnClickEvents(activity);

    }

    private static void injectListOnClickEvents(final Activity activity) {
        Class<? extends Activity> clazz = activity.getClass();

        Method[] methods = clazz.getDeclaredMethods();
        for (final Method method:methods) {
            OnItemClick onItemClick= method.getAnnotation(OnItemClick.class);

                if(onItemClick!=null){
                   int recycleViewId= onItemClick.value();
                    View recycleView = activity.findViewById(recycleViewId);
                    if(recycleView instanceof  RecyclerView){
                        ((RecyclerView) recycleView).addOnItemTouchListener(new RecyclerItemClickListener(activity, new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position)
                            {
                                try {
                                    method.invoke(activity,view,position);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }));
                    }
                }

        }
    }

    private static void injectEvents(Activity activity) {
        Class<? extends Activity> clazz = activity.getClass();

        Method[] methods = clazz.getDeclaredMethods();


        for (Method method:methods) {
            Annotation[] annotations = method.getAnnotations();

            for (Annotation annotation:annotations ) {
                Class<? extends Annotation> annotationType = annotation.annotationType();
                if(annotationType!=null){
                    EventBase eventBase = annotationType.getAnnotation(EventBase.class);
                    if(eventBase!=null){
                        String callListener = eventBase.callListener();
                        String listenerSetter = eventBase.listenerSetter();
                        Class<?> listenerType = eventBase.listenerType();

                        try {
                            Method valueMethod = annotationType.getDeclaredMethod("value");
                            int[] viewIds = (int[]) valueMethod.invoke(annotation);

                            ListenerInvocationHandler invocationHandler=new ListenerInvocationHandler(activity);
                            invocationHandler.addMethod(callListener,method);
                            Object listener = Proxy.newProxyInstance(listenerType.getClassLoader(),
                                    new Class[]{listenerType}, invocationHandler);

                            for (int viewId : viewIds) {
                                View view = activity.findViewById(viewId);
                                Method setter = view.getClass().getMethod(listenerSetter, listenerType);
                                setter.invoke(view,listener);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private static void injectViews(Activity activity) {
        Class<? extends Activity> clazz = activity.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field:
             fields) {
            InjectView injectView = field.getAnnotation(InjectView.class);
            if(injectView!=null) {
                int viewId = injectView.value();
                Method method = null;
                try {
                    method = clazz.getMethod("findViewById", int.class);
                    View view = (View) method.invoke(activity, viewId);
                    field.setAccessible(true);
                    field.set(activity, view);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private static void injectLayout(Activity activity) {
        Class<? extends Activity> clazz = activity.getClass();
        ContentView contentView = clazz.getAnnotation(ContentView.class);
        if(contentView!=null){
            int layoutId = contentView.value();
            try {
                Method method = clazz.getMethod("setContentView", int.class);
                method.invoke(activity,layoutId);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

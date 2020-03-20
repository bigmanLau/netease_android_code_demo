package com.example.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD) //作用在类之上
@Retention(RetentionPolicy.CLASS) //要在编译时进行一些预处理操作，注解会在class文件中存在
public @interface BindView {
    int value();
}

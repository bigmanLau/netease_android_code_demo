package com.kotlin.bigman.library.annotation;

import android.view.View;

import com.kotlin.bigman.library.RecyclerItemClickListener;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OnItemClick {
    int value();
}
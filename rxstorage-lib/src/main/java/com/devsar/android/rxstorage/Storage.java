package com.devsar.android.rxstorage;

import android.content.Context;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Juan on 3/4/17.
 */

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface Storage {
    String name() default "";
    int mode() default Context.MODE_PRIVATE;
}

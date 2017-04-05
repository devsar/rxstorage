package com.devsar.android.rxstorageprocessor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;

import javax.lang.model.element.Modifier;

/**
 * Created by Juan on 5/4/17.
 */

public class ConstructorBuilder {

    public MethodSpec getWithPrefs() {
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterSpec
                        .builder(ClassName.get("android.content", "SharedPreferences"), "preferences")
                        .build())
                .addStatement("super(preferences)")
                .build();
    }

    public MethodSpec getWithPrefsScheduler() {
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterSpec
                        .builder(ClassName.get("android.content", "SharedPreferences"), "preferences")
                        .build())
                .addParameter(ParameterSpec
                        .builder(ClassName.get("io.reactivex", "Scheduler"), "scheduler")
                        .build())
                .addStatement("super(preferences, scheduler)")
                .build();
    }

    public MethodSpec getPrefsBuilder() {
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterSpec
                        .builder(ClassName.get("android.content", "Context"), "context")
                        .build())
                .addParameter(ParameterSpec
                        .builder(ClassName.get(String.class), "name")
                        .build())
                .addParameter(ParameterSpec
                        .builder(ClassName.get(Integer.class), "mode")
                        .build())
                .addStatement("super(context, name, mode)")
                .build();
    }

    public MethodSpec getPrefsBuilderWithScheduler() {
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterSpec
                        .builder(ClassName.get("android.content", "Context"), "context")
                        .build())
                .addParameter(ParameterSpec
                        .builder(ClassName.get(String.class), "name")
                        .build())
                .addParameter(ParameterSpec
                        .builder(ClassName.get(Integer.class), "mode")
                        .build())
                .addParameter(ParameterSpec
                        .builder(ClassName.get("io.reactivex", "Scheduler"), "scheduler")
                        .build())
                .addStatement("super(context, name, mode, scheduler)")
                .build();
    }

    public MethodSpec getContextOnly(String name, int mode) {
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterSpec
                        .builder(ClassName.get("android.content", "Context"), "context")
                        .build())
                .addStatement("super(context, \"" + name + "\", " + mode + ")")
                .build();
    }

    public MethodSpec getContextSchedulerOnly(String name, int mode) {
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterSpec
                        .builder(ClassName.get("android.content", "Context"), "context")
                        .build())
                .addParameter(ParameterSpec
                        .builder(ClassName.get("io.reactivex", "Scheduler"), "scheduler")
                        .build())
                .addStatement("super(context, \"" + name + "\", " + mode + ", scheduler)")
                .build();
    }
}

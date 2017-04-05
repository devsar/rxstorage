package com.devsar.android.rxstorageprocessor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;

import java.util.Set;

import javax.lang.model.element.Modifier;

/**
 * Created by Juan on 5/4/17.
 */

public class AccessorBuilder {

    private static final String stringName = "String";
    private static final String longName = "Long";
    private static final String intName = "Int";
    private static final String booleanName = "Boolean";
    private static final String floatName = "Float";

    public MethodSpec getStringGetter(String propertyName, String key, String defVal) {
        final String def = !defVal.isEmpty() ? "\"" + defVal + "\"" : "\"\"";
        return getGetter(String.class, propertyName, key, stringName, def);
    }

    public MethodSpec getLongGetter(String propertyName, String key, String defVal) {
        final String def = !defVal.isEmpty() ? defVal : "0";
        return getGetter(Long.class, propertyName, key, longName, def);
    }

    public MethodSpec getBooleanGetter(String propertyName, String key, String defVal) {
        final String def = !defVal.isEmpty() ? defVal : "false";
        return getGetter(Boolean.class, propertyName, key, booleanName, def);
    }

    public MethodSpec getIntegerGetter(String propertyName, String key, String defVal) {
        final String def = !defVal.isEmpty() ? defVal : "0";
        return getGetter(Integer.class, propertyName, key, intName, def);
    }

    public MethodSpec getFloatGetter(String propertyName, String key, String defVal) {
        final String def = !defVal.isEmpty() ? defVal : "0f";
        return getGetter(Float.class, propertyName, key, floatName, def);
    }

    public MethodSpec getStringSetGetter(String propertyName, String key) {
        return MethodSpec.methodBuilder(propertyName)
                .addModifiers(Modifier.PUBLIC)
                .returns(ParameterizedTypeName.get(
                        ClassName.get("io.reactivex", "Single"),
                        ParameterizedTypeName.get(Set.class, String.class)))
                .addParameter(ParameterSpec
                        .builder(ParameterizedTypeName.get(Set.class, String.class), "defaultValue")
                        .build())
                .addStatement("return getStringSet(\"" + key + "\", defaultValue)")
                .build();
    }

    public MethodSpec getStringSetter(String propertyName, String key) {
        return getSetter(String.class, propertyName, key, stringName);
    }

    public MethodSpec getLongSetter(String propertyName, String key) {
        return getSetter(Long.class, propertyName, key, longName);
    }

    public MethodSpec getBooleanSetter(String propertyName, String key) {
        return getSetter(Boolean.class, propertyName, key, booleanName);
    }

    public MethodSpec getIntegerSetter(String propertyName, String key) {
        return getSetter(Integer.class, propertyName, key, intName);
    }

    public MethodSpec getFloatSetter(String propertyName, String key) {
        return getSetter(Float.class, propertyName, key, floatName);
    }

    public MethodSpec getStringSetSetter(String propertyName, String key) {
        String capName = Character.toUpperCase(propertyName.charAt(0)) + propertyName.substring(1, propertyName.length());
        return MethodSpec.methodBuilder("save" + capName)
                .addModifiers(Modifier.PUBLIC)
                .returns(ClassName.get("io.reactivex", "Completable"))
                .addParameter(ParameterSpec.builder(ParameterizedTypeName.get(Set.class, String.class), "value").build())
                .addStatement("return saveStringSet(\"" + key + "\", value)")
                .build();
    }

    private MethodSpec getGetter(Class<?> typeClass, String propertyName, String key, String typeName, String defaultValue) {
        return MethodSpec.methodBuilder(propertyName)
                .addModifiers(Modifier.PUBLIC)
                .returns(ParameterizedTypeName.get(
                        ClassName.get("io.reactivex", "Single"), ClassName.get(typeClass)))
                .addStatement("return get" + typeName + "(\"" + key + "\", " + defaultValue + ")")
                .build();
    }

    private MethodSpec getSetter(Class<?> typeClass, String propertyName, String key, String typeName) {
        String capName = Character.toUpperCase(propertyName.charAt(0)) + propertyName.substring(1, propertyName.length());
        return MethodSpec.methodBuilder("save" + capName)
                .addModifiers(Modifier.PUBLIC)
                .returns(ClassName.get("io.reactivex", "Completable"))
                .addParameter(ParameterSpec.builder(ClassName.get(typeClass), "value").build())
                .addStatement("return save" + typeName + "(\"" + key + "\", value)")
                .build();
    }
}

package com.devsar.android.rxstorageprocessor;

import com.devsar.android.rxstorage.Storage;
import com.devsar.android.rxstorage.StoredField;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import javax.tools.JavaFileObject;


@SupportedAnnotationTypes("com.devsar.android.rxstorage.Storage")
public class StorageProcessor extends AbstractProcessor {

    private AccessorBuilder accessorBuilder;
    private ConstructorBuilder constructorBuilder;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        accessorBuilder = new AccessorBuilder();
        constructorBuilder = new ConstructorBuilder();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        List<TypeElement> types = new ArrayList<>(
                ElementFilter.typesIn(roundEnv.getElementsAnnotatedWith(Storage.class)));
        for (TypeElement type : types) {
            try { generateStorageClass(type, roundEnv); }
            catch (IOException e) { e.printStackTrace(); }
        }
        return true;
    }

    private void generateStorageClass(TypeElement type, RoundEnvironment env) throws IOException {
        final String subclassName = "Devsar" + type.getSimpleName().toString();
        final String packageName = ((PackageElement) type.getEnclosingElement())
                .getQualifiedName()
                .toString();
        TypeSpec.Builder subclassBuilder = TypeSpec.classBuilder(subclassName)
                .superclass(ClassName.get("com.devsar.android.rxstorage", "ReactiveStorage"))
                .addModifiers(Modifier.PUBLIC);

        addConstructors(subclassBuilder, type.getAnnotation(Storage.class));
        addMethods(subclassBuilder, ElementFilter.methodsIn(
                env.getElementsAnnotatedWith(StoredField.class)));

        final JavaFile javaFile = JavaFile.builder(packageName, subclassBuilder.build()).build();
        final JavaFileObject sourceFile = processingEnv.getFiler()
                .createSourceFile(packageName + "." + subclassName);
        final Writer writer = sourceFile.openWriter();
        writer.write(javaFile.toString());
        writer.close();
    }

    private void addConstructors(TypeSpec.Builder builder, Storage ann) {
        if (ann.name().isEmpty()) builder.addMethod(constructorBuilder.getWithPrefs())
                .addMethod(constructorBuilder.getWithPrefsScheduler())
                .addMethod(constructorBuilder.getPrefsBuilder())
                .addMethod(constructorBuilder.getPrefsBuilderWithScheduler());
        else builder.addMethod(constructorBuilder.getContextOnly(ann.name(), ann.mode()))
                .addMethod(constructorBuilder.getContextSchedulerOnly(ann.name(), ann.mode()));
    }

    private void addMethods(TypeSpec.Builder builder, Set<ExecutableElement> methods) {
        for (ExecutableElement elem : methods) {
            final String name = elem.getSimpleName().toString();
            final StoredField annotation = elem.getAnnotation(StoredField.class);
            final String key = !annotation.key().isEmpty() ? annotation.key() : name;
            final String defVal = annotation.withDefault();
            if (isReturnType(elem, String.class)) {
                builder.addMethod(accessorBuilder.getStringGetter(name, key, defVal))
                        .addMethod(accessorBuilder.getStringSetter(name, key));
            } else if (isReturnType(elem, Long.class)) {
                builder.addMethod(accessorBuilder.getLongGetter(name, key, defVal))
                        .addMethod(accessorBuilder.getLongSetter(name, key));
            } else if (isReturnType(elem, Boolean.class)) {
                builder.addMethod(accessorBuilder.getBooleanGetter(name, key, defVal))
                        .addMethod(accessorBuilder.getBooleanSetter(name, key));
            } else if (isReturnType(elem, Integer.class)) {
                builder.addMethod(accessorBuilder.getIntegerGetter(name, key, defVal))
                        .addMethod(accessorBuilder.getIntegerSetter(name, key));
            } else if (isReturnType(elem, Float.class)) {
                builder.addMethod(accessorBuilder.getFloatGetter(name, key, defVal))
                        .addMethod(accessorBuilder.getFloatSetter(name, key));
            } else if (isReturnType(elem, ParameterizedTypeName.get(Set.class, String.class))) {
                builder.addMethod(accessorBuilder.getStringSetGetter(name, key))
                        .addMethod(accessorBuilder.getStringSetSetter(name, key));
            }
        }
    }

    private boolean isReturnType(ExecutableElement elem, Class<?> clazz) {
        return elem.getReturnType().toString().equals(clazz.getName());
    }

    private boolean isReturnType(ExecutableElement elem, TypeName typeName) {
        return elem.getReturnType().toString().equals(typeName.toString());
    }
}

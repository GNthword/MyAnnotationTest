package com.milog.myannotationcompile;

import com.google.auto.service.AutoService;
import com.milog.annotation.MiloConfig2;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * Created by miloway on 2018/6/21.
 */

@AutoService(Processor.class)
public class MiloAnnotationProcessor extends AbstractProcessor{

    private Filer filer;
    private Elements elements;
    private Messager messager;
    private Map<String, AnnotationClass> builderMap;
    private boolean g = true;
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        filer = processingEnv.getFiler();
        elements = processingEnv.getElementUtils();
        messager = processingEnv.getMessager();
        builderMap = new HashMap<String, AnnotationClass>();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> strings = new LinkedHashSet<>(1);
        strings.add(MiloConfig2.class.getCanonicalName());
        return super.getSupportedAnnotationTypes();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        builderMap.clear();
        collectionClass(roundEnv);
        builderJava();
        if (g) {
            g = false;
            for (TypeElement element : annotations) {
                if (element.getQualifiedName().toString().equals(MiloConfig2.class.getCanonicalName())) {
                    // main method
                    MethodSpec main = MethodSpec.methodBuilder("main")
                            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                            .returns(void.class)
                            .addParameter(String[].class, "args")
                            .addStatement("$T.out.println($S)", System.class, "Hello, JavaPoet!")
                            .build();
                    // HelloWorld class
                    TypeSpec helloWorld = TypeSpec.classBuilder("HelloWorld")
                            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                            .addMethod(main)
                            .build();

                    try {
                        // build com.example.HelloWorld.java
                        JavaFile javaFile = JavaFile.builder("com.example.a", helloWorld)
                                .addFileComment(" This codes are generated automatically. Do not modify!")
                                .build();
                        // write to file
                        javaFile.writeTo(filer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return true;
    }

    private void collectionClass(RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(MiloConfig2.class)) {
            AnnotationClass annotationClass = getBuilder(element);
            MiloConfigField field = new MiloConfigField(element);
            annotationClass.addFields(field);
        }
    }

    private AnnotationClass getBuilder(Element element) {
        TypeElement typeElement = (TypeElement) element.getEnclosingElement();
        String fullName = typeElement.getQualifiedName().toString();
        AnnotationClass annotatedClass = builderMap.get(fullName);
        if (annotatedClass == null) {
            annotatedClass = new AnnotationClass(typeElement, elements);
            builderMap.put(fullName, annotatedClass);
        }
        return annotatedClass;
    }


    private void  builderJava() {
        for (AnnotationClass builder : builderMap.values()) {
            try {
                builder.buildFile().writeTo(filer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}



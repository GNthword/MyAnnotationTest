package com.milog.myannotationcompile;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * Created by miloway on 2018/6/22.
 */

public class AnnotationClass {
    private TypeElement typeElement;
    private ArrayList<MiloConfigField> fields;
    private Elements elements;

    public AnnotationClass(TypeElement typeElement, Elements elements) {
        typeElement = typeElement;
        elements = elements;
        fields = new ArrayList<MiloConfigField>();
    }

    public void addFields(MiloConfigField field) {
        fields.add(field);
    }


    public JavaFile buildFile() {



        TypeSpec injectClass = TypeSpec.classBuilder(typeElement.getSimpleName() + "$$MiloConfig")
                .addModifiers(Modifier.PUBLIC)
                //.addSuperinterface(ParameterizedTypeName.get(TypeUtil.BINDER, TypeName.get(mTypeElement.asType())))
                //.addMethod(bindViewMethod.build())
                //.addMethod(unBindViewMethod.build())
                .build();

        String packageName = elements.getPackageOf(typeElement).getQualifiedName().toString();

        return JavaFile.builder(packageName, injectClass).build();
    }

}

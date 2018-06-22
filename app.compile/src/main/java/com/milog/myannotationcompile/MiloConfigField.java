package com.milog.myannotationcompile;

import com.milog.annotation.MiloConfig2;

import javax.lang.model.element.Element;

/**
 * Created by miloway on 2018/6/22.
 */

public class MiloConfigField {


    private int value;
    private int intType;
    private String stringType;

    public MiloConfigField(Element element) {
        MiloConfig2 miloConfig2 = element.getAnnotation(MiloConfig2.class);
        value = miloConfig2.value();
        intType = miloConfig2.intType();
        stringType = miloConfig2.stringType();
        checkValue();
    }

    /**
     * check
     */
    private void checkValue() {
        if (value < 0 || intType < 0) {
            System.out.println("error value");
        }
    }



    public int getValue() {
        return value;
    }

    public int getIntType() {
        return intType;
    }

    public String getStringType() {
        return stringType;
    }
}

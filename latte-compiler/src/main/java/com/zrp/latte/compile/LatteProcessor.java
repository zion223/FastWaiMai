package com.zrp.latte.compile;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;

public class LatteProcessor extends AbstractProcessor {


    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        return false;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        final Set<String> types = new LinkedHashSet<>();
        return null;
    }


}

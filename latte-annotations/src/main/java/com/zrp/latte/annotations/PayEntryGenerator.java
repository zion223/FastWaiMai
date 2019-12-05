package com.zrp.latte.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface PayEntryGenerator {

    String packageName();
    
    Class<?> payEntryTemplate();
}

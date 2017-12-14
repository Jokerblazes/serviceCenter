package com.joker.entity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
/**
 * 非空注解
 * https://github.com/Jokerblazes/serviceCenter.git
 */
public @interface NonEmpty {
    String value() default "";
}

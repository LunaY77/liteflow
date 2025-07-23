package com.yomahub.liteflow.ai.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * AI 输入节点注解
 *
 * @author 苍镜月
 * @since TODO
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AIInput {

    InputField[] mapping() default {};

    @Retention(RetentionPolicy.RUNTIME)
    @interface InputField {
        String name();
        String expression();
        String defaultValue() default "";
        boolean required() default false;
    }
}

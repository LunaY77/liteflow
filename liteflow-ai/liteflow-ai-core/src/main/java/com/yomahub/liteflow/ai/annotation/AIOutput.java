package com.yomahub.liteflow.ai.annotation;

import dev.langchain4j.model.chat.request.ResponseFormatType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * AI 输出节点注解
 *
 * @author 苍镜月
 * @since TODO
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AIOutput {
    ResponseFormatType type() default ResponseFormatType.TEXT;
    String methodExpress() default "setData";
    String key();
    Class<?> entity() default Object.class;
    OutputField[] mapping() default {};

    @Retention(RetentionPolicy.RUNTIME)
    @interface OutputField {
        String name();
        String methodExpress() default "setData";
        String key();
    }
}

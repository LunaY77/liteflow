package com.yomahub.liteflow.ai.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * AI Chat 节点注解
 *
 * @author 苍镜月
 * @since TODO
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AIChat {

    /**
     * 系统提示词
     */
    String systemPrompt() default "你是一个意图分类助手";

    /**
     * 用户提示词
     */
    String userPrompt() default "";

    /**
     * 是否开启 stream，默认 false
     */
    boolean streaming() default false;

}

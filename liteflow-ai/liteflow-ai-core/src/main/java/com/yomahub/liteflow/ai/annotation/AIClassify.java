package com.yomahub.liteflow.ai.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * AI 分类节点注解
 *
 * @author 苍镜月
 * @since TODO
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AIClassify {
    /**
     * AI 厂商 (e.g. openai, ollama, etc.)
     */
    String provider() default "";

    /**
     * 系统提示词
     */
    String systemPrompt() default "";

    /**
     * 用户提示词
     */
    String userPrompt() default "";

    /**
     * 预定义分类列表
     */
    String[] categories() default {};

    /**
     * 是否多标签分类，默认 false
     */
    boolean multiLabel() default false;
}

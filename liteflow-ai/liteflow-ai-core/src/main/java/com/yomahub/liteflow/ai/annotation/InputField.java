package com.yomahub.liteflow.ai.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 输入字段映射注解
 * 定义单个输入字段的映射关系
 *
 * @author 苍镜月
 * @since TODO
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface InputField {

    /**
     * 字段名称（必需）
     *
     * @return 字段名
     */
    String name();

    /**
     * 上下文路径表达式（必需）
     *
     * @return 表达式
     */
    String expression();

    /**
     * 默认值
     *
     * @return 默认值
     */
    String defaultValue() default "";

    /**
     * 是否必需
     *
     * @return 是否必需
     */
    boolean required() default true;
} 
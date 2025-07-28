package com.yomahub.liteflow.ai.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 输出字段映射注解
 * 定义单个输出字段的映射关系
 *
 * @author 苍镜月
 * @since TODO
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface OutputField {

    /**
     * 字段名称（必需）
     *
     * @return 字段名
     */
    String name();

    /**
     * 方法表达式，用于设置输出值
     *
     * @return 方法表达式
     */
    String methodExpress() default "setData";

    /**
     * 输出键值
     *
     * @return 键值
     */
    String key();
} 
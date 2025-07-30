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
     * 字段名称（必需）。
     * <p>
     * 此名称必须与 prompt 模板中的占位符名称完全对应。
     * <ul>
     * <li>
     * <b>例如:</b><br>
     * 如果 prompt 中存在占位符 {@code {{productName}}}，则此字段的值必须为 {@code "productName"}。
     * </li>
     * </ul>
     */
    String name();

    /**
     * 上下文路径表达式（必需）。
     * <p>
     * 用于从一个必须提供 {@code get} 方法的上下文中检索数据。
     * 表达式支持以下两种形式：
     * <ul>
     * <li>
     * <b>直接属性检索:</b><br>
     * 例如，从上下文中获取一个名为 {@code productName} 的字符串对象，
     * 表达式应为：{@code "productName"}。
     * </li>
     * <li>
     * <b>嵌套属性检索 (例如 Map):</b><br>
     * 例如，从上下文的一个名为 {@code nameMap} 的 Map 对象中，获取键为 {@code nameKey} 的值，
     * 表达式应为：{@code "nameMap.nameKey"}。
     * </li>
     * </ul>
     */
    String expression();

    /**
     * 默认值
     */
    String defaultValue() default "";

    /**
     * 是否必需
     */
    boolean required() default true;
} 
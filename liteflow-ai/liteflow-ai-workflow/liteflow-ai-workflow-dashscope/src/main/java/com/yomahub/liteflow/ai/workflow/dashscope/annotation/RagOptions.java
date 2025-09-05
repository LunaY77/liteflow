package com.yomahub.liteflow.ai.workflow.dashscope.annotation;

import com.google.gson.JsonObject;
import com.yomahub.liteflow.ai.util.KeyValue;

import java.util.List;

/**
 * DashScope WorkFlow Rag配置
 *
 * @author 苍镜月
 * @since TODO
 */

public @interface RagOptions {

    /**
     * 知识库 id 列表(知识库id之间通过逗号隔开 or 使用上下文表达式)
     * <p>对应 ApplicationParam 中的 images</p>
     * <p>{@link com.alibaba.dashscope.app.RagOptions#setPipelineIds(List)}</p>
     *
     * <p>支持上下文表达式，开启请在开头结尾加上双花括号包裹，如: "{{pipelineIds}}"</p>
     * <p>
     * 上下文表达式用于从一个必须提供 {@code get} 方法的上下文中检索数据。
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
    String pipelineIds() default "";

    /**
     * 文件列表(会话文件id之间通过逗号隔开 or 使用上下文表达式)
     * <p>对应 ApplicationParam 中的 images</p>
     * <p>{@link com.alibaba.dashscope.app.RagOptions#setFileIds(List)}</p>
     *
     * <p>支持上下文表达式，开启请在开头结尾加上双花括号包裹，如: "{{fieldIds}}"</p>
     * <p>
     * 上下文表达式用于从一个必须提供 {@code get} 方法的上下文中检索数据。
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
    String fieldIds() default "";

    /**
     * 文档标签 ID 列表(文档标签id之间通过逗号隔开 or 使用上下文表达式)
     * <p>对应 ApplicationParam 中的 images</p>
     * <p>{@link com.alibaba.dashscope.app.RagOptions#setFileIds(List)}</p>
     *
     * <p>支持上下文表达式，开启请在开头结尾加上双花括号包裹，如: "{{tags}}"</p>
     * <p>
     * 上下文表达式用于从一个必须提供 {@code get} 方法的上下文中检索数据。
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
    String tags() default "";

    /**
     * 元数据键值对 (value 支持上下文表达式)
     * <p>对应 ApplicationParam 中的 metadataFilter</p>
     * <p>{@link com.alibaba.dashscope.app.RagOptions#setMetadataFilter(JsonObject)}</p>
     */
    KeyValue[] metadataFilter() default {};

    /**
     * 结构化数据键值对 (value 支持上下文表达式)
     * <p>对应 ApplicationParam 中的 structuredFilter</p>
     * <p>{@link com.alibaba.dashscope.app.RagOptions#setStructuredFilter(JsonObject)}</p>
     */
    KeyValue[] structuredFilter() default {};

    /**
     * 会话文件列表(会话文件id之间通过逗号隔开 or 使用上下文表达式)
     * <p>对应 ApplicationParam 中的 images</p>
     * <p>{@link com.alibaba.dashscope.app.RagOptions#setSessionFileIds(List)}</p>
     *
     * <p>支持上下文表达式，开启请在开头结尾加上双花括号包裹，如: "{{sessionFileIds}}"</p>
     * <p>
     * 上下文表达式用于从一个必须提供 {@code get} 方法的上下文中检索数据。
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
    String sessionFileIds() default "";
}

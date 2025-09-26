package com.yomahub.liteflow.ai.annotation.model.node;

import com.yomahub.liteflow.ai.context.ChatContext;
import com.yomahub.liteflow.ai.engine.interact.transport.TransportType;
import com.yomahub.liteflow.ai.engine.tool.registry.DelegatingToolRegistry;
import com.yomahub.liteflow.ai.engine.tool.registry.ScanningToolRegistry;
import com.yomahub.liteflow.ai.engine.tool.registry.StaticToolRegistry;
import com.yomahub.liteflow.ai.engine.tool.registry.ToolRegistry;
import com.yomahub.liteflow.ai.tool.SpringBeanToolRegistry;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * AI Chat 节点注解
 *
 * @author 苍镜月
 * @since 2.16.0
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AIChat {

    /**
     * 聊天历史的上下文路径表达式(需在上下文中设置对应的 List&lt;Message&gt; 对象)
     *
     * <p>
     * 启用后将不使用 {@code systemPrompt()} 和 {@code userPrompt()}
     * </p>
     * <p>
     * {@link com.yomahub.liteflow.ai.engine.model.chat.message.Message}
     * </p>
     *
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
    String history() default "";

    /**
     * 系统提示词
     * <p>
     * 支持多种类型的 prompt 注入解决方案：
     * <ul>
     * <li>
     * <b>直接文本:</b><br>
     * 直接在注解中定义提示词文本，例如：{@code "你是一个专业的AI助手"}
     * </li>
     * <li>
     * <b>类路径资源:</b><br>
     * 从类路径中读取提示词文件，格式：{@code "classpath:prompts/system-prompt.txt"}
     * </li>
     * <li>
     * <b>文件系统资源:</b><br>
     * 从文件系统中读取提示词文件，格式：{@code "file:/path/to/prompts/system-prompt.txt"}
     * </li>
     * <li>
     * <b>URL资源:</b><br>
     * 从网络URL中读取提示词内容，格式：{@code "http://example.com/prompts/system-prompt.txt"}
     * </li>
     * <li>
     * <b>文本资源:</b><br>
     * 使用文本前缀标识直接文本内容，格式：{@code "text:你是一个专业的AI助手"}
     * </li>
     * </ul>
     * </p>
     */
    String systemPrompt() default "";

    /**
     * 用户提示词
     * <p>
     * 支持多种类型的 prompt 注入解决方案：
     * <ul>
     * <li>
     * <b>直接文本:</b><br>
     * 直接在注解中定义提示词文本，例如：{@code "请分析以下数据"}
     * </li>
     * <li>
     * <b>类路径资源:</b><br>
     * 从类路径中读取提示词文件，格式：{@code "classpath:prompts/user-prompt.txt"}
     * </li>
     * <li>
     * <b>文件系统资源:</b><br>
     * 从文件系统中读取提示词文件，格式：{@code "file:/path/to/prompts/user-prompt.txt"}
     * </li>
     * <li>
     * <b>URL资源:</b><br>
     * 从网络URL中读取提示词内容，格式：{@code "http://example.com/prompts/user-prompt.txt"}
     * </li>
     * <li>
     * <b>文本资源:</b><br>
     * 使用文本前缀标识直接文本内容，格式：{@code "text:请分析以下数据"}
     * </li>
     * </ul>
     * </p>
     */
    String userPrompt() default "";

    /**
     * 是否开启 stream，默认 true
     */
    boolean streaming() default true;

    /**
     * 传输类型，默认 SSE
     * <p>
     * 请在此查看传输类型 -> {@link TransportType}
     */
    TransportType transportType() default TransportType.SSE;

    /**
     * 需要启用的工具名列表（默认不启用）
     * <p>
     * 工具注册于 {@link ChatContext#getToolRegistry()}，请于上下文中传入
     * <p>
     * 如果上下文中没有传入工具注册器，则会使用注册为 Spring Bean 的 {@link ToolRegistry}
     * ，框架默认实现了 {@link SpringBeanToolRegistry}。
     * 可以将 Tool 注册为 Bean 实现自动发现与注册。
     * <p>
     *
     * @see ToolRegistry
     * @see StaticToolRegistry
     * @see ScanningToolRegistry
     * @see DelegatingToolRegistry
     * @see SpringBeanToolRegistry
     */
    String[] toolNames() default {};
}

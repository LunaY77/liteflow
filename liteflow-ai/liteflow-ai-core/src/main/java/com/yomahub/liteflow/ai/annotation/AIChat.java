package com.yomahub.liteflow.ai.annotation;

import com.yomahub.liteflow.ai.context.ChatContext;
import com.yomahub.liteflow.ai.engine.interact.transport.TransportType;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatOptions;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatRequest;
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
 * @since TODO
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AIChat {

    /**
     * 从上下文中获取的请求参数的上下文路径表达式。
     * <p>
     * 如果你不希望在注解中进行静态的模型配置，或者你希望使用特定厂商实现的 ChatRequest(其中可能存在一些独有的参数)，
     * 请使用这个属性，并在上下文中提供对应的 {@link ChatRequest} 值。
     * 如果不提供，框架将统一使用 {@link ChatRequest} 发起请求。
     * <p>
     * {@link ChatRequest} 可以是对应提供商的具体实现类。
     * <p>
     * 如果你使用了这个属性可以不进行 {@link AIChat#systemPrompt()} 和 {@link AIChat#userPrompt()} 和 {@link AIChat#streaming()} 的配置，
     * 因为这些配置会从 {@link ChatRequest} 中获取。如果进行了配置，优先使用 {@link ChatRequest} 中的配置。
     * <p>
     * 以及，你会发现 {@link ChatRequest#getOptions()} 的 {@link ChatOptions} 和 {@link AIComponent} 中的配置有重合。
     * 同样的，即使你进行了 {@link AIComponent} 的配置，会优先使用 {@link ChatRequest} 的配置。
     * <p>
     * <b>请注意：如果相关的配置为空或默认值，则会使用 {@link AIComponent} 和 {@link AIChat} 中的配置!!!</b>
     * <p>
     * 该方法用于从一个必须提供 {@code get} 方法的上下文中检索数据。
     * <p>
     * 表达式支持以下两种形式：
     * <ul>
     * <li>
     * <b>直接属性检索:</b><br>
     * 例如，从上下文中获取 OpenAI 的 ChatRequest，他的名字为 {@code openAIChatRequest}，
     * 表达式应为：{@code "openAIChatRequest"}。
     * </li>
     * <li>
     * <b>嵌套属性检索 (例如 Map):</b><br>
     * 例如，从上下文的一个名为 {@code requestMap} 的 Map 对象中，获取键为 {@code openAI} 的 ChatRequest 对象，
     * 表达式应为：{@code "requestMap.openAI"}。
     * </li>
     * </ul>
     */
    String getChatRequest() default "";

    /**
     * 系统提示词
     */
    String systemPrompt() default "";

    /**
     * 用户提示词
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
     * 需要启用的工具名列表（默认全部启用）
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

package com.yomahub.liteflow.ai.annotation;

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
 * @since TODO
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AIChat {

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

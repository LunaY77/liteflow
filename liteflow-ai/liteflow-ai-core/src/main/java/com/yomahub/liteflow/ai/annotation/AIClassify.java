package com.yomahub.liteflow.ai.annotation;

import com.yomahub.liteflow.ai.context.ChatContext;
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
 * AI 分类节点注解
 *
 * @author 苍镜月
 * @since TODO
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AIClassify {

    /**
     * 系统提示词
     */
    String systemPrompt() default "你是一个意图分类助手";

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

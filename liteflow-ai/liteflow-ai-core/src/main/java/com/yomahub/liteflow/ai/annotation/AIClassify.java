package com.yomahub.liteflow.ai.annotation;

import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatOptions;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatRequest;

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
}

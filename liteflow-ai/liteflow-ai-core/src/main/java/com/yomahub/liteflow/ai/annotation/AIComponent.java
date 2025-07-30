package com.yomahub.liteflow.ai.annotation;

import com.yomahub.liteflow.ai.util.KeyValue;
import com.yomahub.liteflow.ai.util.TriState;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * AI 组件注解，提供配置和标识功能
 * <p>
 * 配合具体的 AI 组件使用
 * <ul>
 *     <li>{@link AIChat}</li>
 *     <li>{@link AIClassify}</li>
 *     <li>{@link AIRetrieval}</li>
 * </ul>
 *
 * @author 苍镜月
 * @since TODO
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AIComponent {

    /**
     * AI 厂商 (e.g. openai, ollama, etc.)
     */
    String provider() default "";

    /**
     * 节点 ID
     */
    String nodeId() default "";

    /**
     * 节点名称
     */
    String nodeName() default "";

    // --- 连接、鉴权参数 ---
    /**
     * API URL
     */
    String baseUrl() default "";
    /**
     * 模型名
     */
    String model() default "";
    /**
     * API KEY
     */
    String apiKey() default "";
    /**
     * 版本
     */
    String version() default "";

    // --- 核心参数 ---
    /**
     * 温度参数
     */
    double temperature() default -1.0;
    /**
     * Top P 参数
     */
    double topP() default -1.0;
    /**
     * Top K 参数
     */
    int topK() default -1;
    /**
     * 最大 Token 数量
     * 统一：maxTokens, maxOutputTokens, numPredict
     */
    int maxTokens() default -1;
    /**
     * 停止序列
     * 统一: stop, stopSequences
     */
    String[] stop() default {};
    /**
     * seed
     */
    int seed() default -1;

    // --- 惩罚参数 ---
    /**
     * 重复惩罚
     */
    double repeatPenalty() default -1.0;
    /**
     * 存在惩罚
     */
    double presencePenalty() default -1.0;
    /**
     * 频率惩罚
     */
    double frequencyPenalty() default -1.0;

    // --- Format ----
    /**
     * 响应格式
     */
    String responseFormat() default "";
    /**
     * 是否严格遵循 JSON Schema
     */
    TriState strictJsonSchema() default TriState.UNSET;

    // --- Tool Calling ----
    /**
     * 是否并行 ToolCall
     */
    TriState parallelToolCalls() default TriState.UNSET;

    // --- 网络和日志参数 ---
    /**
     * 超时时间
     */
    String timeout() default "";
    /**
     * 最大重试次数
     */
    int maxRetries() default -1;
    /**
     * 是否记录请求日志
     */
    TriState logRequests() default TriState.UNSET;
    /**
     * 是否记录响应日志
     */
    TriState logResponses() default TriState.UNSET;

    // --- 其他参数 ---
    /**
     * 自定义请求头
     */
    KeyValue[] customHeaders() default {};

}

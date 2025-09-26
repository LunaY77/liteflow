package com.yomahub.liteflow.ai.annotation;

import com.yomahub.liteflow.ai.annotation.model.node.AIChat;
import com.yomahub.liteflow.ai.annotation.model.node.AIClassify;
import com.yomahub.liteflow.ai.util.KeyValue;
import com.yomahub.liteflow.ai.util.TriState;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.Duration;

/**
 * AI 组件注解，提供配置和标识功能
 * <p>
 * 配合具体的 AI 组件使用
 * <ul>
 * <li>{@link AIChat}</li>
 * <li>{@link AIClassify}</li>
 * </ul>
 *
 * @author 苍镜月
 * @since 2.16.0
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
     * API URL，和 endPoint 配合使用
     * <p>
     * 请不要在意是否以斜杠结尾or开头，框架会自动处理。
     * <p>
     * 你可以只使用 apiUrl，但不提供 endPoint，这样会使用默认的端点，
     * 请查看对应模型提供商依赖下的 Constant 类，里面定义了默认的端点。
     * <p>
     * 例如：
     * <ul>
     * <li>
     * openai-apiUrl: {@code https://api.openai.com/v1}<br>
     * openai-endPoint: {@code chat/completions}
     * </li>
     * </ul>
     */
    String apiUrl() default "";

    /**
     * API 端点，和 apiUrl 配合使用
     * <p>
     * 请不要在意是否以斜杠结尾or开头，框架会自动处理。
     * <p>
     * 你可以只使用 apiUrl，但不提供 endPoint，这样会使用默认的端点，
     * 请查看对应模型提供商依赖下的 Constant 类，里面定义了默认的端点。
     * <p>
     * 例如：
     * <ul>
     * <li>
     * openai-apiUrl: {@code https://api.openai.com/v1}<br>
     * openai-endPoint: {@code chat/completions}
     * </li>
     * </ul>
     */
    String endPoint() default "";

    /**
     * 模型名
     */
    String model() default "";

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
     */
    int maxTokens() default -1;

    /**
     * 停止序列
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

    // --- Tool Calling ----
    /**
     * TODO not implement
     * 是否并行 ToolCall
     */
    TriState parallelToolCalls() default TriState.UNSET;

    /**
     * 是否自动进行 ToolCall
     */
    TriState autoToolCallEnabled() default TriState.TRUE;

    // --- 网络和日志参数 ---
    /**
     * <p>
     * 连接超时时间
     * </p>
     *
     * <p>
     * 该值最终会被解析为一个 {@link Duration} 对象。为了提供灵活性，
     * 支持以下两种字符串格式：
     * </p>
     *
     * <ol>
     * <li><b>标准 ISO-8601 格式</b>:
     * 这是由 {@link Duration#parse(CharSequence)} 支持的标准格式。
     * <p>
     * <b>示例:</b>
     * </p>
     * <ul>
     * <li>{@code "PT30S"} 代表 30 秒。</li>
     * <li>{@code "PT10M"} 代表 10 分钟。</li>
     * <li>{@code "PT2H"} 代表 2 小时。</li>
     * <li>{@code "P1D"} 代表 1 天。</li>
     * </ul>
     * </li>
     * <li><b>自定义简化格式</b>:
     * 为了方便配置，也支持由数字和单位后缀组成的简化格式 (单位不区分大小写)。
     * <p>
     * <b>支持的单位:</b>
     * </p>
     * <ul>
     * <li>{@code s} - 秒</li>
     * <li>{@code m} - 分钟</li>
     * <li>{@code h} - 小时</li>
     * </ul>
     * <p>
     * <b>示例:</b>
     * </p>
     * <ul>
     * <li>{@code "60s"} 代表 60 秒。</li>
     * <li>{@code "5m"} 代表 5 分钟。</li>
     * </ul>
     * </li>
     * </ol>
     *
     * <p>
     * <b>默认值行为:</b><br>
     * 如果该值保持默认的空字符串 ({@code ""})，处理该注解的系统将会应用一个预设的、
     * 全局的默认超时时间(60s)。
     * </p>
     * 
     * @see Duration
     */
    String connectTimeout() default "";

    /**
     * <p>
     * 读取超时时间
     * </p>
     *
     * <p>
     * 该值最终会被解析为一个 {@link Duration} 对象。为了提供灵活性，
     * 支持以下两种字符串格式：
     * </p>
     *
     * <ol>
     * <li><b>标准 ISO-8601 格式</b>:
     * 这是由 {@link Duration#parse(CharSequence)} 支持的标准格式。
     * <p>
     * <b>示例:</b>
     * </p>
     * <ul>
     * <li>{@code "PT30S"} 代表 30 秒。</li>
     * <li>{@code "PT10M"} 代表 10 分钟。</li>
     * <li>{@code "PT2H"} 代表 2 小时。</li>
     * <li>{@code "P1D"} 代表 1 天。</li>
     * </ul>
     * </li>
     * <li><b>自定义简化格式</b>:
     * 为了方便配置，也支持由数字和单位后缀组成的简化格式 (单位不区分大小写)。
     * <p>
     * <b>支持的单位:</b>
     * </p>
     * <ul>
     * <li>{@code s} - 秒</li>
     * <li>{@code m} - 分钟</li>
     * <li>{@code h} - 小时</li>
     * </ul>
     * <p>
     * <b>示例:</b>
     * </p>
     * <ul>
     * <li>{@code "60s"} 代表 60 秒。</li>
     * <li>{@code "5m"} 代表 5 分钟。</li>
     * </ul>
     * </li>
     * </ol>
     *
     * <p>
     * <b>默认值行为:</b><br>
     * 如果该值保持默认的空字符串 ({@code ""})，处理该注解的系统将会应用一个预设的、
     * 全局的默认超时时间(60s)。
     * </p>
     * 
     * @see Duration
     */
    String readTimeout() default "";

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

    /**
     * 是否开启思考模式
     */
    TriState enableThinking() default TriState.UNSET;

}

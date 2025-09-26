package com.yomahub.liteflow.ai.workflow.dashscope.annotation;

import com.alibaba.dashscope.app.FlowStreamMode;
import com.google.gson.JsonObject;
import com.yomahub.liteflow.ai.annotation.AIComponent;
import com.yomahub.liteflow.ai.annotation.model.io.AIOutput;
import com.yomahub.liteflow.ai.engine.interact.pipeline.InteractContext;

import java.lang.annotation.*;
import java.util.List;

/**
 * DashScope 工作流注解。
 * <p>
 * 一个专门用于配置 DashScope 应用调用的注解，它基于 {@link AIComponent} 元注解构建。
 * 该注解旨在简化与 DashScope 工作流交互的 AI 组件的配置过程。
 *
 * <h3>使用示例</h3>
 *
 * <p>
 * 此示例演示了如何配置一个 DashScope 工作流并进行调用。
 * 工作流的输出将通过 {@link AIOutput} 直接映射到自定义上下文类中的 {@code result} 字段。
 * </p>
 * <pre>{@code
 * // 假设存在一个自定义上下文类：
 * // public class MyContext {
 * //     private String productTopic;
 * //     private ApplicationResult result;
 * //     // 标准的 getters 和 setters...
 * // }
 * @AIComponent(
 *      nodeId = "dsWorkflowNode",
 *      nodeName = "DashScope-Workflow",
 * )
 * @DashScopeWorkflow(
 *      appId = "app_123456",
 *      prompt = "为关于“{{productTopic}}”的产品生成一段简短且引人注目的描述"
 * )
 * @AIOutput(
 *      methodExpress = "setResult" // 将输出映射到 MyContext.setResult(ApplicationResult) 方法
 * )
 * public interface ProductDescGenerator {
 * }
 * }</pre>
 *
 * <h3>流式输出</h3>
 *
 * <p>
 *     要启用流式输出，请将 {@code stream} 属性设置为 {@code true}。这将允许逐步接收响应，适用于需要实时反馈的场景。
 *     并且请在上下文中设置 {@link com.yomahub.liteflow.ai.context.StreamHandler#onText(String, InteractContext)} 以处理流式数据。
 * </p>
 *
 * @author 苍镜月
 * @see <a href="https://help.aliyun.com/zh/model-studio/call-application-through-api">DashScope应用调用 API 参考</a>
 * @since 2.16.0
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DashScopeWorkflow {

    /**
     * 应用 ID
     * <p>必填参数，对应 DashScope ApplicationParam 中的 appId</p>
     * <p>{@link com.alibaba.dashscope.app.ApplicationParam#setAppId(String)}</p>
     */
    String appId();

    /**
     * 提示词(直接填写文本 or 使用上下文表达式)
     * <p>对应 ApplicationParam 中的 prompt</p>
     * <p>{@link com.alibaba.dashscope.app.ApplicationParam#setPrompt(String)}</p>
     *
     * <p>支持上下文表达式，开启请在开头结尾加上双花括号包裹，如: "{{prompt}}"</p>
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
    String prompt() default "";

    /**
     * 聊天历史的上下文路径表达式(需在上下文中设置对应的 List&lt;Message&gt; 对象)
     * <p>对应 ApplicationParam 中的 history</p>
     * <p>{@link com.alibaba.dashscope.app.ApplicationParam#setHistory(List)}</p>
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
     * 聊天消息的上下文路径表达式(需在上下文中设置对应的 List&lt;Message&gt; 对象)
     * <p>对应 ApplicationParam 中的 history</p>
     * <p>{@link com.alibaba.dashscope.app.ApplicationParam#setMessages(List)}</p>
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
    String messages() default "";

    /**
     * 会话 ID
     * <p>对应 ApplicationParam 中的 sessionId</p>
     * <p>{@link com.alibaba.dashscope.app.ApplicationParam#setSessionId(String)}</p>
     * <p>
     * 用于存储聊天历史的会话 ID
     * <p>注意：如果传递了 history，此参数将被忽略</p>
     */
    String sessionId() default "";

    /**
     * 是否返回 RAG 或插件处理详情
     *
     * <p>对应 ApplicationParam 中的 hasThoughts</p>
     * <p>{@link com.alibaba.dashscope.app.ApplicationParam#setHasThoughts(Boolean)}</p>
     */
    boolean hasThoughts() default false;

    /**
     * 流程或插件的额外参数(直接填写 json字符串 or 使用上下文表达式)
     * <p>对应 ApplicationParam 中的 bizParams</p>
     * <p>{@link com.alibaba.dashscope.app.ApplicationParam#setBizParams(JsonObject)}</p>
     *
     * <p>支持上下文表达式，开启请在开头结尾加上双花括号包裹，如: "{{bizParams}}"</p>
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
    String bizParams() default "";

    /**
     * 采样策略参数
     * <p>对应 ApplicationParam 中的 topP</p>
     * <p>{@link com.alibaba.dashscope.app.ApplicationParam#setTopP(Double)}</p>
     * <p>
     * 取值范围: 0.0 - 1.0
     */
    double topP() default -1.0;

    /**
     * Top-K 采样策略参数
     * <p>对应 ApplicationParam 中的 topK</p>
     * <p>{@link com.alibaba.dashscope.app.ApplicationParam#setTopK(Integer)}</p>
     */
    int topK() default -1;

    /**
     * 随机种子
     * <p>对应 ApplicationParam 中的 seed</p>
     * <p>{@link com.alibaba.dashscope.app.ApplicationParam#setSeed(Integer)}</p>
     * <p>
     * 用于控制模型生成的随机性
     */
    int seed() default -1;

    /**
     * 温度参数
     * <p>对应 ApplicationParam 中的 temperature</p>
     * <p>{@link com.alibaba.dashscope.app.ApplicationParam#setTemperature(Float)}</p>
     * <p>
     * 用于控制随机性和多样性程度，取值范围: 0.0 - 2.0
     */
    float temperature() default -1.0f;

    /**
     * 是否开启流式输出
     * 当设置为 true 时，将使用流式 API 进行请求
     */
    boolean stream() default false;

    /**
     * 是否启用增量输出
     * <p>对应 ApplicationParam 中的 incrementalOutput</p>
     * <p>{@link com.alibaba.dashscope.app.ApplicationParam#setIncrementalOutput(Boolean)}</p>
     * <p>
     * 如果为 true，后续输出将默认包含之前输入的内容
     */
    boolean incrementalOutput() default false;

    /**
     * 长期记忆 ID (直接填写文本 or 使用上下文表达式)
     * <p>对应 ApplicationParam 中的 memoryId</p>
     * <p>{@link com.alibaba.dashscope.app.ApplicationParam#setMemoryId(String)}</p>
     * <p>
     * 用于存储终端用户和助手之间的长期上下文摘要
     *
     * <p>支持上下文表达式，开启请在开头结尾加上双花括号包裹，如: "{{memoryId}}"</p>
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
    String memoryId() default "";

    /**
     * 图片列表(会话文件id之间通过逗号隔开 or 使用上下文表达式)
     * <p>对应 ApplicationParam 中的 images</p>
     * <p>{@link com.alibaba.dashscope.app.ApplicationParam#setImages(List)}</p>
     *
     * <p>支持上下文表达式，开启请在开头结尾加上双花括号包裹，如: "{{images}}"</p>
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
    String images() default "";

    /**
     * RAG 选项配置
     * <p>对应 ApplicationParam 中的 ragOptions</p>
     * <p>{@link com.alibaba.dashscope.app.ApplicationParam#setRagOptions(com.alibaba.dashscope.app.RagOptions)}</p>
     */
    RagOptions ragOptions() default @RagOptions;

    /**
     * MCP 服务器列表(mcp服务器之间通过逗号隔开 or 使用上下文表达式)
     * <p>对应 ApplicationParam 中的 mcpServers</p>
     * <p>{@link com.alibaba.dashscope.app.ApplicationParam#setMcpServers(List)}</p>
     *
     * <p>支持上下文表达式，开启请在开头结尾加上双花括号包裹，如: "{{mcpServers}}"</p>
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
    String mcpServers() default "";

    /**
     * 是否启用网络搜索
     * <p>对应 ApplicationParam 中的 enableWebSearch</p>
     * <p>{@link com.alibaba.dashscope.app.ApplicationParam#setEnableWebSearch(Boolean)}</p>
     */
    boolean enableWebSearch() default false;

    /**
     * 是否启用系统时间
     * <p>对应 ApplicationParam 中的 enableSystemTime</p>
     * <p>{@link com.alibaba.dashscope.app.ApplicationParam#setEnableSystemTime(Boolean)}</p>
     */
    boolean enableSystemTime() default false;

    /**
     * 是否启用高级模型调用
     * <p>对应 ApplicationParam 中的 enablePremium</p>
     * <p>{@link com.alibaba.dashscope.app.ApplicationParam#setEnablePremium(Boolean)}</p>
     */
    boolean enablePremium() default false;

    /**
     * 对话轮数
     * <p>对应 ApplicationParam 中的 dialogRound</p>
     * <p>{@link com.alibaba.dashscope.app.ApplicationParam#setDialogRound(Integer)}</p>
     */
    int dialogRound() default -1;

    /**
     * 模型 ID
     * <p>对应 ApplicationParam 中的 modelId</p>
     * <p>{@link com.alibaba.dashscope.app.ApplicationParam#setModelId(String)}</p>
     */
    String modelId() default "";

    /**
     * 流程代理的流模式
     * <p>对应 ApplicationParam 中的 flowStreamMode</p>
     * <p>{@link com.alibaba.dashscope.app.ApplicationParam#setFlowStreamMode(com.alibaba.dashscope.app.FlowStreamMode)}</p>
     */
    FlowStreamMode flowStreamMode() default FlowStreamMode.FULL_THOUGHTS;

    /**
     * 是否启用思考模式
     * <p>对应 ApplicationParam 中的 enableThinking</p>
     * <p>{@link com.alibaba.dashscope.app.ApplicationParam#setEnableThinking(Boolean)}</p>
     */
    boolean enableThinking() default false;
}

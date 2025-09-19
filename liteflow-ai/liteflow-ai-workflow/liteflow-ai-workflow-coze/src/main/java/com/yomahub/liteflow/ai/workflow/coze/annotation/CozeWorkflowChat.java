package com.yomahub.liteflow.ai.workflow.coze.annotation;

import com.yomahub.liteflow.ai.util.KeyValue;

import java.lang.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * Coze对话流注解
 *
 * <p>使用示例：</p>
 * <pre>{@code
 * @CozeWorkflow(
 *     workflowId = "123456",
 *     appId = "app_123",
 *     botId = "bot_456",
 *     additionalMessages = "#{messages}",
 *     parameters = "#{workflowParams}"
 * )
 * public interface MyCozeWorkflow {
 *     String process(String input);
 * }
 * }</pre>
 *
 * @author 苍镜月
 * @since TODO
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CozeWorkflowChat {

    /**
     * 工作流 ID
     * <p>必填参数，对应 Coze API 中的 workflow_id</p>
     * <p>{@link com.coze.openapi.client.workflows.chat.WorkflowChatReq#setWorkflowID(String)}</p>
     */
    String workflowId();

    /**
     * 附加消息(需要在上下文中设置对应的 List&lt;Message&gt; 对象)
     * <p>对应 Coze API 中的 additional_messages</p>
     * <p>{@link com.coze.openapi.client.workflows.chat.WorkflowChatReq#setAdditionalMessages(List)}</p>
     *
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
    String additionalMessages();

    /**
     * 工作流开始节点的输入参数及取值 (value 支持上下文表达式)
     * <p>开启上下文表达式请在开头结尾加上双花括号包裹，如: "{{parameters}}"</p>
     * <p>对应 Coze API 中的 parameters</p>
     * <p>{@link com.coze.openapi.client.workflows.chat.WorkflowChatReq#setParameters(Map)}</p>
     */
    KeyValue[] parameters();

    /**
     * 应用 ID
     * <p>可选参数，对应 Coze API 中的 app_id</p>
     * <p>{@link com.coze.openapi.client.workflows.chat.WorkflowChatReq#setAppID(String)}</p>
     */
    String appId() default "";

    /**
     * Bot ID
     * <p>可选参数，对应 Coze API 中的 bot_id</p>
     * <p>{@link com.coze.openapi.client.workflows.chat.WorkflowChatReq#setBotID(String)}</p>
     */
    String botId() default "";

    /**
     * 会话ID
     * <p>可选参数，对应 Coze API 中的 conversation_id</p>
     * <p>{@link com.coze.openapi.client.workflows.chat.WorkflowChatReq#setConversationID(String)}</p>
     */
    String conversationId() default "";

    /**
     * 用于指定一些额外的字段, 例如某些插件 会隐式用到的经纬度等字段。(value 支持上下文表达式)
     * <p>开启上下文表达式请在开头结尾加上双花括号包裹，如: "{{parameters}}"</p>
     * <p>对应 Coze API 中的 ext</p>
     * <p>{@link com.coze.openapi.client.workflows.chat.WorkflowChatReq#setExt(Map)}</p>
     */
    KeyValue[] ext() default {};

    /**
     * 连接超时时间，单位毫秒，默认-1表示不设置
     */
    int connectTimeout() default -1;

    /**
     * 读取超时时间，单位毫秒，默认-1表示不设置
     */
    int readTimeout() default -1;

    /**
     * 写入超时时间，单位毫秒，默认-1表示不设置
     */
    int writeTimeout() default -1;

    /**
     * 客户端标识符，用于区分不同的客户或应用，默认空字符串表示不设置
     */
    String customerToken() default "";
}

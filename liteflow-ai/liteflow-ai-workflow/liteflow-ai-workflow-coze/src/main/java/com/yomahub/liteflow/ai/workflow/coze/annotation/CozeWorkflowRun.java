package com.yomahub.liteflow.ai.workflow.coze.annotation;

import com.yomahub.liteflow.ai.util.KeyValue;

import java.lang.annotation.*;
import java.util.Map;

/**
 * Coze工作流注解
 *
 * @author 苍镜月
 * @since 2.16.0
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CozeWorkflowRun {

    /**
     * 工作流 ID
     * <p>必填参数，对应 Coze API 中的 workflow_id</p>
     * <p>{@link com.coze.openapi.client.workflows.chat.WorkflowChatReq#setWorkflowID(String)}</p>
     */
    String workflowId();

    /**
     * 工作流开始节点的输入参数及取值 (value 支持上下文表达式)
     * <p>开启上下文表达式请在开头结尾加上双花括号包裹，如: "{{parameters}}"</p>
     * <p>对应 Coze API 中的 parameters</p>
     * <p>{@link com.coze.openapi.client.workflows.chat.WorkflowChatReq#setParameters(Map)}</p>
     */
    KeyValue[] parameters() default {};

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
     * 用于指定一些额外的字段, 例如某些插件 会隐式用到的经纬度等字段。(value 支持上下文表达式)
     * <p>开启上下文表达式请在开头结尾加上双花括号包裹，如: "{{parameters}}"</p>
     * <p>对应 Coze API 中的 ext</p>
     * <p>{@link com.coze.openapi.client.workflows.chat.WorkflowChatReq#setExt(Map)}</p>
     */
    KeyValue[] ext() default {};

    /**
     * 是否开启流式输出
     * 当设置为 true 时，将使用流式 API 进行请求
     */
    boolean stream() default false;

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

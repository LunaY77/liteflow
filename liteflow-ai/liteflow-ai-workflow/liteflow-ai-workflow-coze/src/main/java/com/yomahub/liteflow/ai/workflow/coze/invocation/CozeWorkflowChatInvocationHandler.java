package com.yomahub.liteflow.ai.workflow.coze.invocation;

import cn.hutool.core.util.StrUtil;
import com.coze.openapi.client.chat.model.ChatEvent;
import com.coze.openapi.client.workflows.chat.WorkflowChatReq;
import com.coze.openapi.service.auth.TokenAuth;
import com.coze.openapi.service.config.Consts;
import com.coze.openapi.service.service.CozeAPI;
import com.yomahub.liteflow.ai.context.ChatContext;
import com.yomahub.liteflow.ai.engine.interact.pipeline.InteractContext;
import com.yomahub.liteflow.ai.parse.context.ProcessorContext;
import com.yomahub.liteflow.ai.proxy.invocation.AbstractAIInvocationHandler;
import com.yomahub.liteflow.ai.util.SpringUtil;
import com.yomahub.liteflow.ai.workflow.coze.annotation.CozeWorkflowChat;
import com.yomahub.liteflow.ai.workflow.coze.config.CozeWorkflowProperty;
import com.yomahub.liteflow.ai.workflow.coze.util.KeyValueUtil;
import com.yomahub.liteflow.ai.workflow.coze.wrap.CozeWorkflowChatProxyWrapBean;
import io.reactivex.Flowable;

import java.util.List;
import java.util.Map;

import static com.yomahub.liteflow.ai.util.SetUtil.setIfPresent;

/**
 * Coze 对话流 调用处理器
 *
 * @author 苍镜月
 * @since 2.16.0
 */
public class CozeWorkflowChatInvocationHandler extends AbstractAIInvocationHandler<CozeWorkflowChatProxyWrapBean> {

    public CozeWorkflowChatInvocationHandler(CozeWorkflowChatProxyWrapBean wrapBean) {
        super(wrapBean);
    }

    @Override
    protected Object doExecuteAIProcess(ProcessorContext<?> processorContext, Object[] args) {
        CozeWorkflowChat annotation = wrapBean.getAnnotation();

        CozeWorkflowProperty property = SpringUtil.getBean(CozeWorkflowProperty.class);

        CozeAPI api = new CozeAPI.Builder()
                .auth(new TokenAuth(property.getApiKey()))
                .baseURL(
                        StrUtil.isBlank(property.getBaseUrl()) ?
                                Consts.COZE_CN_BASE_URL :
                                property.getBaseUrl()
                )
                .build();

        WorkflowChatReq.WorkflowChatReqBuilder<?, ?> builder = WorkflowChatReq.builder();

        setIfPresent(builder::workflowID, annotation.workflowId());

        setIfPresent(builder::additionalMessages, annotation.additionalMessages(), processorContext, List.class);

        if (annotation.parameters().length > 0) {
            Map<String, Object> paramMap = KeyValueUtil.buildObjectMapFromKeyValue(annotation.parameters(), processorContext);
            if (!paramMap.isEmpty()) {
                builder.parameters(paramMap);
            }
        }

        setIfPresent(builder::appID, annotation.appId());

        setIfPresent(builder::botID, annotation.botId());

        setIfPresent(builder::conversationID, annotation.conversationId());

        if (annotation.ext().length > 0) {
            Map<String, String> extMap = KeyValueUtil.buildStringMapFromKeyValue(annotation.ext(), processorContext);
            if (!extMap.isEmpty()) {
                builder.ext(extMap);
            }
        }

        setIfPresent(builder::connectTimeout, annotation.connectTimeout());

        setIfPresent(builder::readTimeout, annotation.readTimeout());

        setIfPresent(builder::writeTimeout, annotation.writeTimeout());

        setIfPresent(builder::customerToken, annotation.customerToken());

        Flowable<ChatEvent> res = api.workflows().chat().stream(builder.build());
        InteractContext context = new InteractContext();
        res.blockingForEach(chunk -> {
            ChatContext chatContext = processorContext.getChatContext();
            chatContext.getStreamHandler().onText(chunk.getMessage().getContent(), context);
        });
        return null;
    }

    @Override
    protected void checkValidation(ProcessorContext<?> processorContext) {
        // 空实现
    }
}

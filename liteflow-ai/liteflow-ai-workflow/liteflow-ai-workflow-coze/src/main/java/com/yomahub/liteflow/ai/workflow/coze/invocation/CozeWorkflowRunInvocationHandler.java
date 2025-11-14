package com.yomahub.liteflow.ai.workflow.coze.invocation;

import com.yomahub.liteflow.ai.parse.context.ProcessorContext;
import com.yomahub.liteflow.ai.proxy.invocation.AbstractAIInvocationHandler;
import com.yomahub.liteflow.ai.workflow.coze.wrap.CozeWorkflowRunProxyWrapBean;

/**
 * Coze 工作流 调用处理器
 *
 * @author 苍镜月
 * @since 2.16.0
 */

public class CozeWorkflowRunInvocationHandler extends AbstractAIInvocationHandler<CozeWorkflowRunProxyWrapBean> {

    public CozeWorkflowRunInvocationHandler(CozeWorkflowRunProxyWrapBean wrapBean) {
        super(wrapBean);
    }

    @Override
    protected Object doExecuteAIProcess(ProcessorContext<?> processorContext, Object[] args) {
        return null;
//        CozeWorkflowRun annotation = wrapBean.getAnnotation();
//
//        CozeWorkflowProperty property = SpringUtil.getBean(CozeWorkflowProperty.class);
//
//        CozeAPI api = new CozeAPI.Builder()
//                .auth(new TokenAuth(property.getApiKey()))
//                .baseURL(
//                        StrUtil.isBlank(property.getBaseUrl()) ?
//                                Consts.COZE_CN_BASE_URL :
//                                property.getBaseUrl()
//                )
//                .build();
//
//        RunWorkflowReq.RunWorkflowReqBuilder<?, ?> builder = RunWorkflowReq.builder();
//
//        setIfPresent(builder::workflowID, annotation.workflowId());
//
//        if (annotation.parameters().length > 0) {
//            Map<String, Object> paramMap = KeyValueUtil.buildObjectMapFromKeyValue(annotation.parameters(), processorContext);
//            if (!paramMap.isEmpty()) {
//                builder.parameters(paramMap);
//            }
//        }
//
//        setIfPresent(builder::appID, annotation.appId());
//
//        setIfPresent(builder::botID, annotation.botId());
//
//        if (annotation.ext().length > 0) {
//            Map<String, String> extMap = KeyValueUtil.buildStringMapFromKeyValue(annotation.ext(), processorContext);
//            if (!extMap.isEmpty()) {
//                builder.ext(extMap);
//            }
//        }
//
//        setIfPresent(builder::connectTimeout, annotation.connectTimeout());
//
//        setIfPresent(builder::readTimeout, annotation.readTimeout());
//
//        setIfPresent(builder::writeTimeout, annotation.writeTimeout());
//
//        setIfPresent(builder::customerToken, annotation.customerToken());
//
//        WorkflowRunService runs = api.workflows().runs();
//
//        if (annotation.stream()) {
//            Flowable<WorkflowEvent> res = runs.stream(builder.build());
//            InteractContext context = new InteractContext();
//            res.blockingForEach(chunk -> {
//                ChatContext chatContext = processorContext.getChatContext();
//                chatContext.getStreamHandler().onText(chunk.getMessage().getContent(), context);
//            });
//            return null;
//        } else {
//            return runs.create(builder.build());
//        }
    }

    @Override
    protected void checkValidation(ProcessorContext<?> processorContext) {
        // 空实现
    }
}

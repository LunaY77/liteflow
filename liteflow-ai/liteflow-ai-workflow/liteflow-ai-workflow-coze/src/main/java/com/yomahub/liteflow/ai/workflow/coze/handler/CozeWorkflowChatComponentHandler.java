package com.yomahub.liteflow.ai.workflow.coze.handler;

import com.yomahub.liteflow.ai.annotation.AIComponent;
import com.yomahub.liteflow.ai.proxy.handler.WorkflowComponentHandler;
import com.yomahub.liteflow.ai.proxy.wrap.AIProxyWrapBean;
import com.yomahub.liteflow.ai.workflow.coze.annotation.CozeWorkflowChat;
import com.yomahub.liteflow.ai.workflow.coze.invocation.CozeWorkflowChatInvocationHandler;
import com.yomahub.liteflow.ai.workflow.coze.wrap.CozeWorkflowChatProxyWrapBean;

import java.lang.reflect.InvocationHandler;

/**
 * Coze 对话流组件处理器
 *
 * @author 苍镜月
 * @since 2.16.0
 */
public class CozeWorkflowChatComponentHandler extends WorkflowComponentHandler<CozeWorkflowChat> {

    @Override
    public Class<CozeWorkflowChat> getSupportedAnnotationType() {
        return CozeWorkflowChat.class;
    }

    @Override
    protected AIProxyWrapBean<CozeWorkflowChat> createWrapBean(AIComponent aiComponent, CozeWorkflowChat annotation,
                                                               Class<?> interfaceClass, String beanName) {
        return new CozeWorkflowChatProxyWrapBean(aiComponent, annotation, interfaceClass, beanName);
    }

    @Override
    protected InvocationHandler getInvocationHandler(AIProxyWrapBean<CozeWorkflowChat> wrapBean) {
        return new CozeWorkflowChatInvocationHandler((CozeWorkflowChatProxyWrapBean) wrapBean);
    }
}

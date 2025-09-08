package com.yomahub.liteflow.ai.workflow.coze.handler;

import com.yomahub.liteflow.ai.annotation.AIComponent;
import com.yomahub.liteflow.ai.proxy.handler.WorkflowComponentHandler;
import com.yomahub.liteflow.ai.proxy.wrap.AIProxyWrapBean;
import com.yomahub.liteflow.ai.workflow.coze.annotation.CozeWorkflowRun;
import com.yomahub.liteflow.ai.workflow.coze.invocation.CozeWorkflowRunInvocationHandler;
import com.yomahub.liteflow.ai.workflow.coze.wrap.CozeWorkflowRunProxyWrapBean;

import java.lang.reflect.InvocationHandler;

/**
 * Coze 工作流组件处理器
 *
 * @author 苍镜月
 * @since TODO
 */
public class CozeWorkflowRunComponentHandler extends WorkflowComponentHandler<CozeWorkflowRun> {

    @Override
    public Class<CozeWorkflowRun> getSupportedAnnotationType() {
        return CozeWorkflowRun.class;
    }

    @Override
    protected AIProxyWrapBean<CozeWorkflowRun> createWrapBean(AIComponent aiComponent, CozeWorkflowRun annotation,
                                                              Class<?> interfaceClass, String beanName) {
        return new CozeWorkflowRunProxyWrapBean(aiComponent, annotation, interfaceClass, beanName);
    }

    @Override
    protected InvocationHandler getInvocationHandler(AIProxyWrapBean<CozeWorkflowRun> wrapBean) {
        return new CozeWorkflowRunInvocationHandler((CozeWorkflowRunProxyWrapBean) wrapBean);
    }
}

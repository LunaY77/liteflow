package com.yomahub.liteflow.ai.workflow.dashscope.handler;

import com.yomahub.liteflow.ai.annotation.AIComponent;
import com.yomahub.liteflow.ai.proxy.handler.WorkflowComponentHandler;
import com.yomahub.liteflow.ai.proxy.wrap.AIProxyWrapBean;
import com.yomahub.liteflow.ai.workflow.dashscope.annotation.DashScopeWorkflow;
import com.yomahub.liteflow.ai.workflow.dashscope.invocation.DashScopeWorkflowInvocationHandler;
import com.yomahub.liteflow.ai.workflow.dashscope.wrap.DashScopeWorkflowProxyWrapBean;

import java.lang.reflect.InvocationHandler;

/**
 * DashScope 工作流组件处理器
 *
 * @author 苍镜月
 * @since TODO
 */
public class DashScopeWorkflowComponentHandler extends WorkflowComponentHandler<DashScopeWorkflow> {

    @Override
    public Class<DashScopeWorkflow> getSupportedAnnotationType() {
        return DashScopeWorkflow.class;
    }

    @Override
    protected AIProxyWrapBean<DashScopeWorkflow> createWrapBean(AIComponent aiComponent, DashScopeWorkflow annotation,
                                                                Class<?> interfaceClass, String beanName) {
        return new DashScopeWorkflowProxyWrapBean(aiComponent, annotation, interfaceClass, beanName);
    }

    @Override
    protected InvocationHandler getInvocationHandler(AIProxyWrapBean<DashScopeWorkflow> wrapBean) {
        return new DashScopeWorkflowInvocationHandler((DashScopeWorkflowProxyWrapBean) wrapBean);
    }
}

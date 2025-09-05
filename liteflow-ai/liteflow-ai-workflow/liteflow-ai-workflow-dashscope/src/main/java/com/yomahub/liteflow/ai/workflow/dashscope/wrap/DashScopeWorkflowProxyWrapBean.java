package com.yomahub.liteflow.ai.workflow.dashscope.wrap;

import com.yomahub.liteflow.ai.annotation.AIComponent;
import com.yomahub.liteflow.ai.proxy.wrap.AIProxyWrapBean;
import com.yomahub.liteflow.ai.workflow.dashscope.annotation.DashScopeWorkflow;

/**
 * DashScope 工作流代理包装Bean
 * 
 * @author 苍镜月
 * @since TODO
 */
public class DashScopeWorkflowProxyWrapBean extends AIProxyWrapBean<DashScopeWorkflow> {

    public DashScopeWorkflowProxyWrapBean(AIComponent aiComponent, DashScopeWorkflow annotation,
            Class<?> interfaceClass, String beanName) {
        super(aiComponent, annotation, interfaceClass, beanName);
    }
}

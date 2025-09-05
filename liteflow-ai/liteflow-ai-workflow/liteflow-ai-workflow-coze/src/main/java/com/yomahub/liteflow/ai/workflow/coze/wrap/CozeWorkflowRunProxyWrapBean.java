package com.yomahub.liteflow.ai.workflow.coze.wrap;

import com.yomahub.liteflow.ai.annotation.AIComponent;
import com.yomahub.liteflow.ai.proxy.wrap.AIProxyWrapBean;
import com.yomahub.liteflow.ai.workflow.coze.annotation.CozeWorkflowRun;

/**
 * Coze工作流运行代理包装Bean
 *
 * @author 苍镜月
 * @since TODO
 */

public class CozeWorkflowRunProxyWrapBean extends AIProxyWrapBean<CozeWorkflowRun> {

    public CozeWorkflowRunProxyWrapBean(AIComponent aiComponent, CozeWorkflowRun annotation,
                                        Class<?> interfaceClass, String beanName) {
        super(aiComponent, annotation, interfaceClass, beanName);
    }
}

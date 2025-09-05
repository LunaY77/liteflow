package com.yomahub.liteflow.ai.workflow.coze.wrap;

import com.yomahub.liteflow.ai.annotation.AIComponent;
import com.yomahub.liteflow.ai.proxy.wrap.AIProxyWrapBean;
import com.yomahub.liteflow.ai.workflow.coze.annotation.CozeWorkflowChat;

/**
 * Coze 对话流代理包装Bean
 *
 * @author 苍镜月
 * @since TODO
 */
public class CozeWorkflowChatProxyWrapBean extends AIProxyWrapBean<CozeWorkflowChat> {

    public CozeWorkflowChatProxyWrapBean(AIComponent aiComponent, CozeWorkflowChat annotation,
                                         Class<?> interfaceClass, String beanName) {
        super(aiComponent, annotation, interfaceClass, beanName);
    }
}

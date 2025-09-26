package com.yomahub.liteflow.ai.proxy.wrap;

import com.yomahub.liteflow.ai.annotation.model.node.AIChat;
import com.yomahub.liteflow.ai.annotation.AIComponent;

/**
 * AI聊天节点包装 Bean
 *
 * @author 苍镜月
 * @since 2.16.0
 */

public class ChatProxyWrapBean extends AIProxyWrapBean<AIChat> {

    public ChatProxyWrapBean(AIComponent aiComponent, AIChat annotation, Class<?> interfaceClass, String beanName) {
        super(aiComponent, annotation, interfaceClass, beanName);
    }
}

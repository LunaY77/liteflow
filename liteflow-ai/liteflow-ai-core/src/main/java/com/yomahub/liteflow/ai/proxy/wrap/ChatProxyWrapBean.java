package com.yomahub.liteflow.ai.proxy.wrap;

import com.yomahub.liteflow.ai.annotation.AIChat;
import com.yomahub.liteflow.ai.annotation.AIComponent;

/**
 * AI聊天节点包装 Bean
 *
 * @author 苍镜月
 * @since TODO
 */

public class ChatProxyWrapBean extends AIProxyWrapBean<AIChat> {

    private boolean streaming;

    public ChatProxyWrapBean(AIComponent aiComponent, AIChat annotation, Class<?> interfaceClass, String beanName) {
        super(aiComponent, annotation, interfaceClass, beanName);
    }

    public boolean isStreaming() {
        return streaming;
    }

    public void setStreaming(boolean streaming) {
        this.streaming = streaming;
    }
}

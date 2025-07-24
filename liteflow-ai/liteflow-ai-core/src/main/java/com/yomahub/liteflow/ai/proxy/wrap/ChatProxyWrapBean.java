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

    private String systemPrompt;

    private String userPrompt;

    private boolean streaming;

    public ChatProxyWrapBean() {
        super();
    }

    public ChatProxyWrapBean(AIComponent aiComponent, AIChat annotation,
                           Class<?> interfaceClass, String beanName) {
        super(aiComponent, interfaceClass, beanName);
        // TODO anno
    }

    public String getSystemPrompt() {
        return systemPrompt;
    }

    public String getUserPrompt() {
        return userPrompt;
    }

    public boolean isStreaming() {
        return streaming;
    }
}

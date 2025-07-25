package com.yomahub.liteflow.ai.parse.anno;

import com.yomahub.liteflow.ai.annotation.AIChat;
import com.yomahub.liteflow.ai.domain.enums.AITypeEnum;
import com.yomahub.liteflow.ai.parse.AbstractAnnotationProcessor;
import com.yomahub.liteflow.ai.parse.ProcessorContext;
import com.yomahub.liteflow.ai.proxy.wrap.ChatProxyWrapBean;
import com.yomahub.liteflow.ai.util.SetUtil;

/**
 * TODO
 *
 * @author 苍镜月
 * @since TODO
 */

public class ChatAnnotationProcessor extends AbstractAnnotationProcessor<AIChat, ChatProxyWrapBean> {

    @Override
    public void postProcessBeforeTrigger(AIChat annotation, ProcessorContext<ChatProxyWrapBean> context) {
        ChatProxyWrapBean wrapBean = context.getWrapBean();

        SetUtil.setIfPresent(wrapBean::setStreaming, annotation.streaming());

        SetUtil.setIfPresent(wrapBean::setUserPrompt, annotation.userPrompt());

        SetUtil.setIfPresent(wrapBean::setSystemPrompt, annotation.systemPrompt());
    }

    @Override
    public void postProcessAfterTrigger(AIChat annotation, ProcessorContext<ChatProxyWrapBean> context) {

    }

    @Override
    protected AITypeEnum getAIType() {
        return AITypeEnum.CHAT;
    }
}

package com.yomahub.liteflow.ai.parse.anno;

import com.yomahub.liteflow.ai.annotation.AIChat;
import com.yomahub.liteflow.ai.domain.enums.AITypeEnum;
import com.yomahub.liteflow.ai.parse.AbstractAnnotationProcessor;
import com.yomahub.liteflow.ai.parse.ProcessorContext;
import com.yomahub.liteflow.ai.proxy.wrap.ChatProxyWrapBean;
import com.yomahub.liteflow.ai.util.SetUtil;

/**
 * AI聊天注解处理器
 *
 * @author 苍镜月
 * @since TODO
 */
public class ChatAnnotationProcessor extends AbstractAnnotationProcessor<AIChat, ChatProxyWrapBean> {

    @Override
    public void postProcessBeforeTrigger(AIChat annotation, ProcessorContext<ChatProxyWrapBean> context) {
        ChatProxyWrapBean wrapBean = context.getWrapBean();

        // 设置基本属性
        SetUtil.setIfPresent(wrapBean::setStreaming, annotation.streaming());

        // 处理系统提示词
        parsePrompt(annotation.systemPrompt(), context, wrapBean::setSystemPrompt);

        // 处理用户提示词
        parsePrompt(annotation.userPrompt(), context, wrapBean::setUserPrompt);
    }

    @Override
    public void postProcessAfterTrigger(AIChat annotation, ProcessorContext<ChatProxyWrapBean> context, Object result) {
        // TODO: 处理输出参数绑定
    }

    @Override
    protected AITypeEnum getAIType() {
        return AITypeEnum.CHAT;
    }
}

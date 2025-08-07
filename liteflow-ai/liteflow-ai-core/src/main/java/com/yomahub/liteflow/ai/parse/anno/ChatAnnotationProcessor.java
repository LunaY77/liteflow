package com.yomahub.liteflow.ai.parse.anno;

import com.yomahub.liteflow.ai.annotation.AIChat;
import com.yomahub.liteflow.ai.domain.dto.ParsedChatAnnotationConfig;
import com.yomahub.liteflow.ai.domain.enums.AITypeEnum;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatRequest;
import com.yomahub.liteflow.ai.parse.AbstractAnnotationProcessor;
import com.yomahub.liteflow.ai.parse.context.ContextAccessor;
import com.yomahub.liteflow.ai.parse.context.ProcessorContext;
import com.yomahub.liteflow.ai.util.SetUtil;

/**
 * AI聊天注解处理器
 *
 * @author 苍镜月
 * @since TODO
 */
public class ChatAnnotationProcessor extends AbstractAnnotationProcessor<AIChat, ParsedChatAnnotationConfig> {

    @Override
    public void postProcessBeforeTrigger(AIChat annotation, ProcessorContext<ParsedChatAnnotationConfig> context) {
        // 解析模型配置
        parseModelConfig(context);

        // 注解解析配置
        ParsedChatAnnotationConfig annotationConfig = new ParsedChatAnnotationConfig();
        context.setParsedAnnotationConfig(annotationConfig);

        // 设置基本属性
        SetUtil.setIfPresent(annotationConfig::setStreaming, annotation.streaming());
        SetUtil.setIfPresent(annotationConfig::setTransportType, annotation.transportType());

        // 处理系统提示词
        parsePrompt(annotation.systemPrompt(), context, annotationConfig::setSystemPrompt);

        // 处理用户提示词
        parsePrompt(annotation.userPrompt(), context, annotationConfig::setUserPrompt);

        // 处理结构化输出参数绑定
        parseOutput(context);

        // 从上下文获取动态 ChatRequest
        ChatRequest contextChatRequest = ContextAccessor.searchContextByExpression(annotation.getChatRequest(), context);

        // 组装 ChatRequest
        CHAT_REQUEST_ASSEMBLER.assemble(contextChatRequest, context);
    }

    @Override
    public void postProcessAfterTrigger(ProcessorContext<ParsedChatAnnotationConfig> context, Object result) {
        mapOutput2Context(context, result);
    }

    @Override
    protected AITypeEnum getAIType() {
        return AITypeEnum.CHAT;
    }
}

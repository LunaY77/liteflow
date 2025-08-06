package com.yomahub.liteflow.ai.parse.anno;

import com.yomahub.liteflow.ai.annotation.AIClassify;
import com.yomahub.liteflow.ai.domain.dto.ParsedAnnotationConfig;
import com.yomahub.liteflow.ai.domain.enums.AITypeEnum;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatRequest;
import com.yomahub.liteflow.ai.parse.AbstractAnnotationProcessor;
import com.yomahub.liteflow.ai.parse.context.ContextAccessor;
import com.yomahub.liteflow.ai.parse.context.ProcessorContext;
import com.yomahub.liteflow.ai.proxy.wrap.ClassifyProxyWrapBean;
import com.yomahub.liteflow.ai.util.SetUtil;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * AI 意图识别注解处理器
 *
 * @author 苍镜月
 * @since TODO
 */

public class ClassifyAnnotationProcessor extends AbstractAnnotationProcessor<AIClassify, ClassifyProxyWrapBean> {

    @Override
    public void postProcessBeforeTrigger(AIClassify annotation, ProcessorContext<ClassifyProxyWrapBean> context) {
        // 解析模型配置
        parseModelConfig(context);

        ClassifyProxyWrapBean wrapBean = context.getWrapBean();
        ParsedAnnotationConfig annotationConfig = context.getParsedAnnotationConfig();

        SetUtil.setIfPresent(wrapBean::setCategories, Arrays.stream(annotation.categories()).collect(Collectors.toList()));

        SetUtil.setIfPresent(wrapBean::setMultiLabel, annotation.multiLabel());

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
    public void postProcessAfterTrigger(ProcessorContext<ClassifyProxyWrapBean> context, Object result) {
        mapOutput2Context(context, result);
    }

    @Override
    protected AITypeEnum getAIType() {
        return AITypeEnum.CLASSIFY;
    }
}

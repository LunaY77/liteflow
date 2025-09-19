package com.yomahub.liteflow.ai.parse.anno;

import com.yomahub.liteflow.ai.annotation.model.node.AIClassify;
import com.yomahub.liteflow.ai.domain.dto.ParsedClassifyAnnotationConfig;
import com.yomahub.liteflow.ai.domain.enums.AITypeEnum;
import com.yomahub.liteflow.ai.parse.AbstractAnnotationProcessor;
import com.yomahub.liteflow.ai.parse.context.ProcessorContext;
import com.yomahub.liteflow.ai.util.SetUtil;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * AI 意图识别注解处理器
 *
 * @author 苍镜月
 * @since TODO
 */

public class ClassifyAnnotationProcessor extends AbstractAnnotationProcessor<AIClassify, ParsedClassifyAnnotationConfig> {

    @Override
    public void postProcessBeforeTrigger(AIClassify annotation, ProcessorContext<ParsedClassifyAnnotationConfig> context) {
        // 解析模型配置
        parseModelConfig(context);

        // 注解解析配置
        ParsedClassifyAnnotationConfig annotationConfig = new ParsedClassifyAnnotationConfig();
        context.setParsedAnnotationConfig(annotationConfig);

        SetUtil.setIfPresent(annotationConfig::setCategories, Arrays.stream(annotation.categories()).collect(Collectors.toList()));

        SetUtil.setIfPresent(annotationConfig::setMultiLabel, annotation.multiLabel());

        // 处理历史消息
        parseHistory(annotation.history(), context, annotationConfig::setHistory);

        // 处理系统提示词
        parsePrompt(annotation.systemPrompt(), context, annotationConfig::setSystemPrompt);

        // 处理用户提示词
        parsePrompt(annotation.userPrompt(), context, annotationConfig::setUserPrompt);

        // 处理结构化输出参数绑定
        parseOutput(context);

        // 组装 ChatRequest
        CLASSIFY_REQUEST_ASSEMBLER.assemble(context);
    }

    @Override
    public void postProcessAfterTrigger(ProcessorContext<ParsedClassifyAnnotationConfig> context, Object result) {
        mapOutput2Context(context, result);
    }

    @Override
    protected AITypeEnum getAIType() {
        return AITypeEnum.CLASSIFY;
    }
}

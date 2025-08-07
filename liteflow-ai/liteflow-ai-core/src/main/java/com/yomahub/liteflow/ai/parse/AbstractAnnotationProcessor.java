package com.yomahub.liteflow.ai.parse;

import cn.hutool.core.util.StrUtil;
import com.yomahub.liteflow.ai.annotation.AIComponent;
import com.yomahub.liteflow.ai.annotation.AIInput;
import com.yomahub.liteflow.ai.annotation.AIOutput;
import com.yomahub.liteflow.ai.domain.dto.ModelConfigAggregator;
import com.yomahub.liteflow.ai.domain.dto.ParsedAnnotationConfig;
import com.yomahub.liteflow.ai.domain.dto.ParsedChatAnnotationConfig;
import com.yomahub.liteflow.ai.domain.dto.ParsedClassifyAnnotationConfig;
import com.yomahub.liteflow.ai.domain.enums.AITypeEnum;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatRequest;
import com.yomahub.liteflow.ai.engine.model.output.ResponseType;
import com.yomahub.liteflow.ai.parse.assemble.ChatRequestAssembler;
import com.yomahub.liteflow.ai.parse.assemble.ClassifyRequestAssembler;
import com.yomahub.liteflow.ai.parse.assemble.RequestAssembler;
import com.yomahub.liteflow.ai.parse.context.ContextAccessor;
import com.yomahub.liteflow.ai.parse.context.ProcessorContext;
import com.yomahub.liteflow.ai.parse.prompt.PromptTemplateParser;
import com.yomahub.liteflow.ai.parse.prompt.resource.PromptResource;
import com.yomahub.liteflow.log.LFLog;
import com.yomahub.liteflow.log.LFLoggerManager;
import org.springframework.beans.factory.InitializingBean;

import java.lang.annotation.Annotation;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * 抽象粉色奶龙处理器
 *
 * @author 苍镜月
 * @since TODO
 */

public abstract class AbstractAnnotationProcessor<A extends Annotation, C extends ParsedAnnotationConfig> implements AnnotationProcessor<A, C>, InitializingBean {

    protected final LFLog LOG = LFLoggerManager.getLogger(this.getClass());

    // ==== RequestAssembler ====
    protected static final RequestAssembler<ChatRequest, ParsedChatAnnotationConfig> CHAT_REQUEST_ASSEMBLER = new ChatRequestAssembler();
    protected static final RequestAssembler<ChatRequest, ParsedClassifyAnnotationConfig> CLASSIFY_REQUEST_ASSEMBLER = new ClassifyRequestAssembler();
    // ==== RequestAssembler ====

    @Override
    public void afterPropertiesSet() throws Exception {
        AnnotationParser.register(getAIType().getCode(), this);
    }

    /**
     * 获取对应的 AI 节点类型
     *
     * @return {@link AITypeEnum}
     */
    protected abstract AITypeEnum getAIType();

    /**
     * 解析 {@link AIComponent} 模型配置
     *
     * @param context 处理器上下文
     */
    protected void parseModelConfig(ProcessorContext<C> context) {
        // 获取模型配置聚合器
        AIComponent aiComponent = context.getAiComponent();
        if (Objects.isNull(aiComponent)) {
            LOG.warn("AIComponent annotation is null, using default model configuration.");
            context.setConfigAggregator(ModelConfigAggregator.getDefault());
            return;
        }
        context.setConfigAggregator(ModelConfigAggregator.parseFromAnnotation(aiComponent));
    }

    /**
     * 解析提示词
     *
     * @param prompt      提示词
     * @param context     处理器上下文
     * @param setConsumer 设置方法，接受解析后的提示词内容
     */
    protected void parsePrompt(String prompt, ProcessorContext<C> context, Consumer<String> setConsumer) {
        if (StrUtil.isNotBlank(prompt)) {
            try {
                PromptResource resource = context.getResourceLoader().getResource(prompt);

                String content = resource.getContent();

                // 处理模板占位符
                content = PromptTemplateParser.parseTemplate(
                        content,
                        Optional.ofNullable(context.getAiInputAnno())
                                .map(AIInput::mapping)
                                .orElse(null),
                        context
                );

                setConsumer.accept(content);
                LOG.info("Loaded prompt from resource: {}", resource.getDescription());
            } catch (Exception e) {
                LOG.warn("Failed to load prompt resource: {}, using original text", prompt, e);
                setConsumer.accept(prompt);
            }
        }
    }

    /**
     * 解析 {@link AIOutput} 的结构化输出参数
     *
     * @param context 处理器上下文
     */
    protected void parseOutput(ProcessorContext<C> context) {
        AIOutput outputAnno = context.getAiOutputAnno();
        if (Objects.isNull(outputAnno)) return;

        // 这里仅处理结构化输出相关的参数，其他参数交给 after 逻辑进行处理
        ParsedAnnotationConfig annotationConfig = context.getParsedAnnotationConfig();
        // 是否需要结构化输出
        if (Objects.equals(ResponseType.JSON, outputAnno.responseType())) {
            annotationConfig.setResponseType(ResponseType.JSON);
            // 设置输出实体类
            annotationConfig.setEntityClass(outputAnno.entityClass());
        } else {
            annotationConfig.setResponseType(ResponseType.TEXT);
            // 文本输出，设置 entityClass 为 String
            annotationConfig.setEntityClass(String.class);
        }
    }

    /**
     * 将处理结果映射到上下文中
     *
     * @param context 处理器上下文
     * @param result  处理结果
     */
    protected void mapOutput2Context(ProcessorContext<C> context, Object result) {
        if (Objects.isNull(result)) {
            LOG.warn("AI processing result is null, skipping context mapping.");
            return;
        }

        AIOutput outputAnno = context.getAiOutputAnno();
        if (Objects.isNull(outputAnno)) {
            LOG.warn("No AIOutput annotation found, skipping context mapping.");
            return;
        }

        // 将结果映射到上下文中
        String expression = outputAnno.methodExpress();
        if (StrUtil.isNotBlank(expression)) {
            ContextAccessor.setContextValueByExpression(expression, context, result);
            LOG.info("Mapped AI output to context with expression: {}", expression);
        } else {
            LOG.warn("AIOutput expression is blank, skipping context mapping.");
        }
    }
}

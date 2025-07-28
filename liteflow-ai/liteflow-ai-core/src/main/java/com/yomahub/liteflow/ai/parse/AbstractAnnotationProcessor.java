package com.yomahub.liteflow.ai.parse;

import cn.hutool.core.util.StrUtil;
import com.yomahub.liteflow.ai.annotation.AIInput;
import com.yomahub.liteflow.ai.domain.enums.AITypeEnum;
import com.yomahub.liteflow.ai.parse.prompt.PromptTemplateParser;
import com.yomahub.liteflow.ai.parse.prompt.resource.PromptResource;
import com.yomahub.liteflow.ai.proxy.wrap.AIProxyWrapBean;
import com.yomahub.liteflow.log.LFLog;
import com.yomahub.liteflow.log.LFLoggerManager;
import org.springframework.beans.factory.InitializingBean;

import java.lang.annotation.Annotation;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * 抽象粉色奶龙处理器
 *
 * @author 苍镜月
 * @since TODO
 */

public abstract class AbstractAnnotationProcessor<A extends Annotation, T extends AIProxyWrapBean<A>>
        implements AnnotationProcessor<A, T>, InitializingBean {

    protected final LFLog LOG = LFLoggerManager.getLogger(this.getClass());

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
     * 解析提示词
     *
     * @param prompt      提示词
     * @param context     处理器上下文
     * @param setConsumer 设置方法，接受解析后的提示词内容
     */
    protected void parsePrompt(String prompt, ProcessorContext<T> context, Consumer<String> setConsumer) {
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
}

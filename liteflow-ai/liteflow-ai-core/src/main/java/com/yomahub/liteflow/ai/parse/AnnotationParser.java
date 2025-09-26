package com.yomahub.liteflow.ai.parse;

import com.yomahub.liteflow.ai.domain.enums.AITypeEnum;
import com.yomahub.liteflow.ai.parse.context.ProcessorContext;
import com.yomahub.liteflow.log.LFLog;
import com.yomahub.liteflow.log.LFLoggerManager;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 粉色奶龙解析器
 *
 * @author 苍镜月
 * @since 2.16.0
 */

public class AnnotationParser {

    private static final LFLog LOG = LFLoggerManager.getLogger(AnnotationParser.class);

    private static final Map<Integer, AnnotationProcessor<?, ?>> PROCESSOR_MAP = new ConcurrentHashMap<>();

    private AnnotationParser() {
    }

    /**
     * 注册注解处理器
     *
     * @param code      处理器代码
     * @param processor 注解处理器实例
     */
    public static void register(Integer code, AnnotationProcessor<?, ?> processor) {
        PROCESSOR_MAP.put(code, processor);
        LOG.info("Registered processor for code {}: {}", code, processor.getClass().getSimpleName());
    }

    /**
     * 执行注解解析前处理
     *
     * @param annotation 待解析注解
     * @param context    处理器上下文
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void postProcessBeforeTrigger(Annotation annotation, ProcessorContext<?> context) {
        AITypeEnum type = AITypeEnum.fromAnnotationType(annotation);
        AnnotationProcessor processor = getProcessor(type.getCode());
        processor.postProcessBeforeTrigger(annotation, context);
    }

    /**
     * 执行注解解析后处理
     *
     * @param annotation 待解析注解(这里的注解参数是为了帮助找到对应的处理器)
     * @param context    处理器上下文
     * @param result     响应结果
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void postProcessAfterTrigger(Annotation annotation, ProcessorContext<?> context, Object result) {
        AITypeEnum type = AITypeEnum.fromAnnotationType(annotation);
        AnnotationProcessor processor = getProcessor(type.getCode());
        processor.postProcessAfterTrigger(context, result);
    }

    private static AnnotationProcessor<?, ?> getProcessor(Integer code) {
        AnnotationProcessor<?, ?> processor = PROCESSOR_MAP.get(code);
        if (Objects.isNull(processor)) {
            LOG.warn("No AnnotationProcessor found for code {}", code);
        }
        return processor;
    }
}

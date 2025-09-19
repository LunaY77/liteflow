package com.yomahub.liteflow.ai.parse.assemble;

import com.yomahub.liteflow.ai.context.ChatContext;
import com.yomahub.liteflow.ai.domain.dto.ModelConfigAggregator;
import com.yomahub.liteflow.ai.domain.dto.ParsedAnnotationConfig;
import com.yomahub.liteflow.ai.engine.model.ModelRequest;
import com.yomahub.liteflow.ai.parse.context.ProcessorContext;
import com.yomahub.liteflow.log.LFLog;
import com.yomahub.liteflow.log.LFLoggerManager;

/**
 * 抽象请求组装器
 *
 * @author 苍镜月
 * @since TODO
 */

public abstract class AbstractRequestAssembler<C extends ParsedAnnotationConfig> implements RequestAssembler<C> {

    protected final LFLog LOG = LFLoggerManager.getLogger(this.getClass());

    /**
     * 组装请求对象(最终请求对象存储于 {@link ProcessorContext} 中的 {@link ModelRequest} 属性中)
     *
     * @param context 处理器上下文
     */
    public final void assemble(ProcessorContext<C> context) {
        C annotationConfig = context.getParsedAnnotationConfig();
        ModelConfigAggregator config = context.getConfigAggregator();

        // 组装请求并设置到处理器上下文
        context.setModelRequest(doAssemble(annotationConfig, config, context.getChatContext()));
    }

    /**
     * 实际的请求组装逻辑
     *
     * @param annotationConfig 注解解析配置
     * @param config           模型配置聚合
     * @param context          chat上下文
     * @return 最终请求对象
     */
    protected abstract ModelRequest doAssemble(
            C annotationConfig,
            ModelConfigAggregator config,
            ChatContext context
    );
}

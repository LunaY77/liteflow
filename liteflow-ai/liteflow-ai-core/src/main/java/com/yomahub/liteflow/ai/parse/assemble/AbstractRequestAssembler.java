package com.yomahub.liteflow.ai.parse.assemble;

import com.yomahub.liteflow.ai.context.ChatContext;
import com.yomahub.liteflow.ai.domain.dto.ModelConfigAggregator;
import com.yomahub.liteflow.ai.domain.dto.ParsedAnnotationConfig;
import com.yomahub.liteflow.ai.engine.model.ModelRequest;
import com.yomahub.liteflow.ai.parse.context.ProcessorContext;
import com.yomahub.liteflow.ai.util.SetUtil;
import com.yomahub.liteflow.log.LFLog;
import com.yomahub.liteflow.log.LFLoggerManager;

import java.util.function.Supplier;

/**
 * 抽象请求组装器
 *
 * @author 苍镜月
 * @since TODO
 */

public abstract class AbstractRequestAssembler<R extends ModelRequest> implements RequestAssembler<R> {

    protected final LFLog LOG = LFLoggerManager.getLogger(this.getClass());

    /**
     * 组装请求对象(最终请求对象存储于 {@link ProcessorContext} 中的 {@link ModelRequest} 属性中)
     *
     * @param contextRequest 上下文中的请求示例(可以为 null)
     * @param context        处理器上下文
     */
    public final void assemble(R contextRequest, ProcessorContext<?> context) {
        ParsedAnnotationConfig annotationConfig = context.getParsedAnnotationConfig();
        ModelConfigAggregator config = context.getWrapBean().getConfig();

        // 组装请求
        R req = doAssemble(contextRequest, annotationConfig, config, context.getChatContext());
        // 设置到处理器上下文
        context.setModelRequest(req);
    }

    /**
     * 实际的请求组装逻辑
     *
     * @param contextRequest   上下文中的请求示例(可以为 null)
     * @param annotationConfig 注解解析配置
     * @param config           模型配置聚合
     * @param context          chat上下文
     * @return 最终请求对象
     */
    protected abstract R doAssemble(
            R contextRequest,
            ParsedAnnotationConfig annotationConfig,
            ModelConfigAggregator config,
            ChatContext context
    );

    /**
     * 合并多个 Supplier 的值，返回第一个非空的值
     *
     * @param suppliers 多个 Supplier
     * @param <T>       值的类型
     * @return 第一个非空的值，如果都为空则返回 null
     */
    @SafeVarargs
    protected static <T> T merge(Supplier<T>... suppliers) {
        for (Supplier<T> supplier : suppliers) {
            T value = supplier.get();
            if (SetUtil.isPresent(value)) {
                return value;
            }
        }
        return null;
    }
}

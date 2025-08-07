package com.yomahub.liteflow.ai.parse.assemble;

import com.yomahub.liteflow.ai.domain.dto.ParsedAnnotationConfig;
import com.yomahub.liteflow.ai.engine.model.ModelRequest;
import com.yomahub.liteflow.ai.parse.context.ProcessorContext;

/**
 * 请求组装器接口
 *
 * @author 苍镜月
 * @since TODO
 */

public interface RequestAssembler<R extends ModelRequest, C extends ParsedAnnotationConfig> {

    /**
     * 组装请求对象(最终请求对象存储于 {@link ProcessorContext} 中的 {@link ModelRequest} 属性中)
     *
     * @param contextRequest 上下文中的请求示例(可以为 null)
     * @param context        处理器上下文
     */
    void assemble(R contextRequest, ProcessorContext<C> context);
}

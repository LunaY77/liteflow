package com.yomahub.liteflow.ai.parse.assemble;

import com.yomahub.liteflow.ai.context.ChatContext;
import com.yomahub.liteflow.ai.domain.dto.ModelConfigAggregator;
import com.yomahub.liteflow.ai.domain.dto.ParsedClassifyAnnotationConfig;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatRequest;

/**
 * TODO
 *
 * @author 苍镜月
 * @since TODO
 */

public class ClassifyRequestAssembler extends AbstractRequestAssembler<ChatRequest, ParsedClassifyAnnotationConfig> {

    @Override
    protected ChatRequest doAssemble(ChatRequest contextRequest, ParsedClassifyAnnotationConfig annotationConfig, ModelConfigAggregator config, ChatContext context) {
        // TODO

        // 定死使用阻塞式传输

        return null;
    }
}

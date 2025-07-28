package com.yomahub.liteflow.ai.parse.anno;

import com.yomahub.liteflow.ai.annotation.AIRetrieval;
import com.yomahub.liteflow.ai.domain.enums.AITypeEnum;
import com.yomahub.liteflow.ai.parse.AbstractAnnotationProcessor;
import com.yomahub.liteflow.ai.parse.ProcessorContext;
import com.yomahub.liteflow.ai.proxy.wrap.RetrievalProxyWrapBean;

/**
 * AI RAG节点注解处理器
 *
 * @author 苍镜月
 * @since TODO
 */

public class RetrievalAnnotationProcessor extends AbstractAnnotationProcessor<AIRetrieval, RetrievalProxyWrapBean> {

    @Override
    public void postProcessBeforeTrigger(AIRetrieval annotation, ProcessorContext<RetrievalProxyWrapBean> context) {

    }

    @Override
    public void postProcessAfterTrigger(AIRetrieval annotation, ProcessorContext<RetrievalProxyWrapBean> context) {

    }

    @Override
    protected AITypeEnum getAIType() {
        return AITypeEnum.RETRIEVAL;
    }
}

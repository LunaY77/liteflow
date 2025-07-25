package com.yomahub.liteflow.ai.proxy.wrap;

import com.yomahub.liteflow.ai.annotation.AIComponent;
import com.yomahub.liteflow.ai.annotation.AIRetrieval;

/**
 * AI RAG节点包装 Bean
 *
 * @author 苍镜月
 * @since TODO
 */

public class RetrievalProxyWrapBean extends AIProxyWrapBean<AIRetrieval> {

    public RetrievalProxyWrapBean() {
        super();
    }

    public RetrievalProxyWrapBean(AIComponent aiComponent, AIRetrieval annotation,
                                 Class<?> interfaceClass, String beanName) {
        super(aiComponent, interfaceClass, beanName);
        this.annotation = annotation;
    }
}

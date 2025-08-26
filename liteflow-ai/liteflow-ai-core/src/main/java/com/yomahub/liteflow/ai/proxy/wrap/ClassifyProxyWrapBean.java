package com.yomahub.liteflow.ai.proxy.wrap;

import com.yomahub.liteflow.ai.annotation.model.node.AIClassify;
import com.yomahub.liteflow.ai.annotation.AIComponent;

/**
 * AI意图识别节点包装 Bean
 *
 * @author 苍镜月
 * @since TODO
 */

public class ClassifyProxyWrapBean extends AIProxyWrapBean<AIClassify> {

    public ClassifyProxyWrapBean(AIComponent aiComponent, AIClassify annotation, Class<?> interfaceClass, String beanName) {
        super(aiComponent, annotation, interfaceClass, beanName);
    }
}

package com.yomahub.liteflow.ai.proxy.wrap;

import com.yomahub.liteflow.ai.annotation.AIClassify;
import com.yomahub.liteflow.ai.annotation.AIComponent;

import java.util.List;

/**
 * AI意图识别节点包装 Bean
 *
 * @author 苍镜月
 * @since TODO
 */

public class ClassifyProxyWrapBean extends AIProxyWrapBean<AIClassify> {


    private String systemPrompt;

    private String userPrompt;

    private List<String> categories;

    private boolean multiLabel;

    public ClassifyProxyWrapBean() {
        super();
    }

    public ClassifyProxyWrapBean(AIComponent aiComponent, AIClassify annotation,
                             Class<?> interfaceClass, String beanName) {
        super(aiComponent, interfaceClass, beanName);
        // TODO anno
    }

    public String getSystemPrompt() {
        return systemPrompt;
    }

    public String getUserPrompt() {
        return userPrompt;
    }

    public List<String> getCategories() {
        return categories;
    }

    public boolean isMultiLabel() {
        return multiLabel;
    }
}

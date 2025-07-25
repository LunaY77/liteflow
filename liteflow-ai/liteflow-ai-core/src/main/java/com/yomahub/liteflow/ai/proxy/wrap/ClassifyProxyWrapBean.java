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
        this.annotation = annotation;
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

    public void setSystemPrompt(String systemPrompt) {
        this.systemPrompt = systemPrompt;
    }

    public void setUserPrompt(String userPrompt) {
        this.userPrompt = userPrompt;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public void setMultiLabel(boolean multiLabel) {
        this.multiLabel = multiLabel;
    }
}

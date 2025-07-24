package com.yomahub.liteflow.ai.proxy.wrap;

import com.yomahub.liteflow.ai.annotation.AIComponent;
import com.yomahub.liteflow.ai.domain.ModelConfig;

import java.lang.annotation.Annotation;

/**
 * AI节点包装 Bean
 *
 * @author 苍镜月
 * @since TODO
 */

public abstract class AIProxyWrapBean<T extends Annotation> {

    protected AIComponent aiComponent;

    protected ModelConfig config;

    protected String nodeId;

    protected String nodeName;

    protected Class<?> interfaceClass;

    protected String beanName;

    public AIProxyWrapBean() {
    }

    public AIProxyWrapBean(AIComponent aiComponent, Class<?> interfaceClass, String beanName) {
        this.aiComponent = aiComponent;
        this.config = ModelConfig.fromAnnotation(aiComponent);
        this.nodeId = aiComponent.nodeId();
        this.nodeName = aiComponent.nodeName();
        this.interfaceClass = interfaceClass;
        this.beanName = beanName;
    }

    public AIComponent getAiComponent() {
        return aiComponent;
    }

    public String getNodeId() {
        return nodeId;
    }

    public String getNodeName() {
        return nodeName;
    }

    public ModelConfig getConfig() {
        return config;
    }

    public Class<?> getInterfaceClass() {
        return interfaceClass;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setAiComponent(AIComponent aiComponent) {
        this.aiComponent = aiComponent;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public void setConfig(ModelConfig config) {
        this.config = config;
    }

    public void setInterfaceClass(Class<?> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }
}

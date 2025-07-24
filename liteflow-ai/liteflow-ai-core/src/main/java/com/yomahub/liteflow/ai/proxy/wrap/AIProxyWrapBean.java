package com.yomahub.liteflow.ai.proxy.wrap;

import com.yomahub.liteflow.ai.annotation.AIComponent;
import com.yomahub.liteflow.ai.config.ModelConfig;

import java.lang.annotation.Annotation;

/**
 * TODO
 *
 * @author 苍镜月
 * @since TODO
 */

public class AIProxyWrapBean<T extends Annotation> {

    private AIComponent aiComponent;

    private ModelConfig config;

    private String nodeId;

    private String nodeName;

    private T annotation;

    private Class<?> interfaceClass;

    private String beanName;

    public AIProxyWrapBean() {
    }

    public AIProxyWrapBean(AIComponent aiComponent, T annotation,
                           Class<?> interfaceClass, String beanName) {
        this.aiComponent = aiComponent;
        this.config = ModelConfig.fromAnnotation(aiComponent);
        this.nodeId = aiComponent.nodeId();
        this.nodeName = aiComponent.nodeName();
        this.annotation = annotation;
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

    public T getAnnotation() {
        return annotation;
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

    public void setAnnotation(T annotation) {
        this.annotation = annotation;
    }

    public void setInterfaceClass(Class<?> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }
}

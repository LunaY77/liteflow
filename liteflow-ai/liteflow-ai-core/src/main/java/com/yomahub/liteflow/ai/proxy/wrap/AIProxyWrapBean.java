package com.yomahub.liteflow.ai.proxy.wrap;

import com.yomahub.liteflow.ai.annotation.AIComponent;

import java.lang.annotation.Annotation;

/**
 * TODO
 *
 * @author 苍镜月
 * @since TODO
 */

public class AIProxyWrapBean<T extends Annotation> {

    protected AIComponent aiComponent;

    protected T annotation;

    protected Class<?> interfaceClass;

    protected String beanName;

    public AIProxyWrapBean() {
    }

    public AIProxyWrapBean(AIComponent aiComponent, T annotation,
                           Class<?> interfaceClass, String beanName) {
        this.aiComponent = aiComponent;
        this.annotation = annotation;
        this.interfaceClass = interfaceClass;
        this.beanName = beanName;
    }

    public String getNodeId() {
        return aiComponent.nodeId();
    }

    public String getNodeName() {
        return aiComponent.nodeName();
    }

    public AIComponent getAiComponent() {
        return aiComponent;
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

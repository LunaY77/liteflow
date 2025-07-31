package com.yomahub.liteflow.ai.proxy.wrap;

import com.yomahub.liteflow.ai.annotation.AIComponent;
import com.yomahub.liteflow.ai.domain.ModelConfig;
import com.yomahub.liteflow.ai.domain.enums.ResponseType;

import java.lang.annotation.Annotation;
import java.util.Objects;

/**
 * AI节点包装 Bean
 *
 * @author 苍镜月
 * @since TODO
 */

public abstract class AIProxyWrapBean<T extends Annotation> {

    protected T annotation;

    protected ModelConfig config;

    protected String nodeId;

    protected String nodeName;

    protected Class<?> interfaceClass;

    protected String beanName;

    protected String systemPrompt;

    protected String userPrompt;

    protected ResponseType responseType = ResponseType.TEXT;

    protected Class<?> entityClass = String.class;

    public AIProxyWrapBean() {
    }

    public AIProxyWrapBean(AIComponent aiComponent, Class<?> interfaceClass, String beanName) {
        this.config = ModelConfig.fromAnnotation(aiComponent);
        this.nodeId = aiComponent.nodeId();
        this.nodeName = aiComponent.nodeName();
        this.interfaceClass = interfaceClass;
        this.beanName = beanName;
    }

    public T getAnnotation() {
        return annotation;
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

    public String getSystemPrompt() {
        return systemPrompt;
    }

    public String getUserPrompt() {
        return userPrompt;
    }

    public ResponseType getResponseType() {
        return responseType;
    }

    public Class<?> getEntityClass() {
        return entityClass;
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

    public void setSystemPrompt(String systemPrompt) {
        this.systemPrompt = systemPrompt;
    }

    public void setUserPrompt(String userPrompt) {
        this.userPrompt = userPrompt;
    }

    public void setResponseType(ResponseType responseType) {
        this.responseType = responseType;
    }

    public void setEntityClass(Class<?> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AIProxyWrapBean<?> that = (AIProxyWrapBean<?>) o;
        return Objects.equals(getAnnotation(), that.getAnnotation()) &&
                Objects.equals(getConfig(), that.getConfig()) &&
                Objects.equals(getNodeId(), that.getNodeId()) &&
                Objects.equals(getNodeName(), that.getNodeName()) &&
                Objects.equals(getInterfaceClass(), that.getInterfaceClass()) &&
                Objects.equals(getBeanName(), that.getBeanName()) &&
                Objects.equals(getSystemPrompt(), that.getSystemPrompt()) &&
                Objects.equals(getUserPrompt(), that.getUserPrompt()) &&
                getResponseType() == that.getResponseType() &&
                Objects.equals(getEntityClass(), that.getEntityClass());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                getAnnotation(),
                getConfig(),
                getNodeId(),
                getNodeName(),
                getInterfaceClass(),
                getBeanName(),
                getSystemPrompt(),
                getUserPrompt(),
                getResponseType(),
                getEntityClass()
        );
    }
}

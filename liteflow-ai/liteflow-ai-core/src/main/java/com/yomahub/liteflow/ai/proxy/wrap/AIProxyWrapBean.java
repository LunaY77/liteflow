package com.yomahub.liteflow.ai.proxy.wrap;

import com.yomahub.liteflow.ai.annotation.AIComponent;
import com.yomahub.liteflow.ai.parse.context.ProcessorContext;

import java.lang.annotation.Annotation;

/**
 * AI节点包装 Bean
 *
 * @author 苍镜月
 * @since TODO
 */

public abstract class AIProxyWrapBean<T extends Annotation> {

    // AI组件注解
    protected final T annotation;
    // AI 组件配置注解
    protected final AIComponent aiComponent;
    // LiteFlow Node ID
    protected final String nodeId;
    // LiteFlow Node 名称
    protected final String nodeName;
    // 代理接口名称
    protected final Class<?> interfaceClass;
    // Bean 名称
    protected final String beanName;
    // 处理器上下文
    protected ProcessorContext<?> processorContext;

    public AIProxyWrapBean(AIComponent aiComponent, T annotation, Class<?> interfaceClass, String beanName) {
        this.aiComponent = aiComponent;
        this.annotation = annotation;
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

    public AIComponent getAiComponent() {
        return aiComponent;
    }

    public Class<?> getInterfaceClass() {
        return interfaceClass;
    }

    public String getBeanName() {
        return beanName;
    }

    public ProcessorContext<?> getProcessorContext() {
        return processorContext;
    }

    public void setProcessorContext(ProcessorContext<?> processorContext) {
        this.processorContext = processorContext;
    }
}

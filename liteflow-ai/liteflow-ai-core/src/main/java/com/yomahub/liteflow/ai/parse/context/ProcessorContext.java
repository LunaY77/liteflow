package com.yomahub.liteflow.ai.parse.context;

import com.yomahub.liteflow.ai.annotation.AIInput;
import com.yomahub.liteflow.ai.annotation.AIOutput;
import com.yomahub.liteflow.ai.context.ChatContext;
import com.yomahub.liteflow.ai.domain.dto.ParsedAnnotationConfig;
import com.yomahub.liteflow.ai.engine.model.ModelRequest;
import com.yomahub.liteflow.ai.parse.prompt.loader.DefaultPromptResourceLoader;
import com.yomahub.liteflow.ai.parse.prompt.loader.PromptResourceLoader;
import com.yomahub.liteflow.ai.proxy.wrap.AIProxyWrapBean;
import com.yomahub.liteflow.core.NodeComponent;

/**
 * 注解解析处理器上下文
 *
 * @author 苍镜月
 * @since TODO
 */

public class ProcessorContext<T extends AIProxyWrapBean<?>> {

    private final T wrapBean;
    private final ParsedAnnotationConfig parsedAnnotationConfig;
    private final ChatContext chatContext;
    private final NodeComponent nodeComponent;
    private final AIInput aiInputAnno;
    private final AIOutput aiOutputAnno;
    private final PromptResourceLoader resourceLoader;

    private ModelRequest modelRequest;

    public ProcessorContext(T wrapBean, ChatContext chatContext, NodeComponent nodeComponent) {
        this.wrapBean = wrapBean;
        parsedAnnotationConfig = new ParsedAnnotationConfig();
        this.chatContext = chatContext;
        this.nodeComponent = nodeComponent;
        this.aiInputAnno = wrapBean.getInterfaceClass().getAnnotation(AIInput.class);
        this.aiOutputAnno = wrapBean.getInterfaceClass().getAnnotation(AIOutput.class);
        this.resourceLoader = new DefaultPromptResourceLoader();
    }

    public T getWrapBean() {
        return wrapBean;
    }

    public ChatContext getChatContext() {
        return chatContext;
    }

    public NodeComponent getNodeComponent() {
        return nodeComponent;
    }

    public AIInput getAiInputAnno() {
        return aiInputAnno;
    }

    public AIOutput getAiOutputAnno() {
        return aiOutputAnno;
    }

    public PromptResourceLoader getResourceLoader() {
        return resourceLoader;
    }

    public ParsedAnnotationConfig getParsedAnnotationConfig() {
        return parsedAnnotationConfig;
    }

    public ModelRequest getModelRequest() {
        return modelRequest;
    }

    public void setModelRequest(ModelRequest modelRequest) {
        this.modelRequest = modelRequest;
    }
}

package com.yomahub.liteflow.ai.parse.context;

import com.yomahub.liteflow.ai.annotation.AIComponent;
import com.yomahub.liteflow.ai.annotation.model.io.AIInput;
import com.yomahub.liteflow.ai.annotation.model.io.AIOutput;
import com.yomahub.liteflow.ai.context.ChatContext;
import com.yomahub.liteflow.ai.domain.dto.ModelConfigAggregator;
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
 * @since 2.16.0
 */

public class ProcessorContext<C extends ParsedAnnotationConfig> {

    private final AIComponent aiComponent;
    private final ChatContext chatContext;
    private final NodeComponent nodeComponent;
    private final AIInput aiInputAnno;
    private final AIOutput aiOutputAnno;
    private final PromptResourceLoader resourceLoader;

    // 模型配置聚合
    protected ModelConfigAggregator configAggregator;
    private C parsedAnnotationConfig;
    private ModelRequest modelRequest;

    public ProcessorContext(AIProxyWrapBean<?> wrapBean, ChatContext chatContext, NodeComponent nodeComponent) {
        this.aiComponent = wrapBean.getAiComponent();
        this.chatContext = chatContext;
        this.nodeComponent = nodeComponent;
        this.aiInputAnno = wrapBean.getInterfaceClass().getAnnotation(AIInput.class);
        this.aiOutputAnno = wrapBean.getInterfaceClass().getAnnotation(AIOutput.class);
        this.resourceLoader = new DefaultPromptResourceLoader();
    }

    public AIComponent getAiComponent() {
        return aiComponent;
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

    public ModelConfigAggregator getConfigAggregator() {
        return configAggregator;
    }

    public void setConfigAggregator(ModelConfigAggregator configAggregator) {
        this.configAggregator = configAggregator;
    }

    public C getParsedAnnotationConfig() {
        return parsedAnnotationConfig;
    }

    public void setParsedAnnotationConfig(C parsedAnnotationConfig) {
        this.parsedAnnotationConfig = parsedAnnotationConfig;
    }

    public ModelRequest getModelRequest() {
        return modelRequest;
    }

    public void setModelRequest(ModelRequest modelRequest) {
        this.modelRequest = modelRequest;
    }
}

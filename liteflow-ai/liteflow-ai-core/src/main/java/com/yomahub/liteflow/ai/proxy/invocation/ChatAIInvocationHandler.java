package com.yomahub.liteflow.ai.proxy.invocation;

import com.yomahub.liteflow.ai.context.ChatContext;
import com.yomahub.liteflow.ai.model.ModelFactory;
import com.yomahub.liteflow.ai.parse.AnnotationParser;
import com.yomahub.liteflow.ai.parse.ProcessorContext;
import com.yomahub.liteflow.ai.proxy.wrap.ChatProxyWrapBean;
import com.yomahub.liteflow.core.NodeComponent;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;

/**
 * 聊天组件的调用处理器
 *
 * @author 苍镜月
 * @since TODO
 */

public class ChatAIInvocationHandler extends AbstractAIInvocationHandler<ChatProxyWrapBean> {

    public ChatAIInvocationHandler(ChatProxyWrapBean wrapBean) {
        super(wrapBean);
    }

    @Override
    protected Void executeAIProcess(NodeComponent nodeComponent, Object[] args) {
        ChatContext chatContext = nodeComponent.getContextBean(ChatContext.class);
        ProcessorContext<ChatProxyWrapBean> processorContext = new ProcessorContext<>(wrapBean, chatContext, nodeComponent);

        // 注解解析前置处理
        AnnotationParser.postProcessBeforeTrigger(wrapBean.getAnnotation(), processorContext);

        if (wrapBean.isStreaming()) {
            return processStreaming(nodeComponent, args);
        } else {
            return processBlocking(nodeComponent, args);
        }
    }

    private Void processStreaming(NodeComponent nodeComponent, Object[] args) {
        StreamingChatModel streamingChatModel = ModelFactory.getStreamingChatModel(wrapBean.getConfig());

        return null;
    }

    private Void processBlocking(NodeComponent nodeComponent, Object[] args) {
        ChatModel chatModel = ModelFactory.getChatModel(wrapBean.getConfig());

        LOG.info("Processing chat request with model: {}", chatModel.getClass().getSimpleName());
        return null;
    }
}

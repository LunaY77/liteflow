package com.yomahub.liteflow.ai.proxy.invocation;

import com.yomahub.liteflow.ai.model.ModelFactory;
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
    protected Object doExecuteAIProcess(ProcessorContext<ChatProxyWrapBean> processorContext, Object[] args) {
        NodeComponent nodeComponent = processorContext.getNodeComponent();
        if (wrapBean.isStreaming()) {
            return processStreaming(nodeComponent);
        } else {
            return processBlocking(nodeComponent);
        }
    }

    private Void processStreaming(NodeComponent nodeComponent) {
        StreamingChatModel streamingChatModel = ModelFactory.getStreamingChatModel(wrapBean.getConfig());

        return null;
    }

    private Void processBlocking(NodeComponent nodeComponent) {
        ChatModel chatModel = ModelFactory.getChatModel(wrapBean.getConfig());

        LOG.info("Processing chat request with model: {}", chatModel.getClass().getSimpleName());
        return null;
    }
}

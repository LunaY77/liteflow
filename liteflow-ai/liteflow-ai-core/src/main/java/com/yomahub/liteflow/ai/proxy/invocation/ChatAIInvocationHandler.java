package com.yomahub.liteflow.ai.proxy.invocation;

import com.yomahub.liteflow.ai.context.ChatContext;
import com.yomahub.liteflow.ai.exception.LiteFlowAIException;
import com.yomahub.liteflow.ai.model.ModelFactory;
import com.yomahub.liteflow.ai.parse.ProcessorContext;
import com.yomahub.liteflow.ai.proxy.invocation.service.AiServiceFactory;
import com.yomahub.liteflow.ai.proxy.wrap.ChatProxyWrapBean;
import com.yomahub.liteflow.core.NodeComponent;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.service.TokenStream;

import java.util.Objects;
import java.util.Optional;

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
        StreamingChatModel streamingChatModel = ModelFactory.getStreamingChatModel(wrapBean);
        // 创建AI服务实例
        Object aiService = AiServiceFactory.createAiService(wrapBean.getEntityClass(), streamingChatModel);

        try {
            TokenStream tokenStream = AiServiceFactory.chatStream(aiService, wrapBean.getUserPrompt(), wrapBean.getSystemPrompt());
            // 处理流式响应
            ChatContext chatContext = nodeComponent.getContextBean(ChatContext.class);
            // 如果存在流处理器，则将TokenStream传递给它
            if (Objects.nonNull(chatContext)) {
                Optional.of(chatContext.getStreamHandler())
                        .ifPresent(streamHandler -> streamHandler.acceptTokenStream(tokenStream));
            }
        } catch (Throwable e) {
            throw new LiteFlowAIException("Error during streaming chat processing", e);
        }
        return null;
    }

    private Object processBlocking(NodeComponent nodeComponent) {
        ChatModel chatModel = ModelFactory.getChatModel(wrapBean);

        // 创建AI服务实例
        Object aiService = AiServiceFactory.createAiService(wrapBean.getEntityClass(), chatModel);

        try {
            return AiServiceFactory.chat(aiService, wrapBean.getUserPrompt(), wrapBean.getSystemPrompt());
        } catch (Throwable e) {
            throw new LiteFlowAIException("Error during blocking chat processing", e);
        }
    }
}

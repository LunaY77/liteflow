package com.yomahub.liteflow.ai.proxy.invocation;

import com.yomahub.liteflow.ai.parse.context.ProcessorContext;
import com.yomahub.liteflow.ai.proxy.wrap.ChatProxyWrapBean;
import com.yomahub.liteflow.core.NodeComponent;

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
    protected void checkValidation(ProcessorContext<ChatProxyWrapBean> processorContext) {
        // 调用父类的校验方法
        super.checkValidation(processorContext);
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
//        StreamingChatModel streamingChatModel = ModelFactory.getStreamingChatModel(wrapBean);
//        // 创建AI服务实例
//        Object aiService = AiServiceFactory.createAiService(wrapBean.getEntityClass(), streamingChatModel);
//
//        try {
//            TokenStream tokenStream = AiServiceFactory.chatStream(aiService, wrapBean.getUserPrompt(), wrapBean.getSystemPrompt());
//            // 处理流式响应
//            ChatContext chatContext = nodeComponent.getContextBean(ChatContext.class);
//            // 如果存在流处理器，则将TokenStream传递给它
//            if (Objects.nonNull(chatContext)) {
//                Optional.of(chatContext.getStreamHandler())
//                        .ifPresent(streamHandler -> streamHandler.acceptTokenStream(tokenStream));
//            }
//        } catch (Throwable e) {
//            throw new LiteFlowAIException("Error during streaming chat processing", e);
//        }
        return null;
    }

    private Object processBlocking(NodeComponent nodeComponent) {
//        ChatModel chatModel = ModelFactory.getChatModel(wrapBean);
//
//        // 创建AI服务实例
//        Object aiService = AiServiceFactory.createAiService(wrapBean.getEntityClass(), chatModel);
//
//        try {
//            Object result = AiServiceFactory.chat(aiService, wrapBean.getUserPrompt(), wrapBean.getSystemPrompt());
//            LOG.info("Chat response: {}", result);
//            return result;
//        } catch (Throwable e) {
//            throw new LiteFlowAIException("Error during blocking chat processing", e);
//        }
        return null;
    }
}

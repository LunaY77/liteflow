package com.yomahub.liteflow.ai.proxy.invocation;

import com.yomahub.liteflow.ai.annotation.AIChat;
import com.yomahub.liteflow.ai.model.ModelFactory;
import com.yomahub.liteflow.ai.proxy.wrap.AIProxyWrapBean;
import com.yomahub.liteflow.core.NodeComponent;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;

/**
 * 聊天组件的调用处理器
 *
 * @author 苍镜月
 * @since TODO
 */

public class ChatAIInvocationHandler extends AbstractAIInvocationHandler {

    public ChatAIInvocationHandler(AIProxyWrapBean<AIChat> wrapBean) {
        super(wrapBean);
    }

    @Override
    protected Void executeAIProcess(NodeComponent nodeComponent, Object[] args) {
        AIChat aiChatAnno = (AIChat) wrapBean.getAnnotation();
        if (aiChatAnno.streaming()) {
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

        return null;
    }
}

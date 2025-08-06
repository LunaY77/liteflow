package com.yomahub.liteflow.ai.proxy.invocation;

import com.yomahub.liteflow.ai.engine.model.chat.ChatModel;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatRequest;
import com.yomahub.liteflow.ai.model.ModelFactory;
import com.yomahub.liteflow.ai.parse.context.ProcessorContext;
import com.yomahub.liteflow.ai.proxy.wrap.ChatProxyWrapBean;

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
        ChatModel chatModel = ModelFactory.getChatModel(wrapBean);
        ChatRequest chatRequest = processorContext.getModelRequest().toChatRequest();

        if (wrapBean.isStreaming()) {
            chatModel.stream(chatRequest);
            return null;
        } else {
            return chatModel.chat(chatRequest);
        }
    }
}

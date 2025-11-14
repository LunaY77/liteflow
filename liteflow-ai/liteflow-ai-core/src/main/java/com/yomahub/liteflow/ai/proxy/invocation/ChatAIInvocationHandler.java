package com.yomahub.liteflow.ai.proxy.invocation;

import com.yomahub.liteflow.ai.context.StreamHandler;
import com.yomahub.liteflow.ai.engine.interact.chunk.ChunkEvent;
import com.yomahub.liteflow.ai.engine.model.chat.ChatModel;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatRequest;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatResponse;
import com.yomahub.liteflow.ai.model.ModelFactory;
import com.yomahub.liteflow.ai.parse.context.ProcessorContext;
import com.yomahub.liteflow.ai.proxy.wrap.ChatProxyWrapBean;
import io.reactivex.rxjava3.core.Flowable;
import org.reactivestreams.Publisher;

import java.util.Objects;

/**
 * 聊天组件的调用处理器
 *
 * @author 苍镜月
 * @since 2.16.0
 */

public class ChatAIInvocationHandler extends AbstractAIInvocationHandler<ChatProxyWrapBean> {

    public ChatAIInvocationHandler(ChatProxyWrapBean wrapBean) {
        super(wrapBean);
    }

    @Override
    protected void checkValidation(ProcessorContext<?> processorContext) {
        // 调用父类的校验方法
        super.checkValidation(processorContext);
    }

    @Override
    protected Object doExecuteAIProcess(ProcessorContext<?> processorContext, Object[] args) {
        ChatModel chatModel = ModelFactory.getChatModel(wrapBean);
        ChatRequest chatRequest = processorContext.getModelRequest().toChatRequest();

        if (chatRequest.isStreaming()) {
            return processStreaming(chatModel, chatRequest, processorContext.getChatContext().getStreamHandler());
        } else {
            return chatModel.chat(chatRequest);
        }
    }

    private ChatResponse processStreaming(ChatModel chatModel, ChatRequest chatRequest, StreamHandler streamHandler) {
        // 获取用户定义的 StreamHandler（如果有）
        if (Objects.isNull(streamHandler)) {
            // 如果未提供，使用 pass-through 处理器
            streamHandler = StreamHandler.passThrough();
        }

        // 获取原始的事件流
        Publisher<ChunkEvent> eventStream = chatModel.stream(chatRequest);

        // 应用用户的 StreamHandler 进行响应式转换
        Publisher<ChunkEvent> handledStream = streamHandler.handle(eventStream);

        return Flowable.fromPublisher(handledStream)
                .blockingLast()
                .getFinalResponse();
    }
}

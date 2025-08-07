package com.yomahub.liteflow.ai.proxy.invocation;

import com.yomahub.liteflow.ai.engine.interact.callbacks.ResultHandler;
import com.yomahub.liteflow.ai.engine.interact.pipeline.InteractContext;
import com.yomahub.liteflow.ai.engine.model.chat.ChatModel;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatRequest;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatResponse;
import com.yomahub.liteflow.ai.exception.LiteFlowAIException;
import com.yomahub.liteflow.ai.model.ModelFactory;
import com.yomahub.liteflow.ai.parse.context.ProcessorContext;
import com.yomahub.liteflow.ai.proxy.wrap.ChatProxyWrapBean;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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
    protected void checkValidation(ProcessorContext<?> processorContext) {
        // 调用父类的校验方法
        super.checkValidation(processorContext);
    }

    @Override
    protected Object doExecuteAIProcess(ProcessorContext<?> processorContext, Object[] args) {
        ChatModel chatModel = ModelFactory.getChatModel(wrapBean);
        ChatRequest chatRequest = processorContext.getModelRequest().toChatRequest();

        if (chatRequest.isStreaming()) {
            return processStreaming(chatModel, chatRequest);
        } else {
            return chatModel.chat(chatRequest);
        }
    }

    private ChatResponse processStreaming(ChatModel chatModel, ChatRequest chatRequest) {
        // 创建一个 CompletableFuture 用于异步处理，他将持有最终的 ChatResponse
        final CompletableFuture<ChatResponse> future = new CompletableFuture<>();
        // 获取用户传入的 ResultHandler
        final ResultHandler externalResultHandler = chatRequest.getResultHandler();

        // 定义内部 ResultHandler，包装用户的处理逻辑
        ResultHandler internalResultHandler = new ResultHandler() {
            @Override
            public ChatResponse onCompletion(ChatResponse response, InteractContext context) {
                if (Objects.nonNull(externalResultHandler)) {
                    return externalResultHandler.onCompletion(response, context);
                }
                return response;
            }

            @Override
            public ChatResponse onFinal(ChatResponse response, InteractContext context) {
                ChatResponse finalResponse = response;
                try {
                    if (Objects.nonNull(externalResultHandler)) {
                        finalResponse = externalResultHandler.onFinal(response, context);
                    }
                } finally {
                    future.complete(finalResponse);
                }
                return finalResponse;
            }
        };

        // 设置内部 ResultHandler 到请求中
        chatRequest.setResultHandler(internalResultHandler);
        // 执行流式聊天请求
        chatModel.stream(chatRequest);

        // 阻塞等待 CompletableFuture 完成，并返回最终的 ChatResponse
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new LiteFlowAIException("error while processing streaming chat request", e);
        }
    }
}

package com.yomahub.liteflow.ai.parse.assemble;

import com.yomahub.liteflow.ai.context.ChatContext;
import com.yomahub.liteflow.ai.context.StreamHandler;
import com.yomahub.liteflow.ai.domain.dto.ModelConfigAggregator;
import com.yomahub.liteflow.ai.domain.dto.ParsedChatAnnotationConfig;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatOptions;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatRequest;
import com.yomahub.liteflow.ai.engine.model.chat.message.Message;
import com.yomahub.liteflow.ai.engine.model.chat.message.SystemMessage;
import com.yomahub.liteflow.ai.engine.model.chat.message.UserMessage;
import com.yomahub.liteflow.ai.engine.model.output.structure.TypeReference;
import com.yomahub.liteflow.ai.model.ModelFactory;
import com.yomahub.liteflow.ai.util.SetUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * ChatRequest 组装器
 *
 * @author 苍镜月
 * @since TODO
 */

public class ChatRequestAssembler extends AbstractRequestAssembler<ChatRequest, ParsedChatAnnotationConfig> {

    @SuppressWarnings("rawtypes")
    @Override
    protected ChatRequest doAssemble(ChatRequest contextRequest, ParsedChatAnnotationConfig annotationConfig, ModelConfigAggregator config, ChatContext context) {
        ChatOptions contextOptions = Optional.ofNullable(contextRequest)
                .map(ChatRequest::getOptions)
                .orElse(ChatOptions.builder().build());

        ChatRequest.Builder<?> builder = ModelFactory.getChatRequestBuilder(config.getProvider());

        // 1. 连接 StreamHandler 回调
        StreamHandler streamHandler = context.getStreamHandler();
        if (Objects.nonNull(streamHandler)) {
            LOG.info("Connecting StreamHandler to ChatRequest");
            builder.onStart(streamHandler::onStart)
                    .onClose(streamHandler::onClose)
                    .onError(streamHandler::onError)
                    .onText(streamHandler::onText)
                    .onThinking(streamHandler::onThinking)
                    .onToolsCalling(streamHandler::onToolsCalling)
                    .onUsage(streamHandler::onUsage)
                    .onGrounding(streamHandler::onGrounding)
                    .onCompletion(streamHandler::onCompletion)
                    .onFinal(streamHandler::onFinal);
        }

        // 2. 合并 ChatOptions
        ChatOptions.Builder<?> optionsBuilder = ChatOptions.builder();
        optionsBuilder.temperature(merge(contextOptions::getTemperature, config::getTemperature));
        optionsBuilder.topP(merge(contextOptions::getTopP, config::getTopP));
        optionsBuilder.topK(merge(contextOptions::getTopK, config::getTopK));
        optionsBuilder.maxTokens(merge(contextOptions::getMaxTokens, config::getMaxTokens));
        optionsBuilder.seed(merge(contextOptions::getSeed, config::getSeed));
        optionsBuilder.enableThinking(merge(contextOptions::getEnableThinking, () -> config.getEnableThinking().toBool()));
        builder.options(optionsBuilder.build());

        // 3. 合并 Message
        List<Message> messages = new ArrayList<>();
        SetUtil.setIfPresent(t -> messages.add(new SystemMessage(t)), annotationConfig.getSystemPrompt());
        SetUtil.setIfPresent(t -> messages.add(new UserMessage(t)), annotationConfig.getUserPrompt());

        builder.messages(
                merge(() -> Objects.nonNull(contextRequest) ? contextRequest.getMessages() : null, () -> messages)
        );

        // 4. streaming 相关参数
        builder.streaming(
                Boolean.TRUE.equals(merge(() -> Objects.nonNull(contextRequest) ? contextRequest.isStreaming() : null, annotationConfig::isStreaming))
        );
        builder.transportType(
                merge(() -> Objects.nonNull(contextRequest) ? contextRequest.getTransportType() : null, annotationConfig::getTransportType)
        );

        // 4. 结构化输出
        builder.targetType(
                merge(() -> Objects.nonNull(contextRequest) ? contextRequest.getTargetType() : null,
                        () -> new TypeReference(annotationConfig.getTypeName()) {
                        }.getType())
        );
        builder.responseType(
                merge(() -> Objects.nonNull(contextRequest) ? contextRequest.getResponseType() : null, annotationConfig::getResponseType)
        );
        builder.strict(
                Boolean.TRUE.equals(merge(() -> Objects.nonNull(contextRequest) ? contextRequest.isStrict() : null, annotationConfig::isStrict))
        );

        return builder.build();
    }
}

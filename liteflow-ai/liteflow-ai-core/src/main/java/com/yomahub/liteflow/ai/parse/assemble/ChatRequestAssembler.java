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
import com.yomahub.liteflow.ai.engine.tool.registry.StaticToolRegistry;
import com.yomahub.liteflow.ai.engine.tool.registry.ToolRegistry;
import com.yomahub.liteflow.ai.model.ModelFactory;
import com.yomahub.liteflow.ai.util.SetUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

/**
 * ChatRequest 组装器
 *
 * @author 苍镜月
 * @since TODO
 */

public class ChatRequestAssembler extends AbstractRequestAssembler<ParsedChatAnnotationConfig> {

    @SuppressWarnings("rawtypes")
    @Override
    protected ChatRequest doAssemble(ParsedChatAnnotationConfig annotationConfig, ModelConfigAggregator config, ChatContext context) {
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

        // 2. ChatOptions
        ChatOptions.Builder<?> optionsBuilder = ChatOptions.builder();
        optionsBuilder.temperature(config.getTemperature());
        optionsBuilder.topP(config.getTopP());
        optionsBuilder.topK(config.getTopK());
        optionsBuilder.maxTokens(config.getMaxTokens());
        optionsBuilder.seed(config.getSeed());
        optionsBuilder.enableThinking(config.getEnableThinking().toBool());
        builder.options(optionsBuilder.build());

        // 3. Message
        List<Message> messages = new ArrayList<>();
        SetUtil.setIfPresent(t -> messages.add(new SystemMessage(t)), annotationConfig.getSystemPrompt());
        SetUtil.setIfPresent(t -> messages.add(new UserMessage(t)), annotationConfig.getUserPrompt());

        builder.messages(messages);

        // 4. streaming 相关参数
        builder.streaming(annotationConfig.isStreaming());
        builder.transportType(annotationConfig.getTransportType());

        // 4. 结构化输出
        builder.targetType(new TypeReference(annotationConfig.getTypeName()) {
        }.getType());
        builder.responseType(annotationConfig.getResponseType());
        builder.strict(annotationConfig.isStrict());

        // 5. 工具调用
        ToolRegistry toolRegistry = context.getToolRegistry();
        if (Objects.nonNull(toolRegistry)) {
            StaticToolRegistry staticToolRegistry = new StaticToolRegistry();
            HashSet<String> targetToolNames = new HashSet<>(annotationConfig.getToolNames());
            toolRegistry.getAllTools()
                    .stream()
                    .filter(tool -> targetToolNames.isEmpty() || targetToolNames.contains(tool.getName()))
                    .forEach(staticToolRegistry::register);
            builder.toolRegistry(staticToolRegistry);
        }

        return builder.build();
    }
}

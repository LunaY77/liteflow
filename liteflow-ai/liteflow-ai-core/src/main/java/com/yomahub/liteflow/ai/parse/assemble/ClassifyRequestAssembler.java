package com.yomahub.liteflow.ai.parse.assemble;

import com.yomahub.liteflow.ai.context.ChatContext;
import com.yomahub.liteflow.ai.context.StreamHandler;
import com.yomahub.liteflow.ai.domain.dto.ModelConfigAggregator;
import com.yomahub.liteflow.ai.domain.dto.ParsedClassifyAnnotationConfig;
import com.yomahub.liteflow.ai.engine.interact.transport.TransportType;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatOptions;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatRequest;
import com.yomahub.liteflow.ai.engine.model.chat.message.Message;
import com.yomahub.liteflow.ai.engine.model.chat.message.SystemMessage;
import com.yomahub.liteflow.ai.engine.model.chat.message.UserMessage;
import com.yomahub.liteflow.ai.engine.model.output.ResponseType;
import com.yomahub.liteflow.ai.engine.model.output.structure.TypeReference;
import com.yomahub.liteflow.ai.engine.tool.registry.StaticToolRegistry;
import com.yomahub.liteflow.ai.engine.tool.registry.ToolRegistry;
import com.yomahub.liteflow.ai.model.ModelFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.yomahub.liteflow.ai.util.SetUtil.setIfPresent;

/**
 * ChatRequest 组装器（意图识别）
 *
 * @author 苍镜月
 * @since TODO
 */

public class ClassifyRequestAssembler extends AbstractRequestAssembler<ParsedClassifyAnnotationConfig> {

    @SuppressWarnings("rawtypes")
    @Override
    protected ChatRequest doAssemble(ParsedClassifyAnnotationConfig annotationConfig, ModelConfigAggregator config, ChatContext context) {
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
        setIfPresent(optionsBuilder::temperature, config.getTemperature());
        setIfPresent(optionsBuilder::topP, config.getTopP());
        setIfPresent(optionsBuilder::topK, config.getTopK());
        setIfPresent(optionsBuilder::maxTokens, config.getMaxTokens());
        setIfPresent(optionsBuilder::seed, config.getSeed());
        setIfPresent(optionsBuilder::enableThinking, config.getEnableThinking().toBool());
        builder.options(optionsBuilder.build());


        // 3. Message
        List<Message> messages = new ArrayList<>();
        // 意图分类的系统消息
        messages.add(buildClassifyMessage(annotationConfig));
        setIfPresent(t -> messages.add(new SystemMessage(t)), annotationConfig.getSystemPrompt());
        setIfPresent(t -> messages.add(new UserMessage(t)), annotationConfig.getUserPrompt());

        builder.messages(messages);

        // 4. streaming 相关参数
        // 定死使用阻塞式传输
        builder.streaming(false);
        builder.transportType(TransportType.HTTP);

        // 4. 结构化输出
        // 如果开启了多意图分类，那么返回值就是 List<String>，否则就是单一的 String
        if (annotationConfig.isMultiLabel()) {
            builder.targetType(new TypeReference<List<String>>() {
            });
            builder.responseType(ResponseType.JSON);
            builder.strict(true);
        } else {
            builder.targetType(new TypeReference<String>() {
            });
            builder.responseType(ResponseType.TEXT);
            builder.strict(false);
        }

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

    /**
     * 构建意图分类的系统消息
     *
     * @param annotationConfig 注解配置
     * @return 系统消息
     */
    private Message buildClassifyMessage(ParsedClassifyAnnotationConfig annotationConfig) {
        StringBuilder sb = new StringBuilder();
        sb.append("You are an expert intent classifier.\n");
        sb.append("Your task is to analyze the user's query and classify it based on the predefined categories.\n\n");
        sb.append("Available categories are:\n");

        String categoriesString = annotationConfig.getCategories().stream()
                .map(c -> String.format("- %s", c))
                .collect(Collectors.joining("\n"));
        sb.append(categoriesString);
        sb.append("\n\n");

        sb.append("Follow these rules strictly:\n");

        if (annotationConfig.isMultiLabel()) {
            sb.append("1. You may select one or more categories that are relevant to the user's query.\n");
            sb.append("2. Your response MUST be a valid JSON array of strings, containing only the names of the selected categories.\n");
            sb.append("3. For example: [\"category1\", \"category2\"]\n");
        } else {
            sb.append("1. You must select only ONE category that best matches the user's query.\n");
            sb.append("2. Your response MUST be only the name of that single category.\n");
            sb.append("3. For example: category1\n");
        }

        sb.append("4. Do NOT provide any explanations, introductions, or any text other than the category name(s) in the specified format.");

        return new SystemMessage(sb.toString());
    }
}

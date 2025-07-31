package com.yomahub.liteflow.ai.ollama.model;

import com.yomahub.liteflow.ai.domain.ModelConfig;
import com.yomahub.liteflow.ai.domain.constant.ProviderName;
import com.yomahub.liteflow.ai.domain.enums.ResponseType;
import com.yomahub.liteflow.ai.model.ModelProviderRegistrar;
import com.yomahub.liteflow.ai.proxy.wrap.AIProxyWrapBean;
import dev.langchain4j.model.chat.Capability;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.ollama.OllamaStreamingChatModel;

import java.util.Objects;
import java.util.Optional;

/**
 * TODO
 *
 * @author 苍镜月
 * @since TODO
 */

public class OllamaModelProvider extends ModelProviderRegistrar {

    @Override
    public String getProviderName() {
        return ProviderName.OLLAMA;
    }

    @Override
    public Optional<ChatModel> createChatModel(AIProxyWrapBean<?> wrapBean) {
        // 设置模型配置和结构化输出
        ModelConfig modelConfig = wrapBean.getConfig();
        return Optional.of(OllamaChatModel
                .builder()
                .baseUrl(modelConfig.getBaseUrl())
                .modelName(modelConfig.getModel())
        ).map(ollamaChatModelBuilder -> {
            // 开启结构化输出
            if (Objects.equals(ResponseType.JSON, wrapBean.getResponseType())) {
                ollamaChatModelBuilder.responseFormat(ResponseFormat.JSON)
                        .supportedCapabilities(Capability.RESPONSE_FORMAT_JSON_SCHEMA);
            }
            return ollamaChatModelBuilder.build();
        });
    }

    @Override
    public Optional<StreamingChatModel> createStreamingChatModel(AIProxyWrapBean<?> wrapBean) {
        // 设置模型配置和结构化输出
        ModelConfig modelConfig = wrapBean.getConfig();
        return Optional.of(OllamaStreamingChatModel
                .builder()
                .baseUrl(modelConfig.getBaseUrl())
                .modelName(modelConfig.getModel())
                .build()
        );
    }
}

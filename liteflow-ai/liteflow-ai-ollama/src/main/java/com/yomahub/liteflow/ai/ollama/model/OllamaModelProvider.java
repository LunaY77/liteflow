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

import static com.yomahub.liteflow.ai.util.SetUtil.setIfPresent;

/**
 * Ollama 模型提供
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
        return Optional.of(OllamaChatModel.builder())
                // 模型基本参数配置
                .map(ollamaChatModelBuilder -> {
                    setIfPresent(ollamaChatModelBuilder::baseUrl, modelConfig.getBaseUrl());
                    setIfPresent(ollamaChatModelBuilder::modelName, modelConfig.getModel());
                    setIfPresent(ollamaChatModelBuilder::temperature, modelConfig.getTemperature());
                    setIfPresent(ollamaChatModelBuilder::topK, modelConfig.getTopK());
                    setIfPresent(ollamaChatModelBuilder::topP, modelConfig.getTopP());
                    setIfPresent(ollamaChatModelBuilder::repeatPenalty, modelConfig.getRepeatPenalty());
                    setIfPresent(ollamaChatModelBuilder::seed, modelConfig.getSeed());
                    setIfPresent(ollamaChatModelBuilder::numPredict, modelConfig.getMaxTokens());
                    setIfPresent(ollamaChatModelBuilder::stop, modelConfig.getStop());
                    // TODO timeout 类型转换
                    // setIfPresent(ollamaChatModelBuilder::timeout, modelConfig.getTimeout());
                    setIfPresent(ollamaChatModelBuilder::maxRetries, modelConfig.getMaxRetries());
                    return ollamaChatModelBuilder;
                })
                // 结构化输出配置
                .map(ollamaChatModelBuilder -> {
                    if (Objects.equals(ResponseType.JSON, wrapBean.getResponseType())) {
                        ollamaChatModelBuilder.responseFormat(ResponseFormat.JSON)
                                .supportedCapabilities(Capability.RESPONSE_FORMAT_JSON_SCHEMA);
                    }
                    return ollamaChatModelBuilder;
                })
                // 构建模型
                .map(OllamaChatModel.OllamaChatModelBuilder::build);
    }

    @Override
    public Optional<StreamingChatModel> createStreamingChatModel(AIProxyWrapBean<?> wrapBean) {
        // 设置模型配置和结构化输出
        ModelConfig modelConfig = wrapBean.getConfig();
        return Optional.of(OllamaStreamingChatModel.builder())
                // 模型基本参数配置
                .map(ollamaStreamingChatModelBuilder -> {
                    setIfPresent(ollamaStreamingChatModelBuilder::baseUrl, modelConfig.getBaseUrl());
                    setIfPresent(ollamaStreamingChatModelBuilder::modelName, modelConfig.getModel());
                    setIfPresent(ollamaStreamingChatModelBuilder::temperature, modelConfig.getTemperature());
                    setIfPresent(ollamaStreamingChatModelBuilder::topK, modelConfig.getTopK());
                    setIfPresent(ollamaStreamingChatModelBuilder::topP, modelConfig.getTopP());
                    setIfPresent(ollamaStreamingChatModelBuilder::repeatPenalty, modelConfig.getRepeatPenalty());
                    setIfPresent(ollamaStreamingChatModelBuilder::seed, modelConfig.getSeed());
                    setIfPresent(ollamaStreamingChatModelBuilder::numPredict, modelConfig.getMaxTokens());
                    setIfPresent(ollamaStreamingChatModelBuilder::stop, modelConfig.getStop());
                    // TODO timeout 类型转换
                    // setIfPresent(ollamaStreamingChatModelBuilder::timeout, modelConfig.getTimeout());
                    return ollamaStreamingChatModelBuilder;
                })
                // 结构化输出配置
                .map(ollamaStreamingChatModelBuilder -> {
                    if (Objects.equals(ResponseType.JSON, wrapBean.getResponseType())) {
                        ollamaStreamingChatModelBuilder.responseFormat(ResponseFormat.JSON)
                                .supportedCapabilities(Capability.RESPONSE_FORMAT_JSON_SCHEMA);
                    }
                    return ollamaStreamingChatModelBuilder;
                })
                // 构建模型
                .map(OllamaStreamingChatModel.OllamaStreamingChatModelBuilder::build);
    }
}

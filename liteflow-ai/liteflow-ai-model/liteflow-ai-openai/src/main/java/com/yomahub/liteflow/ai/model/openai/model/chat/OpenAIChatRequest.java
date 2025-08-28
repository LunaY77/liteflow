package com.yomahub.liteflow.ai.model.openai.model.chat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.yomahub.liteflow.ai.engine.interact.callbacks.ChunkCallbackTransformer;
import com.yomahub.liteflow.ai.engine.interact.callbacks.ResultHandler;
import com.yomahub.liteflow.ai.engine.interact.transport.TransportListener;
import com.yomahub.liteflow.ai.engine.interact.transport.TransportType;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatOptions;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatRequest;
import com.yomahub.liteflow.ai.engine.model.chat.message.Message;
import com.yomahub.liteflow.ai.engine.model.output.ResponseType;
import com.yomahub.liteflow.ai.engine.model.output.structure.TypeReference;
import com.yomahub.liteflow.ai.engine.tool.registry.ToolRegistry;
import com.yomahub.liteflow.ai.engine.util.ObjectMapperHolder;
import com.yomahub.liteflow.ai.engine.util.request.RequestBody;

import java.util.List;
import java.util.Objects;

/**
 * OpenAI 聊天请求类
 *
 * @author 苍镜月
 * @see <a href="https://doc.ai-api.chat/openai-chat/">OpenAI Chat API Documentation</a>
 * @since TODO
 */

public class OpenAIChatRequest extends ChatRequest {

    // ==== RequestBody 相关参数 =====
    private static final String THINKING_KEY = "enable_thinking";
    private static final String FORMAT_KEY = "response_format";
    // OpenAI 结构化输出请求参数缓存
    private volatile JsonNode responseFormat;
    // ==== RequestBody 相关参数 =====

    public OpenAIChatRequest() {
    }

    public OpenAIChatRequest(
            List<Message> messages,
            ChatOptions options,
            boolean streaming,
            TransportType transportType,
            TransportListener transportListener,
            ResultHandler resultHandler,
            ChunkCallbackTransformer chunkCallbackTransformer,
            ResponseType responseType,
            TypeReference<?> targetType,
            boolean strict,
            ToolRegistry toolRegistry
    ) {
        super(messages, options, streaming, transportType,
                transportListener, resultHandler, chunkCallbackTransformer,
                responseType, targetType, strict, toolRegistry);
    }

    public OpenAIChatRequest(Builder builder) {
        super(builder);
    }

    @Override
    public RequestBody toRequestBody() {
        return super.toRequestBody()
                .putIf(ResponseType.JSON.equals(this.responseType), FORMAT_KEY, getResponseFormat());
    }

    /**
     * 获取 OpenAI 结构化输出的请求格式
     *
     * @see <a href="https://platform.openai.com/docs/guides/structured-outputs/supported-schemas#how-to-use">OpenAI结构化输出</a>
     */
    private JsonNode getResponseFormat() {
        if (Objects.isNull(this.responseFormat)) {
            synchronized (this) {
                if (Objects.isNull(this.responseFormat)) {
                    ObjectNode responseFormat = ObjectMapperHolder.createObjectNode();
                    responseFormat.put("type", "json_schema");
                    ObjectNode jsonSchema = ObjectMapperHolder.createObjectNode();
                    jsonSchema.put("name", outputParser.getTargetType().getTypeName());
                    jsonSchema.set("schema", outputParser.getJsonSchema());

                    responseFormat.set("json_schema", jsonSchema);
//                    responseFormat.put("strict", this.strict);

                    this.responseFormat = responseFormat;
                }
            }
        }
        return this.responseFormat;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends ChatRequest.Builder<Builder> {

        @Override
        public Builder self() {
            return this;
        }

        @Override
        public OpenAIChatRequest build() {
            checkAndAssign();
            return new OpenAIChatRequest(this);
        }
    }
}

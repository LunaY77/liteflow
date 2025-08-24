package com.yomahub.liteflow.ai.model.dashscope.model.chat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.yomahub.liteflow.ai.engine.interact.callbacks.ChunkCallbackTransformer;
import com.yomahub.liteflow.ai.engine.interact.callbacks.ResultHandler;
import com.yomahub.liteflow.ai.engine.interact.transport.TransportListener;
import com.yomahub.liteflow.ai.engine.interact.transport.TransportType;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatOptions;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatRequest;
import com.yomahub.liteflow.ai.engine.model.chat.message.Message;
import com.yomahub.liteflow.ai.engine.model.chat.message.UserMessage;
import com.yomahub.liteflow.ai.engine.model.output.ResponseType;
import com.yomahub.liteflow.ai.engine.model.output.structure.TypeReference;
import com.yomahub.liteflow.ai.engine.tool.registry.ToolRegistry;
import com.yomahub.liteflow.ai.engine.util.ObjectMapperHolder;
import com.yomahub.liteflow.ai.engine.util.request.RequestBody;

import java.util.ArrayList;
import java.util.List;

/**
 * DashScope 聊天请求类
 *
 * @author 苍镜月
 * @since TODO
 */

public class DashScopeChatRequest extends ChatRequest {

    // ==== RequestBody 相关参数 =====
    private static final String INPUT_KEY = "input";
    private static final String FORMAT_KEY = "response_format";
    private static final String RESPONSE_FORMAT_TEXT = "text";
    private static final String RESPONSE_FORMAT_JSON = "json_object";

    @Override
    protected List<Message> appendFormatInstructionsIfNeeded() {
        if (ResponseType.JSON.equals(this.responseType)) {
            this.messages.add(new UserMessage(outputParser.getOutputInstruction()));
            return new ArrayList<>(this.messages);
        }
        return this.messages;
    }

    // ==== RequestBody 相关参数 =====

    public DashScopeChatRequest() {
    }

    public DashScopeChatRequest(
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

    public DashScopeChatRequest(Builder builder) {
        super(builder);
    }

    @Override
    public RequestBody toRequestBody() {
        return super.toRequestBody()
                .put("parameters", getParameters())
                .remove(MESSAGES_KEY)
                .put(INPUT_KEY, getInput())
                .put(FORMAT_KEY, ResponseType.JSON.equals(this.responseType) ?
                        RESPONSE_FORMAT_JSON : RESPONSE_FORMAT_TEXT);
    }

    public JsonNode getParameters() {
        ObjectNode parametersNode = ObjectMapperHolder.createObjectNode();
        parametersNode.put("result_format", "message");
        parametersNode.put("incremental_output", true);
        return parametersNode;
    }

    private JsonNode getInput() {
        ObjectNode inputNode = ObjectMapperHolder.createObjectNode();
        inputNode.set("messages", ObjectMapperHolder.valueToTree(this.messages));
        return inputNode;
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
        public DashScopeChatRequest build() {
            checkAndAssign();
            return new DashScopeChatRequest(this);
        }
    }
}

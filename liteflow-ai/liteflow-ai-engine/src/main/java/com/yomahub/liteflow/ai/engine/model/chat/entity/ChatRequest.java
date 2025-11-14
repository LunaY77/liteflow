package com.yomahub.liteflow.ai.engine.model.chat.entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.yomahub.liteflow.ai.engine.interact.transport.TransportType;
import com.yomahub.liteflow.ai.engine.model.ModelRequest;
import com.yomahub.liteflow.ai.engine.model.chat.message.Message;
import com.yomahub.liteflow.ai.engine.model.output.ResponseType;
import com.yomahub.liteflow.ai.engine.model.output.structure.TypeReference;
import com.yomahub.liteflow.ai.engine.model.output.structure.generator.JsonSchemaGenerator;
import com.yomahub.liteflow.ai.engine.model.output.structure.parser.JsonSchemaParser;
import com.yomahub.liteflow.ai.engine.tool.ToolCallBack;
import com.yomahub.liteflow.ai.engine.tool.ToolDefinition;
import com.yomahub.liteflow.ai.engine.tool.registry.ToolRegistry;
import com.yomahub.liteflow.ai.engine.util.request.RequestBody;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Chat 请求体
 *
 * @author 苍镜月
 * @since 2.16.0
 */
public class ChatRequest implements ModelRequest {

    /**
     * 上下文消息列表
     */
    protected final List<Message> messages;

    /**
     * 聊天选项配置
     */
    protected final ChatOptions options;

    /**
     * 流式输出模式，默认为 true
     */
    protected final boolean streaming;

    /**
     * 传输类型，默认为 SSE（Server-Sent Events）
     */
    protected final TransportType transportType;

    /**
     * 响应类型，默认为文本类型
     */
    protected final ResponseType responseType;

    /**
     * 是否为严格模式
     */
    protected final boolean strict;

    /**
     * 输出解析器，用于解析模型的输出结果，由 targetType 生成
     */
    protected final JsonSchemaParser<?> outputParser;

    /**
     * 可使用的工具注册表
     */
    protected final ToolRegistry toolRegistry;

    // ==== RequestBody 相关参数 =====
    protected static final String MESSAGES_KEY = "messages";
    protected static final String STREAM_KEY = "stream";
    // 对于支持请求体中的结构化参数的模型，可以在实现类中添加结构化参数的 key，并自行添加到 RequestBody 中。

    /**
     * 对于某些模型，可能不支持请求体中的结构化参数，那么如果需要附加结构化输出提示词，可以在此方法中实现。
     *
     * @return 添加了结构化输出提示词的上下文
     */
    protected List<Message> appendFormatInstructionsIfNeeded() {
        return this.messages;
    }

    protected static final String TOOLS_KEY = "tools";
    // ==== RequestBody 相关参数 =====

    public ChatRequest() {
        this.messages = new ArrayList<>();
        this.options = ChatOptions.DEFAULT;
        this.streaming = true; // 默认启用流式输出
        this.transportType = TransportType.SSE; // 默认使用 SSE 传输
        this.responseType = ResponseType.TEXT; // 默认响应类型为文本
        TypeReference<String> targetType = new TypeReference<String>() {
        }; // 默认目标类型为 String
        this.strict = true;
        this.outputParser = JsonSchemaParser.fromTypeReference(targetType);
        this.toolRegistry = null;
    }

    public ChatRequest(
            List<Message> messages,
            ChatOptions options,
            boolean streaming,
            TransportType transportType,
            ResponseType responseType,
            TypeReference<?> targetType,
            boolean strict,
            ToolRegistry toolRegistry
    ) {
        this.messages = messages;
        this.options = options;
        this.streaming = streaming;
        this.transportType = transportType;
        this.responseType = responseType;
        this.strict = strict;
        this.outputParser = JsonSchemaParser.fromTypeReference(targetType, strict);
        this.toolRegistry = toolRegistry;
        checkTransportConsistency();
        checkResponseTypeConsistency();
    }

    /**
     * 私有构造函数，使用 Builder 模式创建 ChatRequest 实例
     *
     * @param builder Builder 实例
     */
    public ChatRequest(Builder<?> builder) {
        this.messages = builder.messages;
        this.options = builder.options;
        this.streaming = builder.streaming;
        this.transportType = builder.transportType;
        this.responseType = builder.responseType;
        this.strict = builder.strict;
        this.outputParser = JsonSchemaParser.fromType(builder.targetType, builder.strict);
        this.toolRegistry = builder.toolRegistry;
        checkTransportConsistency();
        checkResponseTypeConsistency();
    }

    /**
     * 检查传输类型与流式输出模式的一致性。
     * 如果流式输出模式启用但传输类型为HTTP，则抛出异常。
     * 如果阻塞式输出模式启用但传输类型不是HTTP，则抛出异常。
     */
    protected void checkTransportConsistency() {
        if (this.streaming && this.transportType == TransportType.HTTP) {
            throw new IllegalArgumentException("Streaming mode is enabled, but HTTP transport is not supported. Please use SSE or WebSocket transport.");
        } else if (!this.streaming && this.transportType != TransportType.HTTP) {
            throw new IllegalArgumentException("Blocking mode is enabled, but the transport type does not support HTTP. Please use HTTP transport.");
        }
    }

    /**
     * 检查响应类型与目标类型的一致性。
     * 如果响应类型为文本（TEXT），但目标类型不是 String，则抛出异常。
     */
    protected void checkResponseTypeConsistency() {
        if (this.responseType == ResponseType.TEXT &&
                !Objects.equals("java.lang.String", getTargetType().getTypeName())) {
            throw new IllegalArgumentException("Response type is TEXT, but target type is not String. Please check the targetType setting.");
        }
    }

    @Override
    public RequestBody toRequestBody() {
        return RequestBody.of()
                // messages
                .putIfNotEmpty(MESSAGES_KEY, appendFormatInstructionsIfNeeded())
                // 是否开启流式输出
                .put(STREAM_KEY, streaming)
                // chat options
                .merge(options.toRequestBody())
                // tools
                .putIfNotEmpty(TOOLS_KEY, getToolsJsonSchema());
    }

    /**
     * 获取可用工具的 JSON Schema 列表。
     *
     * @return 工具的 JSON Schema 列表，如果没有可用工具则返回空列表。
     */
    protected List<JsonNode> getToolsJsonSchema() {
        if (Objects.isNull(toolRegistry) || toolRegistry.getAllTools().isEmpty()) {
            return Collections.emptyList();
        }
        return toolRegistry.getAllTools()
                .stream()
                .map(ToolCallBack::getDefinition)
                .map(ToolDefinition::toJsonSchema)
                .collect(Collectors.toList());
    }

    public List<Message> getMessages() {
        return messages;
    }

    public ChatOptions getOptions() {
        return options;
    }

    public boolean isStreaming() {
        return streaming;
    }

    public TransportType getTransportType() {
        return transportType;
    }

    public ResponseType getResponseType() {
        return responseType;
    }

    public Type getTargetType() {
        return outputParser.getTargetType();
    }

    public JsonSchemaParser<?> getOutputParser() {
        return outputParser;
    }

    public boolean isStrict() {
        return strict;
    }

    public ToolRegistry getToolRegistry() {
        return toolRegistry;
    }

    public static Builder<?> builder() {
        return new Builder.BuilderImpl();
    }

    public static abstract class Builder<B extends Builder<B>> {
        protected List<Message> messages;
        protected ChatOptions options;
        protected boolean streaming = true; // 默认启用流式输出
        protected TransportType transportType = TransportType.SSE; // 默认使用 SSE 传输
        protected ResponseType responseType = ResponseType.TEXT;
        protected Type targetType = String.class;
        protected boolean strict = true;
        protected ToolRegistry toolRegistry;

        public abstract B self();

        public abstract ChatRequest build();

        /**
         * 检查并分配必要的属性值
         */
        protected void checkAndAssign() {
            if (Objects.isNull(options)) {
                options = ChatOptions.DEFAULT;
            }
        }

        /**
         * 设置上下文消息
         *
         * @param messages 上下文消息列表
         * @see Message
         */
        public B messages(List<Message> messages) {
            this.messages = messages;
            return self();
        }

        /**
         * 设置聊天选项
         *
         * @param options 聊天选项配置
         * @see ChatOptions
         */
        public B options(ChatOptions options) {
            this.options = options;
            return self();
        }

        /**
         * 设置是否启用流式输出
         *
         * @param streaming 是否启用流式输出
         */
        public B streaming(boolean streaming) {
            this.streaming = streaming;
            return self();
        }

        /**
         * 设置传输类型
         *
         * @param transportType 传输类型
         * @see TransportType
         */
        public B transportType(TransportType transportType) {
            this.transportType = transportType;
            return self();
        }

        /**
         * 设置响应类型
         *
         * @param responseType 响应类型
         * @see ResponseType
         */
        public B responseType(ResponseType responseType) {
            this.responseType = responseType;
            return self();
        }

        /**
         * 设置目标类型引用，用于指定响应体的具体类型
         *
         * @param targetType 目标类型引用
         * @see Type
         */
        public B targetType(Type targetType) {
            this.targetType = targetType;
            return self();
        }

        /**
         * 设置目标类型引用，用于指定响应体的具体类型
         *
         * @param targetType 目标类型引用
         * @see TypeReference
         */
        public B targetType(TypeReference<?> targetType) {
            this.targetType = targetType.getType();
            return self();
        }

        /**
         * 是否为严格模式，默认开启
         *
         * @param strict 是否为严格模式
         * @see JsonSchemaGenerator#generate(Type, boolean)
         */
        public B strict(boolean strict) {
            this.strict = strict;
            return self();
        }

        /**
         * 设置可使用的工具
         *
         * @param toolRegistry 工具注册
         * @see ToolRegistry
         * @see com.yomahub.liteflow.ai.engine.tool.registry.StaticToolRegistry
         * @see com.yomahub.liteflow.ai.engine.tool.registry.DelegatingToolRegistry
         * @see com.yomahub.liteflow.ai.engine.tool.registry.ScanningToolRegistry
         */
        public B toolRegistry(ToolRegistry toolRegistry) {
            this.toolRegistry = toolRegistry;
            return self();
        }

        private static class BuilderImpl extends Builder<BuilderImpl> {

            @Override
            public BuilderImpl self() {
                return this;
            }

            @Override
            public ChatRequest build() {
                checkAndAssign();
                return new ChatRequest(this);
            }
        }
    }
}

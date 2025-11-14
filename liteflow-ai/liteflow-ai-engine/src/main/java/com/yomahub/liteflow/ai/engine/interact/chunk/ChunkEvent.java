package com.yomahub.liteflow.ai.engine.interact.chunk;

import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatResponse;

import java.util.Objects;

/**
 * 流式事件封装
 * <p>
 * 该类封装了整个流式交互过程中产生的各种事件，包括：
 * - 请求开始事件 (START)
 * - 数据分块事件 (CHUNK)
 * - 请求完成事件 (COMPLETE)
 * - 错误事件 (ERROR)
 * <p>
 * 每个事件都包含相关的上下文信息，允许用户在处理流时访问中间状态。
 *
 * @author 苍镜月
 * @since 2.16.0
 */
public class ChunkEvent {

    /**
     * 事件类型枚举
     */
    public enum EventType {
        /**
         * 流开始事件 - 在流开始处理时触发
         */
        START,
        /**
         * 数据分块事件 - 接收到新的数据分块时触发
         */
        CHUNK,
        /**
         * 流完成事件 - 所有数据处理完毕时触发
         */
        COMPLETE,
        /**
         * 错误事件 - 处理过程中发生错误时触发
         */
        ERROR
    }

    /**
     * 事件类型
     */
    private final EventType eventType;

    /**
     * 原始的 JSON 响应块（协议相关的原始格式）
     * 在 CHUNK 事件中可能有值，其他事件类型为 null
     */
    private final String rawChunk;

    /**
     * 转换后的框架标准响应块
     * 在 CHUNK 事件中可能有值，其他事件类型为 null
     */
    private final StreamingProtocolChunk transformedChunk;

    /**
     * 交互上下文 - 包含当前的交互状态
     * 在 CHUNK 和 COMPLETE 事件中有值，START 事件为初始化的上下文，ERROR 事件可能为 null
     */
    private final InteractContext context;

    /**
     * 错误信息 - 仅在 ERROR 事件中有值
     */
    private final Throwable error;

    /**
     * 最终响应 - 仅在 COMPLETE 事件中有值
     */
    private final ChatResponse finalResponse;

    /**
     * 私有构造函数
     */
    private ChunkEvent(EventType eventType, String rawChunk, StreamingProtocolChunk transformedChunk,
                       InteractContext context, Throwable error, ChatResponse finalResponse) {
        this.eventType = Objects.requireNonNull(eventType, "eventType cannot be null");
        this.rawChunk = rawChunk;
        this.transformedChunk = transformedChunk;
        this.context = context;
        this.error = error;
        this.finalResponse = finalResponse;
    }

    /**
     * 创建开始事件
     *
     * @param context 交互上下文
     * @return 开始事件
     */
    public static ChunkEvent start(InteractContext context) {
        return new ChunkEvent(EventType.START, null, null, context, null, null);
    }

    /**
     * 创建数据分块事件
     *
     * @param rawChunk         原始 JSON 分块
     * @param transformedChunk 转换后的框架标准分块
     * @param context          交互上下文
     * @return 数据分块事件
     */
    public static ChunkEvent chunk(String rawChunk, StreamingProtocolChunk transformedChunk, InteractContext context) {
        return new ChunkEvent(EventType.CHUNK, rawChunk, transformedChunk, context, null, null);
    }

    /**
     * 创建完成事件
     *
     * @param context       交互上下文
     * @param finalResponse 最终聊天响应
     * @return 完成事件
     */
    public static ChunkEvent complete(InteractContext context, ChatResponse finalResponse) {
        return new ChunkEvent(EventType.COMPLETE, null, null, context, null, finalResponse);
    }

    /**
     * 创建错误事件
     *
     * @param error 错误信息
     * @return 错误事件
     */
    public static ChunkEvent error(Throwable error) {
        return new ChunkEvent(EventType.ERROR, null, null, null, error, null);
    }

    /**
     * 创建错误事件 - 带上下文
     *
     * @param error   错误信息
     * @param context 交互上下文
     * @return 错误事件
     */
    public static ChunkEvent error(Throwable error, InteractContext context) {
        return new ChunkEvent(EventType.ERROR, null, null, context, error, null);
    }

    // ==================== Getters ====================

    public EventType getEventType() {
        return eventType;
    }

    public String getRawChunk() {
        return rawChunk;
    }

    public StreamingProtocolChunk getTransformedChunk() {
        return transformedChunk;
    }

    public InteractContext getContext() {
        return context;
    }

    public Throwable getError() {
        return error;
    }

    public ChatResponse getFinalResponse() {
        return finalResponse;
    }

    // ==================== Convenience Methods ====================

    /**
     * 是否为开始事件
     *
     * @return true 如果是开始事件
     */
    public boolean isStart() {
        return eventType == EventType.START;
    }

    /**
     * 是否为分块事件
     *
     * @return true 如果是分块事件
     */
    public boolean isChunk() {
        return eventType == EventType.CHUNK;
    }

    /**
     * 是否为完成事件
     *
     * @return true 如果是完成事件
     */
    public boolean isComplete() {
        return eventType == EventType.COMPLETE;
    }

    /**
     * 是否为错误事件
     *
     * @return true 如果是错误事件
     */
    public boolean isError() {
        return eventType == EventType.ERROR;
    }

    @Override
    public String toString() {
        return "ChunkEvent{" +
                "eventType=" + eventType +
                ", rawChunk=" + (rawChunk != null ? rawChunk.substring(0, Math.min(50, rawChunk.length())) + "..." : "null") +
                ", transformedChunk=" + transformedChunk +
                ", context=" + context +
                ", error=" + error +
                ", finalResponse=" + finalResponse +
                '}';
    }
}

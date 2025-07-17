package com.yomahub.liteflow.ai.interact.transport;

import com.yomahub.liteflow.ai.interact.pipeline.ChunkProcessPipeline;
import com.yomahub.liteflow.ai.model.chat.entity.ChatConfig;
import com.yomahub.liteflow.ai.model.chat.entity.ChatRequest;
import com.yomahub.liteflow.ai.model.chat.entity.ChatResponse;

/**
 * 数据传输接口
 *
 * @author 苍镜月
 * @since TODO
 */

public interface Transport {

    /**
     * 启动流式传输
     *
     * @param config   聊天配置
     * @param request  聊天请求
     * @param pipeline 处理管道
     * @param listener 传输监听器
     */
    void start(ChatConfig config, ChatRequest request, ChunkProcessPipeline pipeline, TransportListener listener);

    /**
     * 启动阻塞式传输
     *
     * @param config   聊天配置
     * @param request  聊天请求
     * @param pipeline 处理管道
     * @return 聊天响应
     */
    ChatResponse startBlocking(ChatConfig config, ChatRequest request, ChunkProcessPipeline pipeline);

    /**
     * 关闭传输
     */
    void close();

    /**
     * 构建请求体 JSON String
     *
     * @return 请求体 JSON String
     */
    default String buildRequestBody(ChatConfig config, ChatRequest request) {
        return config.toRequestBody()
                .merge(request.toRequestBody())
                .toJsonString();
    }
}

package com.yomahub.liteflow.test.ai.mock.mockbean;

import com.yomahub.liteflow.ai.engine.interact.pipeline.ChunkProcessPipeline;
import com.yomahub.liteflow.ai.engine.interact.transport.Transport;
import com.yomahub.liteflow.ai.engine.interact.transport.TransportListener;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatConfig;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatRequest;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatResponse;
import com.yomahub.liteflow.test.ai.mock.TestDataReader;

import java.util.List;

/**
 * Mock 传输实现类，用于在测试中模拟 LLM 交互。
 *
 * @author 苍镜月
 * @since 2.16.0
 */

public class MockTransport implements Transport {

    private final String blockingResponse;
    private final List<String> streamingChunks;

    private ChunkProcessPipeline pipeline;
    private TransportListener listener = TransportListener.getDefault();
    private boolean isStop = false;

    public MockTransport(MockConfig config) {
        this.blockingResponse = TestDataReader.getBlockingResponse(config.getProvider(), config.getResponseType());
        this.streamingChunks = TestDataReader.getStreamingChunks(config.getProvider(), config.getResponseType());
    }

    @Override
    public void start(ChatConfig config, ChatRequest request, ChunkProcessPipeline pipeline, TransportListener listener) {
        this.listener = listener;
        this.pipeline = pipeline;
        listener.onStart(pipeline.getContext());

        for (String streamingChunk : streamingChunks) {
            pipeline.processStreaming(streamingChunk);
        }

        close();
    }

    @Override
    public ChatResponse startBlocking(ChatConfig config, ChatRequest request, ChunkProcessPipeline pipeline) {
        this.pipeline = pipeline;
        return pipeline.processBlocking(blockingResponse);
    }

    @Override
    public void close() {
        if (!this.isStop) {
            this.isStop = true;
            this.listener.onClose(this.pipeline.getContext());
        }
    }
}

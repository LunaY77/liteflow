package com.yomahub.liteflow.test.ai.mock.mockbean;

import com.yomahub.liteflow.ai.engine.interact.transport.Transport;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatConfig;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatRequest;
import com.yomahub.liteflow.test.ai.mock.TestDataReader;
import io.reactivex.rxjava3.core.Flowable;

import java.util.List;

/**
 * Mock 传输实现类，用于在测试中模拟 LLM 交互。
 *
 * @author 苍镜月
 * @since 2.16.0
 */

public class MockTransport implements Transport {

    private final MockConfig config;

    private boolean isStop = false;

    public MockTransport(MockConfig config) {
        this.config = config;
    }

    @Override
    public Flowable<String> startStreaming(ChatConfig config, ChatRequest request) {
        List<String> streamingChunks = TestDataReader.getStreamingChunks(this.config.getProvider(), this.config.getRequestType());
        return Flowable.fromIterable(streamingChunks);
    }

    @Override
    public String startBlocking(ChatConfig config, ChatRequest request) {
        return TestDataReader.getBlockingResponse(this.config.getProvider(), this.config.getRequestType());
    }

    @Override
    public void close() {
        if (!this.isStop) {
            this.isStop = true;
        }
    }
}

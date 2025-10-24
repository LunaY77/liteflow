package com.yomahub.liteflow.test.ai.mock.mockbean;

import com.yomahub.liteflow.ai.engine.interact.LlmInteractClient;
import com.yomahub.liteflow.ai.engine.interact.transport.TransportType;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatConfig;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatRequest;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatResponse;
import org.mockito.Mockito;

import java.util.concurrent.CompletableFuture;

/**
 * Mock 交互客户端
 *
 * @author 苍镜月
 * @since 2.16.0
 */

public class MockInteractClient extends LlmInteractClient {

    /**
     * 辅助方法：创建一个“间谍” ChatRequest。
     * 这个间谍会拦截 getTransportType().getTransportInstance() 调用链。
     *
     * @param originalRequest 原始的 ChatRequest
     * @return 一个配置了 MockTransport 的 spied ChatRequest
     */
    private ChatRequest createSpiedRequest(ChatRequest originalRequest) {
        // 1. 获取 MockConfig
        final MockConfig mockConfig = MockConfigHolder.getMockConfig();

        // 2. 模拟 TransportType 对象
        TransportType mockTransportType = Mockito.mock(TransportType.class);

        // 3. 存根(Stub) getTransportInstance() 方法，使其返回我们的 MockTransport
        Mockito.when(mockTransportType.getTransportInstance())
                .thenAnswer(invocation -> new MockTransport(mockConfig));

        // 4. "侦察"(Spy) 传入的原始 request 对象
        ChatRequest spyRequest = Mockito.spy(originalRequest);

        // 5. 存根(Stub) spyRequest 上的 getTransportType() 方法
        //    使用 doReturn().when() 是在 Spy 对象上操作的标准实践
        Mockito.doReturn(mockTransportType).when(spyRequest).getTransportType();

        // 6. 返回这个被"侦察"和"存根"的 request
        return spyRequest;
    }

    /**
     * 重写 stream 方法
     */
    @Override
    public void stream(ChatConfig config, ChatRequest request) {
        // 创建一个 spied request，它在内部被配置为使用 MockTransport
        ChatRequest spiedRequest = createSpiedRequest(request);

        // 调用父类的 stream 方法。
        // 当父类的 InteractManager 构造函数被调用时，
        // 它将使用我们的 spiedRequest，并最终获取到 MockTransport。
        super.stream(config, spiedRequest);
    }

    /**
     * 重写 chat 方法
     */
    @Override
    public ChatResponse chat(ChatConfig config, ChatRequest request) {
        // 同上，创建 spied request
        ChatRequest spiedRequest = createSpiedRequest(request);

        // 调用父类的 chat 方法
        return super.chat(config, spiedRequest);
    }

    /**
     * 重写 chatAsync 方法
     */
    @Override
    public CompletableFuture<ChatResponse> chatAsync(ChatConfig config, ChatRequest request) {
        // 同上，创建 spied request
        ChatRequest spiedRequest = createSpiedRequest(request);

        // 调用父类的 chatAsync 方法
        return super.chatAsync(config, spiedRequest);
    }
}

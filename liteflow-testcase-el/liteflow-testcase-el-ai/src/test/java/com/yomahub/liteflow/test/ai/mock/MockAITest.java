package com.yomahub.liteflow.test.ai.mock;

import com.yomahub.liteflow.ai.domain.enums.ProviderEnum;
import com.yomahub.liteflow.ai.model.ModelFactory;
import com.yomahub.liteflow.test.ai.BaseTest;
import com.yomahub.liteflow.test.ai.mock.mockbean.ConfigurableMockModelProvider;
import com.yomahub.liteflow.test.ai.mock.mockbean.MockConfig;
import com.yomahub.liteflow.test.ai.mock.mockbean.MockConfigHolder;
import org.junit.jupiter.api.AfterEach;

/**
 * AI 调用 Mock 测试基类(提供 mock 数据)
 * <p>
 * 按需、参数化的配置 mock，多测试间隔离
 *
 * @author 苍镜月
 * @since 2.16.0
 */

public class MockAITest extends BaseTest {

    /**
     * 动态 Mock Provider 实例
     */
    private static final ConfigurableMockModelProvider MOCK_PROVIDER = new ConfigurableMockModelProvider();

    /**
     * 在每个测试方法执行后调用。
     * 清理 ThreadLocal 并注销 Mock Provider，防止影响其他测试。
     */
    @AfterEach
    public void cleanupMockProvider() {
        // 清理线程本地变量
        MockConfigHolder.clear();
    }

    /**
     * 供子测试类调用，用于配置当前测试所需的 Mock 数据。
     *
     * @param provider     要 Mock 的 Provider
     * @param responseType 要 Mock 的响应类型
     */
    protected void setupChatMock(ProviderEnum provider, TestDataReader.RequestType... responseType) {
        MockConfigHolder.setMockConfig(new MockConfig(provider, responseType));
        ModelFactory.register(MOCK_PROVIDER);
    }
}

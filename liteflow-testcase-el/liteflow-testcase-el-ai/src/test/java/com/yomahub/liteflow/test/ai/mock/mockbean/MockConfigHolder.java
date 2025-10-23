package com.yomahub.liteflow.test.ai.mock.mockbean;

/**
 * 用于在当前测试线程中存储 Mock 配置
 *
 * @author 苍镜月
 * @since 2.16.0
 */

public class MockConfigHolder {

    private static final ThreadLocal<MockConfig> MOCK_CONFIG = new ThreadLocal<>();

    public static void setMockConfig(MockConfig mockConfig) {
        MOCK_CONFIG.set(mockConfig);
    }

    public static MockConfig getMockConfig() {
        return MOCK_CONFIG.get();
    }

    public static void clear() {
        MOCK_CONFIG.remove();
    }
}

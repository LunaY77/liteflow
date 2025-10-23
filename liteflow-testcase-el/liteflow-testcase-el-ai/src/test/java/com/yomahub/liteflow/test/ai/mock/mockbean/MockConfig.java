package com.yomahub.liteflow.test.ai.mock.mockbean;

import com.yomahub.liteflow.ai.domain.enums.ProviderEnum;
import com.yomahub.liteflow.test.ai.mock.TestDataReader;

/**
 * 存储 Mock 配置的数据类
 *
 * @author 苍镜月
 * @since 2.16.0
 */

public class MockConfig {

    private final ProviderEnum provider;
    private final TestDataReader.ResponseType responseType;

    public MockConfig(ProviderEnum provider, TestDataReader.ResponseType responseType) {
        this.provider = provider;
        this.responseType = responseType;
    }

    public ProviderEnum getProvider() {
        return provider;
    }

    public TestDataReader.ResponseType getResponseType() {
        return responseType;
    }
}

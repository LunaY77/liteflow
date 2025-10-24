package com.yomahub.liteflow.test.ai.mock.mockbean;

import com.yomahub.liteflow.ai.domain.enums.ProviderEnum;
import com.yomahub.liteflow.test.ai.mock.TestDataReader;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

/**
 * 存储 Mock 配置的数据类
 *
 * @author 苍镜月
 * @since 2.16.0
 */

public class MockConfig {

    private final ProviderEnum provider;
    private final Queue<TestDataReader.RequestType> requestType;

    public MockConfig(ProviderEnum provider, TestDataReader.RequestType... requestType) {
        if (Objects.isNull(requestType) || requestType.length == 0) {
            throw new IllegalArgumentException("RequestType array cannot be null or empty");
        }
        this.provider = provider;
        this.requestType = new LinkedList<>(Arrays.asList(requestType));
    }

    public ProviderEnum getProvider() {
        return provider;
    }

    public TestDataReader.RequestType getRequestType() {
        TestDataReader.RequestType nextType = requestType.poll();
        if (Objects.isNull(nextType)) {
            throw new IllegalStateException("No more RequestType available in the queue");
        }
        return nextType;
    }
}

package com.yomahub.liteflow.ai.domain.enums;

/**
 * 模型提供商枚举类
 * <p>
 * 为同时保证易用性和扩展性，采用枚举并预留了一个 OTHER 类型，方便用户自定义其他模型提供商的实现，同时也方便 mock 测试
 *
 * @author 苍镜月
 * @since 2.16.0
 */

public enum ProviderEnum {
    OLLAMA("ollama"),
    OPENAI("openai"),
    DASHSCOPE("dashscope"),
    // 预留一个其他 provider 类型，以便扩展 + 测试 mock
    OTHER("other"),
    ;

    private final String providerName;

    ProviderEnum(String providerName) {
        this.providerName = providerName;
    }

    public String getProviderName() {
        return providerName;
    }
}

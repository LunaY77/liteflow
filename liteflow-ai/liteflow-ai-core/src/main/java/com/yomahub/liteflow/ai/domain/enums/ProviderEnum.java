package com.yomahub.liteflow.ai.domain.enums;

/**
 * 模型提供商枚举类
 *
 * @author 苍镜月
 * @since 2.16.0
 */

public enum ProviderEnum {
    OLLAMA("ollama"),
    OPENAI("openai"),
    DASHSCOPE("dashscope"),
    ;

    private final String providerName;

    ProviderEnum(String providerName) {
        this.providerName = providerName;
    }

    public String getProviderName() {
        return providerName;
    }
}

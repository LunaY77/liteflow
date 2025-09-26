package com.yomahub.liteflow.ai.domain.enums;

/**
 * 资源前缀枚举
 *
 * @author 苍镜月
 * @since 2.16.0
 */

public enum ResourcePrefixEnum {
    CLASSPATH_PREFIX("classpath:"),
    FILE_PREFIX("file:"),
    URL_PREFIX("url:"),
    TEXT_PREFIX("text:"),
    ;

    private final String prefix;

    ResourcePrefixEnum(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}

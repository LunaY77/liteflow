package com.yomahub.liteflow.ai.engine.model.chat.message;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 消息类型
 *
 * @author 苍镜月
 * @since 2.16.0
 */

public enum MessageType {
    USER(1, "user"),
    ASSISTANT(2, "assistant"),
    SYSTEM(3, "system"),
    TOOL(4, "tool"),
    ;
    private final Integer code;
    private final String role;

    MessageType(Integer type, String desc) {
        this.code = type;
        this.role = desc;
    }

    public Integer getCode() {
        return code;
    }

    @JsonValue
    public String getRole() {
        return role;
    }

    private static final Map<Integer, MessageType> cache;

    static {
        cache = Arrays.stream(MessageType.values()).collect(Collectors.toMap(MessageType::getCode, Function.identity()));
    }

    public static MessageType of(Integer code) {
        return cache.get(code);
    }
}

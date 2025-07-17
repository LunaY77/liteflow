package com.yomahub.liteflow.ai.interact.protocol;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 流式消息协议类型枚举
 *
 * @author 苍镜月
 * @since TODO
 */

public enum StreamingProtocolType {
    // 文本内容
    TEXT(1, "text"),
    // 工具调用
    TOOL_CALLS(2, "tool_calls"),
    // 推理内容
    THINKING(3, "thinking"),
    // 停止信号
    STOP(4, "stop"),
    // 错误信息
    ERROR(5, "error"),
    // token 使用情况
    USAGE(6, "usage"),
    // base64 格式的图片数据
    BASE64_IMAGE(7, "base64_image"),
    // 未知类型数据
    DATA(8, "未知数据"),
    ;
    private final Integer code;
    private final String desc;

    StreamingProtocolType(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    private static final Map<Integer, StreamingProtocolType> cache;

    static {
        cache = Arrays.stream(StreamingProtocolType.values()).collect(Collectors.toMap(StreamingProtocolType::getCode, Function.identity()));
    }

    public static StreamingProtocolType of(Integer code) {
        return cache.get(code);
    }
}

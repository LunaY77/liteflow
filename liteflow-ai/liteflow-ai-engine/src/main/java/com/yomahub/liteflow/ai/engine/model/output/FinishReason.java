package com.yomahub.liteflow.ai.engine.model.output;

/**
 * 模型生成停止原因
 *
 * @author 苍镜月
 * @since 2.16.0
 */

public enum FinishReason {
    /**
     * 模型正常生成完成
     */
    STOP,

    /**
     * 模型生成达到最大长度限制
     */
    LENGTH,

    /**
     * 需要进行工具调用
     */
    TOOL_CALL,

    /**
     * 其他原因
     */
    OTHER,
    ;
}

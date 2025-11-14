package com.yomahub.liteflow.ai.engine.tool.registry;

import com.yomahub.liteflow.ai.engine.exception.LiteFlowAIEngineException;
import com.yomahub.liteflow.ai.engine.model.chat.message.ToolMessage;
import com.yomahub.liteflow.ai.engine.tool.ToolCall;
import com.yomahub.liteflow.ai.engine.tool.ToolCallBack;

import java.util.Collection;
import java.util.Objects;

/**
 * 工具调用注册接口
 *
 * @author 苍镜月
 * @since 2.16.0
 */

public interface ToolRegistry {

    /**
     * 获取工具
     *
     * @param toolName 工具名称
     * @return 工具实例
     */
    ToolCallBack getTool(String toolName);

    /**
     * 获取所有注册的工具
     *
     * @return 所有工具的集合
     */
    Collection<ToolCallBack> getAllTools();

    /**
     * 执行工具调用
     *
     * @param toolCall 工具调用信息
     * @return 工具调用结果消息
     */
    default ToolMessage executeToolCall(ToolCall toolCall) {
        ToolCallBack toolCallBack = getTool(toolCall.getName());
        if (Objects.isNull(toolCallBack)) {
            throw new LiteFlowAIEngineException("Unable to find target tool with tool name: " + toolCall.getName());
        }
        String toolResult = toolCallBack.call(toolCall.getArguments().toString());
        return new ToolMessage(toolResult, toolCall.getId(), toolCall.getName());
    }
}

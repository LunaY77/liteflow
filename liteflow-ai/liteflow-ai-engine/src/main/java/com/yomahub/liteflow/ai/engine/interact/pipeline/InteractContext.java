package com.yomahub.liteflow.ai.engine.interact.pipeline;

import cn.hutool.core.collection.CollectionUtil;
import com.yomahub.liteflow.ai.engine.model.output.TokenUsage;
import com.yomahub.liteflow.ai.engine.tool.ToolCall;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 大模型交互上下文(engine模块内部使用，非 LiteFlow 上下文类)
 *
 * @author 苍镜月
 * @since TODO
 */

public class InteractContext {

    private final String chatId;

    private final StringBuilder aggregatedText;

    private final StringBuilder aggregatedThinking;

    private TokenUsage tokenUsage;

    // 设计上支持并行工具调用，但是目前仅支持单轮单次调用
    private List<ToolCall> toolCalls;

    private boolean thinkingInContent;

    private boolean isFinished = false;

    public InteractContext() {
        chatId = "interact_" + UUID.randomUUID();
        aggregatedText = new StringBuilder();
        aggregatedThinking = new StringBuilder();
        toolCalls = new ArrayList<>();
    }

    public String getChatId() {
        return chatId;
    }

    public void addText(String text) {
        aggregatedText.append(text);
    }

    public void addThinking(String thinking) {
        aggregatedThinking.append(thinking);
    }

    public String getAggregatedText() {
        return aggregatedText.toString();
    }

    public String getAggregatedThinking() {
        return aggregatedThinking.toString();
    }

    public TokenUsage getTokenUsage() {
        return tokenUsage;
    }

    public boolean isThinkingInContent() {
        return thinkingInContent;
    }

    public void setTokenUsage(TokenUsage tokenUsage) {
        this.tokenUsage = tokenUsage;
    }

    public void setThinkingInContent(boolean thinkingInContent) {
        this.thinkingInContent = thinkingInContent;
    }

    public List<ToolCall> getToolCalls() {
        return toolCalls;
    }

    /**
     * 设置工具调用列表
     *
     * @param toolCalls 工具调用列表
     */
    public void setToolCalls(List<ToolCall> toolCalls) {
        this.toolCalls = toolCalls;
    }

    /**
     * 添加工具调用
     *
     * @param toolCall 工具调用
     */
    public void addToolCall(ToolCall toolCall) {
        this.toolCalls.add(toolCall);
    }

    /**
     * 是否存在工具调用
     *
     * @return true 存在工具调用，false 不存在工具调用
     */
    public boolean hasToolCalls() {
        return CollectionUtil.isNotEmpty(this.toolCalls);
    }

    /**
     * 对当前（最后一个）工具调用添加参数。
     *
     * @param argumentsChunk 工具调用参数片段
     */
    public void addToolCallArguments(String argumentsChunk) {
        if (hasToolCalls()) {
            this.toolCalls.get(this.toolCalls.size() - 1)
                    .addArguments(argumentsChunk);
        }
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }
}

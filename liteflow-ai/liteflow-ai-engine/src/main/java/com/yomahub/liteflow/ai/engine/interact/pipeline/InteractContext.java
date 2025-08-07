package com.yomahub.liteflow.ai.engine.interact.pipeline;

import com.yomahub.liteflow.ai.engine.model.output.TokenUsage;

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

    // TODO arg
    private Object grounding;

    // TODO arg
    private Object toolsCalling;

    private boolean thinkingInContent;

    public InteractContext() {
        chatId = "interact_" + UUID.randomUUID();
        aggregatedText = new StringBuilder();
        aggregatedThinking = new StringBuilder();
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
}

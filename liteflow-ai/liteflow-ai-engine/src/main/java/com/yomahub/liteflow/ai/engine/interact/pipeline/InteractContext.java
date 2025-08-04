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

    private TokenUsage usage;

    // TODO arg
    private Object grounding;

    // TODO arg
    private Object toolsCalling;

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
}

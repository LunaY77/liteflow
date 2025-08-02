package com.yomahub.liteflow.ai.engine.interact.pipeline;

import java.util.UUID;

/**
 * 大模型交互上下文，
 *
 * @author 苍镜月
 * @since TODO
 */

public class ChatContext {

    private final String chatId;

    private final StringBuilder aggregatedText;

    private final StringBuilder aggregatedThinking;

    // TODO arg
    private Object usage;

    // TODO arg
    private Object grounding;

    // TODO arg
    private Object toolsCalling;

    public ChatContext() {
        chatId = "chat_" + UUID.randomUUID();
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

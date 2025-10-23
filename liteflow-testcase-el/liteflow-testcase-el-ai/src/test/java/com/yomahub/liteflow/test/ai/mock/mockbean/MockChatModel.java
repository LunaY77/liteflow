package com.yomahub.liteflow.test.ai.mock.mockbean;

import com.yomahub.liteflow.ai.engine.model.chat.AbstractChatModel;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatConfig;

/**
 * 模拟的 ChatModel
 *
 * @author 苍镜月
 * @since 2.16.0
 */

public class MockChatModel extends AbstractChatModel {

    public MockChatModel(ChatConfig chatConfig) {
        super(chatConfig, new MockInteractClient());
    }
}

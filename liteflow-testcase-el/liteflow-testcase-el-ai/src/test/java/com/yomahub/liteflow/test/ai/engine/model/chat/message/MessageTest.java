/**
 * Message 消息接口测试类
 *
 * @author 苍镜月
 * @since 2.12.1
 */
package com.yomahub.liteflow.test.ai.engine.model.chat.message;

import com.yomahub.liteflow.ai.engine.model.chat.message.Message;
import com.yomahub.liteflow.ai.engine.model.chat.message.MessageType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageTest {

    /**
     * 测试Message接口的基本实现
     */
    @Test
    void testMessageInterface() {
        Message message = new TestMessageImpl(MessageType.USER, "Hello");

        assertEquals("Hello", message.getContent());
        assertEquals(MessageType.USER, message.getMessageType());
    }

    /**
     * 测试不同消息类型
     */
    @Test
    void testDifferentMessageTypes() {
        Message userMessage = new TestMessageImpl(MessageType.USER, "用户消息");
        Message assistantMessage = new TestMessageImpl(MessageType.ASSISTANT, "助手消息");
        Message systemMessage = new TestMessageImpl(MessageType.SYSTEM, "系统消息");
        Message toolMessage = new TestMessageImpl(MessageType.TOOL, "工具消息");

        assertEquals(MessageType.USER, userMessage.getMessageType());
        assertEquals(MessageType.ASSISTANT, assistantMessage.getMessageType());
        assertEquals(MessageType.SYSTEM, systemMessage.getMessageType());
        assertEquals(MessageType.TOOL, toolMessage.getMessageType());

        assertEquals("用户消息", userMessage.getContent());
        assertEquals("助手消息", assistantMessage.getContent());
        assertEquals("系统消息", systemMessage.getContent());
        assertEquals("工具消息", toolMessage.getContent());
    }

    /**
     * 测试消息类型和内容的组合
     */
    @Test
    void testMessageTypeAndContentCombination() {
        Message message = new TestMessageImpl(MessageType.ASSISTANT, "AI回复内容");

        assertNotNull(message.getMessageType());
        assertNotNull(message.getContent());
        assertEquals(MessageType.ASSISTANT, message.getMessageType());
        assertEquals("AI回复内容", message.getContent());
    }

    /**
     * 测试空内容消息
     */
    @Test
    void testEmptyContentMessage() {
        Message emptyMessage = new TestMessageImpl(MessageType.USER, "");
        Message nullMessage = new TestMessageImpl(MessageType.USER, null);

        assertEquals("", emptyMessage.getContent());
        assertNull(nullMessage.getContent());
        assertEquals(MessageType.USER, emptyMessage.getMessageType());
        assertEquals(MessageType.USER, nullMessage.getMessageType());
    }

    /**
     * 测试Message继承Content接口
     */
    @Test
    void testMessageExtendsContent() {
        Message message = new TestMessageImpl(MessageType.SYSTEM, "测试内容");

        // Message应该继承Content接口
        assertTrue(message instanceof com.yomahub.liteflow.ai.engine.model.chat.message.Content);

        // 可以作为Content使用
        com.yomahub.liteflow.ai.engine.model.chat.message.Content content = message;
        assertEquals("测试内容", content.getContent());
    }

    /**
     * 测试消息类型枚举完整性
     */
    @Test
    void testAllMessageTypes() {
        MessageType[] allTypes = MessageType.values();

        for (MessageType type : allTypes) {
            Message message = new TestMessageImpl(type, "测试内容 for " + type.name());
            assertEquals(type, message.getMessageType());
            assertNotNull(message.getContent());
        }
    }

    /**
     * 测试消息对象的不变性
     */
    @Test
    void testMessageImmutability() {
        MessageType originalType = MessageType.USER;
        String originalContent = "原始内容";

        Message message = new TestMessageImpl(originalType, originalContent);

        assertEquals(originalType, message.getMessageType());
        assertEquals(originalContent, message.getContent());

        // 验证多次调用返回相同结果
        assertEquals(message.getMessageType(), message.getMessageType());
        assertEquals(message.getContent(), message.getContent());
    }

    /**
     * 用于测试的Message接口实现类
     */
    private static class TestMessageImpl implements Message {
        private final MessageType messageType;
        private final String content;

        public TestMessageImpl(MessageType messageType, String content) {
            this.messageType = messageType;
            this.content = content;
        }

        @Override
        public MessageType getMessageType() {
            return messageType;
        }

        @Override
        public String getContent() {
            return content;
        }
    }
}

/**
 * AbstractMessage 抽象消息测试类
 *
 * @author 苍镜月
 * @since 2.12.1
 */
package com.yomahub.liteflow.test.ai.engine.model.chat.message;

import com.yomahub.liteflow.ai.engine.exception.LiteFlowAIEngineException;
import com.yomahub.liteflow.ai.engine.model.chat.message.AbstractMessage;
import com.yomahub.liteflow.ai.engine.model.chat.message.MessageType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AbstractMessageTest {

    /**
     * 测试正常构造
     */
    @Test
    void testValidConstruction() {
        AbstractMessage message = new TestAbstractMessage(MessageType.USER, "测试内容");

        assertEquals(MessageType.USER, message.getMessageType());
        assertEquals("测试内容", message.getContent());
    }

    /**
     * 测试null消息类型抛出异常
     */
    @Test
    void testNullMessageTypeThrowsException() {
        Exception exception = assertThrows(LiteFlowAIEngineException.class,
                () -> new TestAbstractMessage(null, "测试内容"));

        assertEquals("消息类型不能为 null", exception.getMessage());
    }

    /**
     * 测试null内容允许
     */
    @Test
    void testNullContentAllowed() {
        AbstractMessage message = new TestAbstractMessage(MessageType.ASSISTANT, null);

        assertEquals(MessageType.ASSISTANT, message.getMessageType());
        assertNull(message.getContent());
    }

    /**
     * 测试空字符串内容
     */
    @Test
    void testEmptyStringContent() {
        AbstractMessage message = new TestAbstractMessage(MessageType.SYSTEM, "");

        assertEquals(MessageType.SYSTEM, message.getMessageType());
        assertEquals("", message.getContent());
    }

    /**
     * 测试所有消息类型
     */
    @Test
    void testAllMessageTypes() {
        MessageType[] allTypes = MessageType.values();

        for (MessageType type : allTypes) {
            AbstractMessage message = new TestAbstractMessage(type, "内容 for " + type.name());
            assertEquals(type, message.getMessageType());
            assertEquals("内容 for " + type.name(), message.getContent());
        }
    }

    /**
     * 测试长文本内容
     */
    @Test
    void testLongTextContent() {
        StringBuilder longContent = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            longContent.append("这是一段很长的消息内容，用于测试AbstractMessage的处理能力。");
        }

        AbstractMessage message = new TestAbstractMessage(MessageType.USER, longContent.toString());

        assertEquals(MessageType.USER, message.getMessageType());
        assertEquals(longContent.toString(), message.getContent());
        assertTrue(message.getContent().length() > 10000);
    }

    /**
     * 测试特殊字符内容
     */
    @Test
    void testSpecialCharacterContent() {
        String specialContent = "特殊字符: !@#$%^&*()_+-=[]{}|;':\",./<>?\n\t\r";
        AbstractMessage message = new TestAbstractMessage(MessageType.TOOL, specialContent);

        assertEquals(MessageType.TOOL, message.getMessageType());
        assertEquals(specialContent, message.getContent());
    }

    /**
     * 测试Unicode内容
     */
    @Test
    void testUnicodeContent() {
        String unicodeContent = "Unicode测试: 你好世界 🌍 مرحبا بالعالم Здравствуй мир";
        AbstractMessage message = new TestAbstractMessage(MessageType.ASSISTANT, unicodeContent);

        assertEquals(MessageType.ASSISTANT, message.getMessageType());
        assertEquals(unicodeContent, message.getContent());
    }

    /**
     * 测试不可变性
     */
    @Test
    void testImmutability() {
        AbstractMessage message = new TestAbstractMessage(MessageType.USER, "原始内容");

        MessageType originalType = message.getMessageType();
        String originalContent = message.getContent();

        // 多次调用应返回相同值
        assertEquals(originalType, message.getMessageType());
        assertEquals(originalContent, message.getContent());

        // 验证字段的final特性（通过反射或其他方式，这里通过行为验证）
        assertEquals(MessageType.USER, message.getMessageType());
        assertEquals("原始内容", message.getContent());
    }

    /**
     * 测试toString方法（如果覆盖了）
     */
    @Test
    void testToString() {
        AbstractMessage message = new TestAbstractMessage(MessageType.SYSTEM, "系统消息");
        String toString = message.toString();

        assertNotNull(toString);
        // 基本验证toString不为空
        assertFalse(toString.trim().isEmpty());
    }

    /**
     * 测试继承关系
     */
    @Test
    void testInheritance() {
        AbstractMessage message = new TestAbstractMessage(MessageType.USER, "测试");

        // 验证继承关系
        assertTrue(message instanceof com.yomahub.liteflow.ai.engine.model.chat.message.Message);
        assertTrue(message instanceof com.yomahub.liteflow.ai.engine.model.chat.message.Content);
    }

    /**
     * 测试多个实例的独立性
     */
    @Test
    void testMultipleInstancesIndependence() {
        AbstractMessage message1 = new TestAbstractMessage(MessageType.USER, "消息1");
        AbstractMessage message2 = new TestAbstractMessage(MessageType.ASSISTANT, "消息2");

        assertNotEquals(message1.getMessageType(), message2.getMessageType());
        assertNotEquals(message1.getContent(), message2.getContent());

        assertEquals(MessageType.USER, message1.getMessageType());
        assertEquals(MessageType.ASSISTANT, message2.getMessageType());
        assertEquals("消息1", message1.getContent());
        assertEquals("消息2", message2.getContent());
    }

    /**
     * 用于测试的AbstractMessage具体实现类
     */
    private static class TestAbstractMessage extends AbstractMessage {

        public TestAbstractMessage(MessageType messageType, String content) {
            super(messageType, content);
        }
    }
}

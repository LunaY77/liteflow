/**
 * MessageType消息类型枚举测试类
 *
 * @author 苍镜月
 * @since TODO
 */
package com.yomahub.liteflow.test.ai.engine.model.chat.message;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.yomahub.liteflow.ai.engine.model.chat.message.MessageType;

class MessageTypeTest {

    /**
     * 测试枚举值的基本属性
     */
    @Test
    void testEnumValues() {
        // 测试USER类型
        assertEquals(1, MessageType.USER.getCode());
        assertEquals("user", MessageType.USER.getRole());

        // 测试ASSISTANT类型
        assertEquals(2, MessageType.ASSISTANT.getCode());
        assertEquals("assistant", MessageType.ASSISTANT.getRole());

        // 测试SYSTEM类型
        assertEquals(3, MessageType.SYSTEM.getCode());
        assertEquals("system", MessageType.SYSTEM.getRole());

        // 测试TOOL类型
        assertEquals(4, MessageType.TOOL.getCode());
        assertEquals("tool", MessageType.TOOL.getRole());
    }

    /**
     * 测试枚举数量
     */
    @Test
    void testEnumCount() {
        MessageType[] values = MessageType.values();
        assertEquals(4, values.length);
    }

    /**
     * 测试枚举顺序
     */
    @Test
    void testEnumOrder() {
        MessageType[] values = MessageType.values();
        assertEquals(MessageType.USER, values[0]);
        assertEquals(MessageType.ASSISTANT, values[1]);
        assertEquals(MessageType.SYSTEM, values[2]);
        assertEquals(MessageType.TOOL, values[3]);
    }

    /**
     * 测试of方法 - 有效code
     */
    @Test
    void testOfMethodWithValidCode() {
        assertEquals(MessageType.USER, MessageType.of(1));
        assertEquals(MessageType.ASSISTANT, MessageType.of(2));
        assertEquals(MessageType.SYSTEM, MessageType.of(3));
        assertEquals(MessageType.TOOL, MessageType.of(4));
    }

    /**
     * 测试of方法 - 无效code
     */
    @Test
    void testOfMethodWithInvalidCode() {
        assertNull(MessageType.of(0));
        assertNull(MessageType.of(5));
        assertNull(MessageType.of(-1));
        assertNull(MessageType.of(999));
    }

    /**
     * 测试of方法 - null值
     */
    @Test
    void testOfMethodWithNull() {
        assertNull(MessageType.of(null));
    }

    /**
     * 测试valueOf方法
     */
    @Test
    void testValueOf() {
        assertEquals(MessageType.USER, MessageType.valueOf("USER"));
        assertEquals(MessageType.ASSISTANT, MessageType.valueOf("ASSISTANT"));
        assertEquals(MessageType.SYSTEM, MessageType.valueOf("SYSTEM"));
        assertEquals(MessageType.TOOL, MessageType.valueOf("TOOL"));
    }

    /**
     * 测试valueOf方法异常情况
     */
    @Test
    void testValueOfWithInvalidName() {
        assertThrows(IllegalArgumentException.class, () -> {
            MessageType.valueOf("INVALID");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            MessageType.valueOf("user");
        });
    }

    /**
     * 测试valueOf方法null参数
     */
    @Test
    void testValueOfWithNull() {
        assertThrows(NullPointerException.class, () -> {
            MessageType.valueOf(null);
        });
    }

    /**
     * 测试toString方法
     */
    @Test
    void testToString() {
        assertEquals("USER", MessageType.USER.toString());
        assertEquals("ASSISTANT", MessageType.ASSISTANT.toString());
        assertEquals("SYSTEM", MessageType.SYSTEM.toString());
        assertEquals("TOOL", MessageType.TOOL.toString());
    }

    /**
     * 测试name方法
     */
    @Test
    void testName() {
        assertEquals("USER", MessageType.USER.name());
        assertEquals("ASSISTANT", MessageType.ASSISTANT.name());
        assertEquals("SYSTEM", MessageType.SYSTEM.name());
        assertEquals("TOOL", MessageType.TOOL.name());
    }

    /**
     * 测试ordinal方法
     */
    @Test
    void testOrdinal() {
        assertEquals(0, MessageType.USER.ordinal());
        assertEquals(1, MessageType.ASSISTANT.ordinal());
        assertEquals(2, MessageType.SYSTEM.ordinal());
        assertEquals(3, MessageType.TOOL.ordinal());
    }

    /**
     * 测试枚举比较
     */
    @Test
    void testEnumComparison() {
        // 测试equals
        assertEquals(MessageType.USER, MessageType.USER);
        assertNotEquals(MessageType.USER, MessageType.ASSISTANT);

        // 测试compareTo
        assertTrue(MessageType.USER.compareTo(MessageType.ASSISTANT) < 0);
        assertTrue(MessageType.ASSISTANT.compareTo(MessageType.USER) > 0);
        assertEquals(0, MessageType.USER.compareTo(MessageType.USER));
    }

    /**
     * 测试不同MessageType的唯一性
     */
    @Test
    void testUniqueness() {
        MessageType[] values = MessageType.values();

        // 测试code唯一性
        for (int i = 0; i < values.length; i++) {
            for (int j = i + 1; j < values.length; j++) {
                assertNotEquals(values[i].getCode(), values[j].getCode());
            }
        }

        // 测试role唯一性
        for (int i = 0; i < values.length; i++) {
            for (int j = i + 1; j < values.length; j++) {
                assertNotEquals(values[i].getRole(), values[j].getRole());
            }
        }
    }

    /**
     * 测试cache机制
     */
    @Test
    void testCacheMechanism() {
        // 多次调用should返回相同的实例
        MessageType type1 = MessageType.of(1);
        MessageType type2 = MessageType.of(1);
        assertSame(type1, type2);
        assertEquals(MessageType.USER, type1);
    }

    /**
     * 测试所有枚举值的完整性
     */
    @Test
    void testAllEnumValuesCompleteness() {
        // 确保所有定义的枚举值都能通过of方法找到
        for (MessageType type : MessageType.values()) {
            assertEquals(type, MessageType.of(type.getCode()));
        }
    }

    /**
     * 测试JsonValue注解功能
     */
    @Test
    void testJsonValueAnnotation() {
        // getRole方法应该返回JSON序列化时使用的值
        assertEquals("user", MessageType.USER.getRole());
        assertEquals("assistant", MessageType.ASSISTANT.getRole());
        assertEquals("system", MessageType.SYSTEM.getRole());
        assertEquals("tool", MessageType.TOOL.getRole());
    }
}

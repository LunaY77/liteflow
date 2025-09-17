/**
 * Content 内容接口测试类
 *
 * @author 苍镜月
 * @since 2.12.1
 */
package com.yomahub.liteflow.test.ai.engine.model.chat.message;

import com.yomahub.liteflow.ai.engine.model.chat.message.Content;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContentTest {

    /**
     * 测试Content接口的基本实现
     */
    @Test
    void testContentInterface() {
        Content content = new TestContentImpl("Hello World");
        assertEquals("Hello World", content.getContent());
    }

    /**
     * 测试null内容
     */
    @Test
    void testNullContent() {
        Content content = new TestContentImpl(null);
        assertNull(content.getContent());
    }

    /**
     * 测试空字符串内容
     */
    @Test
    void testEmptyContent() {
        Content content = new TestContentImpl("");
        assertEquals("", content.getContent());
    }

    /**
     * 测试多行内容
     */
    @Test
    void testMultilineContent() {
        String multilineText = "第一行\n第二行\n第三行";
        Content content = new TestContentImpl(multilineText);
        assertEquals(multilineText, content.getContent());
    }

    /**
     * 测试特殊字符内容
     */
    @Test
    void testSpecialCharacterContent() {
        String specialText = "特殊字符: !@#$%^&*()_+-=[]{}|;':\",./<>?";
        Content content = new TestContentImpl(specialText);
        assertEquals(specialText, content.getContent());
    }

    /**
     * 测试Unicode内容
     */
    @Test
    void testUnicodeContent() {
        String unicodeText = "Unicode: 你好世界 🌍 مرحبا بالعالم";
        Content content = new TestContentImpl(unicodeText);
        assertEquals(unicodeText, content.getContent());
    }

    /**
     * 测试长文本内容
     */
    @Test
    void testLongContent() {
        StringBuilder longText = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            longText.append("这是一段很长的文本内容，用于测试Content接口的处理能力。");
        }

        Content content = new TestContentImpl(longText.toString());
        assertEquals(longText.toString(), content.getContent());
        assertTrue(content.getContent().length() > 10000);
    }

    /**
     * 用于测试的Content接口实现类
     */
    private static class TestContentImpl implements Content {
        private final String content;

        public TestContentImpl(String content) {
            this.content = content;
        }

        @Override
        public String getContent() {
            return content;
        }
    }
}

/**
 * ResponseType AI响应类型枚举测试类
 *
 * @author 苍镜月
 * @since 2.12.1
 */
package com.yomahub.liteflow.test.ai.engine.model.output;

import com.yomahub.liteflow.ai.engine.model.output.ResponseType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResponseTypeTest {

    /**
     * 测试枚举值的存在
     */
    @Test
    void testEnumValues() {
        ResponseType[] values = ResponseType.values();
        
        assertEquals(2, values.length);
        assertTrue(containsValue(values, ResponseType.TEXT));
        assertTrue(containsValue(values, ResponseType.JSON));
    }

    /**
     * 测试枚举值的valueOf方法
     */
    @Test
    void testValueOf() {
        assertEquals(ResponseType.TEXT, ResponseType.valueOf("TEXT"));
        assertEquals(ResponseType.JSON, ResponseType.valueOf("JSON"));
    }

    /**
     * 测试valueOf异常情况
     */
    @Test
    void testValueOfInvalidValue() {
        assertThrows(IllegalArgumentException.class, () -> 
            ResponseType.valueOf("INVALID"));
        
        assertThrows(IllegalArgumentException.class, () -> 
            ResponseType.valueOf("xml"));
        
        assertThrows(NullPointerException.class, () -> 
            ResponseType.valueOf(null));
    }

    /**
     * 测试枚举的name方法
     */
    @Test
    void testEnumName() {
        assertEquals("TEXT", ResponseType.TEXT.name());
        assertEquals("JSON", ResponseType.JSON.name());
    }

    /**
     * 测试枚举的toString方法
     */
    @Test
    void testToString() {
        assertEquals("TEXT", ResponseType.TEXT.toString());
        assertEquals("JSON", ResponseType.JSON.toString());
    }

    /**
     * 测试枚举的ordinal方法
     */
    @Test
    void testOrdinal() {
        assertEquals(0, ResponseType.TEXT.ordinal());
        assertEquals(1, ResponseType.JSON.ordinal());
    }

    /**
     * 测试枚举的equals方法
     */
    @Test
    void testEquals() {
        assertEquals(ResponseType.TEXT, ResponseType.TEXT);
        assertEquals(ResponseType.JSON, ResponseType.JSON);
        assertNotEquals(ResponseType.TEXT, ResponseType.JSON);
        assertNotEquals(ResponseType.JSON, ResponseType.TEXT);
    }

    /**
     * 测试枚举的hashCode方法
     */
    @Test
    void testHashCode() {
        assertEquals(ResponseType.TEXT.hashCode(), ResponseType.TEXT.hashCode());
        assertEquals(ResponseType.JSON.hashCode(), ResponseType.JSON.hashCode());
        assertNotEquals(ResponseType.TEXT.hashCode(), ResponseType.JSON.hashCode());
    }

    /**
     * 测试在switch语句中的使用
     */
    @Test
    void testSwitchStatement() {
        String textResult = processResponseType(ResponseType.TEXT);
        String jsonResult = processResponseType(ResponseType.JSON);
        
        assertEquals("处理文本响应", textResult);
        assertEquals("处理JSON响应", jsonResult);
    }

    /**
     * 测试枚举的语义含义
     */
    @Test
    void testSemanticMeaning() {
        // 验证TEXT类型用于纯文本响应
        ResponseType textType = ResponseType.TEXT;
        assertNotNull(textType);
        assertEquals("TEXT", textType.name());
        
        // 验证JSON类型用于JSON格式响应
        ResponseType jsonType = ResponseType.JSON;
        assertNotNull(jsonType);
        assertEquals("JSON", jsonType.name());
    }

    /**
     * 测试枚举的不可变性
     */
    @Test
    void testImmutability() {
        ResponseType original = ResponseType.TEXT;
        ResponseType same = ResponseType.TEXT;
        
        assertSame(original, same);
        assertEquals(original, same);
    }

    /**
     * 测试枚举在集合中的使用
     */
    @Test
    void testInCollections() {
        java.util.Set<ResponseType> types = java.util.EnumSet.allOf(ResponseType.class);
        
        assertEquals(2, types.size());
        assertTrue(types.contains(ResponseType.TEXT));
        assertTrue(types.contains(ResponseType.JSON));
    }

    /**
     * 辅助方法：检查数组是否包含指定值
     */
    private boolean containsValue(ResponseType[] values, ResponseType target) {
        for (ResponseType value : values) {
            if (value == target) {
                return true;
            }
        }
        return false;
    }

    /**
     * 辅助方法：模拟switch语句处理
     */
    private String processResponseType(ResponseType type) {
        switch (type) {
            case TEXT:
                return "处理文本响应";
            case JSON:
                return "处理JSON响应";
            default:
                return "未知类型";
        }
    }
}

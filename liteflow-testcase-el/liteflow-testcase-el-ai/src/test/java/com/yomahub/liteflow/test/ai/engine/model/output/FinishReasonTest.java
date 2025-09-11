/**
 * FinishReason模型生成停止原因枚举测试类
 *
 * @author 苍镜月
 * @since TODO
 */
package com.yomahub.liteflow.test.ai.engine.model.output;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.yomahub.liteflow.ai.engine.model.output.FinishReason;

class FinishReasonTest {

    /**
     * 测试枚举值的存在性
     */
    @Test
    void testEnumValues() {
        // 验证所有预期的枚举值都存在
        assertNotNull(FinishReason.STOP);
        assertNotNull(FinishReason.LENGTH);
        assertNotNull(FinishReason.TOOL_CALL);
        assertNotNull(FinishReason.OTHER);
    }

    /**
     * 测试枚举数量
     */
    @Test
    void testEnumCount() {
        FinishReason[] values = FinishReason.values();
        assertEquals(4, values.length);
    }

    /**
     * 测试枚举顺序
     */
    @Test
    void testEnumOrder() {
        FinishReason[] values = FinishReason.values();
        assertEquals(FinishReason.STOP, values[0]);
        assertEquals(FinishReason.LENGTH, values[1]);
        assertEquals(FinishReason.TOOL_CALL, values[2]);
        assertEquals(FinishReason.OTHER, values[3]);
    }

    /**
     * 测试valueOf方法
     */
    @Test
    void testValueOf() {
        assertEquals(FinishReason.STOP, FinishReason.valueOf("STOP"));
        assertEquals(FinishReason.LENGTH, FinishReason.valueOf("LENGTH"));
        assertEquals(FinishReason.TOOL_CALL, FinishReason.valueOf("TOOL_CALL"));
        assertEquals(FinishReason.OTHER, FinishReason.valueOf("OTHER"));
    }

    /**
     * 测试valueOf方法异常情况
     */
    @Test
    void testValueOfWithInvalidName() {
        assertThrows(IllegalArgumentException.class, () -> {
            FinishReason.valueOf("INVALID");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            FinishReason.valueOf("stop");
        });
    }

    /**
     * 测试valueOf方法null参数
     */
    @Test
    void testValueOfWithNull() {
        assertThrows(NullPointerException.class, () -> {
            FinishReason.valueOf(null);
        });
    }

    /**
     * 测试toString方法
     */
    @Test
    void testToString() {
        assertEquals("STOP", FinishReason.STOP.toString());
        assertEquals("LENGTH", FinishReason.LENGTH.toString());
        assertEquals("TOOL_CALL", FinishReason.TOOL_CALL.toString());
        assertEquals("OTHER", FinishReason.OTHER.toString());
    }

    /**
     * 测试name方法
     */
    @Test
    void testName() {
        assertEquals("STOP", FinishReason.STOP.name());
        assertEquals("LENGTH", FinishReason.LENGTH.name());
        assertEquals("TOOL_CALL", FinishReason.TOOL_CALL.name());
        assertEquals("OTHER", FinishReason.OTHER.name());
    }

    /**
     * 测试ordinal方法
     */
    @Test
    void testOrdinal() {
        assertEquals(0, FinishReason.STOP.ordinal());
        assertEquals(1, FinishReason.LENGTH.ordinal());
        assertEquals(2, FinishReason.TOOL_CALL.ordinal());
        assertEquals(3, FinishReason.OTHER.ordinal());
    }

    /**
     * 测试枚举比较
     */
    @Test
    void testEnumComparison() {
        // 测试equals
        assertSame(FinishReason.STOP, FinishReason.STOP);
        assertNotSame(FinishReason.STOP, FinishReason.LENGTH);

        // 测试compareTo
        assertTrue(FinishReason.STOP.compareTo(FinishReason.LENGTH) < 0);
        assertTrue(FinishReason.LENGTH.compareTo(FinishReason.STOP) > 0);
        assertEquals(0, FinishReason.STOP.compareTo(FinishReason.STOP));
    }

    /**
     * 测试枚举用于switch语句
     */
    @Test
    void testSwitchStatement() {
        String result = getFinishReasonDescription(FinishReason.STOP);
        assertEquals("正常完成", result);

        result = getFinishReasonDescription(FinishReason.LENGTH);
        assertEquals("达到长度限制", result);

        result = getFinishReasonDescription(FinishReason.TOOL_CALL);
        assertEquals("需要工具调用", result);

        result = getFinishReasonDescription(FinishReason.OTHER);
        assertEquals("其他原因", result);
    }

    /**
     * 辅助方法：用于测试switch语句
     */
    private String getFinishReasonDescription(FinishReason reason) {
        switch (reason) {
            case STOP:
                return "正常完成";
            case LENGTH:
                return "达到长度限制";
            case TOOL_CALL:
                return "需要工具调用";
            case OTHER:
                return "其他原因";
            default:
                return "未知原因";
        }
    }

    /**
     * 测试枚举在集合中的使用
     */
    @Test
    void testEnumInCollections() {
        java.util.Set<FinishReason> reasonSet = java.util.EnumSet.allOf(FinishReason.class);
        assertEquals(4, reasonSet.size());
        assertTrue(reasonSet.contains(FinishReason.STOP));
        assertTrue(reasonSet.contains(FinishReason.LENGTH));
        assertTrue(reasonSet.contains(FinishReason.TOOL_CALL));
        assertTrue(reasonSet.contains(FinishReason.OTHER));
    }

    /**
     * 测试枚举的语义含义
     */
    @Test
    void testSemanticMeaning() {
        // STOP应该表示正常结束，通常是最期望的结果
        assertNotNull(FinishReason.STOP);

        // LENGTH表示因长度限制而结束，这是技术限制
        assertNotNull(FinishReason.LENGTH);

        // TOOL_CALL表示需要调用工具，这是交互式的情况
        assertNotNull(FinishReason.TOOL_CALL);

        // OTHER作为兜底选项，处理其他未预期的情况
        assertNotNull(FinishReason.OTHER);
    }

    /**
     * 测试枚举的不可变性
     */
    @Test
    void testImmutability() {
        FinishReason reason = FinishReason.STOP;
        FinishReason sameReason = FinishReason.STOP;

        // 枚举实例应该是同一个对象
        assertSame(reason, sameReason);

        // 验证枚举实例是单例的
        assertEquals(reason.hashCode(), sameReason.hashCode());
    }
}

/**
 * TokenUsage Token使用统计测试类
 *
 * @author 苍镜月
 * @since 2.12.1
 */
package com.yomahub.liteflow.test.ai.engine.model.output;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.yomahub.liteflow.ai.engine.model.output.TokenUsage;

class TokenUsageTest {

    private Integer inputTokens;
    private Integer outputTokens;
    private Integer totalTokens;

    @BeforeEach
    void setUp() {
        inputTokens = 100;
        outputTokens = 50;
        totalTokens = 150;
    }

    /**
     * 测试默认构造函数
     */
    @Test
    void testDefaultConstructor() {
        TokenUsage usage = new TokenUsage();

        assertNull(usage.getInputTokenCount());
        assertNull(usage.getOutputTokenCount());
        assertNull(usage.getTotalTokenCount());
    }

    /**
     * 测试单参数构造函数
     */
    @Test
    void testSingleParameterConstructor() {
        TokenUsage usage = new TokenUsage(inputTokens);

        assertEquals(inputTokens, usage.getInputTokenCount());
        assertNull(usage.getOutputTokenCount());
        assertEquals(inputTokens, usage.getTotalTokenCount()); // 自动计算
    }

    /**
     * 测试双参数构造函数
     */
    @Test
    void testTwoParameterConstructor() {
        TokenUsage usage = new TokenUsage(inputTokens, outputTokens);

        assertEquals(inputTokens, usage.getInputTokenCount());
        assertEquals(outputTokens, usage.getOutputTokenCount());
        assertEquals(150, usage.getTotalTokenCount()); // 自动计算：100 + 50
    }

    /**
     * 测试三参数构造函数
     */
    @Test
    void testThreeParameterConstructor() {
        TokenUsage usage = new TokenUsage(inputTokens, outputTokens, totalTokens);

        assertEquals(inputTokens, usage.getInputTokenCount());
        assertEquals(outputTokens, usage.getOutputTokenCount());
        assertEquals(totalTokens, usage.getTotalTokenCount());
    }

    /**
     * 测试null值处理 - 构造函数
     */
    @Test
    void testConstructorWithNullValues() {
        // 测试所有参数为null
        TokenUsage usage1 = new TokenUsage(null, null, null);
        assertNull(usage1.getInputTokenCount());
        assertNull(usage1.getOutputTokenCount());
        assertNull(usage1.getTotalTokenCount());

        // 测试部分参数为null
        TokenUsage usage2 = new TokenUsage(inputTokens, null);
        assertEquals(inputTokens, usage2.getInputTokenCount());
        assertNull(usage2.getOutputTokenCount());
        assertEquals(inputTokens, usage2.getTotalTokenCount());

        TokenUsage usage3 = new TokenUsage(null, outputTokens);
        assertNull(usage3.getInputTokenCount());
        assertEquals(outputTokens, usage3.getOutputTokenCount());
        assertEquals(outputTokens, usage3.getTotalTokenCount());
    }

    /**
     * 测试静态sum方法 - 正常情况
     */
    @Test
    void testStaticSumMethod() {
        TokenUsage usage1 = new TokenUsage(50, 25, 75);
        TokenUsage usage2 = new TokenUsage(30, 20, 50);

        TokenUsage result = TokenUsage.sum(usage1, usage2);

        assertEquals(80, result.getInputTokenCount());
        assertEquals(45, result.getOutputTokenCount());
        assertEquals(125, result.getTotalTokenCount());
    }

    /**
     * 测试静态sum方法 - null处理
     */
    @Test
    void testStaticSumMethodWithNull() {
        TokenUsage usage = new TokenUsage(inputTokens, outputTokens, totalTokens);

        // 第一个参数为null
        TokenUsage result1 = TokenUsage.sum(null, usage);
        assertSame(usage, result1);

        // 第二个参数为null
        TokenUsage result2 = TokenUsage.sum(usage, null);
        assertSame(usage, result2);

        // 两个参数都为null
        TokenUsage result3 = TokenUsage.sum(null, null);
        assertNull(result3);
    }

    /**
     * 测试实例add方法
     */
    @Test
    void testInstanceAddMethod() {
        TokenUsage usage1 = new TokenUsage(40, 20, 60);
        TokenUsage usage2 = new TokenUsage(60, 30, 90);

        TokenUsage result = usage1.add(usage2);

        assertEquals(100, result.getInputTokenCount());
        assertEquals(50, result.getOutputTokenCount());
        assertEquals(150, result.getTotalTokenCount());

        // 验证原对象没有被修改
        assertEquals(40, usage1.getInputTokenCount());
        assertEquals(20, usage1.getOutputTokenCount());
        assertEquals(60, usage1.getTotalTokenCount());
    }

    /**
     * 测试实例add方法 - null处理
     */
    @Test
    void testInstanceAddMethodWithNull() {
        TokenUsage usage = new TokenUsage(inputTokens, outputTokens, totalTokens);

        TokenUsage result = usage.add(null);
        assertSame(usage, result);
    }

    /**
     * 测试实例add方法 - 包含null值的TokenUsage
     */
    @Test
    void testInstanceAddMethodWithNullValues() {
        TokenUsage usage1 = new TokenUsage(50, null, 80);
        TokenUsage usage2 = new TokenUsage(null, 30, 60);

        TokenUsage result = usage1.add(usage2);

        assertEquals(50, result.getInputTokenCount());
        assertEquals(30, result.getOutputTokenCount());
        assertEquals(140, result.getTotalTokenCount());
    }

    /**
     * 测试静态sum方法（整数）
     */
    @Test
    void testStaticIntegerSumMethod() {
        // 使用反射访问protected静态方法，或者通过其他公开接口测试
        // 这里通过构造函数的行为来间接测试
        TokenUsage usage = new TokenUsage(50, 30);
        assertEquals(80, usage.getTotalTokenCount());
    }

    /**
     * 测试toString方法
     */
    @Test
    void testToString() {
        TokenUsage usage = new TokenUsage(inputTokens, outputTokens, totalTokens);
        String result = usage.toString();

        assertTrue(result.contains("TokenUsage"));
        assertTrue(result.contains("inputTokenCount=" + inputTokens));
        assertTrue(result.contains("outputTokenCount=" + outputTokens));
        assertTrue(result.contains("totalTokenCount=" + totalTokens));
    }

    /**
     * 测试toString方法 - null值
     */
    @Test
    void testToStringWithNullValues() {
        TokenUsage usage = new TokenUsage();
        String result = usage.toString();

        assertTrue(result.contains("TokenUsage"));
        assertTrue(result.contains("inputTokenCount=null"));
        assertTrue(result.contains("outputTokenCount=null"));
        assertTrue(result.contains("totalTokenCount=null"));
    }

    /**
     * 测试零值处理
     */
    @Test
    void testZeroValues() {
        TokenUsage usage = new TokenUsage(0, 0, 0);

        assertEquals(0, usage.getInputTokenCount());
        assertEquals(0, usage.getOutputTokenCount());
        assertEquals(0, usage.getTotalTokenCount());

        // 测试与其他值相加
        TokenUsage other = new TokenUsage(10, 5, 15);
        TokenUsage result = usage.add(other);

        assertEquals(10, result.getInputTokenCount());
        assertEquals(5, result.getOutputTokenCount());
        assertEquals(15, result.getTotalTokenCount());
    }

    /**
     * 测试负值处理
     */
    @Test
    void testNegativeValues() {
        // 虽然在实际使用中token数量不应该为负，但类应该能够处理
        TokenUsage usage = new TokenUsage(-10, -5, -15);

        assertEquals(-10, usage.getInputTokenCount());
        assertEquals(-5, usage.getOutputTokenCount());
        assertEquals(-15, usage.getTotalTokenCount());
    }

    /**
     * 测试大数值处理
     */
    @Test
    void testLargeValues() {
        Integer largeInput = Integer.MAX_VALUE - 1000;
        Integer largeOutput = 1000;

        TokenUsage usage = new TokenUsage(largeInput, largeOutput);

        assertEquals(largeInput, usage.getInputTokenCount());
        assertEquals(largeOutput, usage.getOutputTokenCount());
        assertEquals(Integer.MAX_VALUE, usage.getTotalTokenCount());
    }

    /**
     * 测试链式操作
     */
    @Test
    void testChainedOperations() {
        TokenUsage usage1 = new TokenUsage(10, 5, 15);
        TokenUsage usage2 = new TokenUsage(20, 10, 30);
        TokenUsage usage3 = new TokenUsage(30, 15, 45);

        TokenUsage result = TokenUsage.sum(TokenUsage.sum(usage1, usage2), usage3);

        assertEquals(60, result.getInputTokenCount());
        assertEquals(30, result.getOutputTokenCount());
        assertEquals(90, result.getTotalTokenCount());
    }

    /**
     * 测试不可变性
     */
    @Test
    void testImmutability() {
        TokenUsage original = new TokenUsage(inputTokens, outputTokens, totalTokens);
        TokenUsage other = new TokenUsage(50, 25, 75);

        TokenUsage result = original.add(other);

        // 验证原始对象没有被修改
        assertEquals(inputTokens, original.getInputTokenCount());
        assertEquals(outputTokens, original.getOutputTokenCount());
        assertEquals(totalTokens, original.getTotalTokenCount());

        // 验证结果是新对象
        assertNotSame(original, result);
    }
}

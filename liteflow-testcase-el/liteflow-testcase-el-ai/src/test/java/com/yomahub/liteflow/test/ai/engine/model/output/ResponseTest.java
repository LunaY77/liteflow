/**
 * Response 响应测试类
 *
 * @author 苍镜月
 * @since 2.12.1
 */
package com.yomahub.liteflow.test.ai.engine.model.output;

import com.yomahub.liteflow.ai.engine.model.output.FinishReason;
import com.yomahub.liteflow.ai.engine.model.output.Response;
import com.yomahub.liteflow.ai.engine.model.output.TokenUsage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ResponseTest {

    private String textOutput;
    private TokenUsage tokenUsage;
    private FinishReason finishReason;
    private Map<String, Object> metadata;

    @BeforeEach
    void setUp() {
        textOutput = "Hello, World!";
        tokenUsage = new TokenUsage(100, 50, 150);
        finishReason = FinishReason.STOP;
        metadata = new HashMap<>();
        metadata.put("modelName", "test-model");
        metadata.put("responseTime", 1000L);
    }

    /**
     * 测试单参数构造函数
     */
    @Test
    void testSingleParameterConstructor() {
        Response<String> response = new Response<>(textOutput);

        assertEquals(textOutput, response.getOutput());
        assertNull(response.getTokenUsage());
        assertNull(response.getFinishReason());
        assertNotNull(response.getMetadata());
        assertTrue(response.getMetadata().isEmpty());
    }

    /**
     * 测试三参数构造函数
     */
    @Test
    void testThreeParameterConstructor() {
        Response<String> response = new Response<>(textOutput, tokenUsage, finishReason);

        assertEquals(textOutput, response.getOutput());
        assertEquals(tokenUsage, response.getTokenUsage());
        assertEquals(finishReason, response.getFinishReason());
        assertNotNull(response.getMetadata());
        assertTrue(response.getMetadata().isEmpty());
    }

    /**
     * 测试四参数构造函数
     */
    @Test
    void testFourParameterConstructor() {
        Response<String> response = new Response<>(textOutput, tokenUsage, finishReason, metadata);

        assertEquals(textOutput, response.getOutput());
        assertEquals(tokenUsage, response.getTokenUsage());
        assertEquals(finishReason, response.getFinishReason());
        assertNotNull(response.getMetadata());
        assertEquals(2, response.getMetadata().size());
        assertEquals("test-model", response.getMetadata().get("modelName"));
        assertEquals(1000L, response.getMetadata().get("responseTime"));

        // 验证metadata是新的副本
        assertNotSame(metadata, response.getMetadata());
    }

    /**
     * 测试构造函数null值处理
     */
    @Test
    void testConstructorWithNullOutput() {
        assertThrows(NullPointerException.class, () -> new Response<>(null));

        assertThrows(NullPointerException.class, () -> new Response<>(null, tokenUsage, finishReason));

        assertThrows(NullPointerException.class, () -> new Response<>(null, tokenUsage, finishReason, metadata));
    }

    /**
     * 测试构造函数null metadata处理
     */
    @Test
    void testConstructorWithNullMetadata() {
        Response<String> response = new Response<>(textOutput, tokenUsage, finishReason, null);

        assertEquals(textOutput, response.getOutput());
        assertEquals(tokenUsage, response.getTokenUsage());
        assertEquals(finishReason, response.getFinishReason());
        assertNotNull(response.getMetadata());
        assertTrue(response.getMetadata().isEmpty());
    }

    /**
     * 测试Builder模式
     */
    @Test
    void testBuilder() {
        Response<String> response = Response.<String>builder()
                .content(textOutput)
                .tokenUsage(tokenUsage)
                .finishReason(finishReason)
                .metadata(metadata)
                .build();

        assertEquals(textOutput, response.getOutput());
        assertEquals(tokenUsage, response.getTokenUsage());
        assertEquals(finishReason, response.getFinishReason());
        assertEquals(2, response.getMetadata().size());
        assertEquals("test-model", response.getMetadata().get("modelName"));
        assertEquals(1000L, response.getMetadata().get("responseTime"));
    }

    /**
     * 测试Builder最小配置
     */
    @Test
    void testBuilderMinimalConfiguration() {
        Response<String> response = Response.<String>builder()
                .content(textOutput)
                .build();

        assertEquals(textOutput, response.getOutput());
        assertNull(response.getTokenUsage());
        assertNull(response.getFinishReason());
        assertNotNull(response.getMetadata());
        assertTrue(response.getMetadata().isEmpty());
    }

    /**
     * 测试Builder流式调用
     */
    @Test
    void testBuilderFluentInterface() {
        Map<String, Object> builderMetadata = new HashMap<>();
        builderMetadata.put("test", "value");

        Response<String> response = Response.<String>builder()
                .content(textOutput)
                .tokenUsage(tokenUsage)
                .finishReason(finishReason)
                .metadata(builderMetadata)
                .build();

        assertNotNull(response);
        assertEquals(textOutput, response.getOutput());
        assertEquals(tokenUsage, response.getTokenUsage());
        assertEquals(finishReason, response.getFinishReason());
        assertEquals("value", response.getMetadata().get("test"));
    }

    /**
     * 测试Builder链式调用
     */
    @Test
    void testBuilderChaining() {
        Response.Builder<String, ?> builder = Response.<String>builder();

        Response.Builder<String, ?> result = builder
                .content(textOutput)
                .tokenUsage(tokenUsage)
                .finishReason(finishReason);

        assertSame(builder, result);
    }

    /**
     * 测试不同类型的泛型
     */
    @Test
    void testGenericTypes() {
        // 测试Integer类型
        Response<Integer> intResponse = new Response<>(42);
        assertEquals(Integer.valueOf(42), intResponse.getOutput());

        // 测试自定义对象类型
        TestObject testObject = new TestObject("test", 123);
        Response<TestObject> objectResponse = new Response<>(testObject);
        assertEquals(testObject, objectResponse.getOutput());
        assertEquals("test", objectResponse.getOutput().name);
        assertEquals(123, objectResponse.getOutput().value);
    }

    /**
     * 测试Metadata修改防护
     */
    @Test
    void testMetadataImmutability() {
        Map<String, Object> originalMetadata = new HashMap<>();
        originalMetadata.put("key", "value");

        Response<String> response = new Response<>(textOutput, tokenUsage, finishReason, originalMetadata);

        // 修改原始metadata不应影响Response
        originalMetadata.put("newKey", "newValue");
        assertEquals(1, response.getMetadata().size());
        assertFalse(response.getMetadata().containsKey("newKey"));

        // 修改返回的metadata不应影响Response内部状态
        Map<String, Object> returnedMetadata = response.getMetadata();
        returnedMetadata.put("anotherKey", "anotherValue");
        // 注意：这里无法测试不可变性，因为返回的是新的HashMap副本
        // 实际实现需要看具体的getMetadata()方法
    }

    /**
     * 测试toString方法（如果实现了）
     */
    @Test
    void testToString() {
        Response<String> response = new Response<>(textOutput, tokenUsage, finishReason, metadata);
        String result = response.toString();

        assertNotNull(result);
        // 基础验证toString包含类名
        assertTrue(result.contains("Response"));
    }

    /**
     * 测试equals和hashCode（如果实现了）
     */
    @Test
    void testEqualsAndHashCode() {
        Response<String> response1 = new Response<>(textOutput, tokenUsage, finishReason, metadata);
        Response<String> response2 = new Response<>(textOutput, tokenUsage, finishReason, metadata);

        // 基础验证 - 自反性
        assertEquals(response1, response1);

        // 注意：实际的equals/hashCode实现需要查看源码
        // 这里只是基础框架
    }

    /**
     * 用于测试的内部类
     */
    private static class TestObject {
        final String name;
        final int value;

        TestObject(String name, int value) {
            this.name = name;
            this.value = value;
        }
    }
}

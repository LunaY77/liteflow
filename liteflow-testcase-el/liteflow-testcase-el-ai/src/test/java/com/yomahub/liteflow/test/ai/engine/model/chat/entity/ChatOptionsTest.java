/**
 * ChatOptions对话选项配置测试类
 *
 * @author 苍镜月
 * @since TODO
 */
package com.yomahub.liteflow.test.ai.engine.model.chat.entity;

import com.yomahub.liteflow.ai.engine.model.ModelOptions;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatOptions;
import com.yomahub.liteflow.ai.engine.util.request.RequestBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChatOptionsTest {

    private ChatOptions chatOptions;
    private Double temperature;
    private Double topP;
    private Integer topK;
    private Integer maxTokens;
    private Integer seed;
    private Boolean enableThinking;

    @BeforeEach
    void setUp() {
        temperature = 0.7;
        topP = 0.9;
        topK = 40;
        maxTokens = 1024;
        seed = 12345;
        enableThinking = true;
    }

    /**
     * 测试默认构造函数
     */
    @Test
    void testDefaultConstructor() {
        ChatOptions options = new ChatOptions();

        assertNotNull(options);
        assertEquals(0.8, options.getTemperature());
        assertEquals(0.9, options.getTopP());
        assertEquals(50, options.getTopK());
        assertEquals(512, options.getMaxTokens());
        assertNull(options.getSeed());
        assertTrue(options.getEnableThinking());
    }

    /**
     * 测试带参数构造函数
     */
    @Test
    void testParameterizedConstructor() {
        ChatOptions options = new ChatOptions(
                temperature, topP, topK, maxTokens, seed, enableThinking);

        assertEquals(temperature, options.getTemperature());
        assertEquals(topP, options.getTopP());
        assertEquals(topK, options.getTopK());
        assertEquals(maxTokens, options.getMaxTokens());
        assertEquals(seed, options.getSeed());
        assertEquals(enableThinking, options.getEnableThinking());
    }

    /**
     * 测试Builder模式构建ChatOptions
     */
    @Test
    void testBuilderPattern() {
        ChatOptions options = ChatOptions.builder()
                .temperature(temperature)
                .topP(topP)
                .topK(topK)
                .maxTokens(maxTokens)
                .seed(seed)
                .enableThinking(enableThinking)
                .build();

        assertEquals(temperature, options.getTemperature());
        assertEquals(topP, options.getTopP());
        assertEquals(topK, options.getTopK());
        assertEquals(maxTokens, options.getMaxTokens());
        assertEquals(seed, options.getSeed());
        assertEquals(enableThinking, options.getEnableThinking());
    }

    /**
     * 测试Builder模式默认值
     */
    @Test
    void testBuilderDefaultValues() {
        ChatOptions options = ChatOptions.DEFAULT;

        assertEquals(0.8, options.getTemperature());
        assertEquals(0.9, options.getTopP());
        assertEquals(50, options.getTopK());
        assertEquals(512, options.getMaxTokens());
        assertNull(options.getSeed());
        assertTrue(options.getEnableThinking());
    }

    /**
     * 测试DEFAULT常量
     */
    @Test
    void testDefaultConstant() {
        ChatOptions defaultOptions = ChatOptions.DEFAULT;

        assertNotNull(defaultOptions);
        assertEquals(0.8, defaultOptions.getTemperature());
        assertEquals(0.9, defaultOptions.getTopP());
        assertEquals(50, defaultOptions.getTopK());
        assertEquals(512, defaultOptions.getMaxTokens());
        assertNull(defaultOptions.getSeed());
        assertTrue(defaultOptions.getEnableThinking());
    }

    /**
     * 测试toRequestBody方法
     */
    @Test
    void testToRequestBody() {
        ChatOptions options = new ChatOptions(
                temperature, topP, topK, maxTokens, seed, enableThinking);

        RequestBody requestBody = options.toRequestBody();
        assertNotNull(requestBody);

        // 验证转换后的结果包含配置数据
        String bodyString = requestBody.toString();
        assertTrue(bodyString.contains("temperature") || bodyString.contains("0.7"));
        assertTrue(bodyString.contains("top_p") || bodyString.contains("0.9"));
        assertTrue(bodyString.contains("top_k") || bodyString.contains("40"));
    }

    /**
     * 测试toRequestBody方法 - 默认值
     */
    @Test
    void testToRequestBodyWithDefaults() {
        ChatOptions options = new ChatOptions();
        RequestBody requestBody = options.toRequestBody();

        assertNotNull(requestBody);
    }

    /**
     * 测试Setter和Getter方法
     */
    @Test
    void testSettersAndGetters() {
        ChatOptions options = new ChatOptions();

        // 测试temperature
        options.setTemperature(0.5);
        assertEquals(0.5, options.getTemperature());

        // 测试topP
        options.setTopP(0.8);
        assertEquals(0.8, options.getTopP());

        // 测试topK
        options.setTopK(30);
        assertEquals(30, options.getTopK());

        // 测试maxTokens
        options.setMaxTokens(2048);
        assertEquals(2048, options.getMaxTokens());

        // 测试seed
        options.setSeed(54321);
        assertEquals(54321, options.getSeed());

        // 测试enableThinking
        options.setEnableThinking(false);
        assertFalse(options.getEnableThinking());
    }

    /**
     * 测试ChatOptions实现ModelOptions接口
     */
    @Test
    void testImplementsModelOptions() {
        ChatOptions options = new ChatOptions();

        // 验证ChatOptions实现了ModelOptions接口
        assertTrue(options instanceof ModelOptions);
        assertInstanceOf(ModelOptions.class, options);
    }

    /**
     * 测试边界值
     */
    @Test
    void testBoundaryValues() {
        // 测试temperature边界值
        ChatOptions options1 = ChatOptions.builder()
                .temperature(0.0)
                .build();
        assertEquals(0.0, options1.getTemperature());

        ChatOptions options2 = ChatOptions.builder()
                .temperature(2.0)
                .build();
        assertEquals(2.0, options2.getTemperature());

        // 测试topP边界值
        ChatOptions options3 = ChatOptions.builder()
                .topP(0.0)
                .build();
        assertEquals(0.0, options3.getTopP());

        ChatOptions options4 = ChatOptions.builder()
                .topP(1.0)
                .build();
        assertEquals(1.0, options4.getTopP());
    }

    /**
     * 测试null值处理
     */
    @Test
    void testNullValues() {
        ChatOptions options = new ChatOptions(
                null, null, null, null, null, null);

        assertNull(options.getTemperature());
        assertNull(options.getTopP());
        assertNull(options.getTopK());
        assertNull(options.getMaxTokens());
        assertNull(options.getSeed());
        assertNull(options.getEnableThinking());
    }

    /**
     * 测试Builder的链式调用
     */
    @Test
    void testBuilderChaining() {
        ChatOptions options = ChatOptions.builder()
                .temperature(0.6)
                .topP(0.95)
                .topK(60)
                .maxTokens(1500)
                .seed(98765)
                .enableThinking(false)
                .build();

        assertEquals(0.6, options.getTemperature());
        assertEquals(0.95, options.getTopP());
        assertEquals(60, options.getTopK());
        assertEquals(1500, options.getMaxTokens());
        assertEquals(98765, options.getSeed());
        assertFalse(options.getEnableThinking());
    }

    /**
     * 测试配置参数的合理性
     */
    @Test
    void testReasonableValues() {
        // 测试合理的AI模型参数
        ChatOptions options = ChatOptions.builder()
                .temperature(0.7) // 创造性和一致性的平衡
                .topP(0.9) // 多样性控制
                .topK(50) // 候选token数量
                .maxTokens(1024) // 常见的token限制
                .seed(42) // 可重现的随机种子
                .enableThinking(true) // 启用思考模式
                .build();

        assertTrue(options.getTemperature() >= 0.0 && options.getTemperature() <= 2.0);
        assertTrue(options.getTopP() >= 0.0 && options.getTopP() <= 1.0);
        assertTrue(options.getTopK() > 0);
        assertTrue(options.getMaxTokens() > 0);
        assertNotNull(options.getSeed());
        assertTrue(options.getEnableThinking());
    }
}

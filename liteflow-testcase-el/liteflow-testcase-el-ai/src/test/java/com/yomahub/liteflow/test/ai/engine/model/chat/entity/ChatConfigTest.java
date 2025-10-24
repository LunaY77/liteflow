/**
 * ChatConfig对话配置信息测试类
 *
 * @author 苍镜月
 * @since TODO
 */
package com.yomahub.liteflow.test.ai.engine.model.chat.entity;

import com.yomahub.liteflow.ai.engine.model.ModelConfig;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ChatConfigTest {

    private String apiUrl;
    private String endPoint;
    private String apiKey;
    private String provider;
    private String model;
    private Duration connectTimeout;
    private Duration readTimeout;
    private Map<String, Object> headersConfig;
    private boolean logRequest;
    private boolean logResponse;
    private boolean autoToolCallEnabled;

    @BeforeEach
    void setUp() {
        apiUrl = "https://api.openai.com";
        endPoint = "/v1/chat/completions";
        apiKey = "test-api-key";
        provider = "openai";
        model = "gpt-3.5-turbo";
        connectTimeout = Duration.ofSeconds(30);
        readTimeout = Duration.ofSeconds(60);
        headersConfig = new HashMap<>();
        headersConfig.put("Custom-Header", "custom-value");
        logRequest = false;
        logResponse = false;
        autoToolCallEnabled = true;
    }

    /**
     * 测试默认构造函数
     */
    @Test
    void testDefaultConstructor() {
        ChatConfig config = new ChatConfig();

        assertNotNull(config);
        assertNull(config.getApiUrl());
        assertNull(config.getEndPoint());
        assertNull(config.getApiKey());
        assertNull(config.getProvider());
        assertNull(config.getModel());
        assertEquals(Duration.ofSeconds(60), config.getConnectTimeout());
        assertEquals(Duration.ofSeconds(60), config.getReadTimeout());
        assertNotNull(config.getHeadersConfig());
        assertTrue(config.getHeadersConfig().isEmpty());
        assertTrue(config.isAutoToolCallEnabled()); // 默认为true
    }

    /**
     * 测试带参数构造函数
     */
    @Test
    void testParameterizedConstructor() {
        ChatConfig config = new ChatConfig(
                apiUrl, endPoint, apiKey, provider, model,
                connectTimeout, readTimeout, headersConfig, logRequest, logResponse, autoToolCallEnabled);

        assertEquals(apiUrl, config.getApiUrl());
        assertEquals(endPoint, config.getEndPoint());
        assertEquals(apiKey, config.getApiKey());
        assertEquals(provider, config.getProvider());
        assertEquals(model, config.getModel());
        assertEquals(connectTimeout, config.getConnectTimeout());
        assertEquals(readTimeout, config.getReadTimeout());
        assertEquals(headersConfig, config.getHeadersConfig());
        assertTrue(config.isAutoToolCallEnabled());
    }

    /**
     * 测试autoToolCallEnabled的setter和getter
     */
    @Test
    void testAutoToolCallEnabledAccessors() {
        ChatConfig config = new ChatConfig();

        // 测试默认值
        assertTrue(config.isAutoToolCallEnabled());

        // 测试设置为false
        config.setAutoToolCallEnabled(false);
        assertFalse(config.isAutoToolCallEnabled());

        // 测试设置为true
        config.setAutoToolCallEnabled(true);
        assertTrue(config.isAutoToolCallEnabled());
    }

    /**
     * 测试ChatConfig继承自ModelConfig
     */
    @Test
    void testInheritanceFromModelConfig() {
        ChatConfig config = new ChatConfig();

        // 验证ChatConfig是ModelConfig的子类
        assertTrue(config instanceof ModelConfig);
        assertInstanceOf(ModelConfig.class, config);
    }

    /**
     * 测试ChatConfig重写的方法
     */
    @Test
    void testOverriddenMethods() {
        // 使用完整参数构造函数创建实例
        ChatConfig config = new ChatConfig(
                apiUrl, endPoint, apiKey, provider, model,
                connectTimeout, readTimeout, headersConfig, logRequest, logResponse, false);

        // 测试继承的方法依然有效
        assertNotNull(config.toRequestBody());
        assertNotNull(config.toRequestHeader());
        assertEquals("https://api.openai.com/v1/chat/completions", config.resolveUrl());
    }

    /**
     * 测试Builder的基本功能
     */
    @Test
    void testBuilderBasicFunctionality() {
        // 测试Builder能够创建有效的配置
        ChatConfig.Builder<?> builder = ChatConfig.builder()
                .apiUrl(apiUrl)
                .endPoint(endPoint)
                .provider(provider)
                .model(model);

        assertNotNull(builder);

        // 构建实例
        ModelConfig config = builder.build();
        assertNotNull(config);
        assertInstanceOf(ChatConfig.class, config);

        ChatConfig chatConfig = (ChatConfig) config;
        assertEquals(apiUrl, chatConfig.getApiUrl());
        assertEquals(endPoint, chatConfig.getEndPoint());
        assertEquals(provider, chatConfig.getProvider());
        assertEquals(model, chatConfig.getModel());
        assertTrue(chatConfig.isAutoToolCallEnabled()); // 默认为true
    }

    /**
     * 测试Builder的autoToolCallEnabled设置
     */
    @Test
    void testBuilderAutoToolCallEnabled() {
        // 测试设置为false
        ModelConfig config1 = ChatConfig.builder()
                .apiUrl(apiUrl)
                .endPoint(endPoint)
                .provider(provider)
                .model(model)
                .autoToolCallEnabled(false)
                .build();

        ChatConfig chatConfig1 = (ChatConfig) config1;
        assertFalse(chatConfig1.isAutoToolCallEnabled());

        // 测试设置为true
        ModelConfig config2 = ChatConfig.builder()
                .apiUrl(apiUrl)
                .endPoint(endPoint)
                .provider(provider)
                .model(model)
                .autoToolCallEnabled(true)
                .build();

        ChatConfig chatConfig2 = (ChatConfig) config2;
        assertTrue(chatConfig2.isAutoToolCallEnabled());
    }
}

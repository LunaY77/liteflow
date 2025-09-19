/**
 * ModelConfig模型配置信息测试类
 *
 * @author 苍镜月
 * @since TODO
 */
package com.yomahub.liteflow.test.ai.engine.model;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.yomahub.liteflow.ai.engine.model.ModelConfig;
import com.yomahub.liteflow.ai.engine.util.request.RequestBody;
import com.yomahub.liteflow.ai.engine.util.request.RequestHeader;

class ModelConfigTest {

    private ModelConfig modelConfig;
    private String apiUrl;
    private String endPoint;
    private String apiKey;
    private String provider;
    private String model;
    private Duration connectTimeout;
    private Duration readTimeout;
    private Map<String, Object> headersConfig;

    @BeforeEach
    void setUp() {
        apiUrl = "https://api.example.com";
        endPoint = "/v1/chat/completions";
        apiKey = "test-api-key";
        provider = "openai";
        model = "gpt-3.5-turbo";
        connectTimeout = Duration.ofSeconds(30);
        readTimeout = Duration.ofSeconds(60);
        headersConfig = new HashMap<>();
        headersConfig.put("Custom-Header", "custom-value");

        modelConfig = new ModelConfig(
                apiUrl, endPoint, apiKey, provider, model,
                connectTimeout, readTimeout, headersConfig);
    }

    /**
     * 测试默认构造函数
     */
    @Test
    void testDefaultConstructor() {
        ModelConfig config = new ModelConfig();

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
    }

    /**
     * 测试带参数构造函数
     */
    @Test
    void testParameterizedConstructor() {
        assertEquals(apiUrl, modelConfig.getApiUrl());
        assertEquals(endPoint, modelConfig.getEndPoint());
        assertEquals(apiKey, modelConfig.getApiKey());
        assertEquals(provider, modelConfig.getProvider());
        assertEquals(model, modelConfig.getModel());
        assertEquals(connectTimeout, modelConfig.getConnectTimeout());
        assertEquals(readTimeout, modelConfig.getReadTimeout());
        assertEquals(headersConfig, modelConfig.getHeadersConfig());
    }

    /**
     * 测试Builder模式构建ModelConfig
     */
    @Test
    void testBuilderPattern() {
        ModelConfig config = ModelConfig.builder()
                .apiUrl(apiUrl)
                .endPoint(endPoint)
                .apiKey(apiKey)
                .provider(provider)
                .model(model)
                .connectTimeout(connectTimeout)
                .readTimeout(readTimeout)
                .headersConfig(headersConfig)
                .build();

        assertEquals(apiUrl, config.getApiUrl());
        assertEquals(endPoint, config.getEndPoint());
        assertEquals(apiKey, config.getApiKey());
        assertEquals(provider, config.getProvider());
        assertEquals(model, config.getModel());
        assertEquals(connectTimeout, config.getConnectTimeout());
        assertEquals(readTimeout, config.getReadTimeout());
        assertEquals(headersConfig, config.getHeadersConfig());
    }

    /**
     * 测试Builder模式必填字段校验
     */
    @Test
    void testBuilderRequiredFieldsValidation() {
        // 测试缺少apiUrl
        assertThrows(NullPointerException.class, () -> {
            ModelConfig.builder()
                    .endPoint(endPoint)
                    .provider(provider)
                    .model(model)
                    .build();
        });

        // 测试缺少endPoint
        assertThrows(NullPointerException.class, () -> {
            ModelConfig.builder()
                    .apiUrl(apiUrl)
                    .provider(provider)
                    .model(model)
                    .build();
        });

        // 测试缺少provider
        assertThrows(NullPointerException.class, () -> {
            ModelConfig.builder()
                    .apiUrl(apiUrl)
                    .endPoint(endPoint)
                    .model(model)
                    .build();
        });

        // 测试缺少model
        assertThrows(NullPointerException.class, () -> {
            ModelConfig.builder()
                    .apiUrl(apiUrl)
                    .endPoint(endPoint)
                    .provider(provider)
                    .build();
        });
    }

    /**
     * 测试Builder模式最小配置
     */
    @Test
    void testBuilderMinimalConfiguration() {
        ModelConfig config = ModelConfig.builder()
                .apiUrl(apiUrl)
                .endPoint(endPoint)
                .provider(provider)
                .model(model)
                .build();

        assertNotNull(config);
        assertEquals(apiUrl, config.getApiUrl());
        assertEquals(endPoint, config.getEndPoint());
        assertEquals(provider, config.getProvider());
        assertEquals(model, config.getModel());
        assertNull(config.getApiKey());
        assertEquals(Duration.ofSeconds(60), config.getConnectTimeout());
        assertEquals(Duration.ofSeconds(60), config.getReadTimeout());
        assertNotNull(config.getHeadersConfig());
    }

    /**
     * 测试toRequestBody方法
     */
    @Test
    void testToRequestBody() {
        RequestBody requestBody = modelConfig.toRequestBody();

        assertNotNull(requestBody);
        // 验证模型名称被添加到请求体中
        assertTrue(requestBody.toString().contains(model));
    }

    /**
     * 测试toRequestHeader方法 - 带API Key
     */
    @Test
    void testToRequestHeaderWithApiKey() {
        RequestHeader requestHeader = modelConfig.toRequestHeader();

        assertNotNull(requestHeader);
        // API Key应该被添加到Authorization header中
        Map<String, String> headerMap = requestHeader.convert();
        String authValue = headerMap.get("Authorization");
        assertNotNull(authValue);
        assertTrue(authValue.startsWith("Bearer "));
        assertTrue(authValue.contains(apiKey));
    }

    /**
     * 测试toRequestHeader方法 - 无API Key
     */
    @Test
    void testToRequestHeaderWithoutApiKey() {
        ModelConfig configWithoutKey = ModelConfig.builder()
                .apiUrl(apiUrl)
                .endPoint(endPoint)
                .provider(provider)
                .model(model)
                .build();

        RequestHeader requestHeader = configWithoutKey.toRequestHeader();
        assertNotNull(requestHeader);
    }

    /**
     * 测试resolveUrl方法 - 正常情况
     */
    @Test
    void testResolveUrlNormal() {
        String resolvedUrl = modelConfig.resolveUrl();
        assertEquals("https://api.example.com/v1/chat/completions", resolvedUrl);
    }

    /**
     * 测试resolveUrl方法 - API URL以斜杠结尾
     */
    @Test
    void testResolveUrlWithTrailingSlash() {
        ModelConfig config = ModelConfig.builder()
                .apiUrl("https://api.example.com/")
                .endPoint("/v1/chat/completions")
                .provider(provider)
                .model(model)
                .build();

        String resolvedUrl = config.resolveUrl();
        assertEquals("https://api.example.com/v1/chat/completions", resolvedUrl);
    }

    /**
     * 测试resolveUrl方法 - EndPoint不以斜杠开头
     */
    @Test
    void testResolveUrlWithoutLeadingSlash() {
        ModelConfig config = ModelConfig.builder()
                .apiUrl("https://api.example.com")
                .endPoint("v1/chat/completions")
                .provider(provider)
                .model(model)
                .build();

        String resolvedUrl = config.resolveUrl();
        assertEquals("https://api.example.com/v1/chat/completions", resolvedUrl);
    }

    /**
     * 测试resolveUrl方法 - 特殊情况组合
     */
    @Test
    void testResolveUrlSpecialCases() {
        ModelConfig config = ModelConfig.builder()
                .apiUrl("https://api.example.com/")
                .endPoint("v1/chat/completions")
                .provider(provider)
                .model(model)
                .build();

        String resolvedUrl = config.resolveUrl();
        assertEquals("https://api.example.com/v1/chat/completions", resolvedUrl);
    }

    /**
     * 测试Setter和Getter方法
     */
    @Test
    void testSettersAndGetters() {
        ModelConfig config = new ModelConfig();

        // 测试apiUrl
        config.setApiUrl("new-api-url");
        assertEquals("new-api-url", config.getApiUrl());

        // 测试endPoint
        config.setEndPoint("new-endpoint");
        assertEquals("new-endpoint", config.getEndPoint());

        // 测试apiKey
        config.setApiKey("new-api-key");
        assertEquals("new-api-key", config.getApiKey());

        // 测试provider
        config.setProvider("new-provider");
        assertEquals("new-provider", config.getProvider());

        // 测试model
        config.setModel("new-model");
        assertEquals("new-model", config.getModel());

        // 测试connectTimeout
        Duration newConnectTimeout = Duration.ofSeconds(120);
        config.setConnectTimeout(newConnectTimeout);
        assertEquals(newConnectTimeout, config.getConnectTimeout());

        // 测试readTimeout
        Duration newReadTimeout = Duration.ofSeconds(180);
        config.setReadTimeout(newReadTimeout);
        assertEquals(newReadTimeout, config.getReadTimeout());
    }

    /**
     * 测试Header操作方法
     */
    @Test
    void testHeaderOperations() {
        ModelConfig config = new ModelConfig();

        // 测试添加Header
        config.addHeader("Test-Header", "test-value");
        assertEquals("test-value", config.getHeadersConfig().get("Test-Header"));

        // 测试移除Header
        config.removeHeader("Test-Header");
        assertFalse(config.getHeadersConfig().containsKey("Test-Header"));
    }

    /**
     * 测试空字符串API Key的处理
     */
    @Test
    void testEmptyApiKeyHandling() {
        ModelConfig config = ModelConfig.builder()
                .apiUrl(apiUrl)
                .endPoint(endPoint)
                .provider(provider)
                .model(model)
                .apiKey("")
                .build();

        RequestHeader requestHeader = config.toRequestHeader();
        assertNotNull(requestHeader);
        // 空字符串应该不会添加Authorization header
        Map<String, String> headerMap = requestHeader.convert();
        assertNull(headerMap.get("Authorization"));
    }

    /**
     * 测试null API Key的处理
     */
    @Test
    void testNullApiKeyHandling() {
        ModelConfig config = ModelConfig.builder()
                .apiUrl(apiUrl)
                .endPoint(endPoint)
                .provider(provider)
                .model(model)
                .apiKey(null)
                .build();

        RequestHeader requestHeader = config.toRequestHeader();
        assertNotNull(requestHeader);
        // null API Key应该不会添加Authorization header
        Map<String, String> headerMap = requestHeader.convert();
        assertNull(headerMap.get("Authorization"));
    }
}

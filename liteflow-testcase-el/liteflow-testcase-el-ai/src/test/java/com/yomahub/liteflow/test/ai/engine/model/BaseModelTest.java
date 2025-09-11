/**
 * BaseModel接口测试类
 *
 * @author 苍镜月
 * @since TODO
 */
package com.yomahub.liteflow.test.ai.engine.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.yomahub.liteflow.ai.engine.model.BaseModel;
import com.yomahub.liteflow.ai.engine.model.ModelConfig;

class BaseModelTest {

    private BaseModel<ModelConfig> baseModel;
    private ModelConfig modelConfig;

    @BeforeEach
    void setUp() {
        // 创建测试用的ModelConfig
        modelConfig = ModelConfig.builder()
                .apiUrl("https://api.example.com")
                .endPoint("/v1/chat/completions")
                .apiKey("test-api-key")
                .provider("test-provider")
                .model("test-model")
                .build();

        // 创建BaseModel的测试实现
        baseModel = () -> modelConfig;
    }

    /**
     * 测试获取模型配置信息
     */
    @Test
    void testGetModelConfig() {
        // 获取模型配置
        ModelConfig result = baseModel.getModelConfig();

        // 验证结果
        assertNotNull(result);
        assertEquals(modelConfig, result);
        assertEquals("https://api.example.com", result.getApiUrl());
        assertEquals("/v1/chat/completions", result.getEndPoint());
        assertEquals("test-api-key", result.getApiKey());
        assertEquals("test-provider", result.getProvider());
        assertEquals("test-model", result.getModel());
    }

    /**
     * 测试BaseModel接口的基本特性
     */
    @Test
    void testBaseModelInterface() {
        // 验证BaseModel是一个接口
        assertTrue(BaseModel.class.isInterface());

        // 验证接口方法存在
        assertDoesNotThrow(() -> {
            BaseModel.class.getMethod("getModelConfig");
        });
    }

    /**
     * 测试返回null配置的情况
     */
    @Test
    void testGetModelConfigWithNull() {
        BaseModel<ModelConfig> nullConfigModel = () -> null;

        // 验证可以返回null
        assertNull(nullConfigModel.getModelConfig());
    }

    /**
     * 测试泛型类型约束
     */
    @Test
    void testGenericTypeConstraint() {
        // 验证泛型类型必须继承自ModelConfig
        BaseModel<? extends ModelConfig> extendedModel = () -> modelConfig;

        assertNotNull(extendedModel.getModelConfig());
        assertInstanceOf(ModelConfig.class, extendedModel.getModelConfig());
    }
}

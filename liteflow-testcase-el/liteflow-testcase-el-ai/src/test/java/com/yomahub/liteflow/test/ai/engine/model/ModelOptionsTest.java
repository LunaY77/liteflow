/**
 * ModelOptions接口测试类
 *
 * @author 苍镜月
 * @since TODO
 */
package com.yomahub.liteflow.test.ai.engine.model;

import com.yomahub.liteflow.ai.engine.model.ModelOptions;
import com.yomahub.liteflow.ai.engine.util.request.RequestBody;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ModelOptionsTest {

    /**
     * 测试ModelOptions接口的基本特性
     */
    @Test
    void testModelOptionsInterface() {
        // 验证ModelOptions是一个接口
        assertTrue(ModelOptions.class.isInterface());

        // 验证接口方法存在
        assertDoesNotThrow(() -> {
            ModelOptions.class.getMethod("toRequestBody");
        });
    }

    /**
     * 测试ModelOptions接口的实现
     */
    @Test
    void testModelOptionsImplementation() {
        // 创建ModelOptions的测试实现
        ModelOptions options = () -> RequestBody.of().put("test-key", "test-value");

        // 测试toRequestBody方法
        RequestBody requestBody = options.toRequestBody();
        assertNotNull(requestBody);

        // 验证转换后的结果包含测试数据
        assertTrue(requestBody.toString().contains("test-key"));
    }

    /**
     * 测试返回空RequestBody的情况
     */
    @Test
    void testEmptyRequestBody() {
        ModelOptions options = () -> RequestBody.of();

        RequestBody requestBody = options.toRequestBody();
        assertNotNull(requestBody);
    }

    /**
     * 测试返回null RequestBody的情况
     */
    @Test
    void testNullRequestBody() {
        ModelOptions options = () -> null;

        RequestBody requestBody = options.toRequestBody();
        assertNull(requestBody);
    }

    /**
     * 测试继承自RequestBodyConvertible接口
     */
    @Test
    void testInheritanceFromRequestBodyConvertible() {
        // 验证ModelOptions继承自RequestBodyConvertible
        assertTrue(com.yomahub.liteflow.ai.engine.util.request.RequestBodyConvertible.class
                .isAssignableFrom(ModelOptions.class));
    }

    /**
     * 测试ModelOptions的多种实现
     */
    @Test
    void testMultipleImplementations() {
        // 实现1：简单的键值对
        ModelOptions options1 = () -> RequestBody.of()
                .put("temperature", 0.8)
                .put("maxTokens", 100);

        // 实现2：复杂的配置
        Map<String, Object> optionsMap = new HashMap<>();
        optionsMap.put("seed", 42);
        ModelOptions options2 = () -> RequestBody.of()
                .put("model", "gpt-3.5-turbo")
                .put("stream", true)
                .put("options", optionsMap);

        // 验证两个实现都能正常工作
        assertNotNull(options1.toRequestBody());
        assertNotNull(options2.toRequestBody());

        // 验证内容不同
        assertNotEquals(options1.toRequestBody().toString(),
                options2.toRequestBody().toString());
    }
}

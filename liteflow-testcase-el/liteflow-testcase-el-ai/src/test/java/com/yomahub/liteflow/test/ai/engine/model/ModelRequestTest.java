/**
 * ModelRequest接口测试类
 *
 * @author 苍镜月
 * @since TODO
 */
package com.yomahub.liteflow.test.ai.engine.model;

import com.yomahub.liteflow.ai.engine.model.ModelRequest;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatRequest;
import com.yomahub.liteflow.ai.engine.util.request.RequestBody;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ModelRequestTest {

    /**
     * 测试ModelRequest接口的基本特性
     */
    @Test
    void testModelRequestInterface() {
        // 验证ModelRequest是一个接口
        assertTrue(ModelRequest.class.isInterface());

        // 验证接口方法存在
        assertDoesNotThrow(() -> {
            ModelRequest.class.getMethod("toRequestBody");
            ModelRequest.class.getMethod("toChatRequest");
        });
    }

    /**
     * 测试ModelRequest接口的基本实现
     */
    @Test
    void testModelRequestImplementation() {
        // 创建ModelRequest的测试实现
        ModelRequest request = () -> RequestBody.of().put("messages", "test-message");

        // 测试toRequestBody方法
        RequestBody requestBody = request.toRequestBody();
        assertNotNull(requestBody);
        assertTrue(requestBody.toString().contains("messages"));
    }

    /**
     * 测试toChatRequest默认方法
     */
    @Test
    void testToChatRequestDefault() {
        // 创建一个实现了ModelRequest的ChatRequest测试类
        ChatRequest chatRequest = new ChatRequest() {
            @Override
            public RequestBody toRequestBody() {
                return RequestBody.of().put("type", "chat");
            }
        };

        // 测试默认的toChatRequest方法
        ChatRequest result = chatRequest.toChatRequest();
        assertNotNull(result);
        assertSame(chatRequest, result);
    }

    /**
     * 测试非ChatRequest类型的toChatRequest转换
     */
    @Test
    void testNonChatRequestToChatRequest() {
        // 创建一个普通的ModelRequest实现
        ModelRequest request = () -> RequestBody.of().put("test", "value");

        // 测试转换为ChatRequest（这会导致ClassCastException）
        assertThrows(ClassCastException.class, request::toChatRequest);
    }

    /**
     * 测试继承自RequestBodyConvertible接口
     */
    @Test
    void testInheritanceFromRequestBodyConvertible() {
        // 验证ModelRequest继承自RequestBodyConvertible
        assertTrue(com.yomahub.liteflow.ai.engine.util.request.RequestBodyConvertible.class
                .isAssignableFrom(ModelRequest.class));
    }

    /**
     * 测试返回null RequestBody的情况
     */
    @Test
    void testNullRequestBody() {
        ModelRequest request = () -> null;

        RequestBody requestBody = request.toRequestBody();
        assertNull(requestBody);
    }

    /**
     * 测试空RequestBody的情况
     */
    @Test
    void testEmptyRequestBody() {
        ModelRequest request = RequestBody::of;

        RequestBody requestBody = request.toRequestBody();
        assertNotNull(requestBody);
    }

    /**
     * 测试ModelRequest的多种实现
     */
    @Test
    void testMultipleImplementations() {
        // 实现1：简单的请求
        ModelRequest request1 = () -> RequestBody.of()
                .put("prompt", "Hello")
                .put("max_tokens", 100);

        // 实现2：复杂的请求
        Map<String, Object> messageMap = new HashMap<>();
        messageMap.put("role", "user");
        messageMap.put("content", "Hello");
        ModelRequest request2 = () -> RequestBody.of()
                .put("messages", messageMap)
                .put("model", "gpt-3.5-turbo")
                .put("stream", false);

        // 验证两个实现都能正常工作
        assertNotNull(request1.toRequestBody());
        assertNotNull(request2.toRequestBody());

        // 验证内容不同
        assertNotEquals(request1.toRequestBody().toString(),
                request2.toRequestBody().toString());
    }

    /**
     * 测试ModelRequest与ChatRequest的类型检查
     */
    @Test
    void testTypeChecking() {
        // 创建ChatRequest实例
        ChatRequest chatRequest = new ChatRequest() {
            @Override
            public RequestBody toRequestBody() {
                return RequestBody.of().put("type", "chat");
            }
        };

        // 验证ChatRequest是ModelRequest的实例
        assertTrue(chatRequest instanceof ModelRequest);

        // 验证可以安全转换
        assertDoesNotThrow(() -> {
            ModelRequest modelRequest = chatRequest;
            ChatRequest converted = modelRequest.toChatRequest();
            assertSame(chatRequest, converted);
        });
    }
}

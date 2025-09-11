/**
 * ModelResponse接口测试类
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
import org.junit.jupiter.api.Test;

import com.yomahub.liteflow.ai.engine.model.ModelResponse;

class ModelResponseTest {

    /**
     * 测试ModelResponse接口的基本特性
     */
    @Test
    void testModelResponseInterface() {
        // 验证ModelResponse是一个接口
        assertTrue(ModelResponse.class.isInterface());

        // 验证接口方法存在
        assertDoesNotThrow(() -> {
            ModelResponse.class.getMethod("getOutput");
        });
    }

    /**
     * 测试ModelResponse接口的基本实现
     */
    @Test
    void testModelResponseImplementation() {
        String testOutput = "test output";

        // 创建ModelResponse的测试实现
        ModelResponse<String> response = () -> testOutput;

        // 测试getOutput方法
        String output = response.getOutput();
        assertNotNull(output);
        assertEquals(testOutput, output);
    }

    /**
     * 测试返回null输出的情况
     */
    @Test
    void testNullOutput() {
        ModelResponse<String> response = () -> null;

        String output = response.getOutput();
        assertNull(output);
    }

    /**
     * 测试不同类型的输出
     */
    @Test
    void testDifferentOutputTypes() {
        // String类型输出
        ModelResponse<String> stringResponse = () -> "Hello World";
        assertEquals("Hello World", stringResponse.getOutput());
        assertInstanceOf(String.class, stringResponse.getOutput());

        // Integer类型输出
        ModelResponse<Integer> intResponse = () -> 42;
        assertEquals(42, intResponse.getOutput());
        assertInstanceOf(Integer.class, intResponse.getOutput());

        // Boolean类型输出
        ModelResponse<Boolean> boolResponse = () -> true;
        assertTrue(boolResponse.getOutput());
        assertInstanceOf(Boolean.class, boolResponse.getOutput());

        // 自定义对象类型输出
        TestOutput customOutput = new TestOutput("test", 123);
        ModelResponse<TestOutput> customResponse = () -> customOutput;
        assertEquals(customOutput, customResponse.getOutput());
        assertInstanceOf(TestOutput.class, customResponse.getOutput());
    }

    /**
     * 测试泛型类型约束
     */
    @Test
    void testGenericTypeConstraint() {
        // 测试通配符泛型
        ModelResponse<?> wildcardResponse = () -> "any type";
        assertNotNull(wildcardResponse.getOutput());

        // 测试有界通配符
        ModelResponse<? extends Number> numberResponse = () -> 3.14;
        assertNotNull(numberResponse.getOutput());
        assertInstanceOf(Number.class, numberResponse.getOutput());
    }

    /**
     * 测试复杂对象输出
     */
    @Test
    void testComplexObjectOutput() {
        ComplexOutput complexOutput = new ComplexOutput();
        complexOutput.setMessage("Complex message");
        complexOutput.setCode(200);
        complexOutput.setSuccess(true);

        ModelResponse<ComplexOutput> response = () -> complexOutput;

        ComplexOutput output = response.getOutput();
        assertNotNull(output);
        assertEquals("Complex message", output.getMessage());
        assertEquals(200, output.getCode());
        assertTrue(output.isSuccess());
    }

    /**
     * 测试ModelResponse的多种实现
     */
    @Test
    void testMultipleImplementations() {
        // 实现1：直接返回值
        ModelResponse<String> directResponse = () -> "Direct value";

        // 实现2：计算后返回
        ModelResponse<Integer> calculatedResponse = () -> {
            int a = 10;
            int b = 20;
            return a + b;
        };

        // 实现3：条件返回
        ModelResponse<String> conditionalResponse = () -> {
            boolean condition = true;
            return condition ? "True result" : "False result";
        };

        // 验证所有实现都能正常工作
        assertEquals("Direct value", directResponse.getOutput());
        assertEquals(30, calculatedResponse.getOutput());
        assertEquals("True result", conditionalResponse.getOutput());
    }

    /**
     * 测试用的简单输出类
     */
    private static class TestOutput {
        private final String name;
        private final int value;

        public TestOutput(String name, int value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public int getValue() {
            return value;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null || getClass() != obj.getClass())
                return false;
            TestOutput that = (TestOutput) obj;
            return value == that.value && name.equals(that.name);
        }

        @Override
        public int hashCode() {
            return name.hashCode() + value;
        }
    }

    /**
     * 测试用的复杂输出类
     */
    private static class ComplexOutput {
        private String message;
        private int code;
        private boolean success;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }
    }
}

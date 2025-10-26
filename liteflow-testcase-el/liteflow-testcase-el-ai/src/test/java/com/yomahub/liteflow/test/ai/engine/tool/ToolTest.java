package com.yomahub.liteflow.test.ai.engine.tool;

import com.yomahub.liteflow.ai.engine.model.output.structure.TypeReference;
import com.yomahub.liteflow.ai.engine.model.output.structure.generator.JsonSchemaGenerator;
import com.yomahub.liteflow.ai.engine.tool.ToolCallBack;
import com.yomahub.liteflow.ai.engine.tool.ToolDefinition;
import com.yomahub.liteflow.ai.engine.tool.annotation.Tool;
import com.yomahub.liteflow.ai.engine.tool.annotation.ToolParam;
import com.yomahub.liteflow.ai.engine.tool.function.FunctionToolCallback;
import com.yomahub.liteflow.ai.engine.tool.registry.ScanningToolRegistry;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * tool test
 *
 * @author 苍镜月
 */

public class ToolTest {

    @Tool(name = "testTool")
    public List<String> testTool(@ToolParam("input") Input<String> input) {
        return Collections.singletonList("Processed: " + input.getValue());
    }

    public static class Input<T> implements Serializable {
        private T value;

        public Input() {
        }

        public Input(T value) {
            this.value = value;
        }

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }
    }

    @Test
    public void testScanningToolRegistry() {
        ScanningToolRegistry scanningToolRegistry = new ScanningToolRegistry("com.yomahub.liteflow.test.ai.engine.tool.domain");
        Collection<ToolCallBack> tools = scanningToolRegistry.getAllTools();
        Assertions.assertEquals(4, tools.size());
        System.out.println(tools);
    }

    @Test
    public void testToolDefinition() {
        ToolDefinition<Input<String>> toolDefinition = new ToolDefinition<>("testTool", "Test Tool", new TypeReference<Input<String>>() {
        });

        Assertions.assertEquals("testTool", toolDefinition.getName());
        Assertions.assertEquals("Test Tool", toolDefinition.getDescription());
        Assertions.assertEquals("com.yomahub.liteflow.test.ai.engine.tool.ToolTest$Input<java.lang.String>", toolDefinition.getInputType().getTypeName());
        Assertions.assertEquals(
                "{\n" +
                        "  \"type\" : \"function\",\n" +
                        "  \"function\" : {\n" +
                        "    \"name\" : \"testTool\",\n" +
                        "    \"description\" : \"Test Tool\",\n" +
                        "    \"parameters\" : {\n" +
                        "      \"type\" : \"object\",\n" +
                        "      \"properties\" : {\n" +
                        "        \"value\" : {\n" +
                        "          \"type\" : \"string\"\n" +
                        "        }\n" +
                        "      },\n" +
                        "      \"required\" : [ \"value\" ],\n" +
                        "      \"additionalProperties\" : false\n" +
                        "    }\n" +
                        "  }\n" +
                        "}",
                toolDefinition.toJsonString()
        );
    }

    @Test
    public void testFunctionCallBack() {
        FunctionToolCallback<Input<String>, List<String>> toolCallback = new FunctionToolCallback<>(
                new ToolDefinition<>("testTool", "Test Tool", new TypeReference<Input<String>>() {
                }),
                this::testTool
        );

        String call = toolCallback.call("{\"value\":\"Hello, World!\"}");
        Assertions.assertEquals("[ \"Processed: Hello, World!\" ]", call);
    }

    @Test
    public void testFunctionCallBackThrow() {
        FunctionToolCallback<Input<String>, List<String>> toolCallback = new FunctionToolCallback<>(
                new ToolDefinition<>("testTool", "Test Tool", new TypeReference("java.lang.String") {
                }),
                this::testTool
        );

        // Input 和 call 方法传入的 json 不一致，应当报错
        Assertions.assertThrows(RuntimeException.class, () -> toolCallback.call("{\"value\":\"Hello, World!\"}"));
    }

    @Test
    public void test() {
        Class<?> clazz = this.getClass();
        Arrays.stream(clazz.getMethods())
                .filter(method -> method.isAnnotationPresent(Tool.class))
                .forEach(method -> {
                    Tool toolAnnotation = method.getAnnotation(Tool.class);

                    System.out.println("returnType:");
                    System.out.println(JsonSchemaGenerator.generate(method.getGenericReturnType()).toPrettyString());
                    System.out.println();

                    String toolName = toolAnnotation.name();
                    System.out.println("Tool Name: " + toolName);

                    Arrays.stream(method.getParameters())
                            .filter(param -> param.isAnnotationPresent(ToolParam.class))
                            .forEach(param -> {
                                ToolParam paramAnnotation = param.getAnnotation(ToolParam.class);
                                String paramName = paramAnnotation.value();
                                boolean required = paramAnnotation.required();
                                System.out.println("Parameter Name: " + paramName + ", Required: " + required);

                                System.out.println(JsonSchemaGenerator.generate(param.getParameterizedType()).toPrettyString());
                            });

                    System.out.println("--------------------------------------------------");

                    Input<String> testInput = new Input<>("Test Input");
                    try {
                        System.out.println(method.invoke(clazz.cast(this), testInput));
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}

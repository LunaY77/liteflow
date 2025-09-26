package com.yomahub.liteflow.ai.engine.model.output.structure.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.yomahub.liteflow.ai.engine.model.output.structure.TypeReference;
import com.yomahub.liteflow.ai.engine.model.output.structure.generator.JsonSchemaGenerator;
import com.yomahub.liteflow.ai.engine.util.ObjectMapperHolder;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * JsonSchema解析器
 *
 * @author 苍镜月
 * @since 2.16.0
 */

public class JsonSchemaParser<T> {

    private final Type targetType;
    private final JsonNode jsonSchema;

    /**
     * 通过 JsonSchema 动态创建，适用于没有静态 Java 类对应的场景
     *
     * @param jsonSchema JsonSchema
     */
    public static JsonSchemaParser<Map<String, Object>> fromJsonNode(JsonNode jsonSchema) {
        return new JsonSchemaParser<>(jsonSchema);
    }

    /**
     * 通过 TypeReference 生成JsonSchema解析器
     *
     * @param reference TypeReference
     */
    public static <T> JsonSchemaParser<T> fromTypeReference(TypeReference<T> reference) {
        return new JsonSchemaParser<>(reference.getType(), true);
    }

    /**
     * 通过 Type 生成JsonSchema解析器
     *
     * @param type 目标类型
     */
    public static <T> JsonSchemaParser<T> fromType(Type type) {
        return new JsonSchemaParser<>(type, true);
    }

    /**
     * 通过类型全限定名生成JsonSchema解析器
     *
     * @param typeName 目标类型的全限定名
     */
    public static <T> JsonSchemaParser<T> fromTypeName(String typeName) {
        return new JsonSchemaParser<>(typeName, true);
    }

    /**
     * 通过 TypeReference 生成JsonSchema解析器
     *
     * @param reference TypeReference
     */
    public static <T> JsonSchemaParser<T> fromTypeReference(TypeReference<T> reference, boolean strict) {
        return new JsonSchemaParser<>(reference.getType(), strict);
    }

    /**
     * 通过 Type 生成JsonSchema解析器
     *
     * @param type 目标类型
     */
    public static <T> JsonSchemaParser<T> fromType(Type type, boolean strict) {
        return new JsonSchemaParser<>(type, strict);
    }

    /**
     * 通过类型全限定名生成JsonSchema解析器
     *
     * @param typeName 目标类型的全限定名
     */
    public static <T> JsonSchemaParser<T> fromTypeName(String typeName, boolean strict) {
        return new JsonSchemaParser<>(typeName, strict);
    }

    /**
     * 通过 JsonSchema 动态创建，适用于没有静态 Java 类对应的场景
     *
     * @param jsonSchema JsonSchema
     */
    public JsonSchemaParser(JsonNode jsonSchema) {
        this.targetType = Map.class;
        this.jsonSchema = jsonSchema;
    }

    /**
     * 通过类型生成JsonSchema解析器
     *
     * @param targetType 目标类型
     */
    public JsonSchemaParser(Type targetType) {
        this(targetType, true);
    }

    /**
     * 通过类全限定名生成JsonSchema解析器
     *
     * @param typeName 目标类型的全限定名
     */
    public JsonSchemaParser(String typeName) {
        this(JsonSchemaGenerator.typeFromString(typeName), true);
    }

    /**
     * 通过类型生成JsonSchema解析器
     *
     * @param targetType 目标类型
     * @param strict     严格模式
     */
    public JsonSchemaParser(Type targetType, boolean strict) {
        this.targetType = targetType;
        this.jsonSchema = JsonSchemaGenerator.generate(targetType, strict);
    }

    /**
     * 通过类全限定名生成JsonSchema解析器
     *
     * @param typeName 目标类型的全限定名
     * @param strict   严格模式
     */
    public JsonSchemaParser(String typeName, boolean strict) {
        this(JsonSchemaGenerator.typeFromString(typeName), strict);
    }

    /**
     * 将JsonSchema字符串转换为目标类型的对象
     *
     * @param output JsonSchema
     *               字符串
     * @return 转换后的对象
     */
    public T convert(String output) {
        String cleanJson = extractJsonFromMarkdown(output);
        return ObjectMapperHolder.readValue(cleanJson, this.targetType);
    }

    private static String extractJsonFromMarkdown(String output) {
        output = output.trim();

        // 检查并剥离外层的 Markdown 代码块
        if (output.startsWith("```") && output.endsWith("```")) {
            // 提取 ``` 和 ``` 中间的内容
            output = output.substring(3, output.length() - 3);
            output = output.trim(); // 对中间内容进行 trim

            // 如果内容以 "json" 开头（不区分大小写），则移除这个标识符
            if (output.toLowerCase().startsWith("json")) {
                // 移除 "json" 及其后的第一个换行符
                int firstNewLine = output.indexOf('\n');
                if (firstNewLine != -1) {
                    // 确保 "json" 标识符后就是换行符
                    String prefix = output.substring(0, firstNewLine).trim();
                    if (prefix.equalsIgnoreCase("json")) {
                        output = output.substring(firstNewLine + 1);
                    }
                } else {
                    // 处理单行情况，如 ```json{...}```
                    output = output.substring(4);
                }
            }
        }
        return output;
    }

    /**
     * 获取输出 json 格式的大模型提示词
     *
     * @return 提示词
     */
    public String getOutputInstruction() {
        String template =
                "Your response should be in JSON format.\n" +
                        "Do not include any explanations, only provide a RFC8259 compliant JSON response following this format without deviation.\n" +
                        "Do not include markdown code blocks in your response.\n" +
                        "Remove the ```json markdown from the output.\n" +
                        "Here is the JSON Schema instance your output must adhere to:\n" +
                        "```\n%s\n```";
        return String.format(template, jsonSchema.toPrettyString());
    }


    /**
     * JsonSchema
     *
     * @return JsonSchema
     */
    public JsonNode getJsonSchema() {
        return jsonSchema;
    }

    /**
     * JsonSchema
     *
     * @return JsonSchema
     */
    public String getJsonSchemaString() {
        return jsonSchema.toPrettyString();
    }

    /**
     * 获取目标类型
     *
     * @return 目标类型
     */
    public Type getTargetType() {
        return targetType;
    }
}

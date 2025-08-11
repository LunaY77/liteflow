package com.yomahub.liteflow.ai.engine.model.output.structure.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yomahub.liteflow.ai.engine.model.output.structure.generator.JsonSchemaGenerator;

import java.lang.reflect.Type;

/**
 * 输出解析器
 *
 * @author 苍镜月
 * @since TODO
 */

public class OutputParser<T> {

    private final Type targetType;
    private final ObjectMapper objectMapper;
    private final JsonNode jsonSchema;

    /**
     * 通过类型生成输出解析器
     *
     * @param targetType 目标类型
     */
    public OutputParser(Type targetType) {
        this.targetType = targetType;
        this.objectMapper = new ObjectMapper();
        this.jsonSchema = JsonSchemaGenerator.generate(targetType);
    }

    /**
     * 通过类全限定名生成输出解析器
     *
     * @param typeName 目标类型的全限定名
     */
    public OutputParser(String typeName) {
        this.targetType = JsonSchemaGenerator.typeFromString(typeName);
        this.objectMapper = new ObjectMapper();
        this.jsonSchema = JsonSchemaGenerator.generate(typeName);
    }

    /**
     * 将输出字符串转换为目标类型的对象
     *
     * @param output 输出字符串
     * @return 转换后的对象
     */
    public T convert(String output) {
        try {
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

            return this.objectMapper.readValue(output, this.objectMapper.constructType(this.targetType));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * JsonSchema
     *
     * @return JsonSchema
     */
    public JsonNode getJsonSchema() {
        return jsonSchema;
    }
}

package com.yomahub.liteflow.ai.engine.tool;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.yomahub.liteflow.ai.engine.model.output.structure.TypeReference;
import com.yomahub.liteflow.ai.engine.model.output.structure.parser.JsonSchemaParser;

import java.lang.reflect.Type;

/**
 * 工具定义
 *
 * @param <I> 工具函数的输入参数类型 (Request DTO)
 * @author 苍镜月
 * @since TODO
 */

public class ToolDefinition<I> {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final String name;

    private final String description;

    private final JsonSchemaParser<I> inputParser;

    public ToolDefinition(String name, String description, TypeReference<I> inputType) {
        this(name, description, inputType.getType());
    }

    public ToolDefinition(String name, String description, Type inputType) {
        this.name = name;
        this.description = description;
        inputParser = JsonSchemaParser.fromType(inputType);
    }

    private ToolDefinition(Builder<I> builder) {
        this.name = builder.name;
        this.description = builder.description;
        this.inputParser = JsonSchemaParser.fromType(builder.inputType);
    }

    /**
     * 将工具定义转换为 JSON Schema 格式
     *
     * @return JSON Schema 格式的工具定义，包含名称、描述和输入参数的 JSON Schema
     */
    public JsonNode toJsonSchema() {
        // root
        ObjectNode root = MAPPER.createObjectNode();
        root.put("type", "function");
        // function
        ObjectNode function = MAPPER.createObjectNode();
        function.put("name", name);
        function.put("description", description);

        // parameters
        JsonNode parameters = this.inputParser.getJsonSchema();
        function.set("parameters", parameters);

        root.set("function", function);
        return root;
    }

    /**
     * 将工具定义转换为 JSON 字符串
     *
     * @return 格式化的 JSON 字符串，包含工具名称、描述和输入参数的 JSON Schema
     */
    public String toJsonString() {
        return toJsonSchema().toPrettyString();
    }

    @Override
    public String toString() {
        return toJsonString();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Type getInputType() {
        return inputParser.getTargetType();
    }

    public String getInputSchema() {
        return inputParser.getJsonSchemaString();
    }

    public JsonSchemaParser<I> getInputParser() {
        return inputParser;
    }

    public static <I> Builder<I> builder() {
        return new Builder<>();
    }

    public static class Builder<I> {
        private String name;
        private String description;
        private Type inputType;

        public Builder<I> name(String name) {
            this.name = name;
            return this;
        }

        public Builder<I> description(String description) {
            this.description = description;
            return this;
        }

        public Builder<I> inputType(TypeReference<I> inputType) {
            this.inputType = inputType.getType();
            return this;
        }

        public Builder<I> inputType(Type inputType) {
            this.inputType = inputType;
            return this;
        }

        public Builder<I> copy(ToolDefinition<I> toolDefinition) {
            this.name = toolDefinition.getName();
            this.description = toolDefinition.getDescription();
            this.inputType = toolDefinition.getInputType();
            return this;
        }

        public ToolDefinition<I> build() {
            return new ToolDefinition<>(this);
        }
    }
}

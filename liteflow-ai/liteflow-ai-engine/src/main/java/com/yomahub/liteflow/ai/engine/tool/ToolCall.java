package com.yomahub.liteflow.ai.engine.tool;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Objects;

/**
 * 大模型回复的 ToolCall 参数
 *
 * @author 苍镜月
 * @since TODO
 */

public class ToolCall {
    private String id;
    private String type;
    private String name;
    private String arguments;

    public ToolCall(String id, String type, String name, String arguments) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.arguments = arguments;
    }

    private ToolCall(Builder builder) {
        this.id = builder.id;
        this.type = builder.type;
        this.name = builder.name;
        this.arguments = builder.arguments;
    }

    public void addArguments(String argumentsChunk) {
        arguments += argumentsChunk;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getArguments() {
        return arguments;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setArguments(String arguments) {
        this.arguments = arguments;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private String type;
        private String name;
        private String arguments;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder arguments(String arguments) {
            this.arguments = arguments;
            return this;
        }

        public Builder arguments(JsonNode argumentsJson) {
            if (Objects.nonNull(argumentsJson)) {
                this.arguments = argumentsJson.toPrettyString();
            }
            return this;
        }

        public ToolCall build() {
            return new ToolCall(this);
        }
    }

    @Override
    public String toString() {
        return "ToolCall{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", arguments='" + arguments + '\'' +
                '}';
    }
}

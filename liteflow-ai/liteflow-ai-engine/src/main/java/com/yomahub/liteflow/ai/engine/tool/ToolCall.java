package com.yomahub.liteflow.ai.engine.tool;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 大模型回复的 ToolCall 参数
 *
 * @author 苍镜月
 * @since 2.16.0
 */

public class ToolCall {
    private String id;
    private String type;
    @JsonProperty("function")
    private Function function;

    private ToolCall(Builder builder) {
        this.id = builder.id;
        this.type = builder.type;
        this.function = new Function(builder.name, builder.arguments);
    }

    public void addArguments(String argumentsChunk) {
        this.function.arguments += argumentsChunk;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return this.function.name;
    }

    public Object getArguments() {
        return this.function.arguments;
    }

    public Function getFunction() {
        return function;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        this.function.name = name;
    }

    public void setArguments(String arguments) {
        this.function.arguments = arguments;
    }

    public void setFunction(Function function) {
        this.function = function;
    }

    public static class Function {
        private String name;

        private Object arguments;

        public Function(String name, Object arguments) {
            this.name = name;
            this.arguments = arguments;
        }

        // Getters and Setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Object getArguments() {
            return arguments;
        }

        public void setArguments(Object arguments) {
            this.arguments = arguments;
        }

        @Override
        public String toString() {
            return "Function{" +
                    "name='" + name + '\'' +
                    ", arguments=" + arguments +
                    '}';
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private String type;
        private String name;
        private Object arguments;

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

        public Builder arguments(Object arguments) {
            this.arguments = arguments;
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
                ", function=" + function +
                '}';
    }
}

package com.yomahub.liteflow.ai.engine.tool.function;

import com.fasterxml.jackson.databind.JsonNode;
import com.yomahub.liteflow.ai.engine.model.output.structure.TypeReference;
import com.yomahub.liteflow.ai.engine.model.output.structure.parser.JsonSchemaParser;
import com.yomahub.liteflow.ai.engine.tool.ToolCallBack;
import com.yomahub.liteflow.ai.engine.tool.ToolDefinition;
import com.yomahub.liteflow.ai.engine.util.ObjectMapperHolder;

import java.lang.reflect.Type;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 一个通用的ToolCallBack实现，用于包装一个标准的Java Function。
 * 它负责处理输入字符串的反序列化和输出对象的序列化。
 *
 * @param <I> 工具函数的输入参数类型 (Request DTO)
 * @param <O> 工具函数的输出参数类型 (Response DTO)
 * @author 苍镜月
 * @since TODO
 */

public class FunctionToolCallback<I, O> implements ToolCallBack {

    private final ToolDefinition<I> toolDefinition;
    private final Function<I, O> function;

    public FunctionToolCallback(ToolDefinition<I> toolDefinition, Function<I, O> function) {
        this.toolDefinition = toolDefinition;
        this.function = function;
    }

    @Override
    public String getName() {
        return this.toolDefinition.getName();
    }

    @Override
    public ToolDefinition<I> getDefinition() {
        return this.toolDefinition;
    }

    @Override
    public String call(String input) {
        JsonSchemaParser<I> inputParser = toolDefinition.getInputParser();
        I request = inputParser.convert(input);
        O response = this.function.apply(request);
        return ObjectMapperHolder.writeValueAsString(response);
    }

    public static <I, O> Builder<I, O> builder(Function<I, O> function) {
        Objects.requireNonNull(function, "function cannot be null");
        return new Builder<>(function);
    }

    public static <I> Builder<I, Void> builder(Consumer<I> consumer) {
        Objects.requireNonNull(consumer, "consumer cannot be null");
        Function<I, Void> function = input -> {
            consumer.accept(input);
            return null;
        };
        return new Builder<>(function);
    }

    public static <O> Builder<Void, O> builder(Supplier<O> supplier) {
        Objects.requireNonNull(supplier, "supplier cannot be null");
        Function<Void, O> function = input -> supplier.get();
        return new Builder<>(function);
    }

    public static final class Builder<I, O> {
        private final ToolDefinition.Builder<I> toolDefinitionBuilder = ToolDefinition.builder();
        private final Function<I, O> function;

        public Builder(Function<I, O> function) {
            this.function = function;
        }

        public Builder<I, O> toolDefinition(ToolDefinition<I> toolDefinition) {
            this.toolDefinitionBuilder.copy(toolDefinition);
            return this;
        }

        public Builder<I, O> name(String name) {
            this.toolDefinitionBuilder.name(name);
            return this;
        }

        public Builder<I, O> description(String description) {
            this.toolDefinitionBuilder.description(description);
            return this;
        }

        public Builder<I, O> inputType(Type inputType) {
            this.toolDefinitionBuilder.inputType(inputType);
            return this;
        }

        public Builder<I, O> inputType(TypeReference<I> inputType) {
            this.toolDefinitionBuilder.inputType(inputType);
            return this;
        }

        public Builder<I, O> inputType(JsonNode inputSchema) {
            this.toolDefinitionBuilder.inputType(inputSchema);
            return this;
        }

        public FunctionToolCallback<I, O> build() {
            ToolDefinition<I> toolDefinition = this.toolDefinitionBuilder.build();
            return new FunctionToolCallback<>(toolDefinition, this.function);
        }
    }
}

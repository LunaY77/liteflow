package com.yomahub.liteflow.ai.engine.model.output.structure.generator;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.victools.jsonschema.generator.*;
import com.github.victools.jsonschema.module.jackson.JacksonModule;
import com.github.victools.jsonschema.module.jackson.JacksonOption;
import com.yomahub.liteflow.ai.engine.model.output.structure.Description;
import com.yomahub.liteflow.ai.engine.model.output.structure.ParameterizedTypeImpl;
import com.yomahub.liteflow.ai.engine.model.output.structure.TypeReference;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JsonSchema生成器
 *
 * @author 苍镜月
 * @since TODO
 */

public class JsonSchemaGenerator {

    private static final Character OPEN_BRACKET = '<';
    private static final Character CLOSE_BRACKET = '>';
    private static final Character COMMA = ',';
    private static final SchemaGenerator strictSchemaGenerator;
    private static final SchemaGenerator schemagenerator;

    static {
        // 配置 JSON Schema 生成器
        JacksonModule module = new JacksonModule(JacksonOption.RESPECT_JSONPROPERTY_REQUIRED);

        // 生成 strict 模式下的 generator
        SchemaGeneratorConfigBuilder strictConfigBuilder = buildJsonSchemaConfig(module);
        strictSchemaGenerator = new SchemaGenerator(strictConfigBuilder.build());

        // 生成非 strict 模式下的 generator
        SchemaGeneratorConfigBuilder configBuilder = buildJsonSchemaConfig(module);

        // strict 为 false 且 required 为 false，允许值为 null
        configBuilder.forFields().withNullableCheck(field -> {
            Description annotation = field.getAnnotation(Description.class);
            return Optional.ofNullable(annotation)
                    .map(a -> !a.required())
                    .orElse(false);
        });

        schemagenerator = new SchemaGenerator(configBuilder.build());
    }

    private static SchemaGeneratorConfigBuilder buildJsonSchemaConfig(JacksonModule module) {
        SchemaGeneratorConfigBuilder configBuilder = new SchemaGeneratorConfigBuilder(
                SchemaVersion.DRAFT_2020_12, OptionPreset.PLAIN_JSON)
                .with(module)
                .with(Option.FORBIDDEN_ADDITIONAL_PROPERTIES_BY_DEFAULT)
                .without(Option.SCHEMA_VERSION_INDICATOR);

        // 尝试解析 Description 注解的 value
        configBuilder.forFields().withDescriptionResolver(field -> {
            Description annotation = field.getAnnotation(Description.class);
            return Optional.ofNullable(annotation)
                    .map(a -> String.join("\n", a.value()))
                    .orElse(null);
        });

        // 全部属性都应当为 required
        configBuilder.forFields().withRequiredCheck(field -> true);

        return configBuilder;
    }

    /**
     * 生成指定类型的 JSON Schema (默认严格模式)
     *
     * @param typeReference 类型引用
     * @return 生成的 JSON Schema
     */
    public static JsonNode generate(TypeReference<?> typeReference) {
        return generate(typeReference.getType(), true);
    }

    /**
     * 生成指定类型的 JSON Schema (默认严格模式)
     *
     * @param typeReference 类型引用
     * @param strict        是否为严格模式
     * @return 生成的 JSON Schema
     */
    public static JsonNode generate(TypeReference<?> typeReference, boolean strict) {
        return generate(typeReference.getType(), strict);
    }

    /**
     * 生成指定类型的 JSON Schema (默认严格模式)
     *
     * @param type 类型
     * @return 生成的 JSON Schema
     */
    public static JsonNode generate(Type type) {
        return generate(type, true);
    }

    /**
     * 生成指定类型的 JSON Schema
     *
     * @param type   类型
     * @param strict 是否为严格模式
     * @return 生成的 JSON Schema
     */
    public static JsonNode generate(Type type, boolean strict) {
        if (strict) {
            return strictSchemaGenerator.generateSchema(type);
        } else {
            return schemagenerator.generateSchema(type);
        }
    }

    /**
     * 通过类全限定名生成 JSON Schema
     *
     * @param typeName 类全限定名
     * @return 生成的 JSON Schema
     */
    public static JsonNode generate(String typeName) {
        return generate(typeName, true);
    }

    /**
     * 通过类全限定名生成 JSON Schema
     *
     * @param typeName 类全限定名
     * @param strict   是否为严格模式
     * @return 生成的 JSON Schema
     */
    public static JsonNode generate(String typeName, boolean strict) {
        Type type = typeFromString(typeName);
        return generate(type, strict);
    }

    public static Type typeFromString(String typeName) {
        try {
            typeName = typeName.trim();
            int openBracket = typeName.indexOf(OPEN_BRACKET);

            if (openBracket == -1) {
                // 无泛型，直接使用类名
                return Class.forName(typeName);
            } else {
                // 有泛型，提取类名和泛型参数
                int closeBracket = findLastCloseBracket(typeName, openBracket);
                // 非闭合括号，直接报错
                if (closeBracket <= openBracket) {
                    throw new IllegalArgumentException("Invalid type format: " + typeName);
                }

                // 解析原始类型
                String rawTypeName = typeName.substring(0, openBracket);
                Class<?> rawType = Class.forName(rawTypeName);

                // 解析泛型参数
                String genericParams = typeName.substring(openBracket + 1, closeBracket);
                List<String> genericParamList = parseGenericParameters(genericParams);

                Type[] genericTypes = new Type[genericParamList.size()];
                for (int i = 0; i < genericTypes.length; i++) {
                    // 递归解析每个泛型参数
                    genericTypes[i] = typeFromString(genericParamList.get(i));
                }

                return new ParameterizedTypeImpl(rawType, genericTypes);
            }
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Class not found for type: " + typeName, e);
        }
    }

    private static int findLastCloseBracket(String str, int start) {
        for (int i = str.length() - 1; i > start; i--) {
            char c = str.charAt(i);
            if (c == CLOSE_BRACKET) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 解析泛型参数字符串
     * <p>
     * 例如：传入参数为 "String, Map<String, String>" 时，最终解析为 [String, Map<String, String>]
     *
     * @param genericParams 泛型参数字符串
     * @return 解析后的泛型参数列表
     */
    private static List<String> parseGenericParameters(String genericParams) {
        List<String> arguments = new ArrayList<>();
        // 括号平衡标识, 这个参数用于泛型嵌套解析
        int balance = 0;
        // 上一次拆分的位置
        int last = 0;

        for (int i = 0; i < genericParams.length(); i++) {
            char c = genericParams.charAt(i);
            if (c == OPEN_BRACKET) {
                // 遇到左括号，增加平衡计数
                balance++;
            } else if (c == CLOSE_BRACKET) {
                // 遇到右括号，减少平衡计数
                balance--;
            } else if (c == COMMA && balance == 0) {
                // 逗号分隔符，且括号平衡时，表示一个泛型参数结束
                arguments.add(genericParams.substring(last, i).trim());
                last = i + 1;
            }
        }
        // 处理最后一个参数
        arguments.add(genericParams.substring(last).trim());

        if (balance != 0) {
            throw new IllegalArgumentException("Mismatched brackets in generic parameters: " + genericParams);
        }

        return arguments;
    }
}

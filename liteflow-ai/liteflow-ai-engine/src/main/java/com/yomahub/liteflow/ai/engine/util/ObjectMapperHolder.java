package com.yomahub.liteflow.ai.engine.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.lang.reflect.Type;

/**
 * ObjectMapperHolder
 *
 * @author 苍镜月
 * @since TODO
 */

public class ObjectMapperHolder {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private ObjectMapperHolder() {
    }

    public static ObjectMapper getInstance() {
        return MAPPER;
    }

    public static JsonNode readTree(String content) {
        try {
            return MAPPER.readTree(content);
        } catch (Exception e) {
            throw new RuntimeException("Failed to read JSON tree from content", e);
        }
    }

    public static <T> T readValue(String content, Type type) {
        try {
            return MAPPER.readValue(content, MAPPER.constructType(type));
        } catch (Exception e) {
            throw new RuntimeException("Failed to read value from content", e);
        }
    }

    public static String writeValueAsString(Object value) {
        try {
            return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(value);
        } catch (Exception e) {
            throw new RuntimeException("Failed to write value as string", e);
        }
    }

    public static <T> T treeToValue(JsonNode node, Class<T> valueType) {
        try {
            return MAPPER.treeToValue(node, valueType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert JSON node to value", e);
        }
    }

    public static ObjectNode createObjectNode() {
        return MAPPER.createObjectNode();
    }

    public static ArrayNode createArrayNode() {
        return MAPPER.createArrayNode();
    }

    public static <T extends JsonNode> T valueToTree(Object value) {
        return MAPPER.valueToTree(value);
    }
}

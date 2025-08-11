package com.yomahub.liteflow.test.ai.engine.structure;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yomahub.liteflow.ai.engine.model.output.structure.generator.JsonSchemaGenerator;
import com.yomahub.liteflow.test.ai.engine.structure.param.Output;
import com.yomahub.liteflow.test.ai.engine.structure.param.OutputWithRequiredFalse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * JsonSchemaGeneratorTest
 *
 * @author 苍镜月
 * @since TODO
 */

public class JsonSchemaGeneratorTest {
    ObjectMapper objectMapper = new ObjectMapper();

    private String toPrettyJson(JsonNode jsonNode) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert JsonNode to pretty string", e);
        }
    }

    @Test
    public void testPrimitiveType() {
        JsonNode stringJson = JsonSchemaGenerator.generate(String.class);
        Assertions.assertEquals(
                "{\n" +
                        "  \"type\" : \"string\"\n" +
                        "}",
                toPrettyJson(stringJson)
        );

        JsonNode intJson = JsonSchemaGenerator.generate(Integer.class);
        Assertions.assertEquals(
                "{\n" +
                        "  \"type\" : \"integer\"\n" +
                        "}",
                toPrettyJson(intJson)
        );

        JsonNode booleanJson = JsonSchemaGenerator.generate(Boolean.class);
        Assertions.assertEquals(
                "{\n" +
                        "  \"type\" : \"boolean\"\n" +
                        "}",
                toPrettyJson(booleanJson)
        );

        JsonNode doubleJson = JsonSchemaGenerator.generate(Double.class);
        Assertions.assertEquals(
                "{\n" +
                        "  \"type\" : \"number\"\n" +
                        "}",
                toPrettyJson(doubleJson)
        );

        JsonNode longJson = JsonSchemaGenerator.generate(Long.class);
        Assertions.assertEquals(
                "{\n" +
                        "  \"type\" : \"integer\"\n" +
                        "}",
                toPrettyJson(longJson)
        );

        JsonNode floatJson = JsonSchemaGenerator.generate(Float.class);
        Assertions.assertEquals(
                "{\n" +
                        "  \"type\" : \"number\"\n" +
                        "}",
                toPrettyJson(floatJson)
        );

        JsonNode charJson = JsonSchemaGenerator.generate(Character.class);
        Assertions.assertEquals(
                "{\n" +
                        "  \"type\" : \"string\"\n" +
                        "}",
                toPrettyJson(charJson)
        );
    }

    @Test
    public void testListType() {
        JsonNode listStringJson = JsonSchemaGenerator.generate("java.util.List<java.lang.String>");
        Assertions.assertEquals(
                "{\n" +
                        "  \"type\" : \"array\",\n" +
                        "  \"items\" : {\n" +
                        "    \"type\" : \"string\"\n" +
                        "  }\n" +
                        "}",
                toPrettyJson(listStringJson)
        );

        JsonNode listIntJson = JsonSchemaGenerator.generate("java.util.List<java.lang.Integer>");
        Assertions.assertEquals(
                "{\n" +
                        "  \"type\" : \"array\",\n" +
                        "  \"items\" : {\n" +
                        "    \"type\" : \"integer\"\n" +
                        "  }\n" +
                        "}",
                toPrettyJson(listIntJson)
        );
    }

    @Test
    public void testOutput() {
        JsonNode strictOutputJson = JsonSchemaGenerator.generate(Output.class);
        Assertions.assertEquals(
                "{\n" +
                        "  \"type\" : \"object\",\n" +
                        "  \"properties\" : {\n" +
                        "    \"data\" : {\n" +
                        "      \"type\" : \"string\",\n" +
                        "      \"description\" : \"a data description\"\n" +
                        "    },\n" +
                        "    \"lst\" : {\n" +
                        "      \"description\" : \"a list description\",\n" +
                        "      \"type\" : \"array\",\n" +
                        "      \"items\" : {\n" +
                        "        \"type\" : \"integer\",\n" +
                        "        \"description\" : \"a list description\"\n" +
                        "      }\n" +
                        "    }\n" +
                        "  },\n" +
                        "  \"required\" : [ \"data\", \"lst\" ],\n" +
                        "  \"additionalProperties\" : false\n" +
                        "}",
                toPrettyJson(strictOutputJson)
        );

        JsonNode nonStrictOutputJson = JsonSchemaGenerator.generate(Output.class, false);
        Assertions.assertEquals(
                "{\n" +
                        "  \"type\" : \"object\",\n" +
                        "  \"properties\" : {\n" +
                        "    \"data\" : {\n" +
                        "      \"type\" : \"string\",\n" +
                        "      \"description\" : \"a data description\"\n" +
                        "    },\n" +
                        "    \"lst\" : {\n" +
                        "      \"description\" : \"a list description\",\n" +
                        "      \"type\" : \"array\",\n" +
                        "      \"items\" : {\n" +
                        "        \"type\" : \"integer\",\n" +
                        "        \"description\" : \"a list description\"\n" +
                        "      }\n" +
                        "    }\n" +
                        "  },\n" +
                        "  \"required\" : [ \"data\", \"lst\" ],\n" +
                        "  \"additionalProperties\" : false\n" +
                        "}",
                toPrettyJson(nonStrictOutputJson)
        );
    }

    @Test
    public void testListOutput() {
        JsonNode listOutputJson = JsonSchemaGenerator.generate("java.util.List<com.yomahub.liteflow.test.ai.engine.structure.param.Output>");
        Assertions.assertEquals(
                "{\n" +
                        "  \"type\" : \"array\",\n" +
                        "  \"items\" : {\n" +
                        "    \"type\" : \"object\",\n" +
                        "    \"properties\" : {\n" +
                        "      \"data\" : {\n" +
                        "        \"type\" : \"string\",\n" +
                        "        \"description\" : \"a data description\"\n" +
                        "      },\n" +
                        "      \"lst\" : {\n" +
                        "        \"description\" : \"a list description\",\n" +
                        "        \"type\" : \"array\",\n" +
                        "        \"items\" : {\n" +
                        "          \"type\" : \"integer\",\n" +
                        "          \"description\" : \"a list description\"\n" +
                        "        }\n" +
                        "      }\n" +
                        "    },\n" +
                        "    \"required\" : [ \"data\", \"lst\" ],\n" +
                        "    \"additionalProperties\" : false\n" +
                        "  }\n" +
                        "}",
                toPrettyJson(listOutputJson)
        );
    }

    @Test
    public void testOutputWithT() {
        JsonNode outputTJson = JsonSchemaGenerator.generate("com.yomahub.liteflow.test.ai.engine.structure.param.OutputWithT<java.lang.Boolean>");
        Assertions.assertEquals(
                "{\n" +
                        "  \"type\" : \"object\",\n" +
                        "  \"properties\" : {\n" +
                        "    \"data\" : {\n" +
                        "      \"type\" : \"boolean\",\n" +
                        "      \"description\" : \"a data description\"\n" +
                        "    },\n" +
                        "    \"lst\" : {\n" +
                        "      \"description\" : \"a list description\",\n" +
                        "      \"type\" : \"array\",\n" +
                        "      \"items\" : {\n" +
                        "        \"type\" : \"integer\",\n" +
                        "        \"description\" : \"a list description\"\n" +
                        "      }\n" +
                        "    }\n" +
                        "  },\n" +
                        "  \"required\" : [ \"data\", \"lst\" ],\n" +
                        "  \"additionalProperties\" : false\n" +
                        "}",
                toPrettyJson(outputTJson)
        );

        JsonNode outputTComplexJson = JsonSchemaGenerator.generate("com.yomahub.liteflow.test.ai.engine.structure.param.OutputWithT<com.yomahub.liteflow.test.ai.engine.structure.param.Output>");
        Assertions.assertEquals(
                "{\n" +
                        "  \"type\" : \"object\",\n" +
                        "  \"properties\" : {\n" +
                        "    \"data\" : {\n" +
                        "      \"type\" : \"object\",\n" +
                        "      \"properties\" : {\n" +
                        "        \"data\" : {\n" +
                        "          \"type\" : \"string\",\n" +
                        "          \"description\" : \"a data description\"\n" +
                        "        },\n" +
                        "        \"lst\" : {\n" +
                        "          \"description\" : \"a list description\",\n" +
                        "          \"type\" : \"array\",\n" +
                        "          \"items\" : {\n" +
                        "            \"type\" : \"integer\",\n" +
                        "            \"description\" : \"a list description\"\n" +
                        "          }\n" +
                        "        }\n" +
                        "      },\n" +
                        "      \"required\" : [ \"data\", \"lst\" ],\n" +
                        "      \"additionalProperties\" : false,\n" +
                        "      \"description\" : \"a data description\"\n" +
                        "    },\n" +
                        "    \"lst\" : {\n" +
                        "      \"description\" : \"a list description\",\n" +
                        "      \"type\" : \"array\",\n" +
                        "      \"items\" : {\n" +
                        "        \"type\" : \"integer\",\n" +
                        "        \"description\" : \"a list description\"\n" +
                        "      }\n" +
                        "    }\n" +
                        "  },\n" +
                        "  \"required\" : [ \"data\", \"lst\" ],\n" +
                        "  \"additionalProperties\" : false\n" +
                        "}",
                toPrettyJson(outputTComplexJson)
        );
    }

    @Test
    public void testOutputWithRequiredFalse() {
        JsonNode strictJson = JsonSchemaGenerator.generate(OutputWithRequiredFalse.class, true);
        Assertions.assertEquals(
                "{\n" +
                        "  \"type\" : \"object\",\n" +
                        "  \"properties\" : {\n" +
                        "    \"data\" : {\n" +
                        "      \"type\" : \"string\",\n" +
                        "      \"description\" : \"a data description\"\n" +
                        "    }\n" +
                        "  },\n" +
                        "  \"required\" : [ \"data\" ],\n" +
                        "  \"additionalProperties\" : false\n" +
                        "}",
                toPrettyJson(strictJson)
        );

        JsonNode nonStrictJson = JsonSchemaGenerator.generate(OutputWithRequiredFalse.class, false);
        Assertions.assertEquals(
                "{\n" +
                        "  \"type\" : \"object\",\n" +
                        "  \"properties\" : {\n" +
                        "    \"data\" : {\n" +
                        "      \"type\" : [ \"string\", \"null\" ],\n" +
                        "      \"description\" : \"a data description\"\n" +
                        "    }\n" +
                        "  },\n" +
                        "  \"required\" : [ \"data\" ],\n" +
                        "  \"additionalProperties\" : false\n" +
                        "}",
                toPrettyJson(nonStrictJson)
        );
    }
}

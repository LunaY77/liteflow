package com.yomahub.liteflow.test.ai.engine.structure;

import com.fasterxml.jackson.databind.JsonNode;
import com.yomahub.liteflow.ai.engine.model.output.structure.TypeReference;
import com.yomahub.liteflow.ai.engine.model.output.structure.generator.JsonSchemaGenerator;
import com.yomahub.liteflow.test.ai.engine.structure.param.Output;
import com.yomahub.liteflow.test.ai.engine.structure.param.OutputWithRequiredFalse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * JsonSchemaGeneratorTest
 *
 * @author 苍镜月
 */

public class JsonSchemaGeneratorTest {

    @Test
    public void testTypeReference() {
        TypeReference<Object> typeReference1 = new TypeReference<Object>("java.util.List<java.lang.String>") {
        };
        JsonNode node1 = JsonSchemaGenerator.generate(typeReference1);
        Assertions.assertEquals("java.util.List<java.lang.String>", typeReference1.getType().getTypeName());
        Assertions.assertEquals(
                "{\n" +
                        "  \"type\" : \"array\",\n" +
                        "  \"items\" : {\n" +
                        "    \"type\" : \"string\"\n" +
                        "  }\n" +
                        "}",
                node1.toPrettyString()
        );

        TypeReference<List<String>> typeReference2 = new TypeReference<List<String>>() {
        };
        JsonNode node2 = JsonSchemaGenerator.generate(typeReference2);
        Assertions.assertEquals("java.util.List<java.lang.String>", typeReference2.getType().getTypeName());
        Assertions.assertEquals(
                "{\n" +
                        "  \"type\" : \"array\",\n" +
                        "  \"items\" : {\n" +
                        "    \"type\" : \"string\"\n" +
                        "  }\n" +
                        "}",
                node2.toPrettyString()
        );
    }

    @Test
    public void testInvalidTypeReference() {
        Assertions.assertThrows(RuntimeException.class, () -> new TypeReference() {});
        Assertions.assertThrows(RuntimeException.class, () -> new TypeReference<Object>("invalid.type.name") {});
    }

    @Test
    public void testPrimitiveType() {
        JsonNode stringJson = JsonSchemaGenerator.generate(String.class);
        Assertions.assertEquals(
                "{\n" +
                        "  \"type\" : \"string\"\n" +
                        "}",
                stringJson.toPrettyString()
        );

        JsonNode intJson = JsonSchemaGenerator.generate(Integer.class);
        Assertions.assertEquals(
                "{\n" +
                        "  \"type\" : \"integer\"\n" +
                        "}",
                intJson.toPrettyString()
        );

        JsonNode booleanJson = JsonSchemaGenerator.generate(Boolean.class);
        Assertions.assertEquals(
                "{\n" +
                        "  \"type\" : \"boolean\"\n" +
                        "}",
                booleanJson.toPrettyString()
        );

        JsonNode doubleJson = JsonSchemaGenerator.generate(Double.class);
        Assertions.assertEquals(
                "{\n" +
                        "  \"type\" : \"number\"\n" +
                        "}",
                doubleJson.toPrettyString()
        );

        JsonNode longJson = JsonSchemaGenerator.generate(Long.class);
        Assertions.assertEquals(
                "{\n" +
                        "  \"type\" : \"integer\"\n" +
                        "}",
                longJson.toPrettyString()
        );

        JsonNode floatJson = JsonSchemaGenerator.generate(Float.class);
        Assertions.assertEquals(
                "{\n" +
                        "  \"type\" : \"number\"\n" +
                        "}",
                floatJson.toPrettyString()
        );

        JsonNode charJson = JsonSchemaGenerator.generate(Character.class);
        Assertions.assertEquals(
                "{\n" +
                        "  \"type\" : \"string\"\n" +
                        "}",
                charJson.toPrettyString()
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
                listStringJson.toPrettyString()
        );

        JsonNode listIntJson = JsonSchemaGenerator.generate("java.util.List<java.lang.Integer>");
        Assertions.assertEquals(
                "{\n" +
                        "  \"type\" : \"array\",\n" +
                        "  \"items\" : {\n" +
                        "    \"type\" : \"integer\"\n" +
                        "  }\n" +
                        "}",
                listIntJson.toPrettyString()
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
                strictOutputJson.toPrettyString()
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
                nonStrictOutputJson.toPrettyString()
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
                listOutputJson.toPrettyString()
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
                outputTJson.toPrettyString()
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
                outputTComplexJson.toPrettyString()
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
                strictJson.toPrettyString()
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
                nonStrictJson.toPrettyString()
        );
    }
}

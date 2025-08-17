package com.yomahub.liteflow.test.ai.engine.structure;

import com.fasterxml.jackson.databind.JsonNode;
import com.yomahub.liteflow.ai.engine.model.output.structure.parser.JsonSchemaParser;
import com.yomahub.liteflow.test.ai.engine.structure.param.Output;
import com.yomahub.liteflow.test.ai.engine.structure.param.OutputWithT;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

/**
 * OutputParserTest
 *
 * @author 苍镜月
 * @since TODO
 */

public class JsonSchemaParserTest {

    @Test
    public void testSimpleConvert() {
        JsonSchemaParser<Output> parser = new JsonSchemaParser<>(Output.class);
        String jsonInput = "{\"data\":\"test data\", \"lst\":[1, 2, 3]}";

        Output result = parser.convert(jsonInput);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("test data", result.getData());
        List<Integer> expectedList = Arrays.asList(1, 2, 3);
        Assertions.assertEquals(expectedList, result.getLst());
    }

    @Test
    public void testComplexConvert() {
        JsonSchemaParser<OutputWithT<Output>> parser = new JsonSchemaParser<>("com.yomahub.liteflow.test.ai.engine.structure.param.OutputWithT<com.yomahub.liteflow.test.ai.engine.structure.param.Output>");
        String jsonInput =
                "{\n" +
                        "  \"data\": {\n" +
                        "    \"data\": \"This is a sample string.\",\n" +
                        "    \"lst\": [10, 20, 30]\n" +
                        "  },\n" +
                        "  \"lst\": [1, 2, 3, 4, 5]\n" +
                        "}";

        OutputWithT<Output> result = parser.convert(jsonInput);
        Assertions.assertNotNull(result);

        Output expectedSubOutput = new Output();
        expectedSubOutput.setData("This is a sample string.");
        expectedSubOutput.setLst(Arrays.asList(10, 20, 30));
        Assertions.assertEquals(expectedSubOutput.getData(), result.getData().getData());
        Assertions.assertEquals(expectedSubOutput.getLst(), result.getData().getLst());

        List<Integer> expectedList = Arrays.asList(1, 2, 3, 4, 5);
        Assertions.assertEquals(expectedList, result.getLst());
    }

    @Test
    public void testConvertWithMarkdownBlock() {
        JsonSchemaParser<Output> parser = new JsonSchemaParser<>(Output.class);
        String jsonContent = "{\"data\":\"markdown data\", \"lst\":[10, 20]}";
        List<Integer> expectedList = Arrays.asList(10, 20);

        String input1 = "```json\n" + jsonContent + "\n```";
        Output result1 = parser.convert(input1);
        Assertions.assertNotNull(result1);
        Assertions.assertEquals("markdown data", result1.getData());
        Assertions.assertEquals(expectedList, result1.getLst());

        String input2 = "```\n" + jsonContent + "\n```";
        Output result2 = parser.convert(input2);
        Assertions.assertNotNull(result2);
        Assertions.assertEquals("markdown data", result2.getData());
        Assertions.assertEquals(expectedList, result2.getLst());

        String input3 = "   ```json\n   " + jsonContent + "   \n```   ";
        Output result3 = parser.convert(input3);
        Assertions.assertNotNull(result3);
        Assertions.assertEquals("markdown data", result3.getData());
        Assertions.assertEquals(expectedList, result3.getLst());

        String input4 = "```JSON" + jsonContent + "```";
        Output result4 = parser.convert(input4);
        Assertions.assertNotNull(result4);
        Assertions.assertEquals("markdown data", result4.getData());
        Assertions.assertEquals(expectedList, result4.getLst());
    }

    @Test
    @SuppressWarnings("rawtypes")
    public void testConvertWithGenericTypeString() {
        String typeName = "com.yomahub.liteflow.test.ai.engine.structure.param.OutputWithT<java.lang.String>";
        JsonSchemaParser parser = new JsonSchemaParser(typeName);
        String jsonInput = "{\"data\":\"generic string\", \"lst\":[99, 100]}";

        Object resultObj = parser.convert(jsonInput);

        Assertions.assertNotNull(resultObj);
        Assertions.assertInstanceOf(OutputWithT.class, resultObj);

        OutputWithT<?> result = (OutputWithT<?>) resultObj;
        Assertions.assertInstanceOf(String.class, result.getData());
        Assertions.assertEquals("generic string", result.getData());
        List<Integer> expectedList = Arrays.asList(99, 100);
        Assertions.assertEquals(expectedList, result.getLst());
    }

    @Test
    public void testConvertInvalidJson() {
        JsonSchemaParser<Output> parser = new JsonSchemaParser<>(Output.class);
        String invalidJsonInput = "{\"data\":\"test data, \"lst\":[1, 2, 3]}";

        Assertions.assertThrows(RuntimeException.class, () -> {
            parser.convert(invalidJsonInput);
        });
    }

    @Test
    public void testGetJsonSchema() {
        JsonSchemaParser<Output> parser = new JsonSchemaParser<>(Output.class);

        JsonNode schema = parser.getJsonSchema();

        Assertions.assertNotNull(schema);
        Assertions.assertEquals("object", schema.get("type").asText());

        JsonNode properties = schema.get("properties");
        Assertions.assertTrue(properties.has("data"));
        Assertions.assertTrue(properties.has("lst"));
        Assertions.assertEquals("string", properties.get("data").get("type").asText());
        Assertions.assertEquals("array", properties.get("lst").get("type").asText());

        JsonNode required = schema.get("required");
        Assertions.assertTrue(required.isArray());
        Assertions.assertEquals(2, required.size());
    }

    @Test
    public void testOutputInstruction() {
        JsonSchemaParser<Output> parser = new JsonSchemaParser<>(Output.class);

        Assertions.assertEquals(
                "Your response should be in JSON format.\n" +
                        "Do not include any explanations, only provide a RFC8259 compliant JSON response following this format without deviation.\n" +
                        "Do not include markdown code blocks in your response.\n" +
                        "Remove the ```json markdown from the output.\n" +
                        "Here is the JSON Schema instance your output must adhere to:\n" +
                        "```\n" +
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
                        "}\n" +
                        "```",
                parser.getOutputInstruction()
        );
    }
}

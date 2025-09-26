package com.yomahub.liteflow.test.ai.engine.tool.domain;

import com.yomahub.liteflow.ai.engine.tool.annotation.Tool;
import com.yomahub.liteflow.ai.engine.tool.annotation.ToolParam;
import com.yomahub.liteflow.ai.engine.tool.function.FunctionToolCallback;
import com.yomahub.liteflow.ai.engine.tool.registry.StaticToolRegistry;
import com.yomahub.liteflow.ai.engine.tool.registry.ToolRegistry;

import java.util.function.Function;

/**
 * 存放测试用的工具调用
 *
 * @author 苍镜月
 */

public class TestTools {

    public static final Function<ToolInput, String> QUERY_WEATHER_WITH_LOCATION =
            input -> "The weather in " + input.getContent() + " is sunny";

    public static ToolRegistry getWeatherTool() {
        FunctionToolCallback<ToolInput, String> weatherTool = FunctionToolCallback
                .builder(QUERY_WEATHER_WITH_LOCATION)
                .name("weather_function")
                .description("输入地点获取当地的天气")
                .inputType(ToolInput.class)
                .build();

        StaticToolRegistry toolRegistry = new StaticToolRegistry();
        toolRegistry.register(weatherTool);
        return toolRegistry;
    }

    @Tool(name = "weather_tool", value = {"查询天气", "获取指定位置的天气信息"})
    public String queryWeatherWithLocation(@ToolParam("location") ToolInput location) {
        return "The weather in " + location + " is sunny, 25°C.";
    }

    @Tool(name = "assemble_tool", value = {"组装工具", "将 a 和 b 组装成答案"})
    public String assemble(@ToolParam("a") String a, @ToolParam("b") String b) {
        return "Assembled result: " + a + " and " + b;
    }

    @Tool(name = "test_tool", value = {"测试工具", "不使用 ToolParam 注解，参数名应当为 arg0 和 arg1"})
    public String tesTool(String input1, String input2) {
        return "Test tool executed with inputs: " + input1 + " and " + input2;
    }

    @Tool(name = "null_tool", value = {"空工具", "不执行任何操作"})
    public void nullTool() {
        System.out.println("这是一个空工具，不执行任何操作");
    }
}

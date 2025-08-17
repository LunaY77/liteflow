package com.yomahub.liteflow.test.ai.engine.tool.domain;

import com.yomahub.liteflow.ai.engine.tool.function.FunctionToolCallback;
import com.yomahub.liteflow.ai.engine.tool.registry.StaticToolRegistry;
import com.yomahub.liteflow.ai.engine.tool.registry.ToolRegistry;

import java.util.function.Function;

/**
 * 存放测试用的工具调用
 *
 * @author 苍镜月
 * @since TODO
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
}

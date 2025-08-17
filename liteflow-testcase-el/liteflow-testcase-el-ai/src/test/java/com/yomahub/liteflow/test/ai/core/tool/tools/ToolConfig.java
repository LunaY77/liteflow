package com.yomahub.liteflow.test.ai.core.tool.tools;

import com.yomahub.liteflow.ai.engine.tool.annotation.Tool;
import com.yomahub.liteflow.ai.engine.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

/**
 * TODO
 *
 * @author 苍镜月
 * @since TODO
 */

@Component
public class ToolConfig {

    @Tool(name = "weather_tool", value = {"查询天气", "获取指定位置的天气信息"})
    public String queryWeatherWithLocation(@ToolParam("location") ToolInput location) {
        return "The weather in " + location + " is sunny, 25°C.";
    }

    @Tool(name = "assemble_tool", value = {"组装工具", "将 a 和 b 组装成答案"})
    public String assemble(@ToolParam("a") String a, @ToolParam("b") String b) {
        return "Assembled result: " + a + " and " + b;
    }
}

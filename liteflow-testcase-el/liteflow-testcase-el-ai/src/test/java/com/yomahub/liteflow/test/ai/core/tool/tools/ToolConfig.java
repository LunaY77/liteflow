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
        return "The weather in " + location.getContent() + " is sunny, 25°C.";
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

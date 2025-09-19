package com.yomahub.liteflow.test.ai.core.tool.tools;

import com.yomahub.liteflow.ai.engine.tool.annotation.ToolParam;

/**
 * tool
 *
 * @author 苍镜月
 * @since TODO
 */

public class ToolInput {

    @ToolParam("输入内容")
    private String content;

    public ToolInput() {
    }

    public ToolInput(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

package com.yomahub.liteflow.ai.engine.tool;

import java.util.List;

/**
 * TODO
 *
 * @author 苍镜月
 * @since TODO
 */

public interface ToolContinuation {

    void proceed(List<ToolResult> toolResults);

    void cancel();
}

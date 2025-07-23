package com.yomahub.liteflow.ai.component.classifier;

import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.model.output.FinishReason;
import dev.langchain4j.model.output.TokenUsage;

import java.util.List;

/**
 * 分类结果
 *
 * @author 苍镜月
 * @since TODO
 */

public class ClassifyResult {

    private List<String> classifyResults;

    private List<String> primaryCategories;

    private String text;

    private List<ToolExecutionRequest> toolExecutionRequestList;

    private String id;

    private String modelName;

    private TokenUsage tokenUsage;

    private FinishReason finishReason;


}

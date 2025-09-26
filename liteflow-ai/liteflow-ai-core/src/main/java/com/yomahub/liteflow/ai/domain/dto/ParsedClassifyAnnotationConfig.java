package com.yomahub.liteflow.ai.domain.dto;

import com.yomahub.liteflow.ai.engine.model.chat.message.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * AIClassify注解解析后配置
 *
 * @author 苍镜月
 * @since 2.16.0
 */

public class ParsedClassifyAnnotationConfig extends ParsedAnnotationConfig {

    private List<Message> history;

    private List<String> categories;

    private boolean multiLabel;

    private List<String> toolNames = new ArrayList<>();

    public List<Message> getHistory() {
        return history;
    }

    public List<String> getCategories() {
        return categories;
    }

    public boolean isMultiLabel() {
        return multiLabel;
    }

    public List<String> getToolNames() {
        return toolNames;
    }

    public void setHistory(List<Message> history) {
        this.history = history;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public void setMultiLabel(boolean multiLabel) {
        this.multiLabel = multiLabel;
    }

    public void setToolNames(List<String> toolNames) {
        this.toolNames = toolNames;
    }
}

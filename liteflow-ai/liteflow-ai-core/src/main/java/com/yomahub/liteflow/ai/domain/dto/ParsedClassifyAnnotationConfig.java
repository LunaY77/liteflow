package com.yomahub.liteflow.ai.domain.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * AIClassify注解解析后配置
 *
 * @author 苍镜月
 * @since TODO
 */

public class ParsedClassifyAnnotationConfig extends ParsedAnnotationConfig {

    private List<String> categories;

    private boolean multiLabel;

    private List<String> toolNames = new ArrayList<>();

    public List<String> getCategories() {
        return categories;
    }

    public boolean isMultiLabel() {
        return multiLabel;
    }

    public List<String> getToolNames() {
        return toolNames;
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

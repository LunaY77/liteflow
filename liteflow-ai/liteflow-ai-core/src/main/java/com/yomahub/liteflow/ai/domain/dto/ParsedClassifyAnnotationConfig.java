package com.yomahub.liteflow.ai.domain.dto;

import java.util.List;

/**
 * TODO
 *
 * @author 苍镜月
 * @since TODO
 */

public class ParsedClassifyAnnotationConfig extends ParsedAnnotationConfig {

    private List<String> categories;

    private boolean multiLabel;

    public List<String> getCategories() {
        return categories;
    }

    public boolean isMultiLabel() {
        return multiLabel;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public void setMultiLabel(boolean multiLabel) {
        this.multiLabel = multiLabel;
    }
}

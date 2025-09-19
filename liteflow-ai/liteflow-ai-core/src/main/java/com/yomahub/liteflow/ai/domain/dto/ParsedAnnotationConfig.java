package com.yomahub.liteflow.ai.domain.dto;

import com.yomahub.liteflow.ai.engine.model.output.ResponseType;

/**
 * 解析后的注解配置类
 *
 * @author 苍镜月
 * @since TODO
 */

public class ParsedAnnotationConfig {

    protected String systemPrompt;
    protected String userPrompt;
    protected ResponseType responseType = ResponseType.TEXT;
    protected String typeName = "java.lang.String";
    protected boolean strict = true;

    public String getSystemPrompt() {
        return systemPrompt;
    }

    public String getUserPrompt() {
        return userPrompt;
    }

    public ResponseType getResponseType() {
        return responseType;
    }

    public String getTypeName() {
        return typeName;
    }

    public boolean isStrict() {
        return strict;
    }

    public void setSystemPrompt(String systemPrompt) {
        this.systemPrompt = systemPrompt;
    }

    public void setUserPrompt(String userPrompt) {
        this.userPrompt = userPrompt;
    }

    public void setResponseType(ResponseType responseType) {
        this.responseType = responseType;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public void setStrict(boolean strict) {
        this.strict = strict;
    }
}

package com.yomahub.liteflow.ai.domain.enums;

import dev.langchain4j.model.chat.request.ResponseFormatType;

/**
 * AI响应类型枚举
 *
 * @author 苍镜月
 * @since TODO
 */
public enum ResponseType {
    /**
     * 纯文本响应
     */
    TEXT(ResponseFormatType.TEXT),
    
    /**
     * JSON格式响应
     */
    JSON(ResponseFormatType.JSON),
    ;
    
    private final ResponseFormatType type;
    
    ResponseType(ResponseFormatType type) {
        this.type = type;
    }
    
    /**
     * 获取对应的LangChain4j响应类型
     * 
     * @return LangChain4j响应类型
     */
    public ResponseFormatType getType() {
        return this.type;
    }
}

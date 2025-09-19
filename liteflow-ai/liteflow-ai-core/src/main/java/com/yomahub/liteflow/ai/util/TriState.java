package com.yomahub.liteflow.ai.util;

/**
 * 注解标识 Boolean 的 True, False, Unset 三种状态
 *
 * @author 苍镜月
 * @since TODO
 */

public enum TriState {
    TRUE,
    FALSE,
    UNSET,
    ;

    public Boolean toBool() {
        switch (this) {
            case TRUE:
                return true;
            case FALSE:
                return false;
            case UNSET:
            default:
                return null;
        }
    }
}

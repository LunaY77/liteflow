package com.yomahub.liteflow.ai.util.request;

/**
 * 实现该接口的类可以转换为请求头
 *
 * @author 苍镜月
 * @since TODO
 */

public interface RequestHeaderConvertible {

    /**
     * 将当前对象转换为 {@link RequestHeader} 实例。
     *
     * @return 转换后的 {@link RequestHeader} 实例
     */
    RequestHeader toRequestHeader();
}

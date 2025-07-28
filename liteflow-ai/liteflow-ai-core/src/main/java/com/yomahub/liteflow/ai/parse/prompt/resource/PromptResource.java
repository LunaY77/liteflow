package com.yomahub.liteflow.ai.parse.prompt.resource;

import java.io.IOException;
import java.io.InputStream;

/**
 * 提示词资源接口
 *
 * @author 苍镜月
 * @since TODO
 */

public interface PromptResource {

    /**
     * 获取资源的输入流
     *
     * @return 输入流
     */
    InputStream getInputStream() throws IOException;

    /**
     * 获取资源的内容
     *
     * @return 资源内容
     */
    String getContent() throws IOException;

    /**
     * 检查资源是否存在
     *
     * @return 如果资源存在则返回 true，否则返回 false
     */
    boolean exists();

    /**
     * 获取资源描述信息 (log 使用)
     *
     * @return 资源描述
     */
    String getDescription();

    /**
     * 获取资源的 URI
     *
     * @return 资源的 URI
     */
    String getURI();

    /**
     * 获取资源的前缀
     *
     * @return 资源前缀 {@link com.yomahub.liteflow.ai.domain.enums.ResourcePrefixEnum}
     */
    String getResourcePrefix();
}

package com.yomahub.liteflow.ai.parse.prompt.resource.impl;

import cn.hutool.core.util.StrUtil;
import com.yomahub.liteflow.ai.domain.enums.ResourcePrefixEnum;
import com.yomahub.liteflow.ai.parse.prompt.resource.AbstractPromptResource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * 文本
 *
 * @author 苍镜月
 * @since TODO
 */

public class TextPromptResource extends AbstractPromptResource {

    private final String content;

    public TextPromptResource(String content) {
        this.content = subPrefix(content);
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public boolean exists() {
        return StrUtil.isNotBlank(content);
    }

    @Override
    public String getDescription() {
        return "text resource [" + (content.length() > 15 ? content.substring(0, 15) + "..." : content) + "]";
    }

    @Override
    public String getURI() {
        return getResourcePrefix() + content;
    }

    @Override
    public String getResourcePrefix() {
        return ResourcePrefixEnum.TEXT_PREFIX.getPrefix();
    }

    @Override
    protected String subPrefix(String location) {
        String prefix = getResourcePrefix();

        return Optional
                .ofNullable(location)
                .filter(s -> s.startsWith(prefix))
                .map(s -> s.startsWith(prefix) ? s.substring(prefix.length()) : s)
                .orElse("");
    }
}

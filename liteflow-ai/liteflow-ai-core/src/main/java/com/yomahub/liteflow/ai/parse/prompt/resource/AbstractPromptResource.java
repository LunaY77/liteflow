package com.yomahub.liteflow.ai.parse.prompt.resource;

import cn.hutool.core.io.IoUtil;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * 抽象提示词资源实现类
 *
 * @author 苍镜月
 * @since TODO
 */

public abstract class AbstractPromptResource implements PromptResource {

    @Override
    public String getContent() throws IOException {
        try (InputStream is = getInputStream()) {
            return new String(IoUtil.readBytes(is), StandardCharsets.UTF_8);
        }
    }

    @Override
    public boolean exists() {
        try {
            getInputStream().close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 删除资源前缀
     *
     * @param location 资源位置
     * @return 去除前缀后的资源位置
     */
    protected String subPrefix(String location) {
        String prefix = getResourcePrefix();
        return Optional
                .ofNullable(location)
                .filter(s -> s.startsWith(prefix))
                .map(s -> s.substring(prefix.length()))
                .orElse("");
    }
}

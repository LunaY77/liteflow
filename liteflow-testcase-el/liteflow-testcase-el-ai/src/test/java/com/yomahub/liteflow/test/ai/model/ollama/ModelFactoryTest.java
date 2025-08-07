package com.yomahub.liteflow.test.ai.model.ollama;

import com.yomahub.liteflow.ai.model.ollama.model.chat.OllamaChatConfig;
import com.yomahub.liteflow.ai.model.ollama.model.chat.OllamaChatModel;
import com.yomahub.liteflow.log.LFLog;
import com.yomahub.liteflow.log.LFLoggerManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * TODO
 *
 * @author 苍镜月
 * @since TODO
 */

@SpringBootTest(classes = {
        ModelFactoryTest.class,
        ModelConfiguration.class,
        OllamaChatModel.class,
})
public class ModelFactoryTest {

    private static final LFLog LOG = LFLoggerManager.getLogger(ModelFactoryTest.class);

    @Resource
    @Qualifier("ollamaChatConfig")
    private OllamaChatConfig ollamaChatConfig;

    @Resource
    @Qualifier("streamingOllamaChatConfig")
    private OllamaChatConfig streamingOllamaChatConfig;
}

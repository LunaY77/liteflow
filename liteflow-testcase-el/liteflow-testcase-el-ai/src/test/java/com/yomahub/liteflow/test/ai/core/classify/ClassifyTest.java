package com.yomahub.liteflow.test.ai.core.classify;

import com.yomahub.liteflow.ai.context.ChatContext;
import com.yomahub.liteflow.ai.util.SpringUtil;
import com.yomahub.liteflow.core.FlowExecutor;
import com.yomahub.liteflow.flow.LiteflowResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;

import javax.annotation.Resource;

/**
 * TODO
 *
 * @author 苍镜月
 * @since TODO
 */

@TestPropertySource(properties = {"spring.config.location=classpath:core/classify/application.yaml"})
@SpringBootTest(classes = {ClassifyTest.class, SpringUtil.class})
@EnableAutoConfiguration
@ComponentScan({"com.yomahub.liteflow.test.ai.core.classify.cmp"})
public class ClassifyTest {

    @Resource
    private FlowExecutor flowExecutor;

    @Test
    public void testClassify() {
        LiteflowResponse response = flowExecutor.execute2Resp("chain1", null, ChatContext.class);
        Assertions.assertTrue(response.isSuccess());
        Assertions.assertEquals("a==>aiSwitch[aiSwitch]==>java", response.getExecuteStepStr());
    }

    @Test
    public void testMultiClassify() {
        LiteflowResponse response = flowExecutor.execute2Resp("chain2", null, ChatContext.class);
        Assertions.assertTrue(response.isSuccess());
        Assertions.assertEquals("a==>aiMultiSwitch[aiMultiSwitch]==>java==>python", response.getExecuteStepStr());
    }

}

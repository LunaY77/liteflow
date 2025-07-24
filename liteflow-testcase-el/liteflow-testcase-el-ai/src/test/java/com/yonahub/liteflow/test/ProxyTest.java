package com.yonahub.liteflow.test;

import com.yomahub.liteflow.ai.proxy.annotation.EnableAIComponent;
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

@TestPropertySource(value = "classpath:application.properties")
@SpringBootTest(classes = {ProxyTest.class})
@EnableAutoConfiguration
@ComponentScan({"com.yonahub.liteflow.test.cmp"})
@EnableAIComponent({"com.yonahub.liteflow.test.cmp"})
public class ProxyTest {

    @Resource
    private FlowExecutor flowExecutor;

    @Test
    public void testProxy() {
        LiteflowResponse liteflowResponse = flowExecutor.execute2Resp("chain1", "arg");
        Assertions.assertTrue(liteflowResponse.isSuccess());
    }
}

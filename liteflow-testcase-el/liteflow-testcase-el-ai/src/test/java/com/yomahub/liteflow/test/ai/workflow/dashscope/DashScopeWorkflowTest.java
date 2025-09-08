package com.yomahub.liteflow.test.ai.workflow.dashscope;

import com.yomahub.liteflow.ai.context.ChatContext;
import com.yomahub.liteflow.ai.util.SpringUtil;
import com.yomahub.liteflow.core.FlowExecutor;
import com.yomahub.liteflow.flow.LiteflowResponse;
import com.yomahub.liteflow.test.ai.workflow.dashscope.cmp.WorkFlowContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;

import javax.annotation.Resource;

@TestPropertySource(properties = {"spring.config.location=classpath:workflow/dashscope/application.yaml"})
@SpringBootTest(classes = {DashScopeWorkflowTest.class, SpringUtil.class})
@EnableAutoConfiguration
@ComponentScan({"com.yomahub.liteflow.test.ai.workflow.dashscope.cmp"})
public class DashScopeWorkflowTest {

    @Resource
    private FlowExecutor flowExecutor;

    @Test
    public void testDashScopeWorkflow() {
        LiteflowResponse response = flowExecutor.execute2Resp("chain1", null, ChatContext.class, WorkFlowContext.class);
        Assertions.assertTrue(response.isSuccess());
    }

}

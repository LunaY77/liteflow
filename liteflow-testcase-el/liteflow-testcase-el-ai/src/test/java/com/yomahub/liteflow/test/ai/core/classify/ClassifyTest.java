package com.yomahub.liteflow.test.ai.core.classify;

import com.yomahub.liteflow.ai.context.ChatContext;
import com.yomahub.liteflow.ai.domain.enums.ProviderEnum;
import com.yomahub.liteflow.core.FlowExecutor;
import com.yomahub.liteflow.flow.LiteflowResponse;
import com.yomahub.liteflow.test.ai.mock.MockAITest;
import com.yomahub.liteflow.test.ai.mock.TestDataReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;

import javax.annotation.Resource;

/**
 * 分类节点测试类
 *
 * @author 苍镜月
 */

@TestPropertySource(properties = {"spring.config.location=classpath:core/classify/application.yaml"})
@SpringBootTest(classes = {ClassifyTest.class})
@EnableAutoConfiguration
@ComponentScan({"com.yomahub.liteflow.test.ai.core.classify.cmp"})
public class ClassifyTest extends MockAITest {

    @Resource
    private FlowExecutor flowExecutor;

    @Test
    public void testOpenAIClassify() {
        setupMock(ProviderEnum.OPENAI, TestDataReader.RequestType.CLASSIFY);
        LiteflowResponse response = flowExecutor.execute2Resp("chain1", null, ChatContext.class);
        Assertions.assertTrue(response.isSuccess());
        Assertions.assertEquals("a==>openaiSwitch[openaiSwitch]==>java", response.getExecuteStepStr());
    }

    @Test
    public void testOpenAIMultiClassify() {
        setupMock(ProviderEnum.OPENAI, TestDataReader.RequestType.CLASSIFY_MULTI);
        LiteflowResponse response = flowExecutor.execute2Resp("chain2", null, ChatContext.class);
        Assertions.assertTrue(response.isSuccess());
        Assertions.assertEquals("a==>openaiMultiSwitch[openaiMultiSwitch]==>java==>python",
                response.getExecuteStepStr());
    }

    @Test
    public void testDashScopeClassify() {
        setupMock(ProviderEnum.DASHSCOPE, TestDataReader.RequestType.CLASSIFY);
        LiteflowResponse response = flowExecutor.execute2Resp("chain3", null, ChatContext.class);
        Assertions.assertTrue(response.isSuccess());
        Assertions.assertEquals("a==>dashscopeSwitch[dashscopeSwitch]==>java", response.getExecuteStepStr());
    }

    @Test
    public void testDashScopeMultiClassify() {
        setupMock(ProviderEnum.DASHSCOPE, TestDataReader.RequestType.CLASSIFY_MULTI);
        LiteflowResponse response = flowExecutor.execute2Resp("chain4", null, ChatContext.class);
        Assertions.assertTrue(response.isSuccess());
        Assertions.assertEquals("a==>dashscopeMultiSwitch[dashscopeMultiSwitch]==>java==>python",
                response.getExecuteStepStr());
    }

    @Test
    public void testOllamaClassify() {
        setupMock(ProviderEnum.OLLAMA, TestDataReader.RequestType.CLASSIFY);
        LiteflowResponse response = flowExecutor.execute2Resp("chain5", null, ChatContext.class);
        Assertions.assertTrue(response.isSuccess());
        Assertions.assertEquals("a==>ollamaSwitch[ollamaSwitch]==>java", response.getExecuteStepStr());
    }

    @Test
    public void testOllamaMultiClassify() {
        setupMock(ProviderEnum.OLLAMA, TestDataReader.RequestType.CLASSIFY_MULTI);
        LiteflowResponse response = flowExecutor.execute2Resp("chain6", null, ChatContext.class);
        Assertions.assertTrue(response.isSuccess());
        Assertions.assertEquals("a==>ollamaMultiSwitch[ollamaMultiSwitch]==>java==>python",
                response.getExecuteStepStr());
    }
}

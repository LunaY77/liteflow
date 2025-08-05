package com.yomahub.liteflow.test.ai.model.ollama;

import com.yomahub.liteflow.ai.engine.interact.transport.TransportType;
import com.yomahub.liteflow.ai.engine.model.chat.ChatModel;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatConfig;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatOptions;
import com.yomahub.liteflow.ai.engine.model.chat.entity.ChatRequest;
import com.yomahub.liteflow.ai.model.ollama.constants.OllamaConstant;
import com.yomahub.liteflow.ai.model.ollama.model.chat.OllamaChatConfig;
import com.yomahub.liteflow.ai.model.ollama.model.chat.OllamaChatModel;
import com.yomahub.liteflow.ai.model.ollama.model.chat.OllamaChatRequest;
import com.yomahub.liteflow.log.LFLog;
import com.yomahub.liteflow.log.LFLoggerManager;
import org.junit.jupiter.api.Test;
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

    @Test
    public void test() {
        ChatConfig config = OllamaChatConfig
                .builder()
                .apiUrl("http://localhost:11434/")
                .endPoint("/api/generate")
                .provider(OllamaConstant.PROVIDER_NAME)
                .model("qwen3:32b")
                .streaming(true)
                .transportType(TransportType.SSE)
                .build();

        ChatRequest request = OllamaChatRequest.builder()
                .prompt("Why sky is blue?")
                .options(ChatOptions.DEFAULT)
                .onStart(context -> LOG.info("chat start"))
                .onClose(context -> LOG.info("chat close"))
                .onText(((text, context) -> {
                    LOG.info("chat text: \n{}", text);
                    return text;
                }))
//                .onCompletion(((response, context) -> {
//                    LOG.info("chat completion: \n{}", response.getMessage().getContent());
//                    AssistantMessage modifiedMessage = new AssistantMessage(
//                            response.getMessage().getContent() + " \n(modified by onCompletion)"
//                    );
//                    response.setMessage(modifiedMessage);
//                    return response;
//                }))
                .build();

        // 请求体构建
        LOG.info("{}",
                request.toRequestBody()
                        .merge(ollamaChatConfig.toRequestBody())
        );

        System.out.println("=========================================");

        // 请求
        ChatModel chatModel = new OllamaChatModel(streamingOllamaChatConfig);
        chatModel.stream(request);

    }

    /*
   2025-07-19 18:42:52.173  INFO 71216 --- [           main] c.y.l.ai.model.ollama.ModelFactoryTest   : [ollama_chat]
    2025-07-19 18:42:52.173  INFO 71216 --- [           main] c.y.l.ai.model.ollama.ModelFactoryTest   : com.yomahub.liteflow.ai.model.ollama.interact.OllamaProtocolTransformer@1d1cbd0f
    2025-07-19 18:42:52.173  INFO 71216 --- [           main] c.y.l.ai.model.ollama.ModelFactoryTest   : {
        "options.temperature":0.8,
        "options.top_p":0.9,
        "options.top_k":50.0,
        "think":false,
        "prompt":"Why sky is blue?",
        "model":"qwen3:32b",
        "stream":false
    }
    =========================================
    2025-07-19 18:43:01.505  INFO 71216 --- [onPool-worker-1] c.y.l.ai.model.ollama.ModelFactoryTest   : chat start
    2025-07-19 18:43:37.115  INFO 71216 --- [onPool-worker-1] com.yomahub.liteflow.ai.engine.util.HttpUtil    : 正在关闭 OkHttp 客户端。
    2025-07-19 18:43:37.117  INFO 71216 --- [onPool-worker-1] com.yomahub.liteflow.ai.engine.util.HttpUtil    : OkHttp 客户端已成功关闭。
    2025-07-19 18:43:37.117  INFO 71216 --- [onPool-worker-1] c.y.l.ai.model.ollama.ModelFactoryTest   : chat completion:
    {"model":"qwen3:32b","created_at":"2025-07-19T10:43:37.097945Z","response":"The sky appears blue due to a phenomenon called **Rayleigh scattering**, which involves the way light interacts with the Earth's atmosphere.\n\n### Here's a simple explanation:\n\n1. **Sunlight is made of many colors** — each color has a different wavelength.\n2. When sunlight enters Earth's atmosphere, it collides with molecules and small particles in the air.\n3. **Blue light (shorter wavelengths)** is scattered in all directions by the gases and particles in the atmosphere **much more effectively** than other colors like red or yellow (which have longer wavelengths).\n4. This scattered blue light is what we see when we look up — hence, the **sky appears blue** during the day.\n\n### Why not violet?\nViolet light has an even shorter wavelength than blue and is scattered even more. However, our eyes are less sensitive to violet, and the sun emits less violet light compared to blue. Also, our eyes' cone cells are more responsive to blue light, so we perceive the sky as blue rather than violet.\n\n### Fun facts:\n- During sunrise or sunset, the sky appears red or orange because the sunlight has to pass through more of the Earth's atmosphere, scattering out the blue light and leaving the longer wavelengths (reds and oranges) to dominate.\n\nLet me know if you'd like a more detailed scientific explanation!","done":true,"done_reason":"stop","context":[151644,872,198,10234,12884,374,6303,30,608,2152,5854,766,151645,198,151644,77091,198,151667,271,151668,271,785,12884,7952,6303,4152,311,264,24844,2598,3070,29187,62969,71816,97219,892,17601,279,1616,3100,83161,448,279,9237,594,16566,382,14374,5692,594,264,4285,16148,1447,16,13,3070,30092,4145,374,1865,315,1657,7987,334,1959,1817,1894,702,264,2155,45306,624,17,13,3197,39020,28833,9237,594,16566,11,432,4530,3341,448,34615,323,2613,18730,304,279,3720,624,18,13,3070,10331,3100,320,8676,261,92859,32295,374,36967,304,678,17961,553,279,44512,323,18730,304,279,16566,3070,58078,803,13444,334,1091,1008,7987,1075,2518,476,13753,320,8206,614,5021,92859,4292,19,13,1096,36967,6303,3100,374,1128,582,1490,979,582,1401,705,1959,16085,11,279,3070,26684,7952,6303,334,2337,279,1899,382,14374,8429,537,79736,5267,53,30912,3100,702,458,1496,23327,45306,1091,6303,323,374,36967,1496,803,13,4354,11,1039,6414,525,2686,16216,311,79736,11,323,279,7015,72780,2686,79736,3100,7707,311,6303,13,7281,11,1039,6414,6,22161,7761,525,803,25988,311,6303,3100,11,773,582,44393,279,12884,438,6303,4751,1091,79736,382,14374,16071,13064,510,12,11954,63819,476,42984,11,279,12884,7952,2518,476,18575,1576,279,39020,702,311,1494,1526,803,315,279,9237,594,16566,11,71816,700,279,6303,3100,323,9380,279,5021,92859,320,53369,323,84038,8,311,40736,382,10061,752,1414,421,498,4172,1075,264,803,11682,12344,16148,0],"total_duration":34312626292,"load_duration":3694902292,"prompt_eval_count":21,"prompt_eval_duration":4762227958,"eval_count":270,"eval_duration":25851820750}
     */
}

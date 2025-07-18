package com.yomahub.liteflow.ai.model.ollama;

import com.yomahub.liteflow.ai.interact.protocol.ProtocolTransformer;
import com.yomahub.liteflow.ai.interact.protocol.ProtocolTransformerFactory;
import com.yomahub.liteflow.ai.model.chat.ChatModel;
import com.yomahub.liteflow.ai.model.chat.entity.ChatOptions;
import com.yomahub.liteflow.ai.model.chat.entity.ChatRequest;
import com.yomahub.liteflow.ai.model.chat.entity.ChatResponse;
import com.yomahub.liteflow.ai.model.chat.message.AssistantMessage;
import com.yomahub.liteflow.ai.model.chat.message.Message;
import com.yomahub.liteflow.ai.model.chat.message.MessageType;
import com.yomahub.liteflow.ai.model.ollama.constants.OllamaConstant;
import com.yomahub.liteflow.ai.model.ollama.model.chat.OllamaChatConfig;
import com.yomahub.liteflow.ai.model.ollama.model.chat.OllamaChatModel;
import com.yomahub.liteflow.ai.model.runtime.ModelRuntimeFactory;
import com.yomahub.liteflow.ai.model.runtime.ModelRuntimeRegistrar;
import com.yomahub.liteflow.ai.util.SpringUtil;
import com.yomahub.liteflow.log.LFLog;
import com.yomahub.liteflow.log.LFLoggerManager;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.*;

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
        ModelRuntimeRegistrar.class,
        SpringUtil.class,
})
public class ModelFactoryTest {

    private static final LFLog LOG = LFLoggerManager.getLogger(ModelFactoryTest.class);

    @Resource
    private OllamaChatConfig ollamaChatConfig;

    @Test
    public void test() {

        List<Message> lst = new ArrayList<>();
        lst.add(new Message() {
            @Override
            public MessageType getMessageType() {
                return MessageType.USER;
            }

            @Override
            public String getContent() {
                return "Why sky is blue?";
            }

            @Override
            public Map<String, Object> getMetaData() {
                return new HashMap<>();
            }
        });

        ChatRequest request = ChatRequest.builder()
                .messages(lst)
                .options(ChatOptions.DEFAULT)
                .onStart(context -> LOG.info("chat start"))
                .onClose(context -> LOG.info("chat close"))
                .onCompletion(((response, context) -> {
                    LOG.info("chat completion: \n{}", response.getMessage().getContent());
                    AssistantMessage modifiedMessage = new AssistantMessage(
                            response.getMessage().getContent() + " \n(modified by onCompletion)"
                    );
                    response.setMessage(modifiedMessage);
                    return response;
                }))
                .build();

        // 自动注册 Model
        Set<String> supportedProviders = ModelRuntimeFactory.getSupportedProviders();
        LOG.info("{}", supportedProviders);

        // 自动注册 转换器
        ProtocolTransformer transformer = ProtocolTransformerFactory.getTransformer(OllamaConstant.PROVIDER_NAME);
        LOG.info("{}", transformer);

        // 请求体构建
        LOG.info("{}",
                request.toRequestBody()
                        .merge(ollamaChatConfig.toRequestBody())
        );

        System.out.println("=========================================");

        // 请求
        ChatModel chatModel = ModelRuntimeFactory.createChatRuntime(OllamaConstant.PROVIDER_NAME, ollamaChatConfig);
        ChatResponse response = chatModel.chat(request);
    }

    /*
    2025-07-18 22:28:40.752  INFO 36312 --- [           main] c.y.l.ai.model.ollama.ModelFactoryTest   : [ollama_chat]
    2025-07-18 22:28:40.752  INFO 36312 --- [           main] c.y.l.ai.model.ollama.ModelFactoryTest   : com.yomahub.liteflow.ai.model.ollama.interact.OllamaProtocolTransformer@a62c7cd
    2025-07-18 22:28:40.752  INFO 36312 --- [           main] c.y.l.ai.model.ollama.ModelFactoryTest   : {
        "messages":[
            {
                "content":"Why sky is blue?",
                "messageType":"USER",
                "metaData":{}
            }
        ],
        "options.temperature":0.8,
        "options.top_p":0.9,
        "options.top_k":50.0,
        "think":false,
        "model":"qwen3:32b",
        "stream":false
    }
    =========================================
    2025-07-18 22:28:40.817  INFO 36312 --- [onPool-worker-1] c.y.l.ai.model.ollama.ModelFactoryTest   : chat start
    2025-07-18 22:29:17.241  INFO 36312 --- [onPool-worker-1] com.yomahub.liteflow.ai.util.HttpUtil    : 正在关闭 OkHttp 客户端。
    2025-07-18 22:29:17.241  INFO 36312 --- [onPool-worker-1] com.yomahub.liteflow.ai.util.HttpUtil    : OkHttp 客户端已成功关闭。
    2025-07-18 22:29:17.242  INFO 36312 --- [onPool-worker-1] c.y.l.ai.model.ollama.ModelFactoryTest   : chat completion:
    "{\"model\":\"qwen3:32b\",\"created_at\":\"2025-07-18T14:29:17.227484Z\",\"response\":\"The sky appears blue due to a phenomenon called **Rayleigh scattering**, which is related to how sunlight interacts with the Earth's atmosphere.\\n\\n### Here's how it works:\\n\\n1. **Sunlight is made of many colors**: Sunlight appears white, but it's actually composed of a spectrum of colors, each with a different wavelength. These colors range from violet and blue (shorter wavelengths) to yellow, orange, and red (longer wavelengths).\\n\\n2. **Earth's atmosphere scatters light**: When sunlight enters Earth's atmosphere, it collides with molecules and small particles in the air. These collisions cause the light to scatter in all directions.\\n\\n3. **Shorter wavelengths scatter more**: Blue and violet light have shorter wavelengths and are scattered much more efficiently than the longer wavelengths like red and yellow. This is due to the physics of how light interacts with the air molecules.\\n\\n4. **Why not violet?**: Although violet light is scattered even more than blue, our eyes are more sensitive to blue light, and the sun emits more blue light than violet. Also, some of the violet is absorbed by the upper atmosphere. So we perceive the sky as **blue**.\\n\\n5. **Sunset colors**: During sunrise or sunset, the sun is lower on the horizon, and the light has to pass through more of the atmosphere. This causes more scattering of the shorter blue and green wavelengths, allowing the longer red and orange wavelengths to dominate, which is why the sky appears red or orange at those times.\\n\\n### In short:\\nThe sky looks blue because the Earth's atmosphere scatters the shorter blue wavelengths of sunlight more than the other colors, and our eyes are most sensitive to blue.\\n\\nLet me know if you'd like a visual or analogy to help explain it further!\",\"done\":true,\"done_reason\":\"stop\",\"context\":[151644,872,198,10234,12884,374,6303,30,608,2152,5854,766,151645,198,151644,77091,198,151667,271,151668,271,785,12884,7952,6303,4152,311,264,24844,2598,3070,29187,62969,71816,97219,892,374,5435,311,1246,39020,83161,448,279,9237,594,16566,382,14374,5692,594,1246,432,4278,1447,16,13,3070,30092,4145,374,1865,315,1657,7987,95518,8059,4145,7952,4158,11,714,432,594,3520,23415,315,264,19745,315,7987,11,1817,448,264,2155,45306,13,4220,7987,2088,504,79736,323,6303,320,8676,261,92859,8,311,13753,11,18575,11,323,2518,320,4825,261,92859,3593,17,13,3070,43824,594,16566,1136,10175,3100,95518,3197,39020,28833,9237,594,16566,11,432,4530,3341,448,34615,323,2613,18730,304,279,3720,13,4220,47353,5240,279,3100,311,44477,304,678,17961,382,18,13,3070,12472,261,92859,44477,803,95518,8697,323,79736,3100,614,23327,92859,323,525,36967,1753,803,29720,1091,279,5021,92859,1075,2518,323,13753,13,1096,374,4152,311,279,21321,315,1246,3100,83161,448,279,3720,34615,382,19,13,3070,10234,537,79736,30,95518,10328,79736,3100,374,36967,1496,803,1091,6303,11,1039,6414,525,803,16216,311,6303,3100,11,323,279,7015,72780,803,6303,3100,1091,79736,13,7281,11,1045,315,279,79736,374,41001,553,279,8416,16566,13,2055,582,44393,279,12884,438,3070,12203,334,382,20,13,3070,30092,746,7987,95518,11954,63819,476,42984,11,279,7015,374,4722,389,279,34074,11,323,279,3100,702,311,1494,1526,803,315,279,16566,13,1096,11137,803,71816,315,279,23327,6303,323,6176,92859,11,10693,279,5021,2518,323,18575,92859,311,40736,11,892,374,3170,279,12884,7952,2518,476,18575,518,1846,3039,382,14374,758,2805,510,785,12884,5868,6303,1576,279,9237,594,16566,1136,10175,279,23327,6303,92859,315,39020,803,1091,279,1008,7987,11,323,1039,6414,525,1429,16216,311,6303,382,10061,752,1414,421,498,4172,1075,264,9124,476,55103,311,1492,10339,432,4623,0],\"total_duration\":36303248917,\"load_duration\":40503917,\"prompt_eval_count\":21,\"prompt_eval_duration\":1824295292,\"eval_count\":358,\"eval_duration\":34437921250}"
     */
}

package com.yomahub.liteflow.ai.proxy.invocation.service;

import com.yomahub.liteflow.ai.util.SetUtil;
import com.yomahub.liteflow.log.LFLog;
import com.yomahub.liteflow.log.LFLoggerManager;
import com.yomahub.liteflow.util.SerialsUtil;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.service.*;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;

import java.lang.reflect.Method;

/**
 * LangChain4j AI 服务的动态工厂类。
 * <p>
 * 这个工厂类帮助创建一个动态生成的接口，目的是将静态的泛型类型动态注入到接口方法中，从而复用LangChain4j的结构化输出的能力
 * <p>
 * <b>目标接口结构示例:</b>
 * <p>
 * 该工厂旨在创建如下结构的 {@code LiteFlowAIAssistant} 接口的实例：
 * <pre>{@code
 * public interface LiteFlowAIAssistant {
 *
 * // 以同步方式进行对话，并返回一个结构化的响应。
 * // @param userMessage 用户的输入消息。
 * // @param systemMessage 预设的系统级指令。
 * // @param <T> 响应结果的泛型类型。
 * // @return 包含模型响应结果的 Result 对象。
 * @SystemMessage("{{systemMessage}}")
 * @UserMessage("{{userMessage}}")
 * <T> Result<T> chat(@V("userMessage") String userMessage, @V("systemMessage") String systemMessage);
 *
 * // 以流式方式进行对话，逐步返回模型的响应。
 * // @param userMessage 用户的输入消息。
 * // @param systemMessage 预设的系统级指令。
 * // @return 一个 TokenStream 对象，用于处理流式响应。
 * @SystemMessage("{{systemMessage}}")
 * @UserMessage("{{userMessage}}")
 * TokenStream chatStream(@V("userMessage") String userMessage, @V("systemMessage") String systemMessage);
 *
 * }
 * }</pre>
 *
 * @author 苍镜月
 * @see dev.langchain4j.service.AiServices
 * @see dev.langchain4j.service.SystemMessage
 * @see dev.langchain4j.service.UserMessage
 * @since TODO
 */

public class AiServiceFactory {

    private static final LFLog LOG = LFLoggerManager.getLogger(AiServiceFactory.class);
    private static final String DYNAMIC_INTERFACE_PREFIX = "com.yomahub.liteflow.ai.proxy.invocation.service.DynamicLiteFlowAIAssistant_";

    /**
     * 使用 {@link AiServices} 创建一个 AI 服务实例。
     *
     * @param dynamicResultType 动态指定的返回类型
     * @param chatModel         chat 模型
     * @return 动态生成的 AI 服务实例
     */
    public static Object createAiService(Class<?> dynamicResultType, ChatModel chatModel) {
        return doCreateAiService(dynamicResultType, chatModel, null);
    }

    /**
     * 使用 {@link AiServices} 创建一个 AI 服务实例。
     *
     * @param dynamicResultType  动态指定的返回类型
     * @param streamingChatModel 流式 chat 模型
     * @return 动态生成的 AI 服务实例
     */
    public static Object createAiService(Class<?> dynamicResultType, StreamingChatModel streamingChatModel) {
        return doCreateAiService(dynamicResultType, null, streamingChatModel);
    }

    /**
     * 使用 {@link AiServices} 创建一个 AI 服务实例。
     *
     * @param dynamicResultType  动态指定的返回类型
     * @param chatModel          chat 模型
     * @param streamingChatModel 流式 chat 模型
     * @return 动态生成的 AI 服务实例
     */
    private static Object doCreateAiService(Class<?> dynamicResultType, ChatModel chatModel, StreamingChatModel streamingChatModel) {
        // 动态创建一个接口
        Class<?> dynamicInterface = createLiteFlowAIAssistant(dynamicResultType);
        LOG.info("successfully created dynamic interface for AiServices: {}", dynamicInterface.getName());

        // 创建 AiService 实例
        AiServices<?> builder = AiServices.builder(dynamicInterface);
        SetUtil.setIfPresent(builder::chatModel, chatModel);
        SetUtil.setIfPresent(builder::streamingChatModel, streamingChatModel);
        return builder.build();
    }

    /**
     * 动态创建一个接口，用于 LangChain4j 的 AI 服务
     * 使用动态代理是因为需要将静态的泛型类型动态注入
     *
     * @param dynamicResultType 动态指定的 chat 方法返回结果类型，例如 String.class
     * @return 动态生成的接口 Class 对象
     */
    private static Class<?> createLiteFlowAIAssistant(Class<?> dynamicResultType) {
        // 获取动态指定的泛型类型
        TypeDescription.Generic genericResultType = TypeDescription.Generic.Builder
                .parameterizedType(Result.class, dynamicResultType)
                .build();

        DynamicType.Builder<?> builder = new ByteBuddy()
                .makeInterface()
                .name(getDynamicInterfaceName(dynamicResultType));

        // 定义 chat 方法
        builder = defineChatMethod(builder, genericResultType);
        // 定义 chatStream 方法
        builder = defineChatStreamMethod(builder);

        DynamicType.Unloaded<?> dynamicType = builder.make();

        return dynamicType
                .load(AiServiceFactory.class.getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
                .getLoaded();
    }

    /**
     * 获取动态生成的接口名称
     *
     * @param dynamicResultType 动态指定的返回类型
     * @return 动态生成的接口名称
     */
    private static String getDynamicInterfaceName(Class<?> dynamicResultType) {
        return DYNAMIC_INTERFACE_PREFIX +
                dynamicResultType.getSimpleName() +
                "_" + SerialsUtil.generateShortUUID();
    }

    /**
     * 定义 chat 方法
     */
    private static DynamicType.Builder<?> defineChatMethod(DynamicType.Builder<?> builder, TypeDescription.Generic genericResultType) {
        return builder.defineMethod("chat", genericResultType, Visibility.PUBLIC)
                .withParameter(String.class, "userMessage")
                .annotateParameter(AnnotationDescription.Builder.ofType(V.class).define("value", "userMessage").build())
                .withParameter(String.class, "systemMessage")
                .annotateParameter(AnnotationDescription.Builder.ofType(V.class).define("value", "systemMessage").build())
                .withoutCode()
                .annotateMethod(
                        AnnotationDescription.Builder.ofType(SystemMessage.class).defineArray("value", "{{systemMessage}}").build(),
                        AnnotationDescription.Builder.ofType(UserMessage.class).defineArray("value", "{{userMessage}}").build()
                );
    }

    /**
     * 定义 chatStream 方法
     */
    private static DynamicType.Builder<?> defineChatStreamMethod(DynamicType.Builder<?> builder) {
        return builder.defineMethod("chatStream", TokenStream.class, Visibility.PUBLIC)
                .withParameter(String.class, "userMessage")
                .annotateParameter(AnnotationDescription.Builder.ofType(V.class).define("value", "userMessage").build())
                .withParameter(String.class, "systemMessage")
                .annotateParameter(AnnotationDescription.Builder.ofType(V.class).define("value", "systemMessage").build())
                .withoutCode()
                .annotateMethod(
                        AnnotationDescription.Builder.ofType(SystemMessage.class).defineArray("value", "{{systemMessage}}").build(),
                        AnnotationDescription.Builder.ofType(UserMessage.class).defineArray("value", "{{userMessage}}").build()
                );
    }

    /**
     * 调用 chat 方法
     *
     * @param aiService     AI 服务实例
     * @param userMessage   用户消息
     * @param systemMessage 系统消息
     * @return 调用结果
     */
    public static Object chat(Object aiService, String userMessage, String systemMessage) throws Throwable {
        Method chatMethod = aiService.getClass().getMethod("chat", String.class, String.class);
        return chatMethod.invoke(aiService, userMessage, systemMessage);
    }

    /**
     * 调用 chatStream 方法
     *
     * @param aiService     AI 服务实例
     * @param userMessage   用户消息
     * @param systemMessage 系统消息
     * @return TokenStream 实例
     */
    public static TokenStream chatStream(Object aiService, String userMessage, String systemMessage) throws Throwable {
        Method chatStreamMethod = aiService.getClass().getMethod("chatStream", String.class, String.class);
        return (TokenStream) chatStreamMethod.invoke(aiService, userMessage, systemMessage);
    }
}
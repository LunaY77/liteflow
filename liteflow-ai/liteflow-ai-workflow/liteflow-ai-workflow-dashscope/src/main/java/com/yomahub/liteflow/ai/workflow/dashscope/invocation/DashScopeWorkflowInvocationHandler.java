package com.yomahub.liteflow.ai.workflow.dashscope.invocation;

import cn.hutool.core.util.StrUtil;
import com.alibaba.dashscope.app.Application;
import com.alibaba.dashscope.app.ApplicationParam;
import com.alibaba.dashscope.app.ApplicationResult;
import com.alibaba.dashscope.app.RagOptions;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.utils.JsonUtils;
import com.google.gson.JsonObject;
import com.yomahub.liteflow.ai.context.ChatContext;
import com.yomahub.liteflow.ai.engine.interact.pipeline.InteractContext;
import com.yomahub.liteflow.ai.exception.LiteFlowAIException;
import com.yomahub.liteflow.ai.parse.context.ContextAccessor;
import com.yomahub.liteflow.ai.parse.context.ProcessorContext;
import com.yomahub.liteflow.ai.proxy.invocation.AbstractAIInvocationHandler;
import com.yomahub.liteflow.ai.util.KeyValue;
import com.yomahub.liteflow.ai.util.SpringUtil;
import com.yomahub.liteflow.ai.workflow.dashscope.annotation.DashScopeWorkflow;
import com.yomahub.liteflow.ai.workflow.dashscope.config.DashScopeWorkflowProperty;
import com.yomahub.liteflow.ai.workflow.dashscope.wrap.DashScopeWorkflowProxyWrapBean;
import io.reactivex.Flowable;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static com.yomahub.liteflow.ai.util.SetUtil.*;

/**
 * DashScope Workflow 调用处理器
 *
 * @author 苍镜月
 * @since 2.16.0
 */
public class DashScopeWorkflowInvocationHandler extends AbstractAIInvocationHandler<DashScopeWorkflowProxyWrapBean> {

    private static final String COMMA_SPLITTER = ",";

    public DashScopeWorkflowInvocationHandler(DashScopeWorkflowProxyWrapBean wrapBean) {
        super(wrapBean);
    }

    @Override
    protected Object doExecuteAIProcess(ProcessorContext<?> processorContext, Object[] args) {
        DashScopeWorkflow dashScopeWorkflow = wrapBean.getAnnotation();

        DashScopeWorkflowProperty property = SpringUtil.getBean(DashScopeWorkflowProperty.class);

        ApplicationParam.ApplicationParamBuilder<?, ?> builder = ApplicationParam.builder();

        builder.appId(dashScopeWorkflow.appId());
        builder.apiKey(property.getApiKey());

        setIfPresent(builder::prompt, dashScopeWorkflow.prompt(), processorContext);

        String history = dashScopeWorkflow.history();
        setIfPresent(builder::history, StrUtil.isNotBlank(history) ?
                ContextAccessor.searchContextByExpression(history, processorContext) : null);

        String messages = dashScopeWorkflow.messages();
        setIfPresent(builder::messages, StrUtil.isNotBlank(messages) ?
                ContextAccessor.searchContextByExpression(messages, processorContext) : null);

        setIfPresent(builder::sessionId, dashScopeWorkflow.sessionId());

        setIfPresent(builder::hasThoughts, dashScopeWorkflow.hasThoughts());

        setIfPresent(builder::bizParams, dashScopeWorkflow.bizParams(),
                processorContext, JsonObject.class, JsonUtils::parse);

        setIfPresent(builder::topP, dashScopeWorkflow.topP());

        setIfPresent(builder::topK, dashScopeWorkflow.topK());

        setIfPresent(builder::seed, dashScopeWorkflow.seed());

        setIfPresent(builder::temperature, dashScopeWorkflow.temperature());

        setIfPresent(builder::incrementalOutput, dashScopeWorkflow.incrementalOutput());

        setIfPresent(builder::memoryId, dashScopeWorkflow.memoryId());

        setIfPresent(builder::images, dashScopeWorkflow.images(), processorContext, List.class,
                s -> Arrays.stream(s.split(COMMA_SPLITTER))
                        .map(String::trim)
                        .collect(Collectors.toList()));

        setIfPresent(builder::ragOptions, buildRagOptions(dashScopeWorkflow.ragOptions(), processorContext));

        setIfPresent(builder::mcpServers, dashScopeWorkflow.mcpServers(), processorContext, List.class,
                s -> Arrays.stream(s.split(COMMA_SPLITTER))
                        .map(String::trim)
                        .collect(Collectors.toList()));

        setIfPresent(builder::enableWebSearch, dashScopeWorkflow.enableWebSearch());

        setIfPresent(builder::enableSystemTime, dashScopeWorkflow.enableSystemTime());

        setIfPresent(builder::enablePremium, dashScopeWorkflow.enablePremium());

        setIfPresent(builder::dialogRound, dashScopeWorkflow.dialogRound());

        setIfPresent(builder::modelId, dashScopeWorkflow.modelId());

        setIfPresent(builder::flowStreamMode, dashScopeWorkflow.flowStreamMode());

        setIfPresent(builder::enableThinking, dashScopeWorkflow.enableThinking());

        ApplicationParam applicationParam = builder.build();
        Application application = StrUtil.isNotBlank(property.getApiUrl()) ?
                new Application(property.getApiUrl()) :
                new Application();

        if (dashScopeWorkflow.stream()) {
            try {
                Flowable<ApplicationResult> res = application.streamCall(applicationParam);
                InteractContext context = new InteractContext();
                res.blockingForEach(chunk -> {
                    ChatContext chatContext = processorContext.getChatContext();
                    chatContext.getStreamHandler().onText(chunk.getOutput().getText(), context);
                    context.addText(chunk.getOutput().getText());
                });
                return null;
            } catch (NoApiKeyException | InputRequiredException e) {
                throw new LiteFlowAIException("DashScope stream call failed", e);
            }
        } else {
            try {
                return application.call(applicationParam);
            } catch (NoApiKeyException | InputRequiredException e) {
                throw new LiteFlowAIException("DashScope call failed", e);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private RagOptions buildRagOptions(com.yomahub.liteflow.ai.workflow.dashscope.annotation.RagOptions ragOptions, ProcessorContext<?> processorContext) {
        AtomicBoolean isRagOptionsSet = new AtomicBoolean(false);
        RagOptions.RagOptionsBuilder<?, ?> ragOptionsBuilder = RagOptions.builder();

        setIfPresent(ids -> {
                    ragOptionsBuilder.pipelineIds(ids);
                    isRagOptionsSet.set(true);
                }, ragOptions.pipelineIds(), processorContext, List.class,
                s -> Arrays.stream(s.split(COMMA_SPLITTER))
                        .map(String::trim)
                        .collect(Collectors.toList()));

        setIfPresent(ids -> {
                    ragOptionsBuilder.fileIds(ids);
                    isRagOptionsSet.set(true);
                }, ragOptions.fieldIds(), processorContext, List.class,
                s -> Arrays.stream(s.split(COMMA_SPLITTER))
                        .map(String::trim)
                        .collect(Collectors.toList()));

        setIfPresent(tags -> {
                    ragOptionsBuilder.tags(tags);
                    isRagOptionsSet.set(true);
                }, ragOptions.tags(), processorContext, List.class,
                s -> Arrays.stream(s.split(COMMA_SPLITTER))
                        .map(String::trim)
                        .collect(Collectors.toList()));

        setIfPresent(ids -> {
                    ragOptionsBuilder.sessionFileIds(ids);
                    isRagOptionsSet.set(true);
                }, ragOptions.sessionFileIds(), processorContext, List.class,
                s -> Arrays.stream(s.split(COMMA_SPLITTER))
                        .map(String::trim)
                        .collect(Collectors.toList()));

        if (ragOptions.metadataFilter().length > 0) {
            JsonObject metadataJson = buildJsonObjectFromKeyValue(ragOptions.metadataFilter(), processorContext);
            if (metadataJson.size() > 0) {
                ragOptionsBuilder.metadataFilter(metadataJson);
                isRagOptionsSet.set(true);
            }
        }

        if (ragOptions.structuredFilter().length > 0) {
            JsonObject structuredJson = buildJsonObjectFromKeyValue(ragOptions.structuredFilter(), processorContext);
            if (structuredJson.size() > 0) {
                ragOptionsBuilder.structuredFilter(structuredJson);
                isRagOptionsSet.set(true);
            }
        }

        return isRagOptionsSet.get() ? ragOptionsBuilder.build() : null;
    }

    /**
     * 将 KeyValue 注解数组转换为 JsonObject，并解析其中的上下文表达式
     */
    private JsonObject buildJsonObjectFromKeyValue(KeyValue[] kvs, ProcessorContext<?> context) {
        JsonObject jsonObject = new JsonObject();
        for (KeyValue kv : kvs) {
            Object resolvedValue = isTemplate(kv.value()) ?
                    resolveContextExpression(kv.value(), context) :
                    kv.value();

            if (Objects.nonNull(resolvedValue)) {
                jsonObject.addProperty(kv.key(), resolvedValue.toString());
            }
        }
        return jsonObject;
    }

    @Override
    protected void checkValidation(ProcessorContext<?> processorContext) {
        // 空实现
    }
}

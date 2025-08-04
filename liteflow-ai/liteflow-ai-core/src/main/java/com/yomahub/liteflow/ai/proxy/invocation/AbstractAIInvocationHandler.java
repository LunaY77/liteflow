package com.yomahub.liteflow.ai.proxy.invocation;

import com.yomahub.liteflow.ai.context.ChatContext;
import com.yomahub.liteflow.ai.domain.ModelConfig;
import com.yomahub.liteflow.ai.exception.LiteFlowAIException;
import com.yomahub.liteflow.ai.parse.AnnotationParser;
import com.yomahub.liteflow.ai.parse.context.ProcessorContext;
import com.yomahub.liteflow.ai.proxy.wrap.AIProxyWrapBean;
import com.yomahub.liteflow.ai.util.SetUtil;
import com.yomahub.liteflow.core.NodeComponent;
import com.yomahub.liteflow.log.LFLog;
import com.yomahub.liteflow.log.LFLoggerManager;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 抽象AI调用处理器
 *
 * @author 苍镜月
 * @since TODO
 */

public abstract class AbstractAIInvocationHandler<T extends AIProxyWrapBean<?>> implements InvocationHandler {

    protected final LFLog LOG = LFLoggerManager.getLogger(this.getClass());

    protected T wrapBean;

    public AbstractAIInvocationHandler(T wrapBean) {
        this.wrapBean = wrapBean;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        return executeAIProcess((NodeComponent) proxy, args);
    }

    /**
     * 执行AI节点处理逻辑
     *
     * @param nodeComponent AI节点组件
     * @param args
     * @return 处理结果
     */
    protected Object executeAIProcess(NodeComponent nodeComponent, Object[] args) {
        // 创建处理上下文
        ProcessorContext<T> processorContext = new ProcessorContext<>(
                wrapBean,
                nodeComponent.getContextBean(ChatContext.class),
                nodeComponent
        );

        try {
            // 注解解析前置处理
            AnnotationParser.postProcessBeforeTrigger(wrapBean.getAnnotation(), processorContext);

            // 校验参数是否符合节点要求
            checkValidation(processorContext);

            // 执行实际的AI处理逻辑
            Object result = doExecuteAIProcess(processorContext, args);

            // 注解解析后置处理
            AnnotationParser.postProcessAfterTrigger(wrapBean.getAnnotation(), processorContext, result);

            return result;
        } catch (Exception e) {
            LOG.error("Error executing AI process for node: {}", nodeComponent.getNodeId(), e);
            throw e;
        }
    }

    /**
     * 校验参数是否符合节点要求
     *
     * @param processorContext 处理器上下文
     */
    protected void checkValidation(ProcessorContext<T> processorContext) {
        // 校验Prompt
        if (SetUtil.isNotPresent(wrapBean.getUserPrompt()) && SetUtil.isNotPresent(wrapBean.getSystemPrompt())) {
            throw new LiteFlowAIException("User prompt and system prompt cannot both be empty");
        }
        // 校验必需参数
        ModelConfig modelConfig = wrapBean.getConfig();
        if (SetUtil.isNotPresent(modelConfig.getProvider())) {
            throw new LiteFlowAIException("Provider cannot be empty for AI node: " + wrapBean.getNodeId());
        }
        if (SetUtil.isNotPresent(modelConfig.getApiUrl())) {
            throw new LiteFlowAIException("API URL cannot be empty for AI node: " + wrapBean.getNodeId());
        }
        if (SetUtil.isNotPresent(modelConfig.getModel())) {
            throw new LiteFlowAIException("Model cannot be empty for AI node: " + wrapBean.getNodeId());
        }
    }

    /**
     * 执行具体的AI处理逻辑
     *
     * @param processorContext 处理上下文
     * @param args             参数
     * @return 处理结果
     */
    protected abstract Object doExecuteAIProcess(ProcessorContext<T> processorContext, Object[] args);
}

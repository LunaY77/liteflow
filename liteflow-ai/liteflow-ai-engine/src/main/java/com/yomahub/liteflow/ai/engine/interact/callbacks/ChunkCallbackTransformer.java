package com.yomahub.liteflow.ai.engine.interact.callbacks;

import com.yomahub.liteflow.ai.engine.interact.pipeline.ChunkTransformer;
import com.yomahub.liteflow.ai.engine.interact.pipeline.InteractContext;
import com.yomahub.liteflow.ai.engine.interact.protocol.StreamingProtocolChunk;
import com.yomahub.liteflow.ai.engine.tool.ToolCall;

import java.util.List;

/**
 * 流式消息处理管道的回调接口。根据块数据的类型进行具体回调
 *
 * @author 苍镜月
 * @since 2.16.0
 */

public interface ChunkCallbackTransformer extends ChunkTransformer {

    static ChunkCallbackTransformer getDefault() {
        return new ChunkCallbackTransformer() {
            @Override
            public String onText(String content, InteractContext context) {
                return content;
            }

            @Override
            public String onThinking(String content, InteractContext context) {
                return content;
            }

            @Override
            public List<ToolCall> onToolsCalling(List<ToolCall> toolCalls, InteractContext context) {
                return toolCalls;
            }

            @Override
            public Object onUsage(Object content, InteractContext context) {
                return content;
            }

            @Override
            public Object onGrounding(Object content, InteractContext context) {
                return content;
            }
        };
    }

    @SuppressWarnings("unchecked")
    default void transform(StreamingProtocolChunk transformedChunk, InteractContext context) {
        switch (transformedChunk.getType()) {
            case TEXT:
                String textContent = (String) transformedChunk.getData();
                String callbackText = this.onText(textContent, context);
                transformedChunk.setData(callbackText);
                context.addText(callbackText);
                break;
            case THINKING:
                String thinkingContent = (String) transformedChunk.getData();
                String callbackThinking = this.onThinking(thinkingContent, context);
                transformedChunk.setData(callbackThinking);
                context.addThinking(callbackThinking);
                break;
            case TOOL_CALLS:
                List<ToolCall> toolCallsContent = (List<ToolCall>) transformedChunk.getData();
                List<ToolCall> responseToolCalls = this.onToolsCalling(toolCallsContent, context);
                transformedChunk.setData(responseToolCalls);
                break;
            case USAGE:
                Object usageContent = transformedChunk.getData();
                Object responseUsage = this.onUsage(usageContent, context);
                transformedChunk.setData(responseUsage);
                break;
            case BASE64_IMAGE:
            case DATA:
            case ERROR:
                // TODO 异常处理
            default:
                // 其他类型暂不处理
                break;
        }
    }

    /**
     * 处理文本消息的回调方法。
     *
     * @param content 文本内容
     * @param context 聊天上下文，包含处理过程中的状态和信息
     */
    String onText(String content, InteractContext context);

    /**
     * 处理思考消息的回调方法。
     *
     * @param content 思考内容
     * @param context 聊天上下文，包含处理过程中的状态和信息
     */
    String onThinking(String content, InteractContext context);

    /**
     * 处理工具调用消息的回调方法。
     *
     * @param toolCalls 工具调用内容，可能是工具调用的结果或相关信息
     * @param context 聊天上下文，包含处理过程中的状态和信息
     */
    List<ToolCall> onToolsCalling(List<ToolCall> toolCalls, InteractContext context);

    /**
     * 处理 Token 统计信息的回调方法
     *
     * @param content Token 统计信息内容
     * @param context 聊天上下文，包含处理过程中的状态和信息
     */
    // TODO args
    Object onUsage(Object content, InteractContext context);

    /**
     * 处理基础信息/搜索结果的回调方法
     *
     * @param content 基础信息或搜索结果内容
     * @param context 聊天上下文，包含处理过程中的状态和信息
     */
    // TODO args
    Object onGrounding(Object content, InteractContext context);

    @Override
    default String getTransformerType() {
        return "ChunkCallback";
    }
}

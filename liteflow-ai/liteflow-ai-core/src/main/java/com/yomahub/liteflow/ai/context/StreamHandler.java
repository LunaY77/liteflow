package com.yomahub.liteflow.ai.context;

/**
 * 流式输出处理器
 *
 * @author 苍镜月
 * @since TODO
 */

public interface StreamHandler {
    // TODO streamhandler 接口定义
//
//    /**
//     * 当语言模型生成新的部分响应（通常是单个 token）时，将调用此方法。
//     *
//     * @param partialResponse 新生成的部分响应文本。
//     */
//    void onPartialResponse(String partialResponse);
//
//    /**
//     * 当使用 {@link RetrievalAugmentor} 检索到任何 {@link Content} 时，将调用此方法。
//     * <p>
//     * 此调用发生在与语言模型进行任何交互之前。
//     *
//     * @param retrievedContents 所有被检索到的内容列表。
//     */
//    void onRetrieved(List<Content> retrievedContents);
//
//    /**
//     * 当任何工具被执行后，将调用此方法。
//     * <p>
//     * 此调用发生在工具方法执行完成之后，下一个工具执行之前。
//     *
//     * @param toolExecution 包含已执行工具的名称、参数和结果的对象。
//     */
//    void onToolExecuted(ToolExecution toolExecution);
//
//    /**
//     * 当语言模型完成流式响应时，将调用此方法。
//     *
//     * @param chatResponse 完整的聊天响应结果。
//     */
//    void onCompleteResponse(ChatResponse chatResponse);
//
//    /**
//     * 当流式处理过程中发生错误时，将调用此方法。
//     *
//     * @param error 捕获到的异常或错误。
//     */
//    void onError(Throwable error);
//
//    /**
//     * 接受一个 {@link TokenStream} 对象，并注册流式处理的回调。
//     * <p>
//     * 自动开启 TokenStream 的处理流程，并在流式响应的各个阶段调用相应的方法。
//     *
//     * @param tokenStream 要处理的 {@link TokenStream} 对象。
//     */
//    default void acceptTokenStream(TokenStream tokenStream) {
//        tokenStream.onPartialResponse(this::onPartialResponse)
//                .onRetrieved(this::onRetrieved)
//                .onToolExecuted(this::onToolExecuted)
//                .onCompleteResponse(this::onCompleteResponse)
//                .onError(this::onError)
//                .start();
//    }
}

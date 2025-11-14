package com.yomahub.liteflow.ai.context;

import com.yomahub.liteflow.ai.engine.interact.chunk.ChunkEvent;
import io.reactivex.rxjava3.core.Flowable;

/**
 * 响应式流处理器
 * <p>
 * 用户通过实现此接口，直接对 ChunkEvent 流进行响应式编程，
 * 对流进行任意的变换、过滤、聚合等操作，最后返回处理后的 Flowable 流。
 *
 * @author 苍镜月
 * @since 2.16.0
 */

@FunctionalInterface
public interface StreamHandler {

    /**
     * 对 ChunkEvent 流进行响应式处理
     * <p>
     * 用户可以在此方法中对事件流进行任意的响应式操作，如：
     * - 过滤特定类型的事件
     * - 转换事件数据
     * - 聚合多个事件
     * - 添加副作用（如日志、监控）
     * - 处理错误和超时
     * <p>
     * 示例：
     * <pre>
     * StreamHandler handler = eventStream -> eventStream
     *     .filter(ChunkEvent::isChunk)
     *     .doOnNext(event -> System.out.println("Received chunk: " + event))
     *     .onErrorRetry(3);
     * </pre>
     *
     * @param eventStream 原始的 ChunkEvent 流
     * @return 处理后的 ChunkEvent 流
     */
    Flowable<ChunkEvent> handle(Flowable<ChunkEvent> eventStream);

    /**
     * 创建一个 pass-through 处理器，不做任何转换直接返回原始流
     *
     * @return pass-through StreamHandler
     */
    static StreamHandler passThrough() {
        return eventStream -> eventStream;
    }

    /**
     * 创建一个组合多个处理器的处理器
     *
     * @param handlers 处理器数组
     * @return 组合后的 StreamHandler
     */
    static StreamHandler composite(StreamHandler... handlers) {
        return eventStream -> {
            Flowable<ChunkEvent> result = eventStream;
            for (StreamHandler handler : handlers) {
                result = handler.handle(result);
            }
            return result;
        };
    }
}

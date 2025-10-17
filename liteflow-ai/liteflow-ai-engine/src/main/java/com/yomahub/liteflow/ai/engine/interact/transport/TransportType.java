package com.yomahub.liteflow.ai.engine.interact.transport;

import java.util.function.Supplier;

import com.yomahub.liteflow.ai.engine.interact.transport.impl.DnJsonTransport;
import com.yomahub.liteflow.ai.engine.interact.transport.impl.HttpTransport;
import com.yomahub.liteflow.ai.engine.interact.transport.impl.SseTransport;

/**
 * 传输方式枚举
 *
 * @author 苍镜月
 * @since 2.16.0
 */
public enum TransportType {
    HTTP(HttpTransport::new),
    SSE(SseTransport::new),
    DN_JSON(DnJsonTransport::new);

    private final Supplier<Transport> transportSupplier;

    TransportType(Supplier<Transport> transportSupplier) {
        this.transportSupplier = transportSupplier;
    }

    public Transport getTransportInstance() {
        return transportSupplier.get();
    }
}

package com.yomahub.liteflow.ai.engine.interact.transport;

import com.yomahub.liteflow.ai.engine.interact.transport.impl.DnJsonTransport;
import com.yomahub.liteflow.ai.engine.interact.transport.impl.HttpTransport;
import com.yomahub.liteflow.ai.engine.interact.transport.impl.SseTransport;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 传输方式枚举
 *
 * @author 苍镜月
 * @since 2.16.0
 */

public enum TransportType {
    HTTP(HttpTransport.class),
    SSE(SseTransport.class),
    DnJson(DnJsonTransport.class),
    // TODO custom
    CUSTOM(null),
    ;

    private final Class<? extends Transport> transportClass;

    TransportType(Class<? extends Transport> transportClass) {
        this.transportClass = transportClass;
    }

    public Class<? extends Transport> getTransportClass() {
        return transportClass;
    }

    public static Transport getTransportInstance(TransportType type) {
        try {
            return type.getTransportClass().getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create transport instance for " + type.getTransportClass().getName(), e);
        }
    }

    private static final Set<Class<? extends Transport>> TRANSPORT_CLASSES = Arrays.stream(TransportType.values())
            .map(TransportType::getTransportClass)
            .collect(Collectors.toSet());

    public static boolean contains(Class<? extends Transport> transportClass) {
        return TRANSPORT_CLASSES.contains(transportClass);
    }
}

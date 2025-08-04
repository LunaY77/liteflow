package com.yomahub.liteflow.ai.engine.log;

import com.alibaba.ttl.TransmittableThreadLocal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO
 *
 * @author 苍镜月
 * @since TODO
 */

public class EngineLogManager {

    private static final Map<String, EngineLog> logMap = new HashMap<>();

    private static final TransmittableThreadLocal<String> requestIdTL = new TransmittableThreadLocal<>();

    public static EngineLog getLogger(Class<?> clazz){
        if (logMap.containsKey(clazz.getName())){
            return logMap.get(clazz.getName());
        }else{
            Logger log = LoggerFactory.getLogger(clazz.getName());
            EngineLog lfLog = new EngineLog(log);
            logMap.put(clazz.getName(), lfLog);
            return lfLog;
        }
    }

    public static void setRequestId(String requestId){
        requestIdTL.set(requestId);
    }

    public static String getRequestId(){
        return requestIdTL.get();
    }

    public static void removeRequestId(){
        requestIdTL.remove();
    }
}

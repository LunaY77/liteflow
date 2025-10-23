package com.yomahub.liteflow.test.ai;

import com.yomahub.liteflow.core.FlowInitHook;
import com.yomahub.liteflow.flow.FlowBus;
import com.yomahub.liteflow.lifecycle.LifeCycleHolder;
import com.yomahub.liteflow.property.LiteflowConfigGetter;
import com.yomahub.liteflow.spi.holder.SpiFactoryInitializing;
import com.yomahub.liteflow.spring.ComponentScanner;
import com.yomahub.liteflow.thread.ExecutorHelper;
import org.junit.jupiter.api.AfterAll;

/**
 * 测试基类
 *
 * @author 苍镜月
 * @since 2.16.0
 */

public class BaseTest {

    @AfterAll
    public static void afterAll() {
        ComponentScanner.cleanCache();
        FlowBus.cleanCache();
        ExecutorHelper.loadInstance().clearExecutorServiceMap();
        SpiFactoryInitializing.clean();
        LiteflowConfigGetter.clean();
        FlowInitHook.cleanHook();
        FlowBus.clearStat();
        LifeCycleHolder.clean();
    }
}

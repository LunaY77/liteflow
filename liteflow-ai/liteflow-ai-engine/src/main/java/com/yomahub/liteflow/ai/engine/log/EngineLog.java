package com.yomahub.liteflow.ai.engine.log;

import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.Marker;

/**
 * engine 模块日志
 *
 * @author 苍镜月
 * @since TODO
 */

public class EngineLog implements Logger {

    private final Logger log;

    public EngineLog(Logger log) {
        this.log = log;
    }

    /**
     * 获取请求ID，用于日志追踪
     */
    private String getRId() {
        String requestId = EngineLogManager.getRequestId();
        if (StrUtil.isBlank(requestId)) {
            return StrUtil.EMPTY;
        } else {
            return StrUtil.format("[{}]: ", requestId);
        }
    }

    @Override
    public String getName() {
        return this.log.getName();
    }

    @Override
    public boolean isTraceEnabled() {
        return this.log.isTraceEnabled();
    }

    @Override
    public void trace(String s) {
        this.log.trace(getRId() + s);
    }

    @Override
    public void trace(String s, Object o) {
        this.log.trace(getRId() + s, o);
    }

    @Override
    public void trace(String s, Object o, Object o1) {
        this.log.trace(getRId() + s, o, o1);
    }

    @Override
    public void trace(String s, Object... objects) {
        this.log.trace(getRId() + s, objects);
    }

    @Override
    public void trace(String s, Throwable throwable) {
        this.log.trace(getRId() + s, throwable);
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return this.log.isTraceEnabled(marker);
    }

    @Override
    public void trace(Marker marker, String s) {
        this.log.trace(marker, getRId() + s);
    }

    @Override
    public void trace(Marker marker, String s, Object o) {
        this.log.trace(marker, getRId() + s, o);
    }

    @Override
    public void trace(Marker marker, String s, Object o, Object o1) {
        this.log.trace(marker, getRId() + s, o, o1);
    }

    @Override
    public void trace(Marker marker, String s, Object... objects) {
        this.log.trace(marker, getRId() + s, objects);
    }

    @Override
    public void trace(Marker marker, String s, Throwable throwable) {
        this.log.trace(marker, getRId() + s, throwable);
    }

    @Override
    public boolean isDebugEnabled() {
        return this.log.isDebugEnabled();
    }

    @Override
    public void debug(String s) {
        this.log.debug(getRId() + s);
    }

    @Override
    public void debug(String s, Object o) {
        this.log.debug(getRId() + s, o);
    }

    @Override
    public void debug(String s, Object o, Object o1) {
        this.log.debug(getRId() + s, o, o1);
    }

    @Override
    public void debug(String s, Object... objects) {
        this.log.debug(getRId() + s, objects);
    }

    @Override
    public void debug(String s, Throwable throwable) {
        this.log.debug(getRId() + s, throwable);
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return this.log.isDebugEnabled(marker);
    }

    @Override
    public void debug(Marker marker, String s) {
        this.log.debug(marker, getRId() + s);
    }

    @Override
    public void debug(Marker marker, String s, Object o) {
        this.log.debug(marker, getRId() + s, o);
    }

    @Override
    public void debug(Marker marker, String s, Object o, Object o1) {
        this.log.debug(marker, getRId() + s, o, o1);
    }

    @Override
    public void debug(Marker marker, String s, Object... objects) {
        this.log.debug(marker, getRId() + s, objects);
    }

    @Override
    public void debug(Marker marker, String s, Throwable throwable) {
        this.log.debug(marker, getRId() + s, throwable);
    }

    @Override
    public boolean isInfoEnabled() {
        return this.log.isInfoEnabled();
    }

    @Override
    public void info(String s) {
            this.log.info(getRId() + s);
    }

    @Override
    public void info(String s, Object o) {
        this.log.info(getRId() + s, o);
    }

    @Override
    public void info(String s, Object o, Object o1) {
        this.log.info(getRId() + s, o, o1);
    }

    @Override
    public void info(String s, Object... objects) {
        this.log.info(getRId() + s, objects);
    }

    @Override
    public void info(String s, Throwable throwable) {
        this.log.info(getRId() + s, throwable);
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return this.log.isInfoEnabled(marker);
    }

    @Override
    public void info(Marker marker, String s) {
        this.log.info(marker, getRId() + s);
    }

    @Override
    public void info(Marker marker, String s, Object o) {
        this.log.info(marker, getRId() + s, o);
    }

    @Override
    public void info(Marker marker, String s, Object o, Object o1) {
        this.log.info(marker, getRId() + s, o, o1);
    }

    @Override
    public void info(Marker marker, String s, Object... objects) {
        this.log.info(marker, getRId() + s , objects);
    }

    @Override
    public void info(Marker marker, String s, Throwable throwable) {
        this.log.info(marker, getRId() + s ,throwable);
    }

    @Override
    public boolean isWarnEnabled() {
        return this.log.isWarnEnabled();
    }

    @Override
    public void warn(String s) {
        this.log.warn(getRId() + s);
    }

    @Override
    public void warn(String s, Object o) {
        this.log.warn(getRId() + s, o);
    }

    @Override
    public void warn(String s, Object... objects) {
        this.log.warn(getRId() + s, objects);
    }

    @Override
    public void warn(String s, Object o, Object o1) {
        this.log.warn(getRId() + s, o, o1);
    }

    @Override
    public void warn(String s, Throwable throwable) {
        this.log.warn(getRId() + s, throwable);
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return this.log.isWarnEnabled(marker);
    }

    @Override
    public void warn(Marker marker, String s) {
        this.log.warn(marker, getRId() + s);
    }

    @Override
    public void warn(Marker marker, String s, Object o) {
        this.log.warn(marker, getRId() + s, o);
    }

    @Override
    public void warn(Marker marker, String s, Object o, Object o1) {
        this.log.warn(marker, getRId() + s, o, o1);
    }

    @Override
    public void warn(Marker marker, String s, Object... objects) {
        this.log.warn(marker, getRId() + s, objects);
    }

    @Override
    public void warn(Marker marker, String s, Throwable throwable) {
        this.log.warn(marker, getRId() + s, throwable);
    }

    @Override
    public boolean isErrorEnabled() {
        return this.log.isErrorEnabled();
    }

    @Override
    public void error(String s) {
        this.log.error(getRId() + s);
    }

    @Override
    public void error(String s, Object o) {
        this.log.error(getRId() + s, o);
    }

    @Override
    public void error(String s, Object o, Object o1) {
        this.log.error(getRId() + s, o, o1);
    }

    @Override
    public void error(String s, Object... objects) {
        this.log.error(getRId() + s, objects);
    }

    @Override
    public void error(String s, Throwable throwable) {
        this.log.error(getRId() + s, throwable);
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return this.log.isErrorEnabled(marker);
    }

    @Override
    public void error(Marker marker, String s) {
        this.log.error(marker, getRId() + s);
    }

    @Override
    public void error(Marker marker, String s, Object o) {
        this.log.error(marker, getRId() + s, o);
    }

    @Override
    public void error(Marker marker, String s, Object o, Object o1) {
        this.log.error(marker, getRId() + s, o, o1);
    }

    @Override
    public void error(Marker marker, String s, Object... objects) {
        this.log.error(marker, getRId() + s, objects);
    }

    @Override
    public void error(Marker marker, String s, Throwable throwable) {
        this.log.error(marker, getRId() + s, throwable);
    }
}

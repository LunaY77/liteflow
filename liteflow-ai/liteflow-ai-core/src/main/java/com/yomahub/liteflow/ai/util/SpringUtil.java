package com.yomahub.liteflow.ai.util;

import cn.hutool.core.lang.TypeReference;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;

/**
 * Spring工具类
 *
 * @author 苍镜月
 * @since TODO
 */

@SuppressWarnings({"unchecked", "rawtypes"})
@Component
public class SpringUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringUtil.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static <T> T getBean(String name) {
        return (T) applicationContext.getBean(name);
    }

    public static <T> T getBean(String name, Object... args) {
        return (T) applicationContext.getBean(name, args);
    }

    public static <T> T getBean(Class<T> clazz) {
        return (T) applicationContext.getBean(clazz);
    }

    public static <T> T getBean(Class<T> clazz, Object... args) {
        return (T) applicationContext.getBean(clazz, args);
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        return (T) applicationContext.getBean(name, clazz);
    }

    public static <T> T getBean(TypeReference<T> reference) {
        ParameterizedType parameterizedType = (ParameterizedType) reference.getType();
        Class<T> rawType = (Class) parameterizedType.getRawType();
        Class<?>[] genericTypes = (Class[]) Arrays.stream(parameterizedType.getActualTypeArguments()).map((type) -> (Class) type).toArray(Class[]::new);
        String[] beanNames = applicationContext.getBeanNamesForType(ResolvableType.forClassWithGenerics(rawType, genericTypes));
        return getBean(beanNames[0], rawType);
    }

    public static String[] getBeanNamesForAnnotation(Class<? extends Annotation> annotationType) {
        return applicationContext.getBeanNamesForAnnotation(annotationType);
    }

    public static Class<?> getType(String name) {
        return applicationContext.getType(name);
    }

}

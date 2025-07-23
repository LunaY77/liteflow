package com.yomahub.liteflow.ai.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * AI检索组件注解
 *
 * @author 苍镜月
 * @since TODO
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AIRetrieval {
    
    /**
     * 检索模型名称
     */
    String model() default "";
    
    /**
     * 向量数据库配置
     */
    String vectorDatabase() default "";
    
    /**
     * 检索数量
     */
    int topK() default 5;
    
    /**
     * 相似度阈值
     */
    double similarityThreshold() default 0.7;
    
    /**
     * 索引名称
     */
    String indexName() default "";
}

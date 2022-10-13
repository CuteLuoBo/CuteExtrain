package com.github.cuteluobo.common.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 配置文件字段
 *
 * @author CuteLuoBo
 * @time 2022-10-11
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigField {
    /**
     * @return 配置注解
     */
    String description() default "";

    /**
     * @return 配置字段名
     */
    String label() default "";

    /**
     * @return 配置字段值
     */
    String value() default "";

    /**
     * @return 是否启用
     */
    boolean enable() default true;
}

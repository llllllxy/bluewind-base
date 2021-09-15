package com.bluewind.base.common.annotation;

import java.lang.annotation.*;

/**
 * @author liuxingyu01
 * @date 2021-09-15-11:40
 * @description 数据源注解，用于多数据源情况下的数据源切换
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSource {
    String value() default "masterDataSource";
}

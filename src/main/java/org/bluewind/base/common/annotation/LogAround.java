package org.bluewind.base.common.annotation;

import java.lang.annotation.*;

/**
 * @author liuxingyu01
 * @date 2021-09-13-13:50
 * @Description: 日志注解：用于输出方法的进入和结束日志
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAround {
    String value() default "";
}

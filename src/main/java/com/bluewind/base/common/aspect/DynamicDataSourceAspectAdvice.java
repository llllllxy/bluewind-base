package com.bluewind.base.common.aspect;

import com.bluewind.base.common.annotation.DataSource;
import com.bluewind.base.common.config.datasource.DynamicDataSource;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author liuxingyu01
 * @date 2021-09-15-11:42
 * @description 动态数据源切面
 **/
@Component
@Aspect
public class DynamicDataSourceAspectAdvice {

    @Around(value = "anyMethod() && @annotation(dataSource)")
    public Object setDataSource(ProceedingJoinPoint jp, DataSource dataSource) throws Throwable {
        String ds = dataSource.value();
        if (ds != null && !ds.equals("")) {
            DynamicDataSource.setDataSource(ds);
        }
        Object o = null;
        try {
            o = jp.proceed();
        } catch (Throwable t) {
            throw t;
        } finally {
            // 用完清除数据源
            DynamicDataSource.clearDataSource();
        }

        return o;
    }

    @Pointcut("execution(* *(..))")
    public void anyMethod() {

    }

    @Pointcut("execution(public * *(..))")
    public void anyPublicMethod() {

    }
}

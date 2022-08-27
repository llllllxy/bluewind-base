package com.bluewind.base.common.config;

import com.bluewind.base.common.base.Result;
import com.bluewind.base.common.consts.HttpStatus;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author liuxingyu01
 * @date 2022-08-27 12:38
 * @description 全局异常处理
 **/
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(RuntimeException.class)
    public Result handleRuntimeException(Exception e) {
        logger.error("GlobalExceptionHandler -- RuntimeException = {e}", e);
        return Result.create(HttpStatus.ERROR, e.getMessage());
    }


    // shiro缺少权限异常
    @ExceptionHandler(value = AuthorizationException.class)
    public Result handleAuthorizationException() {
        return Result.create(HttpStatus.FORBIDDEN, "接口无权限，请联系系统管理员", null);
    }


    // shiro未登陆异常（此异常无法捕捉到，所以在AuthClientAuthenticationFilter.onAccessDenied里做了处理）
    @ExceptionHandler(value = AuthenticationException.class)
    public Result handleAuthenticationException() {
        return Result.create(HttpStatus.UNAUTHORIZED, "会话已失效，请重新登录", null);
    }
}

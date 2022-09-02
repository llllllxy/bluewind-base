package com.bluewind.base.common.config.auth.filter;

import com.bluewind.base.common.base.Result;
import com.bluewind.base.common.config.auth.constant.AuthConstant;
import com.bluewind.base.common.config.auth.util.AuthUtil;
import com.bluewind.base.common.consts.HttpStatus;
import com.bluewind.base.common.util.redis.RedisUtils;
import com.bluewind.base.common.util.spring.SpringContextUtil;
import com.bluewind.base.common.util.web.CookieUtils;
import com.bluewind.base.common.util.web.ResponseUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * @author liuxingyu01
 * @date 2022-08-26 14:33
 * @description 自定义会话过滤器（支持redis会话共享）
 **/
public class AuthClientAuthenticationFilter extends AuthenticationFilter {
    private static final Logger logger = LoggerFactory.getLogger(AuthClientAuthenticationFilter.class);

    private static RedisUtils redisUtils;

    private static RedisUtils getRedisUtils() {
        if (redisUtils == null) {
            Object bean = SpringContextUtil.getBean("redisUtils");
            if (bean == null) {
                logger.error("redisUtils bean is null!");
            }
            redisUtils = (RedisUtils) bean;
        }
        return redisUtils;
    }


    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        return this.validateClient(request, response);
    }


    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) {
        ResponseUtil.sendJson(response, Result.create(HttpStatus.UNAUTHORIZED, "会话已失效，请重新登录"));
        return false;
    }


    /**
     * 检查登录状态
     *
     * @param request ServletRequest
     * @return true or false
     */
    private boolean validateClient(ServletRequest request, ServletResponse response) {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        String token = httpServletRequest.getHeader(AuthConstant.BLUEWIND_TOKEN_KEY);
        if (StringUtils.isNotBlank(token)) {
            return this.isLogin(request, response, token);
        } else {
            String code = CookieUtils.getCookie(httpServletRequest, AuthConstant.BLUEWIND_COOKIE_KEY);
            if (StringUtils.isNotBlank(code)) {
                return this.isLogin(request, response, code);
            }
        }
        return false;
    }


    /**
     * 判断是否登录
     * @param request ServletRequest
     * @param response ServletResponse
     * @param token String
     * @return true or false
     */
    private boolean isLogin(ServletRequest request, ServletResponse response, String token) {
        if (StringUtils.isNotBlank(token)) {
            if (StringUtils.isNotBlank(getRedisUtils().getStr(AuthConstant.BLUEWIND_TOKEN_CACHE + ":" + token))) {
                // client无密认证
                Subject subject = this.getSubject(request, response);
                subject.login(new UsernamePasswordToken(getRedisUtils().getStr(AuthConstant.BLUEWIND_TOKEN_CACHE + ":" + token), ""));
                // 刷新redis缓存，以刷新会话
                getRedisUtils().expire(AuthConstant.BLUEWIND_TOKEN_CACHE + ":" + token, AuthUtil.getSessionsTime());
                return true;
            }
        }
        return false;
    }
}

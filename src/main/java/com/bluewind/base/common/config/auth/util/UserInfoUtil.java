package com.bluewind.base.common.config.auth.util;

import com.bluewind.base.common.config.auth.constant.AuthConstant;
import com.bluewind.base.common.util.redis.RedisUtils;
import com.bluewind.base.common.util.spring.SpringContextUtil;
import com.bluewind.base.common.util.web.CookieUtils;
import com.bluewind.base.common.util.web.ServletUtils;
import com.bluewind.base.module.system.auth.entity.UserInfo;
import com.bluewind.base.module.system.auth.service.AuthService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @author liuxingyu01
 * @date 2022-08-27 13:42
 * @description 用户会话工具类
 **/
public class UserInfoUtil {
    private static final Logger logger = LoggerFactory.getLogger(UserInfoUtil.class);


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


    private static AuthService authService;

    private static AuthService getAuthService() {
        if (authService == null) {
            AuthService bean = SpringContextUtil.getBean(AuthService.class);
            if (bean == null) {
                logger.error("FinanceConvertUtilsService bean is null");
            }
            authService = bean;
            return authService;
        }
        return authService;
    }


    /**
     * 获取当前登录用户的token会话串
     *
     * @return String
     */
    public static String getToken() {
        HttpServletRequest httpServletRequest = ServletUtils.getRequest();
        if (Objects.isNull(httpServletRequest)) {
            return null;
        }
        String token = httpServletRequest.getHeader(AuthConstant.BLUEWIND_TOKEN_KEY);
        if (StringUtils.isBlank(token)) {
            token = CookieUtils.getCookie(httpServletRequest, AuthConstant.BLUEWIND_COOKIE_KEY);
        }
        return token;
    }


    /**
     * 获取指定HttpServletRequest的token会话串
     *
     * @return String
     */
    public static String getToken(HttpServletRequest httpServletRequest) {
        if (Objects.isNull(httpServletRequest)) {
            return null;
        }
        String token = httpServletRequest.getHeader(AuthConstant.BLUEWIND_TOKEN_KEY);
        if (StringUtils.isBlank(token)) {
            token = CookieUtils.getCookie(httpServletRequest, AuthConstant.BLUEWIND_COOKIE_KEY);
        }
        return token;
    }


    /**
     * 获取当前登录用户账户
     *
     * @return String
     */
    public static String getUserName() {
        Subject subject = SecurityUtils.getSubject();
        return (String) subject.getPrincipal();
    }

    /**
     * 获取当前用户的userInfo
     *
     * @return UcUserDTO
     */
    public static UserInfo getUserInfo() {
        String username = getUserName();
        // 先从缓存中取，缓存中取不到再去数据库取
        String redisKey = AuthConstant.BLUEWIND_USERINFO_CACHE + ":" + username;
        UserInfo userInfo = (UserInfo) getRedisUtils().get(redisKey);
        if (Objects.isNull(userInfo)) {
            userInfo = getAuthService().getUserInfo(username);
            getRedisUtils().set(redisKey, userInfo, AuthUtil.getSessionsTime());
        }
        return userInfo;
    }


    /**
     * 获取用户编码
     *
     * @return String
     */
    public static long getUserId() {
        UserInfo userInfo = getUserInfo();
        return userInfo.getUserId();
    }

}

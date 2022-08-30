package com.bluewind.base.common.config.auth.constant;

/**
 * @author liuxingyu01
 * @date 2022-08-26 12:42
 * @description 会话常量
 **/
public class AuthConstant {

    // 登录用户token-key
    public static final String BLUEWIND_TOKEN_KEY = "token";


    // 用户会话redis的key
    public final static String BLUEWIND_TOKEN_CACHE = "bluewind:token:cache";

    // 用户会话key
    public final static String BLUEWIND_COOKIE_KEY = "bluewind-session-key";

    // 密码的盐
    public final static String SALT = "ymp8R3Vg7Kv5$y5fM3*xl&ins7SZcTEY";

    // session编码
    public final static String BLUEWIND_SSO_SHIRO_SESSION_ID = "bluewind-sso-shiro-session-id";

    // 用户会话key（LAMBO_SSO_COOKIE_KEY）的redis的key
    public final static String BLUEWIND_SSO_CODE = "bluewind-sso-code";

    // session编码集合
    public final static String BLUEWIND_SSO_SESSION_IDS = "bluewind-sso-session-ids";

    // 登陆会话数
    public final static String BLUEWIND_USER_SESSION_NUMS = "bluewind:user-session-nums";

    // 尝试登陆次数
    public final static String BLUEWIND_LOGIN_ATTEMPT_TIMES = "bluewind:login-attempt-times";

    // 缓存用户会话信息
    public final static String BLUEWIND_USERINFO_CACHE = "bluewind:userinfo:cache";

    // 缓存权限信息
    public final static String BLUEWIND_PERMISSIONS_CACHE = "bluewind:permissions:cache";

    // 缓存角色信息
    public final static String BLUEWIND_ROLES_CACHE = "bluewind:roles:cache";

}

package com.bluewind.base.common.config.auth.constant;

/**
 * @author liuxingyu01
 * @date 2022-08-26 12:42
 * @description
 **/
public class AuthConstant {

    // 用户会话redis的key
    public final static String LAMBO_SSO_CODE_USERNAME = "lambo-sso-code-username";

    // 用户会话key
    public final static String LAMBO_SSO_COOKIE_KEY = "lambo-sso-key";

    // 密码的盐
    public final static String SALT = "ymp8R3Vg7Kv5$y5fM3*xl&ins7SZcTEY";

    // session编码
    public final static String LAMBO_SSO_SHIRO_SESSION_ID = "lambo-sso-shiro-session-id";

    // 用户会话key（LAMBO_SSO_COOKIE_KEY）的redis的key
    public final static String LAMBO_SSO_CODE = "lambo-sso-code";

    // session编码集合
    public final static String LAMBO_SSO_SESSION_IDS = "lambo-sso-session-ids";

    // 登陆会话数
    public final static String LAMBO_SSO_USER_LOGIN_SESSION_NUM = "lambo-sso-user-login-session-num";

    // 尝试登陆次数
    public final static String LAMBO_SSO_LOGIN_NUMBER = "lambo-sso-user-login-num";
}

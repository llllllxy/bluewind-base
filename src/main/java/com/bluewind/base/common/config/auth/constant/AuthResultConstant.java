package com.bluewind.base.common.config.auth.constant;

/**
 * @author liuxingyu01
 * @date 2022-08-27 9:43
 * @description 登录结果常量
 **/
public class AuthResultConstant {

    // 用户不存在
    public final static int USER_NOT_EXIST = 10000;

    // 用户名密码不匹配
    public final static int USERNAME_AND_PASSWORD_NOT_MATCH = 10001;

    // 用户失效
    public final static int USER_IS_INVALID = 10002;

    // 用户被锁定
    public final static int USER_IS_LOCKED = 10003;

    // 超过最大会话数量
    public final static int EXCEED_SESSIONS_MAXNUM = 10004;

    // 登录验证成功
    public final static int MATCH_SUCCESS = 10005;
}

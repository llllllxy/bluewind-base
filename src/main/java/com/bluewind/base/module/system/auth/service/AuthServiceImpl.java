package com.bluewind.base.module.system.auth.service;

import com.bluewind.base.common.config.auth.constant.AuthConstant;
import com.bluewind.base.common.config.auth.constant.AuthResultConstant;
import com.bluewind.base.common.config.auth.util.AuthUtil;
import com.bluewind.base.common.util.redis.RedisUtils;
import com.bluewind.base.module.system.auth.entity.UserInfo;
import com.bluewind.base.module.system.auth.mapper.AuthMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author liuxingyu01
 * @date 2022-08-26 15:07
 * @description
 **/
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthMapper authMapper;

    @Autowired
    private RedisUtils redisUtils;


    /**
     * 校验用户名密码等会话信息
     * @param username 用户名
     * @param password 密码
     * @return AuthResultConstant
     */
    @Override
    public int authentication(String username, String password) {
        // 是否超过最大尝试次数
        String key = AuthConstant.LAMBO_SSO_LOGIN_NUMBER + ":" + username;
        String value = redisUtils.getStr(key);
        int num = 0;
        if (StringUtils.isNotBlank(value)) {
            num = Integer.parseInt(value);
        }
        if (num > AuthUtil.getLoginMaxNum()) { // 超过最大尝试次数，锁定一段时间
            return AuthResultConstant.USER_IS_LOCKED;
        }

        // 是否超过最大会话数
        key = AuthConstant.LAMBO_SSO_USER_LOGIN_SESSION_NUM + ":" + username;
        value = redisUtils.getStr(key);
        num = 0;
        if (StringUtils.isNotBlank(value)) {
            num = Integer.parseInt(value);
        }
        if (AuthUtil.getSessionsMaxNum() != -1 && num > AuthUtil.getSessionsMaxNum()) { // 超过最大会话数量，不允许登录了
            return AuthResultConstant.EXCEED_SESSIONS_MAXNUM;
        }

        // 开始校验用户名和密码
        UserInfo userInfo = authMapper.getUserInfo(username);
        if (userInfo == null) {
            return AuthResultConstant.USER_NOT_EXIST; // 用户不存在
        }
        // 是否失效
        if (1 == userInfo.getStatus()) {
            return AuthResultConstant.USER_IS_INVALID; // 用户已被禁用或失效
        }
        // 判断密码是否等于
        String localPassword = userInfo.getPassword();
        if (StringUtils.isEmpty(localPassword) || !localPassword.equals(password)) {
            return AuthResultConstant.USERNAME_AND_PASSWORD_NOT_MATCH; // 用户名或密码不正确
        }

        // 校验成功
        return AuthResultConstant.MATCH_SUCCESS;
    }


    /**
     * 当登录失败的时候，记录尝试次数（即使）
     *
     * @param username 用户名
     */
    @Override
    public void recordFailUserLogin(String username) {
        String redisKey = AuthConstant.LAMBO_SSO_LOGIN_NUMBER + ":" + username;
        String value = redisUtils.getStr(redisKey);
        int num = StringUtils.isNotBlank(value) ? Integer.parseInt(value) : 0;
        num = num + 1;
        redisUtils.set(redisKey, String.valueOf(num), AuthUtil.getLoginMaxNumExpiredTime());
    }
}

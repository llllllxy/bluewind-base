package com.bluewind.base.common.config.auth.realm;

import com.bluewind.base.common.config.auth.constant.AuthResultConstant;
import com.bluewind.base.common.config.auth.util.UserInfoUtil;
import com.bluewind.base.module.system.auth.service.AuthService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author liuxingyu01
 * @date 2022-08-26 14:30
 * @description 自定义Realm
 **/
public class AuthClientRealm extends AuthorizingRealm {
    final static Logger logger = LoggerFactory.getLogger(AuthClientRealm.class);

    @Autowired
    private AuthService authService;


    /**
     * 身份认证
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;

        String username = (String) token.getPrincipal();
        String password = new String((char[]) token.getCredentials());

        /*
         * 免密单点登录
         */
        if (StringUtils.isBlank(password)) {
            return new SimpleAuthenticationInfo(username, password, getName());
        }

        // 根据用户名查找到用户信息
        int resutl = authService.authentication(username, password);

        //  超过最大尝试次数，锁定一段时间
        if (resutl == AuthResultConstant.USER_IS_LOCKED) {
            throw new LockedAccountException();
        }
        // 超过最大会话数量
        if (resutl == AuthResultConstant.EXCEED_SESSIONS_MAXNUM) {
            throw new ExcessiveAttemptsException();
        }
        // 没找到帐号(用户不存在)
        if (resutl == AuthResultConstant.USER_NOT_EXIST) {
            throw new UnknownAccountException();
        }
        // 校验用户状态(用户已失效)
        if (resutl == AuthResultConstant.USER_IS_INVALID) {
            throw new DisabledAccountException();
        }
        // 用户名密码不匹配
        if (resutl == AuthResultConstant.USERNAME_AND_PASSWORD_NOT_MATCH) {
            throw new IncorrectCredentialsException();
        }

        return new SimpleAuthenticationInfo(username, password, getName());
    }


    /**
     * 角色权限认证
     *
     * @param principalCollection
     * @return AuthorizationInfo
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String username = (String) principalCollection.getPrimaryPrincipal();

        if (StringUtils.isNoneBlank(username)) {
            // 权限信息对象info,用来存放查出的用户的所有的角色（role）及权限（permission）
            SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

            Long userId = UserInfoUtil.getUserId();
            // 获得用户角色列表
            simpleAuthorizationInfo.setRoles(authService.listUserRoleByUserId(userId));
            // 获得权限列表
            simpleAuthorizationInfo.setStringPermissions(authService.listRolePermissionByUserId(userId));

            return simpleAuthorizationInfo;
        }
        return null;
    }
}

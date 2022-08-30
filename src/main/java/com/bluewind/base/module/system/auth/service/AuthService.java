package com.bluewind.base.module.system.auth.service;

import com.bluewind.base.module.system.auth.entity.UserInfo;

import java.util.Set;

/**
 * @author liuxingyu01
 * @date 2022-08-26 15:07
 * @description
 **/
public interface AuthService {

    int authentication(String username, String password);

    void recordFailUserLogin(String username);

    UserInfo getUserInfo(String username);

    Set<String> listRolePermissionByUserId(Long userId);

    Set<String> listUserRoleByUserId(Long userId);
}

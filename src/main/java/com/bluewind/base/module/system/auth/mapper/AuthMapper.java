package com.bluewind.base.module.system.auth.mapper;

import com.bluewind.base.module.system.auth.entity.UserInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * @author liuxingyu01
 * @date 2022-08-26 15:08
 * @description
 **/
@Repository
public interface AuthMapper {

    UserInfo getUserInfo(String username);

    Set<String> listRolePermissionByUserId(@Param("userId") Long userId);

    Set<String> listUserRoleByUserId(@Param("userId") Long userId);
}

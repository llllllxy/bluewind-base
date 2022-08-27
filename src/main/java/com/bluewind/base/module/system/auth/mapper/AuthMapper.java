package com.bluewind.base.module.system.auth.mapper;

import com.bluewind.base.module.system.auth.entity.UserInfo;
import org.springframework.stereotype.Repository;

/**
 * @author liuxingyu01
 * @date 2022-08-26 15:08
 * @description
 **/
@Repository
public interface AuthMapper {

    UserInfo getUserInfo(String username);
}

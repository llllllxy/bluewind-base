package org.bluewind.base.module.system.demoperson.mapper;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author liuxingyu01
 * @date 2021-09-12-21:15
 **/
@Repository
public interface DemoPersonMapper {
    /**
     * 获取所有用户
     * @param userId
     * @return
     */
    List<Map> getAllPerson(String userId);
}

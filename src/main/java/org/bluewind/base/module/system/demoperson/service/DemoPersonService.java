package org.bluewind.base.module.system.demoperson.service;

import java.util.List;
import java.util.Map;

/**
 * @author liuxingyu01
 * @date 2021-09-12-21:14
 **/
public interface DemoPersonService {
    /**
     * 获取所有用户
     * @param userId
     * @return
     */
    List<Map> getAllPerson(String userId);
}

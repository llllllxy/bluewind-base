package com.bluewind.base.module.system.demoperson.service;

import com.bluewind.base.module.system.demoperson.mapper.DemoPersonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author liuxingyu01
 * @date 2021-09-12-21:14
 **/
@Service
public class DemoPersonServiceImpl implements DemoPersonService {
    @Autowired
    private DemoPersonMapper demoPersonMapper;

    @Override
    public List<Map> getAllPerson(String userId){
        return demoPersonMapper.getAllPerson(userId);
    }
}

package com.bluewind.base.common.config.auth.filter;


import com.bluewind.base.common.util.io.PropertiesFileUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashMap;

/**
 * @author liuxingyu01
 * @date 2022-08-26 14:57
 * @description 自定义FilterChainDefinition
 **/
public class FilterChainDefinitionMapBuilder {

    public LinkedHashMap<String, String> buildFilterChainDefinitionMap() {
        // 注意顺序
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        // 开放登录的相关接口
        map.put("/login", "anon");
        map.put("/logout", "anon");
        // 开放匿名接口
        map.put("/anon/**", "anon");
        // 开放swagger-ui的访问限制
        map.put("/resources/**", "anon");
        map.put("/webjars/**", "anon");
        map.put("/swagger-resources/**", "anon");
        map.put("/v2/api-docs/**", "anon");
        map.put("/v2/api-docs-ext/**", "anon");
        map.put("/swagger-ui.html", "anon");
        map.put("/doc.html", "anon");
        // 额外的一些需要开放的接口，从配置文件里取
        String excludeTargets = null;
        try {
            excludeTargets = PropertiesFileUtil.getInstance("security").get("security.authorization.excludeTargets");
        } catch (Exception ignored) {

        }
        // 从配置文件获取部分接口配置，格式例如：/login:anon;/loginout:anon;
        if (StringUtils.isNotBlank(excludeTargets)) {
            String[] nologin = StringUtils.split(excludeTargets, ";");
            for (String temp : nologin) {
                String[] value = StringUtils.split(temp, ":");
                if (value.length == 2) {
                    map.put(value[0].trim(), value[1].trim());
                }
            }
        }
        // 开放druid监控
        map.put("/druid/**", "anon");
        // 开放api接口
        map.put("/api/**", "anon");
        // 剩下的全部拦截
        map.put("/**", "authc");
        return map;
    }

}

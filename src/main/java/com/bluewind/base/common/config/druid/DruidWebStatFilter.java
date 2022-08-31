package com.bluewind.base.common.config.druid;

import com.alibaba.druid.support.http.WebStatFilter;

import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;

/**
 * @author liuxingyu01
 * @date 2022-08-31 17:14
 * @description druid连接池url监控filter
 **/
@WebFilter(
        filterName = "DruidWebStatFilter",
        urlPatterns = {"/*"},
        initParams = {
                @WebInitParam(name = "exclusions", value = "/configuration/*,/swagger-resources/*,/v2/api-docs/*,/v2/api-docs,/druid/*,/resources/*,/webjars/*,/swagger-resources,/swagger-ui.html")
        })
public class DruidWebStatFilter extends WebStatFilter {
}

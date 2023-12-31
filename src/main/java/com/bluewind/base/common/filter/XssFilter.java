package com.bluewind.base.common.filter;

import com.bluewind.base.common.util.io.PropertiesFileUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author liuxingyu01
 * @date 2021-02-16-10:52
 * @description 防止XSS攻击的过滤器
 **/
@WebFilter(filterName = "xssFilter",
        urlPatterns = {"/*"},
        dispatcherTypes = {DispatcherType.REQUEST, DispatcherType.FORWARD})
public class XssFilter implements Filter {
    final static Logger logger = LoggerFactory.getLogger(XssFilter.class);

    /**
     * 排除链接
     */
    public List<String> excludes = new ArrayList<>();

    /**
     * xss过滤开关
     */
    public boolean enabled = false;

    private final static String tempExcludes = PropertiesFileUtil.getInstance("xssconfig").get("xss.excludes");
    private final static String tempEnabled = PropertiesFileUtil.getInstance("xssconfig").get("xss.enabled");

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        if (StringUtils.isNotEmpty(tempExcludes)) {
            String[] url = StringUtils.split(tempExcludes, ",");
            for (int i = 0; url != null && i < url.length; i++) {
                excludes.add(url[i]);
            }
        }
        if (StringUtils.isNotEmpty(tempEnabled)) {
            enabled = Boolean.valueOf(tempEnabled);
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        logger.info("[{}]执行{}方法：Before！", this.getClass().getSimpleName(), "doFilter");
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        if (handleExcludeURL(req, resp)) {
            chain.doFilter(request, response);
            return;
        }
        XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper((HttpServletRequest) request);
        chain.doFilter(xssRequest, response);
        logger.info("[{}]执行{}方法：After！", this.getClass().getSimpleName(), "doFilter");
    }

    private boolean handleExcludeURL(HttpServletRequest request, HttpServletResponse response) {
        if (!enabled) {
            return true;
        }
        if (excludes == null || excludes.isEmpty()) {
            return false;
        }
        String url = request.getServletPath();
        for (String pattern : excludes) {
            Pattern p = Pattern.compile("^" + pattern);
            Matcher m = p.matcher(url);
            if (m.find()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void destroy() {

    }

}

package com.bluewind.base.common.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author liuxingyu01
 * @date 2022-08-27 13:02
 * @description 配置过滤器处理跨域问题，顺序问题参考：https://blog.csdn.net/weixin_30498921/article/details/96532490
 **/
@WebFilter(filterName = "CORSFilter",
        urlPatterns = {"/*"},
        dispatcherTypes = {DispatcherType.REQUEST})
public class CORSFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse res = (HttpServletResponse) response;
        res.setHeader("Access-Control-Allow-Credentials", "true");
        res.setHeader("Access-Control-Allow-Origin", "*");
        res.setHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS, HEAD");
        res.setHeader("Access-Control-Allow-Headers", "Content-Type,Accept,X-CAF-Authorization-Token,sessionToken,X-TOKEN,Authorization");
        // 设置 缓存可以生存的最大秒数
        res.addHeader("Access-Control-Max-Age", "3600");
        if (((HttpServletRequest) request).getMethod().equals("OPTIONS")) {
            response.getWriter().println("ok");
            return;
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }
}

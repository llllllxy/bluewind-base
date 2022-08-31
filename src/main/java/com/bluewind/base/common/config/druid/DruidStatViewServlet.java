package com.bluewind.base.common.config.druid;

import com.alibaba.druid.support.http.StatViewServlet;

import com.bluewind.base.common.util.DateTool;
import com.bluewind.base.common.util.encrypt.AESUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author liuxingyu01
 * @date 2022-08-31 17:04
 * @description DruidStatViewServlet
 **/
@WebServlet(
        name = "DruidStatViewServlet",
        urlPatterns = {"/druid/*"},
        initParams = {@WebInitParam(name = "loginUsername", value = "admin"),
                      @WebInitParam(name = "loginPassword", value = "10Passw0rd!")
        })
public class DruidStatViewServlet extends StatViewServlet {

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String contextPath = request.getContextPath();
        String servletPath = request.getServletPath();
        String requestURI = request.getRequestURI();

        response.setCharacterEncoding("utf-8");

        if (contextPath == null) {
            contextPath = "";
        }
        String uri = contextPath + servletPath;
        String path = requestURI.substring(contextPath.length() + servletPath.length());

        if (!isPermittedRequest(request)) {
            path = "/nopermit.html";
            returnResourceFile(path, uri, response);
            return;
        }

        if ("/submitLogin".equals(path)) {
            String usernameParam = request.getParameter(PARAM_NAME_USERNAME);
            String passwordParam = request.getParameter(PARAM_NAME_PASSWORD);
            if (username.equals(usernameParam) && password.equals(passwordParam)) {
                setCookie(response);
                response.getWriter().print("success");
            } else {
                response.getWriter().print("error");
            }
            return;
        }

        if (isRequireAuth()
                && !verifyCookie(request)
                && !checkLoginParam(request)
                && !("/login.html".equals(path)
                || path.startsWith("/css")
                || path.startsWith("/js")
                || path.startsWith("/img"))) {
            if (contextPath.equals("") || contextPath.equals("/")) {
                response.sendRedirect("/druid/login.html");
            } else {
                if ("".equals(path)) {
                    response.sendRedirect("druid/login.html");
                } else {
                    response.sendRedirect("login.html");
                }
            }
            return;
        }

        if ("".equals(path)) {
            if (contextPath.equals("") || contextPath.equals("/")) {
                response.sendRedirect("/druid/index.html");
            } else {
                response.sendRedirect("druid/index.html");
            }
            return;
        }

        if ("/".equals(path)) {
            response.sendRedirect("index.html");
            return;
        }

        if (path.contains(".json")) {
            String fullUrl = path;
            if (request.getQueryString() != null && request.getQueryString().length() > 0) {
                fullUrl += "?" + request.getQueryString();
            }
            response.getWriter().print(process(fullUrl));
            return;
        }

        // find file in resources path
        returnResourceFile(path, uri, response);
    }

    private boolean verifyCookie(HttpServletRequest request) {
        AESUtils aesUtils = AESUtils.builder().sKey("1G78Av#yej%WZJ3uiSZRz9oy%UAv4AAA").ivParameter("E%BAAAUTvXfwSuGQ").build();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (StringUtils.equals(cookie.getName(), "druidCookie")) {
                    String valueDecode = aesUtils.decrypt(cookie.getValue());
                    if (StringUtils.equals(valueDecode, DateTool.getCurrentDate())) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private void setCookie(HttpServletResponse response) {
        AESUtils aesUtils = AESUtils.builder().sKey("1G78Av#yej%WZJ3uiSZRz9oy%UAv4AAA").ivParameter("E%BAAAUTvXfwSuGQ").build();
        String today = DateTool.getCurrentDate();
        String valueEncode = aesUtils.encrypt(today);
        Cookie cookie = new Cookie("druidCookie", valueEncode);
        // 1个小时有效期
        cookie.setMaxAge(60 * 60 * 1);
        response.addCookie(cookie);
    }

}

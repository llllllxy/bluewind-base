package com.bluewind.base.common.config.auth.filter;

import com.bluewind.base.common.base.Result;
import com.bluewind.base.common.consts.HttpStatus;
import com.bluewind.base.common.util.web.ResponseUtil;
import org.apache.shiro.session.Session;
import org.apache.shiro.web.filter.AccessControlFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * @author liuxingyu01
 * @date 2022-08-26 14:50
 * @description
 **/
public class AuthClientSessionForceLogoutFilter extends AccessControlFilter {
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        Session session = getSubject(request, response).getSession(false);
        if(session == null) {
            return true;
        }
        boolean forceout = session.getAttribute("FORCE_LOGOUT") == null;
        return  forceout;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        ResponseUtil.sendJson(response, Result.create(HttpStatus.UNAUTHORIZED, "会话已失效，请重新登录"));
        return false;
    }
}

package com.bluewind.base.common.config.auth.session;

import org.apache.shiro.session.mgt.SimpleSession;

/**
 * @author liuxingyu01
 * @date 2022-08-26 11:45
 * @description 重写session
 **/
public class AuthClientSession extends SimpleSession {

    public static enum OnlineStatus {
        on_line("在线"),
        off_line("离线"),
        force_logout("强制退出");

        private final String info;

        private OnlineStatus(String info) {
            this.info = info;
        }

        public String getInfo() {
            return info;
        }
    }

    /**
     * 用户浏览器类型
     */
    private String userAgent;

    /**
     * 在线状态
     */
    private OnlineStatus status = OnlineStatus.off_line;

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public OnlineStatus getStatus() {
        return status;
    }

    public void setStatus(OnlineStatus status) {
        this.status = status;
    }

}

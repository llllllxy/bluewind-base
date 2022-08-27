package com.bluewind.base.common.config.auth.util;

import com.bluewind.base.common.util.io.PropertiesFileUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liuxingyu01
 * @date 2022-08-26 14:48
 * @description
 **/
public class AuthUtil {
    private static final Logger log = LoggerFactory.getLogger(AuthUtil.class);

    private static Map getHost() {
        Map<String, String> map = new HashMap<String, String>();

        try {
            InetAddress addr = InetAddress.getLocalHost();
            String hostIp = addr.getHostAddress().toString(); //获取本机ip
            String hostName = addr.getHostName().toString(); //获取本机计算机名称
            String hostMac = getLocalMac(addr);

            map.put("hostIp", hostIp);
            map.put("hostName", hostName);
            map.put("hostMac", hostMac);

        } catch (Exception e) {
            log.info("获取用户的信息失败，" + e);
        }

        return map;
    }

    private static String getLocalMac(InetAddress ia) {
        StringBuffer sb = new StringBuffer("");

        //获取网卡，获取地址
        try {
            byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
            for (int i = 0; i < mac.length; i++) {
                if (i != 0) {
                    sb.append("-");
                }
                //字节转换为整数
                int temp = mac[i] & 0xff;
                String str = Integer.toHexString(temp);
                if (str.length() == 1) {
                    sb.append("0" + str);
                } else {
                    sb.append(str);
                }
            }
        } catch (SocketException e) {
            log.info("获取用户的mac地址失败，" + e);
        }

        return sb.toString().toUpperCase();
    }

    public static int getSessionsTime() {
        String security = PropertiesFileUtil.getInstance("security").get("security.session.maxInactiveInterval");
        if (StringUtils.isBlank(security)) {
            //默认为1800
            security = "1800";
        }
        return Integer.parseInt(security);
    }

    public static int getSessionsMaxNum() {
        String security = PropertiesFileUtil.getInstance("security").get("security.session.maxNum");
        if (StringUtils.isBlank(security)) {
            // 默认-1不限制
            security = "-1";
        }
        return Integer.parseInt(security);
    }

    public static int getLoginMaxNum() {
        String security = PropertiesFileUtil.getInstance("security").get("security.login.maxNum");
        if (StringUtils.isBlank(security)) {
            // 默认为10
            security = "10";
        }
        return Integer.parseInt(security);
    }

    public static int getLoginMaxNumExpiredTime() {
        String security = PropertiesFileUtil.getInstance("security").get("security.login.maxNum.expiredTime");
        if (StringUtils.isBlank(security)) {
            // 默认为1800
            security = "1800";
        }
        return Integer.parseInt(security);
    }
}

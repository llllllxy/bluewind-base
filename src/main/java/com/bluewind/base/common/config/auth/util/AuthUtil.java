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
 * @description AuthUtil会话工具类
 **/
public class AuthUtil {
    private static final Logger log = LoggerFactory.getLogger(AuthUtil.class);


    /**
     * 获取本地Host
     *
     * 返回结果格式如下：
     *  <pre>
     *     {
     *       "hostIp": "本机ip地址",
     *       "hostName": "本机计算机名称",
     *       "hostMac": "本机mac地址",
     *     }
     *  </pre>
     *
     * @return Map
     */
    private static Map<String, String> getHost() {
        Map<String, String> map = new HashMap<>();
        try {
            InetAddress addr = InetAddress.getLocalHost();
            // 获取本机ip
            String hostIp = addr.getHostAddress();
            // 获取本机计算机名称
            String hostName = addr.getHostName();
            // 获取本机mac地址
            String hostMac = getLocalMac(addr);

            map.put("hostIp", hostIp);
            map.put("hostName", hostName);
            map.put("hostMac", hostMac);
        } catch (Exception e) {
            log.info("AuthUtil -- getHost -- Exception：" + e);
        }
        return map;
    }

    /**
     * 获取MAC地址
     *
     * @param ia InetAddress
     * @return String
     */
    private static String getLocalMac(InetAddress ia) {
        StringBuilder sb = new StringBuilder("");
        // 获取网卡，获取地址
        try {
            byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
            for (int i = 0; i < mac.length; i++) {
                if (i != 0) {
                    sb.append("-");
                }
                // 字节转换为整数
                int temp = mac[i] & 0xff;
                String str = Integer.toHexString(temp);
                if (str.length() == 1) {
                    sb.append("0").append(str);
                } else {
                    sb.append(str);
                }
            }
        } catch (SocketException e) {
            log.info("AuthUtil -- getHost -- Exception：" + e);
        }
        return sb.toString().toUpperCase();
    }

    /**
     * 从security.properties配置文件中获取：会话时间配置
     *
     * @return int
     */
    public static int getSessionsTime() {
        String security = PropertiesFileUtil.getInstance("security").get("security.session.maxInactiveInterval");
        if (StringUtils.isBlank(security)) {
            // 取不到那就默认为1800s
            security = "1800";
        }
        return Integer.parseInt(security);
    }


    /**
     * 从security.properties配置文件中获取：最大会话数量
     *
     * @return
     */
    public static int getSessionsMaxNum() {
        String security = PropertiesFileUtil.getInstance("security").get("security.session.maxNum");
        if (StringUtils.isBlank(security)) {
            // 取不到那就默认-1不限制
            security = "-1";
        }
        return Integer.parseInt(security);
    }


    /**
     * 从security.properties配置文件中获取：登录时最大错误尝试次数（超过后锁定张哈，禁止登录）
     *
     * @return int
     */
    public static int getLoginMaxNum() {
        String security = PropertiesFileUtil.getInstance("security").get("security.login.maxNum");
        if (StringUtils.isBlank(security)) {
            // 默认为10次
            security = "10";
        }
        return Integer.parseInt(security);
    }


    /**
     * 从security.properties配置文件中获取：禁止登录后的锁定时间
     *
     * @return int
     */
    public static int getLoginMaxNumExpiredTime() {
        String security = PropertiesFileUtil.getInstance("security").get("security.login.maxNum.expiredTime");
        if (StringUtils.isBlank(security)) {
            // 默认为1800秒
            security = "1800";
        }
        return Integer.parseInt(security);
    }
}

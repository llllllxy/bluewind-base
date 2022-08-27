package com.bluewind.base.common.config.auth.util;

import com.bluewind.base.common.config.auth.constant.AuthConstant;
import org.apache.shiro.crypto.hash.Sha256Hash;

/**
 * @author liuxingyu01
 * @date 2022-08-26 16:54
 * @description shiro Sha256加密工具类
 **/
public class PasswordUtils {
    /**
     * 迭代次数
     */
    private static final int ITERATIONS = 2;

    // 禁止对象的空初始化
    private PasswordUtils() {
        throw new AssertionError();
    }

    /**
     * 字符串加密函数MD5实现
     *
     * @param password  原始密码
     * @return
     */
    public static String getPassword(String password) {
        // 从配置方法里获取颜盐
        String salt = AuthConstant.SALT;
        // toHex()返回hex编码的加密结果，如果需要base64的话，则为toBase64()
        return new Sha256Hash(password, salt, ITERATIONS).toHex();
    }

    public static void main(String[] args) {
        System.out.println(getPassword("123456"));
    }
}

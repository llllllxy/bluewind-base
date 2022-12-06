package com.bluewind.base.common.util.lang;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author liuxingyu01
 * @date 2022-12-06 14:07
 * @description 对象操作工具类, 继承org.apache.commons.lang3.ObjectUtils类
 **/
public class ObjectUtils extends org.apache.commons.lang3.ObjectUtils {
    private static final Logger logger = LoggerFactory.getLogger(ObjectUtils.class);


    /**
     * 转换为字节数组
     * @param obj
     * @return
     */
    public static byte[] toByteArray(Object obj) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(obj);
        } catch (Exception e) {
            return null;
        }

        return byteArrayOutputStream.toByteArray();
    }

    /**
     * 转换为Double类型
     */
    public static Double toDouble(final Object val) {
        if (val == null) {
            return 0D;
        }
        try {
            return NumberUtils.toDouble(StringUtils.trim(val.toString()));
        } catch (Exception e) {
            return 0D;
        }
    }


    /**
     * 转换为Float类型
     */
    public static Float toFloat(final Object val) {
        return toDouble(val).floatValue();
    }

    /**
     * 转换为Long类型
     */
    public static Long toLong(final Object val) {
        return toDouble(val).longValue();
    }

    /**
     * 转换为Integer类型
     */
    public static Integer toInteger(final Object val) {
        return toLong(val).intValue();
    }

    /**
     * 转换为Boolean类型 'true', 'on', 'y', 't', 'yes' or '1' (case insensitive) will return true. Otherwise, false is returned.
     */
    public static Boolean toBoolean(final Object val) {
        if (val == null) {
            return false;
        }
        return BooleanUtils.toBoolean(val.toString()) || "1".equals(val.toString());
    }


    /**
     * 转换为字符串
     *
     * @param obj
     * @return
     */
    public static String toString(final Object obj) {
        return toString(obj, StringUtils.EMPTY);
    }

    /**
     * 如果对象为空，则使用defaultVal值
     *
     * @param obj
     * @param defaultVal
     * @return
     */
    public static String toString(final Object obj, final String defaultVal) {
        return obj == null ? defaultVal : obj.toString();
    }

    /**
     * 空转空字符串（"" to "" ; null to "" ; "null" to "" ; "NULL" to "" ; "Null" to ""）
     *
     * @param val 需转换的值
     * @return 返回转换后的值
     */
    public static String toStringIgnoreNull(final Object val) {
        return ObjectUtils.toStringIgnoreNull(val, StringUtils.EMPTY);
    }

    /**
     * 空对象转空字符串 （"" to defaultVal ; null to defaultVal ; "null" to defaultVal ; "NULL" to defaultVal ; "Null" to defaultVal）
     *
     * @param val        需转换的值
     * @param defaultVal 默认值
     * @return 返回转换后的值
     */
    public static String toStringIgnoreNull(final Object val, String defaultVal) {
        String str = ObjectUtils.toString(val);
        return !"".equals(str) && !"null".equals(str.trim().toLowerCase()) ? str : defaultVal;
    }


    /**
     * 序列化对象
     *
     * @param object
     * @return
     */
    public static byte[] serialize(Object object) {
        if (object == null) {
            return null;
        }
        long beginTime = System.currentTimeMillis();
        byte[] bytes = null;
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            bytes = baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(oos);
            IOUtils.closeQuietly(baos);
        }
        long totalTime = System.currentTimeMillis() - beginTime;
        if (totalTime > 3000) {
            logger.warn("Serialize time: " + totalTime + "毫秒");
        }
        return bytes;
    }


    /**
     * 反序列化对象
     *
     * @param bytes
     * @return
     */
    public static Object unserialize(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        long beginTime = System.currentTimeMillis();
        Object object = null;
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        try {
            if (bytes.length > 0) {
                bais = new ByteArrayInputStream(bytes);
                ois = new ObjectInputStream(bais);
                object = ois.readObject();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(ois);
            IOUtils.closeQuietly(bais);
        }
        long totalTime = System.currentTimeMillis() - beginTime;
        if (totalTime > 3000) {
            logger.warn("Unserialize time: " + totalTime + "毫秒");
        }
        return object;
    }
}

package com.bluewind.base.common.util;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liuxingyu01
 * @date 2021-09-13-13:37
 * @description 功能:本类功能为将各种对象转化为Json字符串 - 使用 jackson 包
 **/
public class JsonTool {
    final static Logger log = LoggerFactory.getLogger(JsonTool.class);

    /**
     * 将 Object 转化为Json字符串
     *
     * @param 'Object' obj
     * @return String JsonString
     */
    public static String toJsonString(Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "";
        try {
            jsonString = mapper.writeValueAsString(obj);
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("JsonTool -- toJsonString -- Exception=", e);
            }
        }
        return jsonString;
    }


    /**
     * 将 Json String 转化为Map
     *
     * @param 'String JsonString'
     * @return Map returnMap
     */
    public static Map parseMap(String JsonString) {
        Map returnMap = new HashMap();
        ObjectMapper mapper = new ObjectMapper();
        try {
            returnMap = mapper.readValue(JsonString, Map.class);
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("JsonTool -- getMapFromJsonString -- Exception=", e);
            }
        }
        return returnMap;
    }



    /**
     * JSON字符串转Object
     * @param jsonString
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T parseObject(String jsonString, Class<T> clazz) {
        if (jsonString != null && !jsonString.trim().isEmpty()) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(jsonString, clazz);
            } catch (Exception e) {
                if (log.isErrorEnabled()) {
                    log.error("JsonTool -- parseObject -- Exception=", e);
                }
            }
        }
        return null;
    }



    /**
     * JSON字符串转List
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> parseArray(String json, Class<T> clazz) {
        if (json != null && !json.trim().isEmpty()) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(json, mapper.getTypeFactory().constructParametricType(ArrayList.class, clazz));
            } catch (Exception e) {
                if (log.isErrorEnabled()) {
                    log.error("JsonTool -- parseArray -- Exception=", e);
                }
            }
        }
        return null;
    }



    public static void main(String[] args) {
        String jsonStr = "{\"password\":\"123456\",\"username\":\"张三\"}";
        Map map = parseMap(jsonStr);
        System.out.println("map=" + map);


//        String ooh = "[{\"id\":25,\"account\":\"hhyniubi\",\"password\":\"99B26BE5F5F7AF4A576DFB6DF0DD38FF\",\"name\":\"huahiyang\",\"phone\":\"0\",\"avatar\":\"http://halo.lxyccc.top/f778738c-e4f8-4870-b634-56703b4acafe_1608734603765.gif\",\"sex\":0,\"status\":1,\"createUser\":0,\"createTime\":\"2021-01-07 15:28:30\",\"updateUser\":0,\"updateTime\":\"2021-01-10 01:32:03\",\"delFlag\":0},{\"id\":25,\"account\":\"hhyniubi\",\"password\":\"99B26BE5F5F7AF4A576DFB6DF0DD38FF\",\"name\":\"huahiyang\",\"phone\":\"0\",\"avatar\":\"http://halo.lxyccc.top/f778738c-e4f8-4870-b634-56703b4acafe_1608734603765.gif\",\"sex\":0,\"status\":1,\"createUser\":0,\"createTime\":\"2021-01-07 15:28:30\",\"updateUser\":0,\"updateTime\":\"2021-01-10 01:32:03\",\"delFlag\":0}]";
//        List<SysUserInfo> sysy = getListFromJsonString(ooh, SysUserInfo.class);
//
//        System.out.println("SysUserInfo=" + sysy);
    }

}

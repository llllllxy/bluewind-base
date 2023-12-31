package com.bluewind.base.common.util.web;

import com.bluewind.base.common.util.JsonTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author liuxingyu01
 * @date 2021-09-16-13:49
 **/
public class ResponseUtil {
    private final static Logger logger = LoggerFactory.getLogger(ResponseUtil.class);

    public static void sendJson(ServletResponse response, Object responseObject) {
        //将实体对象转换为JSON Object转换
        String jsonStr = JsonTool.toJsonString(responseObject);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try (PrintWriter out = response.getWriter()) {
            out.append(jsonStr);
        } catch (IOException e) {
            logger.error("ResponseUtil -- sendJson -- IOException = {e}", e);
        }
    }
}

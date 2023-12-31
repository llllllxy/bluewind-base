package com.bluewind.base.common.base;


import com.bluewind.base.common.consts.HttpStatus;

import java.io.Serializable;

/**
 * @author liuxingyu01
 * @date 2021-09-13-17:09
 * @description Json统一返回消息类
 **/
public class Result implements Serializable {
    private static final long serialVersionUID = -1491499610244557029L;

    private Integer code;

    private String msg;

    private Object data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Result(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Result(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static Result ok(String msg, Object data) {
        return new Result(HttpStatus.SUCCESS, msg, data);
    }

    public static Result ok(String msg) {
        return new Result(HttpStatus.SUCCESS, msg, null);
    }

    public static Result error(String msg) {
        return new Result(HttpStatus.ERROR, msg);
    }

    public static Result create(Integer code, String msg, Object data) {
        return new Result(code, msg, data);
    }

    public static Result create(Integer code, String msg) {
        return new Result(code, msg);
    }

}

package com.bluewind.base.module.webservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * @author liuxingyu01
 * @date 2021-10-08-11:32
 **/
@WebService // 声明该类是一个webservice服务
public interface WebServiceExample {
    @WebMethod(operationName = "myWebMethod") // 声明该方法是一个webservice方法
    Object myWebMethod(@WebParam(name = "xml") String xml);
}

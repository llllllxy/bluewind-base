package com.bluewind.base.module.webservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.jws.WebService;

/**
 * @author liuxingyu01
 * @date 2021-10-08-11:32
 **/
@WebService(endpointInterface = "com.bluewind.base.module.webservice.WebServiceExample")
// 服务接口全路径, 指定服务端点接口
public class WebServiceExampleImpl implements WebServiceExample {
    @Override
    public Object myWebMethod(String xml) {
        return xml;
    }
}

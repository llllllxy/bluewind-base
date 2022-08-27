package com.bluewind.base.module.system.demoperson.controller;

import com.bluewind.base.common.annotation.DataSource;
import com.bluewind.base.common.annotation.LogAround;
import com.bluewind.base.common.config.auth.util.UserInfoUtil;
import com.bluewind.base.common.util.redis.RedisUtils;
import com.bluewind.base.module.system.demoperson.service.DemoPersonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author liuxingyu01
 * @date 2021-09-12-21:17
 **/
@Api(value = "系统用户管理控制器", tags = "系统用户管理控制器")
@Controller
@RequestMapping("/demoperson")
public class DemoPersonController {
    private static Logger logger = LoggerFactory.getLogger(DemoPersonController.class);

    @Autowired
    private DemoPersonService demoPersonService;

    @Autowired
    private RedisUtils redisUtils;


    @LogAround("testAuth")
    @RequestMapping(value = "/testAuth",method = RequestMethod.GET)
    @ResponseBody
    public Object testAuth() {
        logger.info("测试会话控制成功");
        return "测试会话控制成功";
    }


    @LogAround("testAuth")
    @RequiresPermissions("permissions2")
    @RequestMapping(value = "/testPermissions",method = RequestMethod.GET)
    @ResponseBody
    public Object testPermissions() {
        logger.info("测试testPermissions成功");

        logger.info("UserInfoUtil.getUserName() = {}", UserInfoUtil.getUserName());
        logger.info("UserInfoUtil.getUserInfo() = {}", UserInfoUtil.getUserInfo());

        return "测试testPermissions成功";
    }


    /**
     * 测试rest接口返回，测试成功
     * @param userId
     * @return
     */
    @ApiOperation(value = "查询数据", notes = "查询数据")
    @LogAround("getAllPerson")
    @RequestMapping(value = "/getAllPerson",method = RequestMethod.GET)
    @ResponseBody
    public Object getAllPerson(@RequestParam(required = false,defaultValue = "",value = "userId") String userId,
                               @RequestHeader(required = false,defaultValue = "",value = "token") String token) {
        logger.info("getAllPerson.token ---  = " + token);

        List<Map> personList = demoPersonService.getAllPerson(userId);

        redisUtils.set("token_token", personList);

        return personList;
    }



    /**
     * 测试rest接口返回，测试成功
     * @param userId
     * @return
     */
    @DataSource("slaveDataSource")
    @ApiOperation(value = "查询页面", notes = "查询页面")
    @LogAround("getPersonView")
    @RequestMapping(value = "/getPersonView",method = RequestMethod.GET)
    public String getPersonView(@RequestParam(required = false,defaultValue = "",value = "userId") String userId,
                                Model model) {
        logger.info("getAllPerson.getPersonView ---取出redis  = " + redisUtils.get("token_token"));


        List<Map> personList = demoPersonService.getAllPerson(userId);

        // 将数据存到model对象里，这样thymeleaf就能访问数据
        model.addAttribute("message", "Successful");
        model.addAttribute("personList", personList);
        // 返回jsp文件名
        return "index";
    }
}

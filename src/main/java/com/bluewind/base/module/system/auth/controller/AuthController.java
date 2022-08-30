package com.bluewind.base.module.system.auth.controller;

import com.bluewind.base.common.base.BaseController;
import com.bluewind.base.common.base.Result;
import com.bluewind.base.common.config.auth.constant.AuthConstant;
import com.bluewind.base.common.config.auth.util.AuthUtil;
import com.bluewind.base.common.config.auth.util.PasswordUtils;
import com.bluewind.base.common.config.auth.util.UserInfoUtil;
import com.bluewind.base.common.util.redis.RedisUtils;
import com.bluewind.base.common.util.web.CookieUtils;
import com.bluewind.base.module.system.auth.service.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @author liuxingyu01
 * @date 2022-08-26 15:05
 * @description 本地后台登录管理
 **/
@RestController
@Api(value = "本地后台登录管理", tags = "本地后台登录管理")
public class AuthController extends BaseController {
    final static Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private AuthService authService;


    @ApiOperation(value = "登录")
    @RequestMapping(value = "/login", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Result login(@RequestParam(value = "username") String username,
                        @RequestParam(value = "password") String password,
                        @RequestParam(value = "rememberMe", required = false) String rememberMe,
                        HttpServletResponse response) {
        if (StringUtils.isBlank(username)) {
            return Result.error("账号不能为空！");
        }
        if (StringUtils.isBlank(password)) {
            return Result.error("密码不能为空！");
        }

        Subject subject = SecurityUtils.getSubject();
        // 使用shiro认证
        UsernamePasswordToken userNamePasswordToken = new UsernamePasswordToken(username, PasswordUtils.getPassword(password));
        try {
            if (BooleanUtils.toBoolean(rememberMe)) {
                userNamePasswordToken.setRememberMe(true);
            } else {
                userNamePasswordToken.setRememberMe(false);
            }
            subject.login(userNamePasswordToken);

        } catch (IncorrectCredentialsException | UnknownAccountException e) {
            authService.recordFailUserLogin(username);
            return Result.error("用户名或密码错误！");
        } catch (ExpiredCredentialsException e) {
            authService.recordFailUserLogin(username);
            return Result.error("密码已过期！账号已经被锁定！");
        } catch (LockedAccountException e) {
            authService.recordFailUserLogin(username);
            return Result.error("您已多次输错密码，账号已经被锁定！");
        } catch (DisabledAccountException e) {
            authService.recordFailUserLogin(username);
            return Result.error("账号已经失效！");
        } catch (ExcessiveAttemptsException e) {
            authService.recordFailUserLogin(username);
            return Result.error("该账户在其他地方已登录，请注销后在登录！");
        } catch (Exception e) {
            logger.error("AuthController -- login -- 登陆失败", e);
            authService.recordFailUserLogin(username);
            return Result.error("登陆失败，详情请查看日志！");
        }

        logger.info("AuthController - doLogin - {}登陆成功！", username);
        // 默认验证帐号密码正确，创建token
        String token = UUID.randomUUID().toString().replace("-", "");
        // 记录会话token
        redisUtils.set(AuthConstant.BLUEWIND_TOKEN_CACHE + ":" + token, username, AuthUtil.getSessionsTime());
        // 往Cookie里设置单点登录token
        CookieUtils.setCookie(response, AuthConstant.BLUEWIND_COOKIE_KEY, token, "/", -1);

        // 记录当前用户的会话数量（会话数量+1）
        String num = redisUtils.getStr(AuthConstant.BLUEWIND_USER_SESSION_NUMS + ":" + username);
        if (StringUtils.isBlank(num)) {
            redisUtils.set(AuthConstant.BLUEWIND_USER_SESSION_NUMS + ":" + username, String.valueOf(1), AuthUtil.getSessionsTime());
        } else {
            redisUtils.set(AuthConstant.BLUEWIND_USER_SESSION_NUMS + ":" + username, String.valueOf(Integer.parseInt(num) + 1), AuthUtil.getSessionsTime());
        }

        // 认证成功后，则清空尝试登录次数
        redisUtils.del(AuthConstant.BLUEWIND_LOGIN_ATTEMPT_TIMES + ":" + username);

        return Result.ok("登录成功，欢迎回来！", token);
    }


    @ApiOperation(value = "退出登录")
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    @ResponseBody
    public Object logout(HttpServletRequest request) {
        // 退出登录即为删除redis中存着的会话信息即可
        String token = UserInfoUtil.getToken(request);

        String username = redisUtils.getStr(AuthConstant.BLUEWIND_TOKEN_CACHE + ":" + token);

        String num = redisUtils.getStr(AuthConstant.BLUEWIND_USER_SESSION_NUMS + ":" + username);
        if (StringUtils.isNotBlank(num)) {
            redisUtils.set(AuthConstant.BLUEWIND_USER_SESSION_NUMS + ":" + username, String.valueOf(Integer.parseInt(num) - 1), AuthUtil.getSessionsTime());
        }
        // 删除掉redis里存的会话
        redisUtils.del(AuthConstant.BLUEWIND_TOKEN_CACHE + ":" + token);
        return Result.ok("退出登录成功！");
    }

}

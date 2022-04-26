package com.xiaomou.handler.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiaomou.Result;
import com.xiaomou.ResultInfo;
import com.xiaomou.entity.User;
import com.xiaomou.entity.UserLogin;
import com.xiaomou.service.UserLoginService;
import com.xiaomou.service.impl.auth.MyUserDetails;
import com.xiaomou.util.IpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * @author MouHongDa
 * @date 2022/4/21 12:50
 */
@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private UserLoginService loginService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        MyUserDetails user = (MyUserDetails) authentication.getPrincipal();
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(new ObjectMapper().writeValueAsString(Result.ok()
                .code(ResultInfo.VERIFY_SUCCESS.getCode())
                .message(ResultInfo.VERIFY_SUCCESS.getMessage()).data("user", user)));
        //更新用户登录ip地址，最新登录时间
        String ipAddress = IpUtil.getIp(request);
        String ipSource = IpUtil.getIpSource(ipAddress);
        User loginUser = user.getUser();
        UserLogin login = new UserLogin();
        login.setAvatar(loginUser.getAvatar());
        login.setIpAddress(ipAddress);
        login.setIpSources(ipSource);
        login.setNickname(loginUser.getNickname());
        login.setLoginTime(new Date());
        //查询他上次得登录时间设置为上次登录时间
        //这个时间应该设置到redis中 每次登录就存入redis作为上次登录时间 并且每次更新
        //存入表
        loginService.save(login);
    }
}


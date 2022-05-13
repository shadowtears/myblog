package com.xiaomou.handler.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiaomou.Result;
import com.xiaomou.ResultInfo;
import com.xiaomou.entity.User;
import com.xiaomou.entity.UserLogin;
import com.xiaomou.enums.LoginTypeEnum;
import com.xiaomou.service.UserLoginService;
import com.xiaomou.service.impl.auth.MyUserDetails;
import com.xiaomou.util.IpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;

import static com.xiaomou.constant.RedisPrefixConst.IP_SET;
import static com.xiaomou.constant.RedisPrefixConst.LAST_LONG_TIME;

/**
 * @author MouHongDa
 * @date 2022/4/21 12:50
 */
@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private UserLoginService loginService;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        MyUserDetails user = (MyUserDetails) authentication.getPrincipal();
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(new ObjectMapper().writeValueAsString(Result.ok()
                .code(ResultInfo.VERIFY_SUCCESS.getCode())
                .message(ResultInfo.VERIFY_SUCCESS.getMessage()).data("user", user)));
//      更新用户登录信息
        updateUserLogin(user, request, LoginTypeEnum.EMAIL.getDesc());
    }

    public void updateUserLogin(MyUserDetails user, HttpServletRequest request, String desc) {
        User loginUser = user.getUser();
        //获取当前ip地址和ip来源用户登录ip地址
        String ipAddress = IpUtil.getIp(request);
        String ipSource = IpUtil.getIpSource(ipAddress);
        //获取当前时间
        Date now = new Date();
        //获取用户登录角色
        String authority = null;
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        for (GrantedAuthority grantedAuthority : authorities) {
            authority = grantedAuthority.getAuthority();
        }
        //查询他上次得登录时间设置为上次登录时间
        Date lastLongTime = (Date) redisTemplate.boundValueOps(LAST_LONG_TIME + loginUser.getUsername()).get();
        //这个时间应该设置到redis中 每次登录就存入redis作为上次登录时间 并且每次更新
        redisTemplate.boundValueOps(LAST_LONG_TIME + loginUser.getUsername()).set(now);
        UserLogin login = UserLogin.builder()
                .avatar(loginUser.getAvatar())
                .ipAddress(ipAddress)
                .ipSources(ipSource)
                .userName(loginUser.getUsername())
                .loginType(desc)
                .loginTime(now)
                .roleName(authority)
                .lastLoginTime(lastLongTime)
                .build();
        redisTemplate.boundSetOps(IP_SET).add(ipAddress);
        //存入表判断不存在就保存
        loginService.saveOrUpdate(login);
    }
}
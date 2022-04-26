package com.xiaomou.handler.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiaomou.Result;
import com.xiaomou.ResultInfo;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author MouHongDa
 * @date 2022/4/21 12:50
 */
@Component
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest requeste, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        //来到登录失败处理器
        //这个返回可以封装一下 就2个不用了
        response.setContentType("application/json;charset=UTF-8");
        if (e instanceof AuthenticationServiceException) {
            response.getWriter().write(new ObjectMapper().writeValueAsString(Result.error().message(e.getMessage())));
        } else if (e instanceof BadCredentialsException) {
            response.getWriter().write(new ObjectMapper().writeValueAsString(Result.error()
                    .code(ResultInfo.LOGIN_FAILED.getCode())
                    .message(ResultInfo.LOGIN_FAILED.getMessage())));
        }
    }
}

package com.xiaomou.handler.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiaomou.Result;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author MouHongDa
 * @date 2022/4/21 17:23
 */
@Component
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {
    /**
     *
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(Result.error().message("请登录")));
    }
}

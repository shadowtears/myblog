package com.xiaomou.handler.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author MouHongDa
 * @date 2022/4/21 17:17
 **/
public class MyAuthenticationException extends AuthenticationException {
    public MyAuthenticationException(String msg) {
        super(msg);
    }
}

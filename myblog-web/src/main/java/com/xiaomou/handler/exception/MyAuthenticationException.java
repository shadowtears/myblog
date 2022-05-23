package com.xiaomou.handler.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author MouHongDa
 * @date 2022/5/23 16:31
 */
public class MyAuthenticationException extends AuthenticationException {
    public MyAuthenticationException(String msg) {
        super(msg);
    }
}

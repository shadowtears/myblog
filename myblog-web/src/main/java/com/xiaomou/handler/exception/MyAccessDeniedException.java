package com.xiaomou.handler.exception;

import org.springframework.security.access.AccessDeniedException;

/**
 * @author MouHongDa
 * @date 2022/4/21 21:18
 */
public class MyAccessDeniedException extends AccessDeniedException {

    /**
     * Constructs an <code>AccessDeniedException</code> with the specified message.
     *
     * @param msg the detail message
     */
    public MyAccessDeniedException(String msg) {
        super(msg);
    }
}

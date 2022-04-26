package com.xiaomou.handler.exception;

import com.xiaomou.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author MouHongDa
 * @date 2022/4/20 17:24
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result err(Exception e) {
        log.error(e.toString());
        e.printStackTrace();
        return Result.error();
    }

    @ExceptionHandler(MyRuntimeException.class)
    @ResponseBody
    public Result err1(MyRuntimeException e) {
        log.error(e.getErrMsg());
        return Result.error().code(e.getCode()).message(e.getErrMsg());
    }

}

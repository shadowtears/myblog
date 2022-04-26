package com.xiaomou.handler.exception;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * @author MouHongDa
 * @date 2022/4/20 17:57
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyRuntimeException extends RuntimeException {

    @ApiModelProperty(value = "错误码")
    private Integer code;
    @ApiModelProperty(value = "错误信息")
    private String errMsg;

    public MyRuntimeException(String errMsg) {
        this.errMsg = errMsg;
    }
}

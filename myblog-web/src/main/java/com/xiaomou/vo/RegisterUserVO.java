package com.xiaomou.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author MouHongDa
 * @date 2022/4/25 18:20
 */
@Data
public class RegisterUserVO {

    /**
     * 用户名
     */

    @ApiModelProperty(name = "username", value = "用户名", required = true, dataType = "String")
    private String username;
    /**
     * 密码
     */
    @ApiModelProperty(name = "password", value = "密码", required = true, dataType = "String")
    private String password;

    @ApiModelProperty(name = "email", value = "邮箱", required = true, dataType = "String")
    private  String email;
    /**
     * 验证码
     */
    @ApiModelProperty(name = "code", value = "邮箱验证码", required = true, dataType = "String")
    private String code;


}

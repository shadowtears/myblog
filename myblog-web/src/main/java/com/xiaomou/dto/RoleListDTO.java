package com.xiaomou.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author MouHongDa
 * @date 2022/4/23 13:06
 */
@Data
public class RoleListDTO implements Serializable {
    @ApiModelProperty(value = "角色名")
    private String description;
    @ApiModelProperty(value = "角色名")
    private Integer total;
}

package com.xiaomou.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author MouHongDa
 * @date 2022/4/22 23:36
 */
@Data
public class UserQueryVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String roleName;
    private String nickName;
}

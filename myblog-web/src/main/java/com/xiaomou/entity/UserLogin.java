package com.xiaomou.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author xiaomou
 * @since 2022-04-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_user_login")
@ApiModel(value="UserLogin对象", description="")
public class UserLogin implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "user_login_id", type = IdType.AUTO)
    private Integer userLoginId;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "登录的ip地址")
    private String ipAddress;

    @ApiModelProperty(value = "ip地址来源")
    private String ipSources;

    @ApiModelProperty(value = "登录类型")
    private String loginType;

    @ApiModelProperty(value = "登录时间")
    private Date loginTime;

    @ApiModelProperty(value = "上次登录时间")
    private Date lastLoginTime;

    @ApiModelProperty(value = "登录头像")
    private String avatar;

    @ApiModelProperty(value = "用户角色")
    private String roleName;


}

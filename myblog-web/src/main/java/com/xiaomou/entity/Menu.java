package com.xiaomou.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
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
@TableName("tb_menu")
@ApiModel(value="Menu对象", description="后台菜单信息")
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "//后台菜单id")
    @TableId(value = "menu_id", type = IdType.AUTO)
    private Integer menuId;

    @ApiModelProperty(value = "//菜单列表名字")
    private String menuName;

    @ApiModelProperty(value = "//菜单的url")
    private String menuUrl;

    @ApiModelProperty(value = "//菜单的父ID")
    private Integer parentId;

    @ApiModelProperty(value = "//菜单排序")
    private Integer menuSort;

    @ApiModelProperty(value = "//描述")
    private String description;

    @ApiModelProperty(value = "//按钮图标")
    private String menuIcon;


}

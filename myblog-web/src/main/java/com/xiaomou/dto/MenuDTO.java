package com.xiaomou.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.xiaomou.entity.Menu;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MouHongDa
 * @date 2022/4/22 0:13
 */
@Data
public class MenuDTO {
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
    @ApiModelProperty(value = "//菜单图标")
    private String menuIcon;
    @ApiModelProperty(value = "//二级菜单目录")
    private List<MenuDTO> children = new ArrayList<>();

    public void isParent(Menu menu) {
        this.menuId = menu.getMenuId();
        this.menuName = menu.getMenuName();
        this.menuUrl = menu.getMenuUrl();
        this.parentId = menu.getParentId();
        this.menuSort = menu.getMenuSort();
        this.description = menu.getDescription();
        this.menuIcon = menu.getMenuIcon();
    }

    public MenuDTO isChildren(Menu menu) {
        this.menuId = menu.getMenuId();
        this.menuName = menu.getMenuName();
        this.menuUrl = menu.getMenuUrl();
        this.parentId = menu.getParentId();
        this.menuSort = menu.getMenuSort();
        this.description = menu.getDescription();
        this.menuIcon = menu.getMenuIcon();
        return this;
    }
}

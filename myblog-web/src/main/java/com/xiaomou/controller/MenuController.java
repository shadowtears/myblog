package com.xiaomou.controller;


import com.xiaomou.Result;
import com.xiaomou.dto.MenuDTO;
import com.xiaomou.service.MenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author xiaomou
 * @since 2022-04-20
 */
@RestController
@RequestMapping("/menu")
@Api(tags = "菜单展示模块")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @ApiOperation(value = "获取展示菜单列表")
    @GetMapping("/getMenuList")
    public Result getMenuList(@RequestParam("roleName") String roleName) {
        List<MenuDTO> menuList = menuService.getAllMenuListByUserRole(roleName);
        if (menuList == null) {
            return Result.error().message("你没有任何权限可以访问列表");
        } else {
            return Result.ok().data("menuList", menuList);
        }
    }
}


package com.xiaomou.controller;


import com.baomidou.mybatisplus.generator.config.IFileCreate;
import com.xiaomou.Result;
import com.xiaomou.dto.RoleListDTO;
import com.xiaomou.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/getRoleList")
    public Result getRoleList() {
        List<RoleListDTO> roleList = roleService.getRoleList();
        if (roleList.size() > 0) {
            return Result.ok().data("roleList", roleList);
        } else
            return Result.error();
    }
}


package com.xiaomou.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xiaomou.Result;
import com.xiaomou.entity.UserLogin;
import com.xiaomou.service.UserLoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "用户登录信息模块")
@RequestMapping("/userLogin")
public class UserLoginController {
    @Autowired
    private UserLoginService userLoginService;


    @ApiOperation(value = "分页获取用户登录信息列表")
    @GetMapping("/getUserInfoList")
    public Result getUserInfoList(Integer current, Integer size, String username) {
        IPage<UserLogin> page = userLoginService.getUserInfoList(current, size, username);
        long total = page.getTotal();
        List<UserLogin> data = page.getRecords();
        return Result.ok().data("total", total).data("data", data);
    }

}

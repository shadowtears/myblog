package com.xiaomou.controller;


import com.xiaomou.Result;
import com.xiaomou.dto.BlogHomeInfoDTO;
import com.xiaomou.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xiaomou
 * @since 2022-04-20
 */
@RestController
@Api(tags = "博客信息模块")
@RequestMapping("/blogInfo")
public class UserInfoController {
    @Autowired
    private UserInfoService userInfoService;

    @ApiOperation(value = "获取博主的基本信息")
    @GetMapping("/getBlogInfo")
    public Result getBlogInfo(){
        BlogHomeInfoDTO blogInfo = userInfoService.getBlogInfo();
        return  Result.ok().data("data", blogInfo);
    }
}


package com.xiaomou.controller;


import com.xiaomou.Result;
import com.xiaomou.dto.BlogHomeInfoDTO;
import com.xiaomou.service.UserInfoService;
import com.xiaomou.vo.UserInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author xiaomou
 * @since 2022-04-20
 */
@RestController
@Api(tags = "博客信息模块")
public class UserInfoController {
    @Autowired
    private UserInfoService userInfoService;

    @ApiOperation(value = "获取博主的基本信息")
    @GetMapping("/blogInfo/getBlogInfo")
    public Result getBlogInfo() {
        BlogHomeInfoDTO blogInfo = userInfoService.getBlogInfo();
        return Result.ok().data("data", blogInfo);
    }


    @ApiOperation(value = "修改用户资料")
    @PutMapping("/users/info")
    public Result updateUserInfo(@Valid @RequestBody UserInfoVO userInfoVO) {
        userInfoService.updateUserInfo(userInfoVO);
        return Result.ok().message("修改成功！");
    }

    @ApiOperation(value = "修改用户头像")
    @ApiImplicitParam(name = "file", value = "用户头像", required = true, dataType = "MultipartFile")
    @PostMapping("/users/avatar")
    public Result updateUserInfo(MultipartFile file) {
        String avatar = userInfoService.updateUserAvatar(file);
        return Result.ok().message("修改成功！").data("data",avatar);
    }
}


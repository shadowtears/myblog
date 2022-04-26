package com.xiaomou.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaomou.Result;
import com.xiaomou.ResultInfo;
import com.xiaomou.dto.UserListPageDTO;
import com.xiaomou.service.UserService;
import com.xiaomou.vo.RegisterUserVO;
import com.xiaomou.vo.UserQueryVO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    @ApiOperation(value = "根据用户角色和昵称分页查询用户列表")
    @RequestMapping(value = "/getUserList", method = RequestMethod.GET)
    public Result getUserList(@RequestParam(value = "current", required = true, defaultValue = "1") Integer current,
                              @RequestParam(value = "size", required = true, defaultValue = "5") Integer size,
                              UserQueryVO userQueryVO
    ) {
        Page<UserListPageDTO> page = new Page<>(current, size);
        IPage<UserListPageDTO> userListPage = userService.getUserListPage(page, userQueryVO);
        long total = userListPage.getTotal();
        List<UserListPageDTO> data = userListPage.getRecords();
        if (total > 0) {
            return Result.ok().data("data", data).data("total", total);
        } else {
            return Result.error().code(ResultInfo.NO_DATA_FOUND.getCode())
                    .message(ResultInfo.NO_DATA_FOUND.getMessage());
        }
    }

    @ApiOperation(value = "根据用户Id是否禁言")
    @PutMapping("/updateSilenceById")
    public Result updateSilenceById(@RequestParam(value = "id") Integer id,
                                    @RequestParam(value = "flag") boolean flag) {
        Integer integer = userService.updateSilenceById(id, flag);
        if (integer > 0) {
            return Result.ok().message("更新成功");
        } else {
            return Result.error();
        }
    }


    @ApiOperation(value = "发送邮箱验证码")
    @ApiImplicitParam(name = "email", value = "邮箱", required = true, dataType = "String")
    @GetMapping("/sendEmailCode")
    private Result sendCode(String email) {

        userService.sendCode(email);
        return  Result.ok().message("验证码发送成功,请注意查收");
    }


    @ApiOperation(value = "注册用户")
    @PostMapping("/registerUser")
    public Result registerUser(@RequestBody RegisterUserVO registerUserVO){

        boolean b = userService.registerUser(registerUserVO);
        return  Result.ok().message("注册成功,请前往登录");

    }
}


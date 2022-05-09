package com.xiaomou.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaomou.Result;
import com.xiaomou.ResultInfo;
import com.xiaomou.dto.UserListPageDTO;
import com.xiaomou.entity.User;
import com.xiaomou.service.UserService;
import com.xiaomou.vo.UserQueryVO;
import com.xiaomou.vo.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
@Api(tags = "用户模块")
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
    @ApiImplicitParam(name = "email", value = "用户名", required = true, dataType = "String")
    @GetMapping("/code")
    public Result sendCode(String email) {
        userService.sendCode(email);
        return Result.ok().message("发送成功！");
    }


    @ApiOperation(value = "用户注册")
    @PostMapping("/registerUser")
    public Result saveUser(@Valid @RequestBody UserVO user) {
        userService.saveUser(user);
        return Result.ok().message("注册成功！快去登录吧");
    }

    @ApiOperation(value = "修改密码")
    @PutMapping("/password")
    public Result updatePassword(@Valid @RequestBody UserVO user) {
        userService.updatePassword(user);
        return Result.ok().message("修改成功！快去登录吧");
    }
    @ApiOperation(value = "/分页单表查询用户列表")
    @GetMapping("/getUserListSignal")
    public Result  getUserListSignal(Integer current,Integer size,String nickname){
        QueryWrapper<User> queryWrapper=null;
        if(nickname!=null && nickname!=""){
            queryWrapper   = new QueryWrapper<>();
            queryWrapper.like("nickname", nickname);
        }
        IPage page = userService.page(new Page<>(current, size), queryWrapper);
        long total = page.getTotal();
        List data = page.getRecords();
        return  Result.ok().data("total", total).data("data", data);

    }
}


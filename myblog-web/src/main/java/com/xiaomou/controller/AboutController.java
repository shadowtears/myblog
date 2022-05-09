package com.xiaomou.controller;


import com.xiaomou.Result;
import com.xiaomou.dto.AboutDTO;
import com.xiaomou.mapper.AboutMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
@Api(tags = "关于模块")
public class AboutController {


    @Autowired
    private AboutMapper aboutMapper;



    @ApiOperation("/获取关于我的信息")
    @GetMapping("/getAbout")
    public Result getAbout(){
        AboutDTO about = aboutMapper.getAbout();
        return  Result.ok().data("data", about);
    }

    @ApiOperation(value = "更新关于我")
    @PutMapping("/updateAbout")
    public  Result    updateAbout(String aboutContent){
        int i = aboutMapper.updateAbout(aboutContent);
        return Result.ok();
    }

}

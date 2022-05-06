package com.xiaomou.controller;

import com.xiaomou.Result;
import com.xiaomou.service.AliOssService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author MouHongDa
 * @date 2022/5/5 13:01
 */
@RestController
@Api(tags = "阿里云对象存储ossAPI模块")
public class AliOssController {

    @Autowired
    private AliOssService aliOssService;

    @PostMapping("/uploadImage")
    @ApiOperation(value = "图片上传")
    public Result upload(MultipartFile file){
        String url = aliOssService.upload(file);
        return  Result.ok().data("url", url);
    }
}

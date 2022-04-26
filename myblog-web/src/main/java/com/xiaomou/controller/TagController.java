package com.xiaomou.controller;


import com.xiaomou.Result;
import com.xiaomou.dto.TagDTO;
import com.xiaomou.entity.Tag;
import com.xiaomou.mapper.TagMapper;
import com.xiaomou.service.TagService;
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
@RequestMapping("/tag")
@Api(tags = "标签模块")
public class TagController {
    @Autowired
    private TagService tagService;

    @ApiOperation(value = "后台获取所有的标签")
    @GetMapping("/getTagList")
    public Result getTagList() {
        List<Tag> tags = tagService.list();
        if (tags.size() > 0) {
            return Result.ok().data("data", tags);
        } else {
            return Result.error();
        }
    }

    @ApiOperation(value = "博客查看标签列表")
    @GetMapping("/tags")
    public Result tags(){
        //需要获取标签id，名称，和总共数量
        List<TagDTO> data = tagService.listTagDTO();
        int count = tagService.count();
        return  Result.ok().data("data", data).data("count", count);
    }

}


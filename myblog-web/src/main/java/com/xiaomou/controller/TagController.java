package com.xiaomou.controller;


import com.xiaomou.Result;
import com.xiaomou.dto.ArticlePreviewListDTO;
import com.xiaomou.dto.PageDTO;
import com.xiaomou.dto.TagDTO;
import com.xiaomou.entity.Category;
import com.xiaomou.entity.Tag;
import com.xiaomou.mapper.TagMapper;
import com.xiaomou.service.TagService;
import com.xiaomou.vo.ConditionVO;
import com.xiaomou.vo.TagVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@RequestMapping("/tag")
@Api(tags = "标签模块")
public class TagController {
    @Autowired
    private TagService tagService;

    @ApiOperation(value = "分页后台获取所有的标签")
    @GetMapping("/listTag")
    public Result listTagBackDTO(ConditionVO condition) {
        PageDTO<Tag> tagPageDTO = tagService.listTagBackDTO(condition);
        return Result.ok().message("查询成功").data("data", tagPageDTO);
    }

    @ApiOperation(value = "获取所有的标签")
    @GetMapping("/getTagList")
    public Result getTagList() {
        List<Tag> tagList = tagService.list();
        if (tagList.size() > 0) {
            return Result.ok().data("data", tagList);
        } else {
            return Result.error();
        }
    }

    @ApiOperation(value = "博客查看标签列表")
    @GetMapping("/tags")
    public Result tags() {
        //需要获取标签id，名称，和总共数量
        List<TagDTO> data = tagService.listTagDTO();
        int count = tagService.count();
        return Result.ok().data("data", data).data("count", count);
    }


    @ApiOperation(value = "查看标签下对应的文章")
    @GetMapping("/{tagId}")
    public Result listArticlesByTagId(@PathVariable(value = "tagId") Integer tagId,
                                      Integer current) {
        ArticlePreviewListDTO data = tagService.listTagsByTagId(tagId, current);
        return Result.ok().data("data", data);
    }

    @ApiOperation(value = "添加或修改标签")
    @PostMapping("/saveOrUpdateTag")
    public Result saveOrUpdateTag(@Valid @RequestBody TagVO tagVO) {
        tagService.saveOrUpdateTag(tagVO);
        return Result.ok().message("操作成功");
    }

    @ApiOperation(value = "删除标签")
    @DeleteMapping("/deleteTag")
    public Result deleteTag(@RequestBody List<Integer> tagIdList) {
        tagService.deleteTag(tagIdList);
        return Result.ok().message("删除成功");
    }
}


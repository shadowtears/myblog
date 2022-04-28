package com.xiaomou.controller;

import com.xiaomou.Result;
import com.xiaomou.dto.ArticlePreviewListDTO;
import com.xiaomou.dto.CategoryDTO;
import com.xiaomou.entity.Category;
import com.xiaomou.service.CategoryService;
import io.swagger.annotations.Api;
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
@Api(tags = "分类模块")
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @ApiOperation(value = "获取所有的标签")
    @GetMapping("/getCategoryList")
    public Result getTagList() {
        List<Category> categoryList = categoryService.list();
        if (categoryList.size() > 0) {
            return Result.ok().data("data", categoryList);
        } else {
            return Result.error();
        }
    }

    @ApiOperation(value = "查看博客展示分类列表")
    @GetMapping("/listCategories")
    public Result listCategories() {
        List<CategoryDTO> data = categoryService.listCategories();
        Integer count = categoryService.getBaseMapper().selectCount(null);
        return Result.ok().data("data", data).data("count", count);

    }

    @ApiOperation(value = "查看分类下对应的文章")
    @GetMapping("/{categoryId}")
    public Result listArticlesByCategoryId(@PathVariable(value = "categoryId") Integer categoryId,
                                           Integer current) {

        ArticlePreviewListDTO data = categoryService.listArticlesByCategoryId(categoryId, current);
        return Result.ok().data("data", data);
    }
}


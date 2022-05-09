package com.xiaomou.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaomou.Result;
import com.xiaomou.dto.ArticlePreviewListDTO;
import com.xiaomou.dto.CategoryDTO;
import com.xiaomou.entity.Category;
import com.xiaomou.service.CategoryService;
import com.xiaomou.vo.AddOrEditCategoryVO;
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

    @ApiOperation(value = "获取所有的分类")
    @GetMapping("/getCategoryList")
    public Result getTagList() {
        List<Category> categoryList = categoryService.list();
        if (categoryList.size() > 0) {
            return Result.ok().data("data", categoryList);
        } else {
            return Result.error();
        }
    }

    @ApiOperation(value = "分页条件查询分类列表")
    @GetMapping("/listCategory")
    public Result listTags(@RequestParam(value = "current", required = true, defaultValue = "1") Integer current,
                           @RequestParam(value = "size", required = true, defaultValue = "5") Integer size,
                           @RequestParam(value = "categoryName", required = false) String categoryName) {

        Page<Category> page = new Page<>(current, size);
        QueryWrapper<Category> wrapper = new QueryWrapper<>();
        if (categoryName != null && categoryName != "") {
            wrapper.like("category_name", categoryName);
        }
        Page<Category> tagPage = categoryService.page(page, wrapper);
        long total = tagPage.getTotal();
        List<Category> data = tagPage.getRecords();
        if (total > 0) {
            return Result.ok().data("data", data).data("total", total);
        } else {
            return Result.ok().message("没有相关数据");
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

    @ApiOperation(value = "新增或者编辑分类")
    @PostMapping("/addOrEditCategory")
    public Result addOrEditCategory(@RequestBody AddOrEditCategoryVO addOrEditCategoryVO) {
        boolean b = categoryService.addOrEditCategory(addOrEditCategoryVO);
        if (b) {
            return Result.ok();
        } else {
            throw new RuntimeException();
        }
    }

    @ApiOperation(value = "根据id删除分类")
    @DeleteMapping("/deleteCategory")
    public Result deleteCategories(@RequestBody List<Integer> categoryIdList) {
        categoryService.deleteCategory(categoryIdList);
        return Result.ok().message("删除成功");
    }
}


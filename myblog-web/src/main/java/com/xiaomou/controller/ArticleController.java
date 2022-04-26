package com.xiaomou.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xiaomou.Result;
import com.xiaomou.dto.ArchiveDTO;
import com.xiaomou.dto.ArticleHomeDTO;
import com.xiaomou.dto.ListArticleDTO;
import com.xiaomou.entity.Article;
import com.xiaomou.service.ArticleService;
import com.xiaomou.vo.SaveOrUpdateArticleVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xiaomou
 * @since 2022-04-20
 */
@RestController
@Api(tags = "文章模块")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @ApiOperation(value = "保存或者更新文章")
    @PostMapping("/article/saveOrUpdateArticle")
    public Result saveOrUpdateArticle(@RequestBody SaveOrUpdateArticleVO saveOrUpdateArticleVO) {

        if (saveOrUpdateArticleVO != null) {
            articleService.saveOrUpdateArticle(saveOrUpdateArticleVO);
            return Result.ok();
        } else {
            return Result.error();
        }
    }


    @ApiOperation(value = "根据文章标题分页查询文章列表")
    @GetMapping("/article/listArticle")
    public Result listArticle(@RequestParam(value = "current",required = true,defaultValue = "1") Integer current,
                              @RequestParam(value = "size",required = true,defaultValue = "5")Integer size,
                              @RequestParam(value = "articleTitle",required = false) String articleTitle){

        //获取到了这个数据 但是还要拿到总条数
        List<ListArticleDTO> data = articleService.getArticleList(current, size, articleTitle);
        //
        int total = articleService.count();

        return Result.ok().data("data", data).data("total", total);
    }


    @ApiOperation(value = "根据文章id查询文章信息")
    @GetMapping("/article/getArticleById")
    public Result getArticleByArticleId(@RequestParam(value = "articleId",required = true) Integer articleId){

        SaveOrUpdateArticleVO data = articleService.getArticleById(articleId);

        return  Result.ok().data("data", data);

    }

    @ApiOperation(value = "查看首页文章")
    @ApiImplicitParam(name = "current", value = "当前页码", required = true, dataType = "Long",example = "1")
    @GetMapping("/article/articles")
    private Result listArticles(Long current) {

        List<ArticleHomeDTO> data = articleService.listArticles(current);
        return  Result.ok().data("data",data);
    }

    @ApiOperation(value = "查询文章归档")
    @GetMapping("/articles/archives")
    public Result listArchives(@RequestParam(value = "current",required = true,defaultValue = "1") Integer current){

        IPage<ArchiveDTO> archives = articleService.listArchives(current);
        List<ArchiveDTO> data = archives.getRecords();
        long count = archives.getTotal();
        return  Result.ok().data("data" , data).data("count", count);
    }

    @ApiOperation(value = "查看首页文章")
    @GetMapping("/articles/{articleId}")
    public Result getArticleById(@PathVariable("articleId") Integer articleId){
        Article article = articleService.getById(articleId);
        return  Result.ok().data("data", article);
    }


}


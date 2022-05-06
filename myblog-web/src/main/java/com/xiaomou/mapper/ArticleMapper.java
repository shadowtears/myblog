package com.xiaomou.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaomou.dto.*;
import com.xiaomou.entity.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xiaomou
 * @since 2022-04-20
 */
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {

    List<ListArticleDTO> getArticleList(@Param("current")Integer current, @Param("size")Integer size, @Param("articleTitle") String articleTitle);

    List<ArticleHomeDTO> listArticles(Long current);

    IPage<ArchiveDTO>  listArchives(Page<ArchiveDTO> page);

    IPage<ArticlePreviewDTO> listArticlesByCondition(Page<ArticlePreviewDTO> page, @Param(value = "categoryId") Integer categoryId);

    IPage<ArticlePreviewDTO> listTagsByCondition(Page<ArticlePreviewDTO> page,@Param(value = "tagId") Integer tagId);
}

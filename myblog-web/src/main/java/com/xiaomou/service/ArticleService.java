package com.xiaomou.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xiaomou.dto.*;
import com.xiaomou.entity.Article;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaomou.vo.ConditionVO;
import com.xiaomou.vo.SaveOrUpdateArticleVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xiaomou
 * @since 2022-04-20
 */
public interface ArticleService extends IService<Article> {

    int saveOrUpdateArticle(SaveOrUpdateArticleVO articleVO);

    List<ListArticleDTO> getArticleList(Integer current, Integer size, String articleTitle);


    SaveOrUpdateArticleVO  getArticleById(Integer articleId);

    List<ArticleHomeDTO> listArticles(Long current);

    IPage<ArchiveDTO> listArchives(Integer current);

    List<ArticleSearchDTO> listArticlesBySearch(ConditionVO condition);

    void saveArticleLike(Integer articleId);

    ArticleDTO myGetById(Integer articleId);

    void updateArticleTop(Integer articleId, Integer isTop);

    int deleteArticleById(Integer articleId);
}

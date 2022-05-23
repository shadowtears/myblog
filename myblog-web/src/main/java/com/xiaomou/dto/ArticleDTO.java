package com.xiaomou.dto;

import com.xiaomou.entity.Article;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author MouHongDa
 * @date 2022/5/5 16:15
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDTO {
    /**
     * id
     */
    private Integer articleId;

    /**
     * 文章缩略图
     */
    private String articleCover;

    /**
     * 标题
     */
    private String articleTitle;

    /**
     * 内容
     */
    private String articleContent;

    private Date createTime;

    private Date updateTime;

    private Integer categoryId;

    private String categoryName;

    /**
     * 点赞量
     */
    private Integer likeCount;

    /**
     * 浏览量
     */
    private Integer viewsCount;

    public ArticleDTO(Article article) {
        this.articleId = article.getArticleId();
        this.articleCover = article.getArticleCover();
        this.categoryId = article.getCategoryId();
        this.articleContent = article.getArticleContent();
        this.articleTitle = article.getArticleTitle();
        this.createTime = article.getCreateTime();
        this.updateTime = article.getUpdateTime();
    }
}

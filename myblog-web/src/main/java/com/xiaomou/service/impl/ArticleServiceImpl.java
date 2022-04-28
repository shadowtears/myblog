package com.xiaomou.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaomou.dto.ArchiveDTO;
import com.xiaomou.dto.ArticleHomeDTO;
import com.xiaomou.dto.ArticleSearchDTO;
import com.xiaomou.dto.ListArticleDTO;
import com.xiaomou.entity.Article;
import com.xiaomou.entity.ArticleTag;
import com.xiaomou.mapper.ArticleMapper;
import com.xiaomou.service.ArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaomou.service.ArticleTagService;
import com.xiaomou.service.CommentService;
import com.xiaomou.util.HTMLUtil;
import com.xiaomou.vo.ConditionVO;
import com.xiaomou.vo.SaveOrUpdateArticleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.xiaomou.constant.CommonConst.POST_TAG;
import static com.xiaomou.constant.CommonConst.PRE_TAG;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author xiaomou
 * @since 2022-04-20
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    @Autowired
    private ArticleTagService articleTagService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private ArticleMapper articleMapper;

    /**
     * 更新或者新增文章
     *
     * @param articleVO
     * @return
     */
    @Transactional
    @Override
    public int saveOrUpdateArticle(SaveOrUpdateArticleVO articleVO) {
        //如果是更新 那么articleId 不可能是0或者空记得开启事务
        Integer articleId = articleVO.getArticleId();
        Article article = new Article(articleVO);
        //更新状态
        if (articleId != null
                && !articleVO.getIsDraft()) {
//          如果是更新就先删除原来的所有标签 然后在新增进去标签 分类在文章里面 直接更新即可
            //在关联表里面删除
            QueryWrapper<ArticleTag> wrapper = new QueryWrapper<>();
            wrapper.eq("article_id", articleId);
            articleTagService.remove(wrapper);
        }
        //开始更新
        articleService.saveOrUpdate(article);
        //还要插入这个文章所属的标签
        //获取需要插入的标签id列表
        List<Integer> tagIdList = articleVO.getTagIdList();
        ArrayList<ArticleTag> articleTags = new ArrayList<>();
        ArticleTag articleTag;
        for (Integer tagId : tagIdList) {
            //新增的有个自增id  更新的话也是从里面获取id
            articleTag = new ArticleTag(tagId, article.getArticleId());
            articleTags.add(articleTag);
        }
        //批量保存标签
        boolean b1 = articleTagService.saveBatch(articleTags);
        return 1;
    }

    @Override
    public List<ListArticleDTO> getArticleList(Integer current, Integer size, String articleTitle) {
        return this.baseMapper.getArticleList((current - 1) * size, size, articleTitle);
    }

    @Override
    public SaveOrUpdateArticleVO getArticleById(Integer articleId) {
        //获取当前文章的信息
        System.out.println(articleId);
        Article article = articleService.getById(articleId);
        //在获取当前文章的标签列表id信息
        QueryWrapper<ArticleTag> wrapper = new QueryWrapper<>();
        wrapper.eq("article_id", articleId);
        List<ArticleTag> articleTags = articleTagService.list(wrapper);
        ArrayList<Integer> tagsId = new ArrayList<>();
        for (ArticleTag articleTag : articleTags) {
            tagsId.add(articleTag.getTagId());
        }
        SaveOrUpdateArticleVO articleVO = new SaveOrUpdateArticleVO(article.getArticleId(),
                article.getArticleTitle(), article.getArticleContent(), article.getArticleCover(), article.getCategoryId(), article.getIsTop(), article.getIsDraft(), tagsId);
        return articleVO;
    }

    @Override
    public List<ArticleHomeDTO> listArticles(Long current) {
        List<ArticleHomeDTO> articleHomeDTOS = this.baseMapper.listArticles((current - 1) * 10);
        //文章内容过滤markdown标签展示
        for (ArticleHomeDTO articleDTO : articleHomeDTOS) {
            articleDTO.setArticleContent(HTMLUtil.deleteArticleTag(articleDTO.getArticleContent()));
        }
        return articleHomeDTOS;
    }

    @Override
    public IPage<ArchiveDTO> listArchives(Integer current) {
        Page<ArchiveDTO> page = new Page<>(current, 10);
        IPage<ArchiveDTO> archives = this.baseMapper.listArchives(page);
        return archives;

    }

    @Override
    public List<ArticleSearchDTO> listArticlesBySearch(ConditionVO condition) {
        String keywords = condition.getKeywords();
        // 判空
        if (StringUtils.isBlank(keywords)) {
            return new ArrayList<>();
        }
        // 搜索文章
        List<Article> articleList = articleMapper.selectList(new LambdaQueryWrapper<Article>()
                .and(i -> i.like(Article::getArticleTitle, keywords)
                        .or()
                        .like(Article::getArticleContent, keywords)));
        // 高亮处理
        return articleList.stream().map(item -> {
            // 获取关键词第一次出现的位置
            String articleContent = item.getArticleContent();
            int index = item.getArticleContent().indexOf(keywords);
            if (index != -1) {
                // 获取关键词前面的文字
                int preIndex = index > 25 ? index - 25 : 0;
                String preText = item.getArticleContent().substring(preIndex, index);
                // 获取关键词到后面的文字
                int last = index + keywords.length();
                int postLength = item.getArticleContent().length() - last;
                int postIndex = postLength > 175 ? last + 175 : last + postLength;
                String postText = item.getArticleContent().substring(index, postIndex);
                // 文章内容高亮
                articleContent = (preText + postText).replaceAll(keywords, PRE_TAG + keywords + POST_TAG);
            }
            // 文章标题高亮
            String articleTitle = item.getArticleTitle().replaceAll(keywords, PRE_TAG + keywords + POST_TAG);
            return ArticleSearchDTO.builder()
                    .id(item.getArticleId())
                    .articleTitle(articleTitle)
                    .articleContent(articleContent)
                    .build();
        }).collect(Collectors.toList());
    }
}

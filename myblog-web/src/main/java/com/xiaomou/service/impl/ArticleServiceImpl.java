package com.xiaomou.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaomou.dto.ArchiveDTO;
import com.xiaomou.dto.ArticleHomeDTO;
import com.xiaomou.dto.ListArticleDTO;
import com.xiaomou.entity.Article;
import com.xiaomou.entity.ArticleTag;
import com.xiaomou.mapper.ArticleMapper;
import com.xiaomou.service.ArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaomou.service.ArticleTagService;
import com.xiaomou.service.CommentService;
import com.xiaomou.util.HTMLUtil;
import com.xiaomou.vo.SaveOrUpdateArticleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
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
    private  ArticleService articleService;
    @Autowired
    private CommentService commentService;

    /**
     *  更新或者新增文章
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
        if(       articleId !=null
                && !articleVO.getIsDraft()){
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
        for( Integer tagId:tagIdList){
            //新增的有个自增id  更新的话也是从里面获取id
            articleTag=new ArticleTag(tagId,article.getArticleId());
            articleTags.add(articleTag);
        }
        //批量保存标签
        boolean b1 = articleTagService.saveBatch(articleTags);
        return 1;
    }

    @Override
    public List<ListArticleDTO> getArticleList(Integer current, Integer size, String articleTitle) {
        return  this.baseMapper.getArticleList((current-1)*size,size,articleTitle);
    }

    @Override
    public SaveOrUpdateArticleVO getArticleById(Integer articleId) {
        //获取当前文章的信息
        System.out.println(articleId);
        Article article = articleService.getById(articleId);
        //在获取当前文章的标签列表id信息
        QueryWrapper<ArticleTag> wrapper = new QueryWrapper<>();
        wrapper.eq("article_id",articleId);
        List<ArticleTag> articleTags = articleTagService.list(wrapper);
        ArrayList<Integer> tagsId = new ArrayList<>();
        for ( ArticleTag articleTag  : articleTags){
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
}

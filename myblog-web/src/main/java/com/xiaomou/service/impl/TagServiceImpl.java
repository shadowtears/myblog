package com.xiaomou.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaomou.dto.ArticlePreviewDTO;
import com.xiaomou.dto.ArticlePreviewListDTO;
import com.xiaomou.dto.PageDTO;
import com.xiaomou.dto.TagDTO;
import com.xiaomou.entity.ArticleTag;
import com.xiaomou.entity.Tag;
import com.xiaomou.handler.exception.MyRuntimeException;
import com.xiaomou.mapper.ArticleMapper;
import com.xiaomou.mapper.ArticleTagMapper;
import com.xiaomou.mapper.TagMapper;
import com.xiaomou.service.TagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaomou.vo.ConditionVO;
import com.xiaomou.vo.TagVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author xiaomou
 * @since 2022-04-20
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private ArticleTagMapper articleTagMapper;
    @Autowired
    private TagMapper tagMapper;

    @Override
    public List<TagDTO> listTagDTO() {
        List<TagDTO> tagDTOS = this.baseMapper.listTagDTO();
        return tagDTOS;
    }


    @Override
    public ArticlePreviewListDTO listTagsByTagId(Integer tagId, Integer current) {
        //转换页码
        Page<ArticlePreviewDTO> page = new Page<>(current, 9);
        IPage<ArticlePreviewDTO> articles = articleMapper.listTagsByCondition(page, tagId);
        List<ArticlePreviewDTO> records = articles.getRecords();
        String name = null;
        return new ArticlePreviewListDTO(records, name);
    }

    @Override
    public PageDTO<Tag> listTagBackDTO(ConditionVO condition) {
        // 分页查询标签列表
        Page<Tag> page = new Page<>(condition.getCurrent(), condition.getSize());
        Page<Tag> tagPage = tagMapper.selectPage(page, new LambdaQueryWrapper<Tag>()
                .select(Tag::getTagId, Tag::getTagName, Tag::getCreateTime)
                .like(StringUtils.isNotBlank(condition.getKeywords()), Tag::getTagName, condition.getKeywords())
                .orderByDesc(Tag::getCreateTime));
        return new PageDTO<>(tagPage.getRecords(), (int) tagPage.getTotal());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteTag(List<Integer> tagIdList) {
        //查询标签下是否有文章
        Integer count = articleTagMapper.selectCount(new LambdaQueryWrapper<ArticleTag>()
                .in(ArticleTag::getTagId, tagIdList));
        if (count > 0) {
            throw new MyRuntimeException("删除失败，该标签下存在文章");
        }
        tagMapper.deleteBatchIds(tagIdList);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveOrUpdateTag(TagVO tagVO) {
        Integer count = tagMapper.selectCount(new LambdaQueryWrapper<Tag>()
                .eq(Tag::getTagName, tagVO.getTagName()));
        if (count > 0) {
            throw new MyRuntimeException("标签名已存在");
        }
        Tag tag = Tag.builder()
                .tagId(tagVO.getTagId())
                .tagName(tagVO.getTagName())
                .createTime(Objects.isNull(tagVO.getTagId()) ? new Date() : null)
                .build();
        this.saveOrUpdate(tag);
    }

}

package com.xiaomou.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaomou.dto.ArticlePreviewDTO;
import com.xiaomou.dto.ArticlePreviewListDTO;
import com.xiaomou.dto.TagDTO;
import com.xiaomou.entity.Tag;
import com.xiaomou.mapper.ArticleMapper;
import com.xiaomou.mapper.ArticleTagMapper;
import com.xiaomou.mapper.TagMapper;
import com.xiaomou.service.TagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}

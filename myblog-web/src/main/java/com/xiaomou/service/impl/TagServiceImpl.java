package com.xiaomou.service.impl;

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
 *  服务实现类
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
        return  tagDTOS;
    }
}

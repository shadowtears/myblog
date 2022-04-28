package com.xiaomou.service;

import com.xiaomou.dto.ArticlePreviewListDTO;
import com.xiaomou.dto.TagDTO;
import com.xiaomou.entity.Tag;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xiaomou
 * @since 2022-04-20
 */
public interface TagService extends IService<Tag> {

    List<TagDTO> listTagDTO();

    ArticlePreviewListDTO listTagsByTagId(Integer tagId, Integer current);
}

package com.xiaomou.mapper;

import com.xiaomou.dto.TagDTO;
import com.xiaomou.entity.Tag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

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
public interface TagMapper extends BaseMapper<Tag> {

    List<TagDTO> listTagDTO();
}

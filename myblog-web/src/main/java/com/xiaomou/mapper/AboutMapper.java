package com.xiaomou.mapper;

import com.xiaomou.dto.AboutDTO;
import com.xiaomou.entity.About;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author xiaomou
 * @since 2022-04-20
 */
@Mapper
public interface AboutMapper extends BaseMapper<About> {


    AboutDTO getAbout();

    int updateAbout(String aboutContent);
}

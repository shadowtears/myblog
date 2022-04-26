package com.xiaomou.mapper;

import com.xiaomou.dto.MenuDTO;
import com.xiaomou.entity.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author xiaomou
 * @since 2022-04-20
 */
public interface MenuMapper extends BaseMapper<Menu> {
    //查询后台菜单
    List<Menu> getAllMenuList(@Param("roleName") String roleName);
}

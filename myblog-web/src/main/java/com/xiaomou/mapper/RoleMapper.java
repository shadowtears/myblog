package com.xiaomou.mapper;

import com.xiaomou.dto.RoleListDTO;
import com.xiaomou.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
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
@Mapper
public interface RoleMapper extends BaseMapper<Role> {
    //根据角色获得个数
    List<RoleListDTO> getRoleList();

    //用户注册给与普通用户的权限
    boolean insertNewUser(@Param("userId") Integer userId, @Param("roleId") Integer roleId);
}

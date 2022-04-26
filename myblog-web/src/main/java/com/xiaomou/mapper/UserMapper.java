package com.xiaomou.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaomou.dto.UserListPageDTO;
import com.xiaomou.entity.Api;
import com.xiaomou.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaomou.vo.UserQueryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

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
public interface UserMapper extends BaseMapper<User> {

    //根据用户名查询用户
    List<String> listUserRolesByUsername(@Param("username") String username);

    //根据条件查询用户
    IPage<UserListPageDTO> getUserListPage(Page<UserListPageDTO> page,@Param("userQueryVO") UserQueryVO userQueryVO);

    List<Api>  getApiUrlByUserName(@Param("username") String username);
}

package com.xiaomou.service;

import com.xiaomou.dto.RoleListDTO;
import com.xiaomou.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author xiaomou
 * @since 2022-04-20
 */
public interface RoleService extends IService<Role> {

    List<RoleListDTO> getRoleList();
}

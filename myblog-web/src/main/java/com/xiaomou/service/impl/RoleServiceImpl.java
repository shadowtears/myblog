package com.xiaomou.service.impl;

import com.xiaomou.dto.RoleListDTO;
import com.xiaomou.entity.Role;
import com.xiaomou.mapper.RoleMapper;
import com.xiaomou.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Override
    public List<RoleListDTO> getRoleList() {
        List<RoleListDTO> roleList = this.baseMapper.getRoleList();
        return roleList;
    }
}

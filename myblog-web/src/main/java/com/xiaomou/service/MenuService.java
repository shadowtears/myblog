package com.xiaomou.service;

import com.xiaomou.dto.MenuDTO;
import com.xiaomou.entity.Menu;
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
public interface MenuService extends IService<Menu> {
    //通过角色名查找对应的菜单
    List<MenuDTO> getAllMenuListByUserRole(String roleName);
}

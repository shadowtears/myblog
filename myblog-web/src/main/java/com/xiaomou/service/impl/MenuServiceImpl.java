package com.xiaomou.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xiaomou.dto.MenuDTO;
import com.xiaomou.entity.Menu;
import com.xiaomou.mapper.MenuMapper;
import com.xiaomou.service.MenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
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
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {


    @Override
    public List<MenuDTO> getAllMenuListByUserRole(String roleName) {
        //查出来所有角色对应有用的菜单
        List<Menu> menuList = this.baseMapper.getAllMenuList(roleName);
        //再对菜单进行处理，返回前端需要的菜单格式
        List<MenuDTO> menuDTOList = new ArrayList<>();
        boolean[] visited = new boolean[menuList.size()];
        Arrays.fill(visited, false);
//       false表示未访问true表示已经访问
        for (int i = 0; i < menuList.size(); i++) {
            if (menuList.get(i).getParentId() == null || menuList.get(i).getParentId() == 0) {
                visited[i] = true;
                MenuDTO menuDTO = new MenuDTO();
                Menu menu = menuList.get(i);
                menuDTO.isParent(menuList.get(i));
                findChildren(menuList, visited, menuDTO, menuDTOList);
                menuDTOList.add(menuDTO);
            }
        }
//        try {
//            for (Menu menu : menuList) {
//                //顶级父类
//                MenuDTO menuDTO = new MenuDTO();
//                if (menu.getParentId() == null || menu.getParentId() == 0) {
//                    menuDTO.isParent(menu);
//                    for (Menu children : menuList) {
//                        if (menu.getMenuId() == children.getParentId()) {
//                            menuDTO.getChildren().add(new MenuDTO().isChildren(children));
//                        }
//                    }
//                    menuDTOList.add(menuDTO);
//                }
//            }
//        } catch (Exception e) {
//            throw e;
//        }

        return menuDTOList;
    }

    public void findChildren(List<Menu> menuList, boolean[] visited, MenuDTO menuDTO, List<MenuDTO> menuDTOList) {
        for (int i = 0; i < menuList.size(); i++) {
            if (!visited[i] && menuDTO.getMenuId() == menuList.get(i).getParentId()) {
                visited[i] = true;
                menuDTO.getChildren().add(new MenuDTO().isChildren(menuList.get(i)));
            }
        }
        if (menuDTO.getChildren().size() == 0) {
            return;
        }
        for (MenuDTO menuDTO1 : menuDTO.getChildren()) {
            findChildren(menuList, visited, menuDTO1, menuDTOList);
        }
    }
}

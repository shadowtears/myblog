package com.xiaomou.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaomou.dto.UserListPageDTO;
import com.xiaomou.entity.Api;
import com.xiaomou.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaomou.vo.RegisterUserVO;
import com.xiaomou.vo.UserQueryVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author xiaomou
 * @since 2022-04-20
 */
public interface UserService extends IService<User> {

    //根据用户名获取用户信息
    User getUserByUsername(String username);

    //根据用户名获得权限信息
    List<String> listUserRolesByUsername(String username);

    //根据用户名更新密码
    Integer update(String username, String password);

    //根据用户条件查询用户
    IPage<UserListPageDTO> getUserListPage(Page<UserListPageDTO> page, UserQueryVO userQueryVO);

    //根据用户id改变禁言状态
    Integer updateSilenceById(Integer id, boolean flag);

    //根据用户名获得api
    List<Api> getApiUrlByUserName (String username);
    /**
     * 发送邮箱验证码
     *
     * @param email 邮箱号
     */
    void sendCode(String email);


    boolean  registerUser(RegisterUserVO registerUserVO);
}

package com.xiaomou.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xiaomou.entity.UserLogin;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author xiaomou
 * @since 2022-04-20
 */
public interface UserLoginService extends IService<UserLogin> {

    IPage<UserLogin> getUserInfoList(Integer current, Integer size, String username);

}

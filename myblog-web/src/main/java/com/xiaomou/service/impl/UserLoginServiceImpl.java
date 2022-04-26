package com.xiaomou.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaomou.entity.UserLogin;
import com.xiaomou.mapper.UserLoginMapper;
import com.xiaomou.service.UserLoginService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author xiaomou
 * @since 2022-04-20
 */
@Service
public class UserLoginServiceImpl extends ServiceImpl<UserLoginMapper, UserLogin> implements UserLoginService {
    @Override
    public IPage<UserLogin> getUserInfoList(Integer current, Integer size, String nickname) {
        Page<UserLogin> page = new Page<UserLogin>(current, size);
        LambdaQueryWrapper<UserLogin> wrapper = null;
        if (nickname != null && nickname != "") {
            wrapper = new LambdaQueryWrapper<>();
            wrapper.like(UserLogin::getNickname, nickname);
        }
        Page<UserLogin> userLoginPage = this.baseMapper.selectPage(page, wrapper);
        return userLoginPage;
    }
}

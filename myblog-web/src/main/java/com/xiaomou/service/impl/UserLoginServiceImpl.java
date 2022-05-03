package com.xiaomou.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaomou.entity.UserLogin;
import com.xiaomou.mapper.UserLoginMapper;
import com.xiaomou.service.UserLoginService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.util.Objects;

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
    public IPage<UserLogin> getUserInfoList(Integer current, Integer size, String username) {
        Page<UserLogin> page = new Page<UserLogin>(current, size);
        LambdaQueryWrapper<UserLogin> wrapper = null;
        if (username != null && username != "") {
            wrapper = new LambdaQueryWrapper<>();
            wrapper.like(UserLogin::getUserName, username);
        }
        Page<UserLogin> userLoginPage = this.baseMapper.selectPage(page, wrapper);
        return userLoginPage;
    }

    @Override
    public boolean saveOrUpdate(UserLogin userLogin) {
        if (null == userLogin) {
            return false;
        } else {
            UserLogin login = this.getOne(new LambdaQueryWrapper<UserLogin>().eq(UserLogin::getUserName, userLogin.getUserName()));
//          如果用户登录信息不存在就插入
            if (ObjectUtils.isEmpty(login)) {
                this.save(userLogin);
            }
//          如果用户登录信息存在就更新
            else {
                this.update(userLogin, new LambdaUpdateWrapper<UserLogin>().eq(UserLogin::getUserName, userLogin.getUserName()));
            }
            return true;
        }
    }
}

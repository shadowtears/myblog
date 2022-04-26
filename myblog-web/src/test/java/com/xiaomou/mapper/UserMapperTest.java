package com.xiaomou.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaomou.MyblogMain;
import com.xiaomou.entity.UserLogin;
import com.xiaomou.service.UserLoginService;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author MouHongDa
 * @date 2022/4/21 16:47
 */
@SpringBootApplication
@SpringBootTest(classes = MyblogMain.class)
@MapperScan("com.xiaomou.mapper")
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserLoginService userLoginService;
    @Autowired
    private UserLoginMapper userLoginMapper;

    @Test
    void listUserRolesByUsername() {
        Page<UserLogin> page = new Page<UserLogin>(1, 2);
//        System.out.println(userMapper.listUserRolesByUsername("admin"));
        LambdaQueryWrapper<UserLogin> wrapper = null;
        wrapper = new LambdaQueryWrapper<>();
        wrapper.like(UserLogin::getNickname, "test");
        System.out.println(userLoginMapper.selectPage(page, wrapper).getRecords());
    }
}
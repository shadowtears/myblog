package com.xiaomou.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaomou.dto.BlogHomeInfoDTO;
import com.xiaomou.entity.User;
import com.xiaomou.entity.UserInfo;
import com.xiaomou.entity.UserLogin;
import com.xiaomou.mapper.*;
import com.xiaomou.service.UserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private UserLoginMapper userLoginMapper;


    @Override
    public BlogHomeInfoDTO getBlogInfo() {
        //昵称头像简介 公告 文章数量  分类数量 标签数量  公告 访问量
        //公告先写死 访问量后面在弄
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("nickname", "avatar", "intro").eq("username", "admin@qq.com");
        User user = userMapper.selectOne(queryWrapper);
        //获取文章数量
        Integer articleCount = articleMapper.selectCount(null);
        //分类数量
        Integer categoryCount = categoryMapper.selectCount(null);
        //标签数量
        Integer tagCount = tagMapper.selectCount(null);
        //公告
        String notice = "来了一个小滑稽,点个赞在走吧";
        QueryWrapper<UserLogin> wrapper = new QueryWrapper<>();
        wrapper.select("DISTINCT ip_address");
        Integer viewsCount = userLoginMapper.selectCount(wrapper);
        BlogHomeInfoDTO blogHomeInfoDTO
                = new BlogHomeInfoDTO(user.getNickname(),
                user.getAvatar(),
                user.getIntro(),
                articleCount,
                categoryCount,
                tagCount,
                notice,
                viewsCount);
        return blogHomeInfoDTO;
    }
}

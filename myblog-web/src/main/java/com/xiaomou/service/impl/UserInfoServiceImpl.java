package com.xiaomou.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xiaomou.dto.BlogHomeInfoDTO;
import com.xiaomou.entity.User;
import com.xiaomou.entity.UserInfo;
import com.xiaomou.entity.UserLogin;
import com.xiaomou.mapper.*;
import com.xiaomou.service.AliOssService;
import com.xiaomou.service.UserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaomou.util.UserUtil;
import com.xiaomou.vo.UserInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.Objects;

import static com.xiaomou.constant.RedisPrefixConst.IP_SET;
import static com.xiaomou.constant.RedisPrefixConst.NOTICE;

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
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private AliOssService aliOssService;


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
        Object value = redisTemplate.boundValueOps(NOTICE).get();
        String notice = Objects.nonNull(value) ? value.toString() : "发布你的第一篇公告吧";
        Long viewsCount = redisTemplate.boundSetOps(IP_SET).size();
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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateUserInfo(UserInfoVO userInfoVO) {
        // 封装用户信息
        userMapper.update(null, new LambdaUpdateWrapper<User>()
                .set(User::getNickname, userInfoVO.getNickname())
                .set(User::getIntro, userInfoVO.getIntro())
                .set(User::getUpdateTime, new Date())
                .eq(User::getUserId, UserUtil.getLoginUser().getUserId()));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String updateUserAvatar(MultipartFile file) {
        // 头像上传oss，返回图片地址
        String avatar = aliOssService.upload(file);
        // 更新用户信息
        User user = User.builder()
                .userId(UserUtil.getLoginUser().getUserId())
                .avatar(avatar)
                .build();
        userMapper.updateById(user);
        return avatar;
    }
}

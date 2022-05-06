package com.xiaomou.service.impl.auth;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xiaomou.entity.User;
import com.xiaomou.handler.auth.MyAuthenticationFailureHandler;
import com.xiaomou.handler.auth.MyAuthenticationSuccessHandler;
import com.xiaomou.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.xiaomou.constant.RedisPrefixConst.ARTICLE_USER_LIKE;
import static com.xiaomou.constant.RedisPrefixConst.COMMENT_USER_LIKE;

/**
 * @author MouHongDa
 * @date 2022/4/20 23:23
 */
@Service
public class MyUserDetailsService implements UserDetailsService, UserDetailsPasswordService {

    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username.isEmpty()) {
            throw new UsernameNotFoundException("用户名为空");
        }
        //根据用户名找user
        User user = userService.getUserByUsername(username);
        if (user != null) {
            List<GrantedAuthority> authorities = new ArrayList<>();
            //根据用户名查找用户角色列表
            List<String> roles = userService.listUserRolesByUsername(username);
            //这样写减少对象指针 好一点
            SimpleGrantedAuthority simpleGrantedAuthority = null;
            for (String role : roles) {
                simpleGrantedAuthority = new SimpleGrantedAuthority(role);
                authorities.add(simpleGrantedAuthority);
            }
            MyUserDetails myUserDetails = new MyUserDetails();
            myUserDetails.setUser(user);
            myUserDetails.setAuthorities(authorities);
            myUserDetails.setArticleLikeSet((Set<Integer>) redisTemplate.boundHashOps(ARTICLE_USER_LIKE).get(user.getUserId().toString()));
            myUserDetails.setCommentLikeSet((Set<Integer>) redisTemplate.boundHashOps(COMMENT_USER_LIKE).get(user.getUserId().toString()));
            return myUserDetails;
        } else {
            throw new UsernameNotFoundException("没有该用户");
        }
    }

    @Override
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        Integer update = userService.update(user.getUsername(), newPassword);
        if (update == 1) {
            ((MyUserDetails) user).setPassword(newPassword);
        }
        return user;
    }
}

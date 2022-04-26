package com.xiaomou.util;

import com.xiaomou.entity.User;
import com.xiaomou.service.impl.auth.MyUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author MouHongDa
 * @date 2022/4/25 15:35
 */
public class UserUtil {

    /**
     * 获取当前登录用户
     *
     * @return
     */
    public static User getLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication instanceof UsernamePasswordAuthenticationToken){
            MyUserDetails userAuth=   (MyUserDetails)authentication.getPrincipal();
            return  userAuth.getUser();
        }

        return  null;
    }

    public UserUtil(){}
}


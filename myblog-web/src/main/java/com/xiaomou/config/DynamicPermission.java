package com.xiaomou.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiaomou.entity.Api;
import com.xiaomou.handler.exception.MyAccessDeniedException;
import com.xiaomou.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * @author MouHongDa
 * @date 2022/4/23 21:42
 */
@Component
public class DynamicPermission {
    private final static String PERMISSION_KEY = "PERMISSION_KEY";
    //    @Autowired
//    private   RedisTemplate<String,Object> redisTemplate;
    @Autowired
    HttpServletRequest request;
    @Autowired
    private UserService userService;

    /**
     * 判断有访问API的权限
     *
     * @param authentication
     * @return
     * @throws MyAccessDeniedException
     */
    public boolean checkPermisstion(Authentication authentication) throws MyAccessDeniedException, IOException {
        String name = authentication.getName();
        //如果是匿名用户 给匿名用户get权限 查看
        if (name.equals("anonymousUser")) {
            String method = request.getMethod();
            if (method.equals("GET")) {
                return true;
            } else {
                throw new MyAccessDeniedException("非法操作！");
            }
        }
        //获取当前用户认证信息
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            //获取到用户名登陆时输入表单的用户名
            String username = userDetails.getUsername();
            //通过账号获取资源鉴权查看当前用户下的权限信息表
            //这个鉴权经查询常要用到 可以加入缓存
            //免得
            List<Api> apiUrls = getApiUrlByUserName(username);
            //AntPathMatcher antPathMatcher = new AntPathMatcher();
            //当前访问路径
            String requestURI = request.getRequestURI();
            //提交类型
            String urlMethod = request.getMethod();
            //判断当前路径在不在访问资源中
            boolean hashAntPath = false;
            int hasMethod = -1;
            AntPathMatcher pathMatcher = new AntPathMatcher();
            //判断当前访问路径在不在权限表里面
            for (Api item : apiUrls) {
                if (pathMatcher.match(item.getUrl(), requestURI)) {
                    hashAntPath = true;
                }
                hasMethod = item.getMethod().toUpperCase().indexOf(urlMethod.toUpperCase());
                if (hashAntPath && hasMethod != -1) {
                    break;
                }
            }
            boolean res = hashAntPath && hasMethod != -1;
            if (res) {
                return res;
            } else {
                throw new MyAccessDeniedException("用户权限不足！");
            }
        } else {
            throw new MyAccessDeniedException("不是UserDetails类型！");
        }
    }


    private List<Api> getApiUrlByUserName(String username) throws JsonProcessingException {

        List<Api> urlApis = null;
        String key = PERMISSION_KEY + "_" + username;
        String api = "";
//        String api = (String) redisTemplate.opsForValue().get(key);
        if (api != null && api != "") {
            //urlApis= JSON.parseObject(api, List.class);
            // System.out.println("缓存中"+urlApis);
            // return  urlApis;
            urlApis = (List<Api>) new ObjectMapper().readValue(api, Api.class);
            System.out.println(urlApis);
            return urlApis;
        }
        List<Api> apis = userService.getApiUrlByUserName(username);
        //加入缓存然后设置过期时间为半个小时
//        redisTemplate.opsForValue().set(key, JSON.toJSONString(apis),Duration.ofSeconds(1800L));
        return apis;
    }
}


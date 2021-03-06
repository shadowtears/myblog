package com.xiaomou.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaomou.constant.CommonConst;
import com.xiaomou.constant.RedisPrefixConst;
import com.xiaomou.dto.UserListPageDTO;
import com.xiaomou.entity.Api;
import com.xiaomou.entity.Role;
import com.xiaomou.entity.User;
import com.xiaomou.enums.LoginTypeEnum;
import com.xiaomou.handler.auth.MyAuthenticationSuccessHandler;
import com.xiaomou.handler.exception.MyRuntimeException;
import com.xiaomou.mapper.RoleMapper;
import com.xiaomou.mapper.UserMapper;
import com.xiaomou.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaomou.service.impl.auth.MyUserDetails;
import com.xiaomou.service.impl.auth.MyUserDetailsService;
import com.xiaomou.vo.UserQueryVO;
import com.xiaomou.vo.UserVO;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.xiaomou.constant.RedisPrefixConst.ARTICLE_USER_LIKE;
import static com.xiaomou.constant.RedisPrefixConst.COMMENT_USER_LIKE;

/**
 * <p>
 * ???????????????
 * </p>
 *
 * @author xiaomou
 * @since 2022-04-20
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    RoleMapper roleMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private UserMapper userMapper;
    @Resource
    private HttpServletRequest request;
    @Autowired
    private MyUserDetailsService myUserDetailsService;
    @Autowired
    private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;
    /**
     * qq appId
     */
    @Value("${qq.app-id}")
    private String QQ_APP_ID;

    /**
     * qq??????????????????????????????
     */
    @Value("${qq.user-info-url}")
    private String QQ_USER_INFO_URL;
    @Autowired
    private RestTemplate restTemplate;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        RestTemplate restTemplate = builder.build();
        return restTemplate;
    }

    @Override
    public User getUserByUsername(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        User user = this.baseMapper.selectOne(wrapper);
        return user;
    }

    @Override
    public List<String> listUserRolesByUsername(String username) {
        List<String> roles = this.baseMapper.listUserRolesByUsername(username);
        return roles;
    }

    @Override
    public Integer update(String username, String password) {
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(User::getUsername, username);
        wrapper.set(User::getPassword, password);
        Integer result = this.baseMapper.update(null, wrapper);
        return result;
    }

    @Override
    public IPage<UserListPageDTO> getUserListPage(Page<UserListPageDTO> page, UserQueryVO userQueryVO) {
        return this.baseMapper.getUserListPage(page, userQueryVO);
    }

    @Override
    public Integer updateSilenceById(Integer id, boolean flag) {
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(User::getUserId, id);
        wrapper.set(User::getIsSilence, flag);
        Integer result = this.baseMapper.update(null, wrapper);
        return result;
    }

    @Override
    public List<Api> getApiUrlByUserName(String username) {

        List<Api> list = this.baseMapper.getApiUrlByUserName(username);
        return list;
    }

    /**
     * ?????????????????????
     *
     * @param username ?????????
     */
    @Override
    public void sendCode(String username) {
        if (!checkEmail(username)) {
            throw new MyRuntimeException("?????????????????????");
        }
//        ???????????????????????????
        StringBuilder code = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            code.append(random.nextInt(10));
        }
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("549275946@qq.com");
        message.setTo(username);
        message.setSubject("?????????");
        message.setText("???Matteo?????????????????????Matteo???????????????????????????????????????????????????????????????????????? " + code.toString() + " ?????????15????????????????????????????????????");
        javaMailSender.send(message);
        // ??????????????????redis????????????????????????15??????
        redisTemplate.boundValueOps(RedisPrefixConst.CODE_KEY + username).set(code);
        redisTemplate.expire(RedisPrefixConst.CODE_KEY + username, RedisPrefixConst.CODE_EXPIRE_TIME, TimeUnit.MILLISECONDS);
    }

    /**
     * ????????????????????????
     *
     * @param username ?????????
     * @return ????????????
     */
    private boolean checkEmail(String username) {
        String RULE_EMAIL = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
        //???????????????????????? ?????????????????????
        Pattern p = Pattern.compile(RULE_EMAIL);
        //???????????????????????????
        Matcher m = p.matcher(username);
        //??????????????????
        return m.matches();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveUser(UserVO userVO) {
        // ????????????????????????
        if (checkUser(userVO)) {
            throw new MyRuntimeException("?????????????????????");
        }
        //????????????
        String encode = bCryptPasswordEncoder.encode(userVO.getPassword());
        User user = User.builder()
//                ????????????
                .password("{bcrypt}" + encode)
                //??????????????????
                .avatar(CommonConst.DEFAULT_AVATAR)
                //??????????????????
                .nickname(CommonConst.NICKNAME)
                .createTime(new Date())
                .username(userVO.getUsername())
                .build();
        boolean b = this.save(user);
//      ??????????????????
        boolean c = this.roleMapper.insertNewUser(user.getUserId(), CommonConst.ROLE_ID);
    }

    /**
     * ??????????????????????????????
     *
     * @param userVO ????????????
     * @return ????????????
     */
    private Boolean checkUser(UserVO userVO) {
        if (!userVO.getCode().equals(redisTemplate.boundValueOps(RedisPrefixConst.CODE_KEY + userVO.getUsername()).get())) {
            throw new MyRuntimeException("??????????????????");
        }
        //???????????????????????????
        User userAuth = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .select(User::getUsername).eq(User::getUsername, userVO.getUsername()));
        return Objects.nonNull(userAuth);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updatePassword(UserVO user) {
        // ????????????????????????
        if (!checkUser(user)) {
            throw new MyRuntimeException("?????????????????????");
        }
        List<String> roles = userMapper.listUserRolesByUsername(user.getUsername());
        if (roles.get(0).equals("admin")) {
            throw new MyRuntimeException("????????????");
        }
        // ???????????????????????????
        userMapper.update(new User(), new LambdaUpdateWrapper<User>()
                .set(User::getPassword, "{bcrypt}" + BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()))
                .eq(User::getUsername, user.getUsername()));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MyUserDetails qqLogin(String openId, String accessToken) {
        // ??????????????????
        MyUserDetails myUserDetails = null;
        // ??????????????????????????????????????????
        User user = getUserAuth(openId);
        if (Objects.nonNull(user)) {
            // ??????????????????????????????????????????????????????
            myUserDetails = (MyUserDetails) myUserDetailsService.loadUserByUsername(user.getUsername());
        } else {
            // ???????????????openId???accessToken??????QQ??????????????????????????????
            Map<String, String> formData = new HashMap<>(16);
            // ??????????????????
            formData.put("openid", openId);
            formData.put("access_token", accessToken);
            formData.put("oauth_consumer_key", QQ_APP_ID);
            // ??????QQ?????????????????????
            Map userInfoMap = null;
            try {
                userInfoMap = new ObjectMapper().readValue(restTemplate.getForObject(QQ_USER_INFO_URL, String.class, formData), Map.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if ((Integer) userInfoMap.get("ret") != 0)
                throw new MyRuntimeException("???????????????????????????");
            // ???????????????????????????openId
            String encode = bCryptPasswordEncoder.encode(openId);
            // ???????????????????????????????????????
            User userInfo = User.builder()
                    .username(openId)
                    .password("{bcrypt}" + encode)
                    .nickname((String) userInfoMap.get("nickname"))
                    .avatar((String) userInfoMap.get("figureurl_qq_1"))
                    .createTime(new Date())
                    .build();
            this.userMapper.insert(userInfo);
            //??????????????????
            boolean c = this.roleMapper.insertNewUser(userInfo.getUserId(), CommonConst.ROLE_ID);
            Role role = this.roleMapper.selectById(CommonConst.ROLE_ID);
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
            //?????????????????????
            myUserDetails = (MyUserDetails) myUserDetailsService.loadUserByUsername(userInfo.getUsername());

        }
        myAuthenticationSuccessHandler.updateUserLogin(myUserDetails, request, LoginTypeEnum.QQ.getDesc());
        // ?????????????????????springSecurity??????
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(myUserDetails, null, myUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        return myUserDetails;
    }


    /**
     * ?????????????????????????????????
     *
     * @param openId ???????????????id
     * @return ??????????????????
     */
    private User getUserAuth(String openId) {
        // ??????????????????
        return userMapper.selectOne(new LambdaQueryWrapper<User>()
                .select(User::getUserId,User::getUsername)
                .eq(User::getUsername, openId));
    }
}

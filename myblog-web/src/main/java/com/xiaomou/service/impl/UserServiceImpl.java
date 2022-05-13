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
 * 服务实现类
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
     * qq获取用户信息接口地址
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
     * 发送邮箱验证码
     *
     * @param username 邮箱号
     */
    @Override
    public void sendCode(String username) {
        if (!checkEmail(username)) {
            throw new MyRuntimeException("邮箱格式不正确");
        }
//        生成六位随机验证码
        StringBuilder code = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            code.append(random.nextInt(10));
        }
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("549275946@qq.com");
        message.setTo(username);
        message.setSubject("验证码");
        message.setText("【Matteo】您正在进行“Matteo的个人博客”的身份验证，您的验证码为您的验证码为 " + code.toString() + " 有效期15分钟，请不要告诉他人哦！");
        javaMailSender.send(message);
        // 将验证码存入redis，设置过期时间为15分钟
        redisTemplate.boundValueOps(RedisPrefixConst.CODE_KEY + username).set(code);
        redisTemplate.expire(RedisPrefixConst.CODE_KEY + username, RedisPrefixConst.CODE_EXPIRE_TIME, TimeUnit.MILLISECONDS);
    }

    /**
     * 检测邮箱是否合法
     *
     * @param username 用户名
     * @return 合法状态
     */
    private boolean checkEmail(String username) {
        String RULE_EMAIL = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
        //正则表达式的模式 编译正则表达式
        Pattern p = Pattern.compile(RULE_EMAIL);
        //正则表达式的匹配器
        Matcher m = p.matcher(username);
        //进行正则匹配
        return m.matches();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveUser(UserVO userVO) {
        // 校验账号是否合法
        if (checkUser(userVO)) {
            throw new MyRuntimeException("邮箱已被注册！");
        }
        //加密密码
        String encode = bCryptPasswordEncoder.encode(userVO.getPassword());
        User user = User.builder()
//                设置密码
                .password("{bcrypt}" + encode)
                //设置默认头像
                .avatar(CommonConst.DEFAULT_AVATAR)
                //设置默认昵称
                .nickname(CommonConst.NICKNAME)
                .createTime(new Date())
                .username(userVO.getUsername())
                .build();
        boolean b = this.save(user);
//      绑定用户角色
        boolean c = this.roleMapper.insertNewUser(user.getUserId(), CommonConst.ROLE_ID);
    }

    /**
     * 校验用户数据是否合法
     *
     * @param userVO 用户数据
     * @return 合法状态
     */
    private Boolean checkUser(UserVO userVO) {
        if (!userVO.getCode().equals(redisTemplate.boundValueOps(RedisPrefixConst.CODE_KEY + userVO.getUsername()).get())) {
            throw new MyRuntimeException("验证码错误！");
        }
        //查询用户名是否存在
        User userAuth = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .select(User::getUsername).eq(User::getUsername, userVO.getUsername()));
        return Objects.nonNull(userAuth);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updatePassword(UserVO user) {
        // 校验账号是否合法
        if (!checkUser(user)) {
            throw new MyRuntimeException("邮箱尚未注册！");
        }
        List<String> roles = userMapper.listUserRolesByUsername(user.getUsername());
        if (roles.get(0).equals("admin")) {
            throw new MyRuntimeException("没有权限");
        }
        // 根据用户名修改密码
        userMapper.update(new User(), new LambdaUpdateWrapper<User>()
                .set(User::getPassword, "{bcrypt}" + BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()))
                .eq(User::getUsername, user.getUsername()));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MyUserDetails qqLogin(String openId, String accessToken) {
        // 创建登录信息
        MyUserDetails myUserDetails = null;
        // 校验该第三方账户信息是否存在
        User user = getUserAuth(openId);
        if (Objects.nonNull(user)) {
            // 存在则返回数据库中的用户信息登录封装
            myUserDetails = (MyUserDetails) myUserDetailsService.loadUserByUsername(user.getUsername());
        } else {
            // 不存在通过openId和accessToken获取QQ用户信息，并创建用户
            Map<String, String> formData = new HashMap<>(16);
            // 定义请求参数
            formData.put("openid", openId);
            formData.put("access_token", accessToken);
            formData.put("oauth_consumer_key", QQ_APP_ID);
            // 获取QQ返回的用户信息
            Map userInfoMap = null;
            try {
                userInfoMap = new ObjectMapper().readValue(restTemplate.getForObject(QQ_USER_INFO_URL, String.class, formData), Map.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if ((Integer) userInfoMap.get("ret") != 0)
                throw new MyRuntimeException("第三方登录拉取失败");
            // 第三方登录默认密码openId
            String encode = bCryptPasswordEncoder.encode(openId);
            // 将用户账号和信息存入数据库
            User userInfo = User.builder()
                    .username(openId)
                    .password("{bcrypt}" + encode)
                    .nickname((String) userInfoMap.get("nickname"))
                    .avatar((String) userInfoMap.get("figureurl_qq_1"))
                    .createTime(new Date())
                    .build();
            this.userMapper.insert(userInfo);
            //绑定用户角色
            boolean c = this.roleMapper.insertNewUser(userInfo.getUserId(), CommonConst.ROLE_ID);
            Role role = this.roleMapper.selectById(CommonConst.ROLE_ID);
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
            //初始化返回信息
            myUserDetails = (MyUserDetails) myUserDetailsService.loadUserByUsername(userInfo.getUsername());

        }
        myAuthenticationSuccessHandler.updateUserLogin(myUserDetails, request, LoginTypeEnum.QQ.getDesc());
        // 将登录信息放入springSecurity管理
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(myUserDetails, null, myUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        return myUserDetails;
    }


    /**
     * 检测第三方账号是否注册
     *
     * @param openId 第三方唯一id
     * @return 用户账号信息
     */
    private User getUserAuth(String openId) {
        // 查询账号信息
        return userMapper.selectOne(new LambdaQueryWrapper<User>()
                .select(User::getUserId,User::getUsername)
                .eq(User::getUsername, openId));
    }
}

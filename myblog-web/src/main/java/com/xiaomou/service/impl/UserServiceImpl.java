package com.xiaomou.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaomou.constant.DefaultUser;
import com.xiaomou.dto.UserListPageDTO;
import com.xiaomou.entity.Api;
import com.xiaomou.entity.User;
import com.xiaomou.handler.exception.MyRuntimeException;
import com.xiaomou.mapper.RoleMapper;
import com.xiaomou.mapper.UserMapper;
import com.xiaomou.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaomou.vo.RegisterUserVO;
import com.xiaomou.vo.UserQueryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
     * @param email 邮箱号
     */
    @Override
    public void sendCode(String email) {
        if (!checkEmail(email)) {
            throw new MyRuntimeException("邮箱格式不正确");
        }
        //生成六位随机验证码
//        StringBuilder code = new StringBuilder();
//        Random random = new Random();
//        for (int i = 0; i < 6; i++) {
//            code.append(random.nextInt(10));
//        }
        Integer code = 666666;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("549275946@qq.com");
        message.setTo(email);
        message.setSubject("验证码");
        message.setText("【Matteo】您正在进行“Matteo的个人博客”的身份验证，您的验证码为您的验证码为 " + code.toString() + " 有效期15分钟，请不要告诉他人哦！");
        javaMailSender.send(message);
        //在把验证码放到redis 有效时间存为15分钟 这样就可以了
        //暂时写死验证码
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

    @Transactional
    @Override
    public boolean registerUser(RegisterUserVO registerUserVO) {

        User user = new User();
        String code = registerUserVO.getCode();
        if (!code.equals("666666")) {
            throw new MyRuntimeException("验证码错误");
        }
        //加密密码
        String encode = bCryptPasswordEncoder.encode(registerUserVO.getPassword());
        user.setPassword("{bcrypt}" + encode);
        //设置默认头像
        user.setAvatar(DefaultUser.DEFAULT_AVATAR);
        //设置默认昵称
        user.setNickname(DefaultUser.NICKNAME);
        user.setCreateTime(new Date());
        user.setUsername(registerUserVO.getUsername());
        boolean b = this.save(user);
        boolean c = this.roleMapper.insertNewUser(user.getUserId(), DefaultUser.ROLE_ID);
        return b&c;
    }
}

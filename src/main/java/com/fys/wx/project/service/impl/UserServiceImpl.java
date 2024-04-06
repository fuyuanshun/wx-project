package com.fys.wx.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fys.wx.project.constant.TokenConstant;
import com.fys.wx.project.entity.LoginUser;
import com.fys.wx.project.entity.User;
import com.fys.wx.project.persistence.UserMapper;
import com.fys.wx.project.service.UserService;
import com.fys.wx.project.utils.JwtUtils;
import com.fys.wx.project.utils.ResponseResult;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author fys
 * @date 2024/3/31
 * @description
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final UserMapper userMapper;

    private final AuthenticationManager authenticationManager;

    private final RedisTemplate<String, Object> redisTemplate;

    public UserServiceImpl(UserMapper userMapper, AuthenticationManager authenticationManager, RedisTemplate<String, Object> redisTemplate) {
        this.userMapper = userMapper;
        this.authenticationManager = authenticationManager;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 登录认证
     *  1.security验证账号密码
     *  2.如果认证信息为空，则返回错误
     *  3.否则，创建token，存入redis后并返回
     * @param account 用户账户
     * @param password 用户密码
     * @return
     */
    @Override
    public ResponseResult<String> login(String account, String password) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(account, password);
        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        if (authenticate != null) {
            LoginUser principal = (LoginUser)authenticate.getPrincipal();
            User user = principal.getUser();
            String token = JwtUtils.createToken(user.getId());
            //用户数据存入redis
            redisTemplate.opsForValue().set("login:" + user.getId(), principal, TokenConstant.TOKEN_EXPIRE_TIME, TimeUnit.HOURS);
            user.setLastLoginDate(new Date());
            userMapper.updateById(user);
            return ResponseResult.success(token);
        } else {
            return new ResponseResult<>(0, "用户账户或密码错误", null);
        }
    }

    /**
     * 获取用户列表
     * @return
     */
    @Override
    public List<User> userList() {
        return userMapper.userList();
    }
}

package com.fys.wx.project.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
    public ResponseResult<JSONObject> userList(Integer pageIndex, Integer pageSize) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        long totalCount = userMapper.selectCount(queryWrapper);
        //总页数
        long totalPageCount = totalCount % pageSize == 0 ? totalCount / pageSize : (totalCount / pageSize) + 1;
        //当前页不能小于0
        if(pageIndex < 0){
            pageIndex = 0;
        }
        //当前页不能大于总页数
        if(pageIndex > totalPageCount){
            pageIndex = (int)totalPageCount;
        }
        List<User> userList = userMapper.userList((pageIndex - 1) * pageSize, pageSize);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("pageIndex", pageIndex);
        jsonObject.put("totalCount", totalCount);
        jsonObject.put("pageSize", pageSize);
        jsonObject.put("totalPage", totalPageCount);
        jsonObject.put("records", userList);
        return ResponseResult.success(jsonObject);
    }

    @Override
    public ResponseResult<String> disableUser(Integer id) {
        int i = userMapper.deleteById(id);
        return ResponseResult.success(String.valueOf(i));
    }

    @Override
    public ResponseResult<String> enableUser(Integer id) {
        int i = userMapper.enableUserById(id);
        return ResponseResult.success(String.valueOf(i));
    }
}

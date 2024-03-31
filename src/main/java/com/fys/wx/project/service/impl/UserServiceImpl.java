package com.fys.wx.project.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fys.wx.project.entity.User;
import com.fys.wx.project.persistence.UserMapper;
import com.fys.wx.project.service.UserService;
import com.fys.wx.project.utils.JwtUtil;
import org.springframework.stereotype.Service;

/**
 * @author fys
 * @date 2024/3/31
 * @description
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public String login(String account, String password) {
        User user = userMapper.getUserByUsernameAndPassword(account, password);
        JSONObject obj = JSONUtil.createObj();

        if(user != null){
            obj.set("state", true);
            obj.set("msg", "登陆成功");
            obj.set("token", JwtUtil.createToken(user.getId()));
            return obj.toString();
        }
        obj.set("state", false);
        obj.set("msg", "用户账户或密码错误");
        return obj.toString();
    }
}

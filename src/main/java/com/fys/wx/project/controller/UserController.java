package com.fys.wx.project.controller;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSONObject;
import com.fys.wx.project.service.UserService;
import com.fys.wx.project.utils.ResponseResult;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fys
 * @date 2024/3/31
 * @description 用户接口
 */
@RestController
public class UserController {

    //构造注入
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user/login")
    public ResponseResult<String> login(@RequestParam("account") String account, @RequestParam("password") String password){
        return userService.login(account, password);
    }

    @GetMapping("/user/list")
    @PreAuthorize("hasRole('ADMIN')")
    public String userList(){
        return JSONUtil.toJsonStr(userService.list());
    }
}

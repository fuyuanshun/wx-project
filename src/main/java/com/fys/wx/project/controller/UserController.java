package com.fys.wx.project.controller;

import com.fys.wx.project.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

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
    public String login(@RequestParam("account") String account, @RequestParam("password") String password){
        return userService.login(account, password);
    }

    @PostMapping("/user/list")
    public String userList(){
        return "";
    }
}

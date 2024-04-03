package com.fys.wx.project.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.fys.wx.project.service.UserService;
import com.fys.wx.project.utils.ResponseResult;
import com.fys.wx.project.utils.UserUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

/**
 * @author fys
 * @date 2024/3/31
 * @description 用户接口
 */
@RestController
@RequestMapping("/user")
public class UserController {

    //构造注入
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 登录接口
     * @param json
     * @return
     */
    @PostMapping("/login")
    @CrossOrigin
    public ResponseResult<String> login(@RequestBody String json){
        if(StrUtil.isNotEmpty(json)){
            JSONObject jsonObject = JSONObject.parseObject(json);
            String account = jsonObject.getString("account");
            String password = jsonObject.getString("password");
            if (StrUtil.isNotEmpty(account) && StrUtil.isNotEmpty(password)) {
                return userService.login(account, password);
            }
        }
        return ResponseResult.error();
    }

    /**
     * 解析token返回用户信息
     * @param request
     * @return
     */
    @GetMapping(value = "/info", produces = "application/json;charset=utf-8")
    @CrossOrigin
    public ResponseResult<JSONObject> userInfo(HttpServletRequest request){
        Object user = request.getAttribute("user");
        //信息脱敏
        JSONObject jsonObject = UserUtils.desensitivityUser(user.toString());
        return ResponseResult.success(jsonObject);
    }

}

package com.fys.wx.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fys.wx.project.entity.User;
import com.fys.wx.project.utils.ResponseResult;

/**
 * @author fys
 * @date 2024/3/31
 * @description
 */
public interface UserService extends IService<User> {

    /**
     * 登录
     * @param account
     * @param password
     * @return
     */
    ResponseResult<String> login(String account, String password);
}

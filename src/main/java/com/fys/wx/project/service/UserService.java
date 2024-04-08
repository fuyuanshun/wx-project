package com.fys.wx.project.service;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fys.wx.project.entity.User;
import com.fys.wx.project.utils.ResponseResult;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

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

    /**
     * 获取用户列表
     * @return
     */
    ResponseResult<JSONObject> userList(Integer pageIndex, Integer pageSize);

    /**
     * 根据id禁用用户
     * @param id
     * @return
     */
    ResponseResult<String> disableUser(Integer id);

    /**
     * 根据id启用用户
     * @param id
     * @return
     */
    ResponseResult<String> enableUser(Integer id);
}

package com.fys.wx.project.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fys.wx.project.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author fys
 * @date 2024/3/31
 * @description
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户账号和用户密码登录
     * @param account 用户账号
     * @param password 用户密码
     * @return
     */
    User getUserByUsernameAndPassword(@Param("account") String account, @Param("password")String password);

    User getUserRoleByUserName(@Param("account") String account);

    /**
     * 获取用户列表
     * @return
     */
    List<User> userList(@Param("offset") Integer offset, @Param("pageSize") Integer Size);
}

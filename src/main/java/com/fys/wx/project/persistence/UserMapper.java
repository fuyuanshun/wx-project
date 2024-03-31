package com.fys.wx.project.persistence;

import com.fys.wx.project.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author fys
 * @date 2024/3/31
 * @description
 */
@Mapper
public interface UserMapper {

    /**
     * 根据用户账号和用户密码登录
     * @param account 用户账号
     * @param password 用户密码
     * @return
     */
    User getUserByUsernameAndPassword(@Param("account") String account, @Param("password")String password);
}

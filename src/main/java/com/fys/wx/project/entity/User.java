package com.fys.wx.project.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * @author fys
 * @date 2024/3/31
 * @description
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer id;
    //用户名称
    private String username;
    //用户账号
    private String account;
    //用户密码
    private String password;
    //权限
    private String role;
    //用户手机号
    private String phone;
    //用户地址
    private String userAddress;
    private Date createDate;
    private Date lastLoginDate;
}

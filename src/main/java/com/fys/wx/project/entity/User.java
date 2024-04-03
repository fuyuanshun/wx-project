package com.fys.wx.project.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @author fys
 * @date 2024/3/31
 * @description
 */

@Data
@TableName("f_user")
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 8933396039304683239L;

    @TableId(value = "id", type= IdType.AUTO)
    private Integer id;
    //用户名称
    @TableField("user_name")
    private String username;
    //用户账号
    @TableField("user_account")
    private String account;
    //用户密码
    @TableField("user_password")
    private String password;
    //权限
    @TableField(exist = false)
    private String role;
    //用户手机号
    private String phone;
    //用户地址
    @TableField("user_address")
    private String userAddress;
    //用户账户创建日期
    @TableField("create_date")
    private Date createDate;
    //用户最后一次登陆日期
    @TableField("last_login_date")
    private Date lastLoginDate;
    //用户头像
    private String avatar;
    private Integer enable;
}

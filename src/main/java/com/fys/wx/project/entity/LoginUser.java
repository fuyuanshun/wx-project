package com.fys.wx.project.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author fys
 * @date 2024/4/2
 * @description 登录用户包装类
 */
public class LoginUser extends org.springframework.security.core.userdetails.User implements Serializable {

    @Serial
    private static final long serialVersionUID = -3871742032575939973L;

    private User user;

    public LoginUser(User user){
        super(user.getAccount(), user.getPassword(), user.getEnable() == 1, true, true, true, new ArrayList<>());
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

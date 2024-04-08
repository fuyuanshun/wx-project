package com.fys.wx.project.conf;

import com.fys.wx.project.entity.LoginUser;
import com.fys.wx.project.entity.User;
import com.fys.wx.project.persistence.UserMapper;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author fys
 * @date 2024/4/1
 * @description
 */
public class DBUserDetailsManager implements UserDetailsService {

    @Resource
    private UserMapper mapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = mapper.getUserRoleByUserName(username);
        if (user == null || user.getEnable() != 1) {
            throw new UsernameNotFoundException(username);
        } else {
            return new LoginUser(user);
        }
    }
}

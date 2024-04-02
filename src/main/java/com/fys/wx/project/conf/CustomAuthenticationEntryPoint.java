package com.fys.wx.project.conf;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

/**
 * @author fys
 * @date 2024/4/1
 * @description
 */
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        JSONObject obj = JSONUtil.createObj();
        obj.set("code", -1);
        obj.set("msg", "未认证");
        obj.set("data", authException.getLocalizedMessage());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(obj);
    }
}

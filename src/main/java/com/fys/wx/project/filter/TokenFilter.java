package com.fys.wx.project.filter;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.fys.wx.project.entity.LoginUser;
import com.fys.wx.project.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author fys
 * @date 2024/4/2
 * @description
 */
@Component
public class TokenFilter extends OncePerRequestFilter {

    private final RedisTemplate<String, Object> redisTemplate;

    public TokenFilter(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 拦截请求校验token
     * @param request request
     * @param response response
     * @param filterChain 过滤器链
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.setContentType("applicatoin/json;charset=utf-8");

        String token = request.getHeader("token");
        //没有token直接放行，交给springSecurity处理就行
        if(StrUtil.isEmpty(token)){
            filterChain.doFilter(request, response);
            return;
        }
        String userid;
        try {
            userid = JwtUtils.decodeToken(token);
        } catch (Exception e) {
            response.getWriter().println("非法或已过期token");
            return;
        }
        //获取redis中的
        Object principal = redisTemplate.opsForValue().get("login:" + userid);
        if (principal == null) {
            response.getWriter().println("用户登录过期，请重新登录");
            return;
        }
        //将redis中的用户数据转为json
        JSONObject json = JSONObject.from(principal);
        ArrayList<GrantedAuthority> roles = new ArrayList<>();
        JSONObject user = json.getJSONObject("user");
        String role;

        //将权限字符串封装为权限对象
        if ((role = user.getString("role")) != null) {
            for(String s : role.split(",")){
                roles.add((GrantedAuthority) () -> s);
            }
        }
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(principal, null, roles);

        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

        filterChain.doFilter(request, response);

    }


}

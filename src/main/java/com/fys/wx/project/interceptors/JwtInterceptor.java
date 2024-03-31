package com.fys.wx.project.interceptors;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fys.wx.project.utils.JwtUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author fys
 * @date 2024/4/1
 * @description
 */
public class JwtInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String msg;

        String token = request.getHeader("token");
        try {
            JwtUtil.verifyToken(token);
            //无异常直接放行请求
            return true;
        } catch (TokenExpiredException expire){
            msg = "token过期";
        } catch (AlgorithmMismatchException algorithmMismatchException){
            msg = "token算法不一致";
        } catch (Exception e){
            msg = "token无效";
        }

        response.setContentType("application/text;charset=utf-8");
        response.getWriter().println(msg);
        return false;
    }
}

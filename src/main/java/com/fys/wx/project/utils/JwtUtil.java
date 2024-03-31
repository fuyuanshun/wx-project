package com.fys.wx.project.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;

/**
 * @author fys
 * @date 2024/4/1
 * @description JWT工具类
 */
public class JwtUtil {
    //过期时间一天
    private static final int EXPIRE_TIME = 60 * 24;
    //密钥
    private static final String TOKEN_SECRET = "qwerty!123@";

    /**
     * 不允许实例化
     */
    private JwtUtil(){}

    /**
     * 获取token
     * @param id
     * @return
     */
    public static String createToken(int id){
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.MINUTE, EXPIRE_TIME);
        return JWT.create()
                .withClaim("id", id)
                .withExpiresAt(instance.getTime())
                .sign(Algorithm.HMAC256(TOKEN_SECRET));
    }

    /**
     * 验证token，失败会抛出异常
     * @param token
     * @return
     */
    public static void verifyToken(String token){
        JWT.require(Algorithm.HMAC256(TOKEN_SECRET))
                                .build()
                                .verify(token);
    }
}

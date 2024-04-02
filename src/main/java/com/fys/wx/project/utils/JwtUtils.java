package com.fys.wx.project.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fys.wx.project.constant.TokenConstant;

import java.util.Calendar;

/**
 * @author fys
 * @date 2024/4/2
 * @description token生成与解析工具类
 */
public class JwtUtils {


    private JwtUtils(){}

    /**
     * 根据id创建token
     * @param id 用户id
     * @return token
     */
    public static String createToken(int id){
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.HOUR, TokenConstant.TOKEN_EXPIRE_TIME);
        return JWT.create()
                .withClaim("id", id)
                .withExpiresAt(instance.getTime())
                .sign(Algorithm.HMAC256(TokenConstant.TOKEN_SECRET));
    }

    /**
     * 解析token
     * @param token token
     * @return 用户id
     */
    public static String decodeToken(String token){
        DecodedJWT verify = JWT.require(Algorithm.HMAC256(TokenConstant.TOKEN_SECRET)).build().verify(token);
        return verify.getClaim("id").toString();
    }
}

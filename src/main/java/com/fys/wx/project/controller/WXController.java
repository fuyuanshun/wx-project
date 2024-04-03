package com.fys.wx.project.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import com.fys.wx.project.constant.RedisKeyConstant;
import com.fys.wx.project.constant.URLConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author fys
 * @date 2024/3/16
 * @description 微信接口
 */
@RestController
@RequestMapping("/wx")
public class WXController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Value("${wx.default_token}")
    private String defaultToken;

    @Value("${wx.appid}")
    private String appid;

    @Value("${wx.secret}")
    private String secret;

    /**
     * 对接微信公众号
     * @param signature 签名
     * @param timestamp 时间戳
     * @param nonce 随机数
     * @param echostr 随机字符串
     * @return
     */
    @GetMapping("/verify")
    public String verifyWX(@RequestParam("signature") String signature, @RequestParam("timestamp") String timestamp,
                           @RequestParam("nonce") int nonce, @RequestParam("echostr") String echostr){

        String[] arr = new String[]{defaultToken, timestamp, String.valueOf(nonce)};
        Arrays.sort(arr);
        String content = StrUtil.join("", arr);
        String encoding = SecureUtil.sha1(content);
        if (encoding.equalsIgnoreCase(signature)) {
            System.out.println("微信签名验证成功, echostr:" + echostr);
            return echostr;
        } else return "error";
    }


    /**
     *  获取accessToken
     * @return
     */
    @GetMapping("/get/token")
    public String getAccessToken(){
        String token = redisTemplate.opsForValue().get(RedisKeyConstant.TOKEN_KEY);
        if (token != null && !"".equals(token)) {
            Long exToken = redisTemplate.getExpire(RedisKeyConstant.TOKEN_KEY);
            //小于5分钟时才会重新获取token并且重新更新redis
            if (exToken != null && exToken > (5L * 60)) {
                System.out.println("token已存在于redis，剩余时间：" + exToken);
                return token;
            }
        }
        System.out.println("---------------token不存在或者存活时间小于5分钟，重新请求token：");
        Map<String, Object> param = new HashMap<>();
        //获取token时该参数是固定值
        param.put("grant_type", "client_credential");
        param.put("appid", appid);
        param.put("secret", secret);
        String s = HttpUtil.get(URLConstant.GET_TOKEN_URL, param);
        System.out.println("------------------请求结果：" + s);
        redisTemplate.opsForValue().set("wx_token", s);
        redisTemplate.expire("wx_token", 120 * 60, TimeUnit.SECONDS);
        return s;
    }

    /**
     * 根据accessToken获取ticket
     * @param token
     * @return
     */
    @GetMapping("/get/ticket")
    public String getTicket(@RequestParam("token") String token){
        //token可能有误，要先验证token与redis中的token是否一致
        String redisToken = redisTemplate.opsForValue().get(RedisKeyConstant.TOKEN_KEY);
        if (redisToken != null && token.equals(new JSONObject(redisToken).get("access_token"))) {
            String ticket = redisTemplate.opsForValue().get(RedisKeyConstant.TICKET_KEY);
            if (ticket != null && !"".equals(ticket)) {
                Long exTicket = redisTemplate.getExpire(RedisKeyConstant.TICKET_KEY);
                //小于5分钟时才会重新获取token并且重新更新redis
                if (exTicket != null && exTicket > (5L * 60)) {
                    System.out.println("ticket已存在于redis，剩余时间：" + exTicket);
                    return ticket;
                }
            }
            System.out.println("---------------ticket不存在或者存活时间小于5分钟，重新请求ticket：");
            Map<String, Object> param = new HashMap<>();
            //获取ticket时该参数是固定值
            param.put("access_token", token);
            param.put("type", "jsapi");
            String s = HttpUtil.get(URLConstant.GET_TICKET_URL, param);
            System.out.println("------------------请求结果：" + s);
            redisTemplate.opsForValue().set("wx_ticket", s);
            redisTemplate.expire("wx_ticket", 120 * 60, TimeUnit.SECONDS);
            return s;
        }
        //redis中不存在token，说明需要重新生成token再调用此接口
        return "false";
    }
}

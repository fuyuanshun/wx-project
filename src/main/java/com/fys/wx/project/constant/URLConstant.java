package com.fys.wx.project.constant;

/**
 * @author fys
 * @date 2024/3/16
 * @description 用于微信接口地址的常量
 */
public interface URLConstant {

    /**
     *
     */
    String GET_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token";

    /**
     * 参数：access_token
     * type: 固定为jsapi
     */
    String GET_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket";
}

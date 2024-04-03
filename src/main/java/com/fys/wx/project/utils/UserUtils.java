package com.fys.wx.project.utils;

import com.alibaba.fastjson2.JSONObject;

/**
 * @author fys
 * @date 2024/4/3
 * @description
 */
public class UserUtils {
    private UserUtils(){}


    public static JSONObject desensitivityUser(String string){
        JSONObject jsonObject = JSONObject.parseObject(string);
        jsonObject.put("password", "");
        return jsonObject;
    }
}

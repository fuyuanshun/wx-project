package com.fys.wx.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author fys
 * @date 2024/3/16
 * @description
 */
@Controller
public class HtmlController {

    @RequestMapping("index")
    public String index(){
        return "index";
    }
}

package com.pinyougou.manager.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author bear
 * @version 1.0
 * @description com.pinyougou.manager.controller
 * @date 2018/3/27
 */
@RestController
@RequestMapping("/login")
public class LoginController {
    @RequestMapping("getLoginUser")
    public Map getLoginUser(){
        //安全文本处理器得到文本,接着得到授权接着得到登录名
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Map map = new HashMap();
        map.put("LoginUser",name);
        return map;




    }
}

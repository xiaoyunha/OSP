package com.json.osp.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 主要用来解析路由并转发到相应的html中
 */
@Controller
@RequestMapping(value = "/front")
public class RouteController {

    @RequestMapping(value = "/login")
    public String loginPage() {
        // 转发至店铺列表页面
        return "index";
    }
    @RequestMapping(value = "/main")
    public String mainPage() {
        // 转发至店铺列表页面
        return "main";
    }
    @RequestMapping(value = "/admin")
    public String adminPage() {
        // 转发至店铺列表页面
        return "admin";
    }
    @RequestMapping(value = "/assistant")
    public String shopAssistantPage() {
        // 转发至店铺列表页面
        return "assistant";
    }
}

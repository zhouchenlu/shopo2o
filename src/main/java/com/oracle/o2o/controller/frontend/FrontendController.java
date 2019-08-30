package com.oracle.o2o.controller.frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/frontend")
public class FrontendController {

    //商品详情页路由
    @RequestMapping(value = "/shopdetail",
            method = RequestMethod.GET)
    public String showShopDetail() {
        return "frontend/shopdetail";
    }

    //商品详情页路由
    @RequestMapping(value = "/productdetail",
            method = RequestMethod.GET)
    public String showProductDetail() {
        return "frontend/productdetail";
    }

    @RequestMapping(value = "/shoplist",
            method = RequestMethod.GET)
    public String showShoplist() {
        return "frontend/shoplist";
    }


    @RequestMapping(value = "/index",
            method = RequestMethod.GET)
    public String index() {
        // 转发至首页页面
        return "frontend/index";
    }

}

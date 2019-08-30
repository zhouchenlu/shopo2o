package com.oracle.o2o.controller.shopadmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/shopadmin",
        method = {RequestMethod.GET})
public class ShopAdminController {

    @RequestMapping(value = "/productmanagement")
    public String productManagement() {
        // 转发至商品管理页面
        return "shop/productmanagement";
    }

    @RequestMapping(value = "/productoperation")
    public String productOperation() {
        //因为在spring-web.xml中已经设置了前后缀
        //所以不用写/html
        return "shop/productoperation";
    }

    @RequestMapping(value = "/productcategorymanagement")
    public String ProductCategoryManage() {
        return "shop/productcategorymanagement";
    }

    @RequestMapping(value = "/shopmanagement")
    public String shopManagement() {
        return "shop/shopmanagement";
    }

    @RequestMapping(value = "/shoplist")
    public String shopList() {
        //因为在spring-web.xml中已经设置了前后缀
        //所以不用写/html
        return "shop/shoplist";
    }

    @RequestMapping(value = "/shopoperation")
    public String shopOperation() {
        //因为在spring-web.xml中已经设置了前后缀
        //所以不用写/html
        return "shop/shopoperation";
    }


}

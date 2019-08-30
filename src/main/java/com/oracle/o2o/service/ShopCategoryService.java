package com.oracle.o2o.service;

import java.util.List;

import com.oracle.o2o.entity.ShopCategory;

public interface ShopCategoryService {

    //根据查询条件获取ShopCategory列表
    List<ShopCategory> getShopCategoryList(
            ShopCategory shopCategoryCondition);

}

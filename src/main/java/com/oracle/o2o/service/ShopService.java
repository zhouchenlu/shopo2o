package com.oracle.o2o.service;

import java.io.InputStream;

import com.oracle.o2o.dto.ImageHolder;
import com.oracle.o2o.dto.ShopExecution;
import com.oracle.o2o.entity.Shop;
import com.oracle.o2o.exceptions.ShopOperationException;

public interface ShopService {

    //根据shopCondition分页返回相应店铺列表
    public ShopExecution getShopList(Shop shopCondition,
                                     int pageIndex, int pageSize);

    //通过店铺Id获取店铺信息
    Shop getByShopId(long shopId);

    //更新店铺信息，包括对图片的处理
    ShopExecution modifyShop(Shop shop,
                             ImageHolder thumbnail) throws ShopOperationException;

    //注册店铺信息，包括图片处理
    ShopExecution addShop(Shop shop, ImageHolder thumbnail);
}

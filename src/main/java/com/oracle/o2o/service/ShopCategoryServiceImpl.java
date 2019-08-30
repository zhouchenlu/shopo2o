package com.oracle.o2o.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.oracle.o2o.dao.ShopCategoryDao;
import com.oracle.o2o.entity.ShopCategory;

@Service
public class ShopCategoryServiceImpl
        implements ShopCategoryService {
    @Autowired
    private ShopCategoryDao shopCategoryDao;

    public List<ShopCategory> getShopCategoryList(
            ShopCategory shopCategoryCondition) {
        return shopCategoryDao.queryShopCategory(
                shopCategoryCondition);
    }

}

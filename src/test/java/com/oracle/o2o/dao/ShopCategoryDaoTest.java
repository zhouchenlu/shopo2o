package com.oracle.o2o.dao;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.oracle.o2o.dao.ShopCategoryDao;
import com.oracle.o2o.entity.ShopCategory;

public class ShopCategoryDaoTest {

    @Autowired
    private ShopCategoryDao shopCategoryDao;

    @Test
    public void testQueryShopCategory() {
        //不搜索，先传一个空对象进去
        List<ShopCategory> shopCategoryList =
                shopCategoryDao.queryShopCategory(null);
        System.out.println(shopCategoryList.size());
    }

}

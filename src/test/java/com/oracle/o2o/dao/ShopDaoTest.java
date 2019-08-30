package com.oracle.o2o.dao;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.oracle.o2o.entity.Area;
import com.oracle.o2o.entity.PersonInfo;
import com.oracle.o2o.entity.Shop;
import com.oracle.o2o.entity.ShopCategory;
import com.oracle.o2o.dao.ShopDao;

import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShopDaoTest {
    @Autowired
    private ShopDao shopDao;

    //查看tb_shop数据库
    //字段owner_id为1的一共10条数据
    @Test
    public void testQueryShopListAndCount() {
        Shop shopCondition = new Shop();
        ShopCategory childCategory = new ShopCategory();
        ShopCategory parentCategory = new ShopCategory();
        //ShopCategoryId为12的下面有6家店铺
        parentCategory.setShopCategoryId(12L);
        childCategory.setParent(parentCategory);
        shopCondition.setShopCategory(childCategory);
        List<Shop> shopList = shopDao
                .queryShopList(shopCondition, 0, 7);
        int count = shopDao.queryShopCount(shopCondition);
        System.out.println("店铺列表的大小：" + shopList.size());
        System.out.println("店铺总数：" + count);
    }


    @Test
    @Ignore
    public void testQueryByShopId() {
        //传入的是tb_shop表中的shopId，查看数据库确定
        long shopId = 1;
        Shop shop = shopDao.queryByShopId(shopId);
        System.out.println("areaId:" + shop.getArea().getAreaId());
        System.out.println("areaName" + shop.getArea().getAreaName());
    }

    @Test
    @Ignore//这样执行下面的方法，此方法不会执行
    public void testInsertShop() {
        Shop shop = new Shop();
        PersonInfo owner = new PersonInfo();
        Area area = new Area();
        ShopCategory shopCategory = new ShopCategory();
        owner.setUserId(2L);
        area.setAreaId(3);
        shopCategory.setShopCategoryId(10L);
        shop.setOwner(owner);
        shop.setArea(area);
        shop.setShopCategory(shopCategory);
        shop.setShopName("测试的店铺");
        shop.setShopDesc("test");
        shop.setShopAddr("test");
        shop.setPhone("test");
        shop.setShopImg("test");
        shop.setCreateTime(new Date());
        shop.setEnableStatus(1);
        shop.setAdvice("审核中");
        int effectedNum = shopDao.insertShop(shop);
        assertEquals(1, effectedNum);
    }

    @Test
    @Ignore//让它们先不执行
    public void testUpdateShop() {
        Shop shop = new Shop();
        shop.setShopId(1L);
        shop.setShopDesc("测试描述");
        shop.setShopAddr("测试地址");
        shop.setLastEditTime(new Date());
        int effectedNum = shopDao.updateShop(shop);
        assertEquals(1, effectedNum);
    }


}

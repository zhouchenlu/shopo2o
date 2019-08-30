package com.oracle.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;

import com.oracle.o2o.dto.ImageHolder;
import com.oracle.o2o.dto.ShopExecution;
import com.oracle.o2o.entity.Area;
import com.oracle.o2o.entity.PersonInfo;
import com.oracle.o2o.entity.Shop;
import com.oracle.o2o.entity.ShopCategory;
import com.oracle.o2o.enums.ShopStateEnum;
import com.oracle.o2o.exceptions.ShopOperationException;
import com.oracle.o2o.service.ShopService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShopServiceTest {
    @Autowired
    private ShopService shopService;

    @Test
    public void testGetShopList() {
        Shop shopCondition = new Shop();
        ShopCategory sc = new ShopCategory();
        sc.setShopCategoryId(14L);
        shopCondition.setShopCategory(sc);
        //选取第1页
        ShopExecution se = shopService.getShopList(shopCondition, 2, 2);
        System.out.println("店铺列表数为：" + se.getShopList().size());
        System.out.println("店铺总数为：" + se.getCount());
    }

    @Test
    @Ignore
    public void testModifyShop() throws ShopOperationException,
            FileNotFoundException {
        Shop shop = new Shop();
        shop.setShopId(1L);
        shop.setShopName("修改后的店铺名称");
        File shopImg = new File("abc.jpg");
        InputStream is = new FileInputStream(shopImg);
        ImageHolder imageHolder = new ImageHolder("abc.jpg", is);
        ShopExecution shopExecution =
                shopService.modifyShop(shop, imageHolder);
        System.out.println("新的图片地址为：" + shopExecution
                .getShop().getShopImg());
    }

    @Test
    @Ignore
    public void testAddShop() throws ShopOperationException,
            FileNotFoundException {
        Shop shop = new Shop();
        PersonInfo owner = new PersonInfo();
        Area area = new Area();
        ShopCategory shopCategory = new ShopCategory();
        owner.setUserId(1L);
        area.setAreaId(2);
        shopCategory.setShopCategoryId(10L);
        shop.setOwner(owner);
        shop.setArea(area);
        shop.setShopCategory(shopCategory);
        shop.setShopName("测试的店铺3");
        shop.setShopDesc("test3");
        shop.setShopAddr("test3");
        shop.setPhone("test3");
        shop.setCreateTime(new Date());
        shop.setEnableStatus(ShopStateEnum.CHECK.getState());
        shop.setAdvice("审核中");
        //初始化文件
        File shopImg = new File("abc.jpg");
        InputStream is = new FileInputStream(shopImg);
        ImageHolder imageHolder = new ImageHolder(shopImg.getName(), is);
        ShopExecution se = shopService.addShop(shop, imageHolder);
        assertEquals(ShopStateEnum.CHECK.getState(), se.getState());
    }

}

package com.oracle.o2o.service;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.oracle.o2o.dao.ShopDao;
import com.oracle.o2o.dto.ImageHolder;
import com.oracle.o2o.dto.ShopExecution;
import com.oracle.o2o.entity.Shop;
import com.oracle.o2o.enums.ShopStateEnum;
import com.oracle.o2o.exceptions.ShopOperationException;
import com.oracle.o2o.util.ImageUtil;
import com.oracle.o2o.util.PageCalculator;
import com.oracle.o2o.util.PathUtil;

import javax.annotation.Resource;

@Service
public class ShopServiceImpl implements ShopService {


    @Resource
    private ShopDao shopDao;

    //这里是pageIndex，dao里是rowIndex
    //这是因为前端页面只认识第几页，数据库只认识第几条
    //所以这里我们需要写一个工具类，实现之间的转换
    public ShopExecution getShopList(Shop shopCondition,
                                     int pageIndex, int pageSize) {
        //将页码转换成行码
        int rowIndex = PageCalculator.calculateRowIndex(
                pageIndex, pageSize);
        //依据查询条件，调用dao层返回相关的店铺列表
        List<Shop> shopList = shopDao.queryShopList(
                shopCondition, rowIndex, pageSize);
        //依据相同的查询条件，返回店铺总数
        int count = shopDao.queryShopCount(shopCondition);
        ShopExecution se = new ShopExecution();
        if (shopList != null) {
            se.setShopList(shopList);
            se.setCount(count);
        } else {
            se.setState(ShopStateEnum.INNER_ERROR.getState());
        }
        return se;
    }


    @Transactional
    public ShopExecution addShop(Shop shop,
                                 ImageHolder thumbnail) {
        // 空值判断
        if (shop == null) {
            return new ShopExecution(ShopStateEnum.NULL_SHOP);
        }
        try {
            // 给店铺信息赋初始值
            shop.setEnableStatus(0);//0未上架，审核中
            shop.setCreateTime(new Date());
            shop.setLastEditTime(new Date());
            // 添加店铺信息
            int effectedNum = shopDao.insertShop(shop);
            if (effectedNum <= 0) {
                throw new ShopOperationException("店铺创建失败");
            } else {
                //判断传入的文件是否为空
                if (thumbnail.getImage() != null) {
                    // 存储图片
                    try {
                        addShopImg(shop, thumbnail);

                    } catch (Exception e) {
                        throw new ShopOperationException("addShopImg error:"
                                + e.getMessage());
                    }
                    // 更新店铺的图片地址
                    effectedNum = shopDao.updateShop(shop);
                    if (effectedNum <= 0) {
                        throw new ShopOperationException("更新图片地址失败");
                    }
                }
            }
        } catch (Exception e) {
            throw new ShopOperationException("addShop error:" + e.getMessage());

        }
        return new ShopExecution(ShopStateEnum.CHECK, shop);
    }

    private void addShopImg(Shop shop,
                            ImageHolder thumbnail) {
        //获取shop图片目录的相对值路径
        String dest = PathUtil.getShopImagePath(shop.getShopId());
        //存储图片并返回相应的相对路径
        String shopImgAddr = ImageUtil.generateThumbnail(
                thumbnail, dest);
        shop.setShopImg(shopImgAddr);

    }

    //根据店铺ID获取Shop信息
    @Override
    public Shop getByShopId(long shopId) {
        return shopDao.queryByShopId(shopId);
    }

    @Override
    public ShopExecution modifyShop(Shop shop,
                                    ImageHolder thumbnail)
            throws ShopOperationException {
        //如果是非法的，就new出异常抛出
        if (shop == null || shop.getShopId() == null) {
            return new ShopExecution(ShopStateEnum.NULL_SHOP);
        } else {
            try {
                //1.判断是否需要处理图片
                if (thumbnail.getImage() != null && thumbnail.getImageName() != null
                        && "".equals(thumbnail.getImageName())) {
                    Shop tempShop = shopDao.queryByShopId(shop.getShopId());
                    //如果之前的图片就不为空，就需要删除之前的
                    if (tempShop.getShopImg() != null) {
                        ImageUtil.deleteFileOrPath(tempShop.getShopImg());
                    }
                    //否则就调用之前写好的方法，生成图片
                    addShopImg(shop, thumbnail);
                }
                //2.更新店铺信息
                shop.setLastEditTime(new Date());
                int effectedNum = shopDao.updateShop(shop);
                //一般为一条记录，如果不是则错误
                if (effectedNum <= 0) {
                    return new ShopExecution(ShopStateEnum.INNER_ERROR);
                } else {
                    shop = shopDao.queryByShopId(shop.getShopId());
                    return new ShopExecution(ShopStateEnum.SUCCESS, shop);
                }
            } catch (Exception e) {
                throw new ShopOperationException("modifyShop error:"
                        + e.getMessage());
            }

        }

    }


}

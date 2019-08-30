package com.oracle.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.oracle.o2o.entity.Shop;

public interface ShopDao {

    // 分页查询店铺，可输入的条件有：店铺名(模糊),店铺状态，店铺类别，区域Id,owner
    //rowIndex：从第几行开始取数据  pageSize：返回的条数
    List<Shop> queryShopList(@Param("shopCondition") Shop shopCondition,
                             @Param("rowIndex") int rowIndex,
                             @Param("pageSize") int pageSize);

    //返回queryShopList总数
    int queryShopCount(@Param("shopCondition") Shop shopCondition);

    //新增店铺返回1表示插入成功一条记录,返回-1则失败
    int insertShop(Shop shop);

    //更新店铺信息
    int updateShop(Shop shop);

    //通过shopid查询店铺信息
    Shop queryByShopId(long shopId);

}

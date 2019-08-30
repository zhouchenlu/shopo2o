package com.oracle.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.oracle.o2o.entity.Product;

public interface ProductDao {

    //删除商品类别之前，将商品类别ID置为空
    int updateProductCategoryToNull(long productCategoryId);

    //查询商品列表并分页，可输入的条件有：
    //商品名（模糊），商品状态，店铺Id,商品类别
    List<Product> queryProductList(@Param("productCondition")
                                           Product productCondition, @Param("rowIndex") int rowIndex,
                                   @Param("pageSize") int pageSize);

    //查询对应的商品总数
    int queryProductCount(@Param("productCondition")
                                  Product productCondition);

    // 插入商品
    int insertProduct(Product product);

    //通过productId查询唯一的商品信息
    Product queryProductById(long productId);

    //更新商品信息
    int updateProduct(Product product);

}

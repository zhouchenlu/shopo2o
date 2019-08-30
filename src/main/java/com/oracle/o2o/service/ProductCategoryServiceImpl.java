package com.oracle.o2o.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oracle.o2o.dao.ProductCategoryDao;
import com.oracle.o2o.dao.ProductDao;
import com.oracle.o2o.dto.ProductCategoryExecution;
import com.oracle.o2o.entity.ProductCategory;
import com.oracle.o2o.enums.ProductCategoryStateEnum;
import com.oracle.o2o.exceptions.ProductCategoryOperationException;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

    @Autowired
    private ProductCategoryDao productCategoryDao;
    @Autowired
    private ProductDao productDao;


    public List<ProductCategory> getProductCategoryList(long shopId) {
        return productCategoryDao
                .queryProductCategoryList(shopId);
    }

    @Transactional
    public ProductCategoryExecution batchAddProductCategory(
            List<ProductCategory> productCategoryList)
            throws ProductCategoryOperationException {
        if (productCategoryList != null &&
                productCategoryList.size() > 0) {
            try {
                int effectedNum = productCategoryDao
                        .batchInsertProductCategory(
                                productCategoryList);
                if (effectedNum <= 0) {
                    throw new ProductCategoryOperationException(
                            "店铺类别创建失败");
                } else {
                    return new ProductCategoryExecution(
                            ProductCategoryStateEnum.SUCCESS);
                }
            } catch (Exception e) {
                throw new ProductCategoryOperationException(
                        "batchAddProductCategory error: "
                                + e.getMessage());
            }
        } else {
            return new ProductCategoryExecution(
                    ProductCategoryStateEnum.EMPTY_LIST);
        }
    }

    @Transactional
    public ProductCategoryExecution deleteProductCategory(
            long productCategoryId, long shopId)
            throws ProductCategoryOperationException {
        //将此商品类别下的商品的类别Id设置为空(后面去做)
        // 解除tb_product里的商品与该producategoryId的关联
        try {
            int effectedNum = productDao
                    .updateProductCategoryToNull(productCategoryId);
            if (effectedNum < 0) {
                throw new ProductCategoryOperationException(
                        "商品类别更新失败");
            }
        } catch (Exception e) {
            throw new ProductCategoryOperationException(
                    "deleteProductCategory error: " + e.getMessage());
        }
        //先做删除
        try {
            int effectedNum = productCategoryDao
                    .deleteProductCategory(productCategoryId, shopId);
            if (effectedNum <= 0) {
                throw new ProductCategoryOperationException(
                        "商品类别删除失败");
            } else {
                return new ProductCategoryExecution(
                        ProductCategoryStateEnum.SUCCESS);
            }
        } catch (Exception e) {
            throw new ProductCategoryOperationException(
                    "deleteProductCategory error:" + e.getMessage());
        }
    }

}
	



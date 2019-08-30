package com.oracle.o2o.controller.shopadmin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oracle.o2o.dto.ProductCategoryExecution;
import com.oracle.o2o.dto.Result;
import com.oracle.o2o.entity.ProductCategory;
import com.oracle.o2o.entity.Shop;
import com.oracle.o2o.enums.ProductCategoryStateEnum;
import com.oracle.o2o.exceptions.ProductCategoryOperationException;
import com.oracle.o2o.service.ProductCategoryService;

@Controller
@RequestMapping("/shopadmin")
public class ProductCategoryManagementController {
    @Autowired
    private ProductCategoryService productCategoryService;

    @RequestMapping(value = "/addproductcategorys",
            method = RequestMethod.POST)
    @ResponseBody
    //因为是从前端批量传入的，所以用@RequestBody接收
    private Map<String, Object> addProductCategorys(
            @RequestBody List<ProductCategory> productCategoryList,
            HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        //productCategoryList需要用到ShopId
        Shop currentShop = (Shop) request.getSession()
                .getAttribute("currentShop");
        for (ProductCategory pc : productCategoryList) {
            pc.setShopId(currentShop.getShopId());
        }
        if (productCategoryList != null
                && productCategoryList.size() > 0) {
            try {
                ProductCategoryExecution pe =
                        productCategoryService
                                .batchAddProductCategory(productCategoryList);
                if (pe.getState() == ProductCategoryStateEnum
                        .SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", pe.getStateInfo());
                }
            } catch (ProductCategoryOperationException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }

        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请至少输入一个商品类别");
        }
        return modelMap;
    }


    @RequestMapping(value = "/getproductcategorylist",
            method = RequestMethod.GET)
    @ResponseBody
    private Result<List<ProductCategory>>
    getProductCategoryList(HttpServletRequest request) {

        //因为在getShopManagementInfo的方法里，
        //页面载入进来之后，会往Session里面放一个值
        //所以这里就不需要再set了，直接可以获取

        Shop currentShop = (Shop) request.getSession()
                .getAttribute("currentShop");
        List<ProductCategory> list = null;
        //currentShop如果胃口，说明你对这个商铺的类别没有操作权限
        if (currentShop != null && currentShop.getShopId() > 0) {
            list = productCategoryService
                    .getProductCategoryList(currentShop.getShopId());
            return new Result<List<ProductCategory>>(true, list);
        } else {
            ProductCategoryStateEnum ps =
                    ProductCategoryStateEnum.INNER_ERROR;
            return new Result<List<ProductCategory>>(
                    false, ps.getState(), ps.getStateInfo());
        }
    }

    @RequestMapping(value = "/removeproductcategory",
            method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> removeProductCategory(
            Long productCategoryId, HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        if (productCategoryId != null && productCategoryId > 0) {
            try {
                Shop currentShop = (Shop) request.getSession()
                        .getAttribute("currentShop");
                ProductCategoryExecution pe = productCategoryService
                        .deleteProductCategory(productCategoryId,
                                currentShop.getShopId());
                if (pe.getState() ==
                        ProductCategoryStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", pe.getStateInfo());
                }
            } catch (ProductCategoryOperationException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请至少选择一个商品类别");
        }
        return modelMap;
    }


}

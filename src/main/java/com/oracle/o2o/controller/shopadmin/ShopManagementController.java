package com.oracle.o2o.controller.shopadmin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oracle.o2o.dto.ImageHolder;
import com.oracle.o2o.dto.ShopExecution;
import com.oracle.o2o.entity.Area;
import com.oracle.o2o.entity.PersonInfo;
import com.oracle.o2o.entity.Shop;
import com.oracle.o2o.entity.ShopCategory;
import com.oracle.o2o.enums.ShopStateEnum;
import com.oracle.o2o.exceptions.ShopOperationException;
import com.oracle.o2o.service.AreaService;
import com.oracle.o2o.service.ShopCategoryService;
import com.oracle.o2o.service.ShopService;
import com.oracle.o2o.util.CodeUtil;
import com.oracle.o2o.util.HttpServletRequestUtil;
import com.oracle.o2o.util.ImageUtil;
import com.oracle.o2o.util.PathUtil;

@Controller
@RequestMapping("/shopadmin")
public class ShopManagementController {

    @Autowired
    private ShopService shopService;
    @Autowired
    private ShopCategoryService shopCategoryService;
    @Autowired
    private AreaService areaService;

    //店铺管理相关的页面
    @RequestMapping(value = "/getshopmanagementinfo",
            method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getShopManagementInfo(
            HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        //获取店铺Id，因为是要在店铺列表中选择一个店铺点击过来的
        long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        if (shopId <= 0) {
            Object currentShopObj = request.getSession()
                    .getAttribute("currentShop");
            if (currentShopObj == null) {
                modelMap.put("redirect", true);
                //重定向到这个url
                modelMap.put("url", "/shop/shopadmin/shoplist");
            } else {
                Shop currentShop = (Shop) currentShopObj;
                modelMap.put("redirect", false);
                modelMap.put("shopId", currentShop.getShopId());
            }
        } else {
            Shop currentShop = new Shop();
            currentShop.setShopId(shopId);
            request.getSession().setAttribute("currentShop", currentShop);
            modelMap.put("redirect", false);
        }
        return modelMap;
    }

    @RequestMapping(value = "/getshoplist", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getShopList(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        PersonInfo user = new PersonInfo();
        //由于目前并没有设置登录，所以假定设定一个userid
        user.setUserId(1L);
        //给user加个名字，便于页面显示
        user.setName("test");
        request.getSession().setAttribute("user", user);
        user = (PersonInfo) request.getSession().getAttribute("user");
        try {
            Shop shopCondition = new Shop();
            shopCondition.setOwner(user);
            ShopExecution se = shopService.getShopList(shopCondition, 0, 100);
            modelMap.put("shopList", se.getShopList());
            modelMap.put("user", user);
            modelMap.put("success", true);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
        }
        return modelMap;
    }


    @RequestMapping(value = "/modifyshop", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> modifyShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "输入了错误的验证码");
            return modelMap;
        }
        // 1.接收并转化相应的参数，包括店铺信息以及图片信息
        String shopStr = HttpServletRequestUtil.getString(request,
                "shopStr");
        ObjectMapper mapper = new ObjectMapper();
        Shop shop = null;
        try {
            shop = mapper.readValue(shopStr, Shop.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }
        CommonsMultipartFile shopImg = null;
        CommonsMultipartResolver commonsMultipartResolver =
                new CommonsMultipartResolver(
                        request.getSession().getServletContext());
        if (commonsMultipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
        }
        // 2.修改店铺信息
        if (shop != null && shop.getShopId() != null) {
            //不用保留，这是因为修改店铺信息的时候
            //是不用修改用户信息的
//			PersonInfo owner = new PersonInfo();
//			owner.setUserId(1L);
//			shop.setOwner(owner);
            //getOriginalFilename()获取原本文件的名字
            ShopExecution se;
            try {
                //如果文件为空，就传入null值
                if (shopImg == null) {
                    se = shopService.modifyShop(shop, null);
                } else {
                    ImageHolder imageHolder = new ImageHolder(
                            shopImg.getOriginalFilename(),
                            shopImg.getInputStream());
                    se = shopService.modifyShop(shop, imageHolder);
                }
                //如果返回值的状态为待审核的状态，就显示成功的状态
                if (se.getState() == ShopStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    //返回出错原因 getStateInfo就是状态的注释
                    modelMap.put("errMsg", se.getStateInfo());
                }
            } catch (ShopOperationException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            } catch (IOException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            }
            return modelMap;
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入店铺Id");
            return modelMap;
        }
    }


    @RequestMapping(value = "/getshopbyid", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getShopById(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        Long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        //>-1说明前端传进来了
        if (shopId > -1) {
            try {
                Shop shop = shopService.getByShopId(shopId);
                List<Area> areaList = areaService.getAreaList();
                modelMap.put("shop", shop);
                modelMap.put("areaList", areaList);
                modelMap.put("success", true);
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty shopId");
        }
        return modelMap;
    }

    @RequestMapping(value = "/getshopinitinfo", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getShopInitInfo() {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        List<ShopCategory> shopCategoryList = new ArrayList<ShopCategory>();
        List<Area> areaList = new ArrayList<Area>();
        try {
            shopCategoryList = shopCategoryService
                    .getShopCategoryList(new ShopCategory());
            areaList = areaService.getAreaList();
            modelMap.put("shopCategoryList", shopCategoryList);
            modelMap.put("areaList", areaList);
            modelMap.put("success", true);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
        }
        return modelMap;
    }


    @RequestMapping(value = "/registershop", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> registerShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        //判断
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "输入了错误的验证码");
            return modelMap;
        }

        // 1.接收并转化相应的参数，包括店铺信息以及图片信息
        String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
        //要转换实体类，需要先new一个这个类
        ObjectMapper mapper = new ObjectMapper();
        Shop shop = null;
        try {
            shop = mapper.readValue(shopStr, Shop.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }
        //接下来接收图片，这个是Spring自带的CommonsMultipartFile
        CommonsMultipartFile shopImg = null;
        //CommonsMultipartResolver：文件上传解析器
        //用来解析request里面的信息
        CommonsMultipartResolver commonsMultipartResolver =
                new CommonsMultipartResolver(request.getSession().getServletContext());
        //判断request里面是否有上传的文件流，如果有就需要做转换成另一个类
        if (commonsMultipartResolver.isMultipart(request)) {
            //转换成这个类，然后进行转换
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
        } else {
            //如果不具备文件流，
            modelMap.put("success", false);
            modelMap.put("errMsg", "上传图片不能为空");
            return modelMap;
        }
        // 2.注册店铺
        if (shop != null && shopImg != null) {
            //可以从Session里获取当前的用户信息
            //user从哪来呢，因为要干这一切肯定得先登陆，
            //可以在登录的时候把user放进去
            PersonInfo owner = (PersonInfo) request
                    .getSession().getAttribute("user");
            //owner.setUserId(1L);不需要了，因为Session就默认获取了
            shop.setOwner(owner);
            //getOriginalFilename()获取原本文件的名字
            ShopExecution se;
            try {
                ImageHolder imageHolder = new ImageHolder(
                        shopImg.getOriginalFilename(),
                        shopImg.getInputStream());
                se = shopService.addShop(shop, imageHolder);
                //如果返回值的状态为待审核的状态，就显示成功的状态
                if (se.getState() == ShopStateEnum.CHECK.getState()) {
                    modelMap.put("success", true);
                    //该用户可以操作的店铺列表
                    @SuppressWarnings("unchecked")
                    List<Shop> shopList = (List<Shop>)
                            request.getSession().getAttribute("shopList");
                    //如果是第一次创建
                    if (shopList == null || shopList.size() == 0) {
                        shopList = new ArrayList<Shop>();
                    }
                    shopList.add(se.getShop());
                    request.getSession().setAttribute("shopList", shopList);
                } else {
                    modelMap.put("success", false);
                    //返回出错原因 getStateInfo就是状态的注释
                    modelMap.put("errMsg", se.getStateInfo());
                }
            } catch (IOException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            }
            return modelMap;
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入店铺信息");
            return modelMap;
        }
        //3.返回结果
        //因为在每一个try里面都已经做了返回值了
        //所以这个第3步可以省略了
    }

//	private static void inputStreamToFile(InputStream ins,
//			File file) {
//		//需要一个输出流，将输出流转换为文件即可
//		FileOutputStream os = null;
//		try {
//			os = new FileOutputStream(file);
//			int bytesRead = 0;
//			//需要用这个buffered读inputS里面的内容
//			byte[] buffer = new byte[1024];
//			while((bytesRead = ins.read(buffer)) != -1) {
//				os.write(buffer,0,bytesRead);
//			}
//		} catch (Exception e) {
//			throw new RuntimeException("调用inputStreamToFile产生异常"
//		          +e.getMessage());
//		}finally {
//			try {
//				if(os != null) {
//					os.close();
//				}
//				if(ins != null) {
//					ins.close();
//				}
//			} catch (Exception e2) {
//				throw new RuntimeException("关闭inputStreamToFile的IO产生异常"
//				          +e2.getMessage());
//			}
//		}
//	}


}

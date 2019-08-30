package com.oracle.o2o.util;

public class PathUtil {

    private static String seperator =
            System.getProperty("file.separator");

    public static String getImageBasePath() {
        //根据不同的操作系统选择不同的根目录
        String os = System.getProperty("os.name");
        String basePath = "";
        //如果是Window系统
        if (os.toLowerCase().startsWith("win")) {
            basePath = "F:/projectdev/image/";
        } else {
            basePath = "/Users/baidu/work/image/";
        }
        basePath = basePath.replace("/", seperator);
        return basePath;
    }

    //获取店铺图片的绝对路径(每家店铺图片都保存在每家店铺)
    public static String getShopImagePath(long shopId) {
        String imagePath = "upload/images/item/shop/" + shopId + "/";
        return imagePath.replace("/", seperator);
    }

}

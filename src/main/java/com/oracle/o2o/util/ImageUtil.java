package com.oracle.o2o.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import javax.imageio.ImageIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.oracle.o2o.dto.ImageHolder;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

public class ImageUtil {

    private static String basePath =
            Thread.currentThread().getContextClassLoader()
                    .getResource("").getPath();
    private static final SimpleDateFormat sDateFormat
            = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final Random r = new Random();
    private static Logger logger =
            LoggerFactory.getLogger(ImageUtil.class);


    /**
     * 将CommonsMultipartFile转换成File类
     */
    public static File transferCommonsMultipartFileToFile(
            CommonsMultipartFile cFile) {
        File newFile = new File(cFile.getOriginalFilename());
        try {
            //将原来的文件流写进新创建的文件里面去
            cFile.transferTo(newFile);
        } catch (IllegalStateException e) {
            logger.error(e.toString());
            e.printStackTrace();
        } catch (IOException e) {
            logger.error(e.toString());
            e.printStackTrace();
        }
        return newFile;
    }

    // 用来处理缩略图，和商品的小图
    // CommonsMultipartFile文件处理对象
    // targetAddr图片的存储路径
    public static String generateThumbnail(
            ImageHolder thumbnail,
            String targetAddr) {
        // 获取不重复的随机名
        String realFileName = getRandomFileName();
        // 获取文件的扩展名如png,jpg等
        String extension = getFileExtension(thumbnail.getImageName());
        // 如果目标路径不存在，则自动创建
        makeDirPath(targetAddr);
        // 获取文件存储的相对路径(带文件名)
        String relativeAddr = targetAddr + realFileName + extension;
        logger.debug("current relativeAddr is :" + relativeAddr);
        // 获取文件要保存到的目标路径
        File dest = new File(PathUtil.getImageBasePath() + relativeAddr);
        logger.debug("current complete addr is :" +
                PathUtil.getImageBasePath() + relativeAddr);
        logger.debug("basePath is :" + basePath);
        // 调用Thumbnails生成带有水印的图片
        try {
            System.out.println("basePath---->1"+basePath);
            BufferedImage read = ImageIO.read(new File(basePath + "/watermark.jpg"));
            System.out.println("---->2");
            Thumbnails.of(thumbnail.getImage()).size(200, 200)
                    .watermark(Positions.BOTTOM_RIGHT,
                            read, 0.25f)
                    .outputQuality(0.8f).toFile(dest);
        } catch (IOException e) {
            logger.error(e.toString());
            throw new RuntimeException("创建缩略图失败：" + e.toString());
        }
        // 返回图片相对路径地址
        return relativeAddr;
    }

    /*
     * 创建目标路径所涉及到的目录，即/home/work/xiangze/xxx.jpg,
     * 那么 home work xiangze这三个文件夹都得自动创建
     */
    private static void makeDirPath(String targetAddr) {
        //目标文件所属的全路径
        String realFileParentPath = PathUtil.getImageBasePath()
                + targetAddr;
        //将全路径给传进去
        File dirPath = new File(realFileParentPath);
        //如果不存在此路径，创建
        if (!dirPath.exists()) {
            dirPath.mkdirs();
        }
    }

    //获取输入文件流的扩展名
    private static String getFileExtension(
            String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    //生成随机文件名，规则当前年月日小时分钟秒钟+五位随机数
    public static String getRandomFileName() {
        // 获取随机的五位数
        int rannum = r.nextInt(89999) + 10000;
        String nowTimeStr = sDateFormat.format(new Date());
        return nowTimeStr + rannum;
    }

    public static void main(String[] args) throws IOException {
        // 获取水印图片的绝对值路径
        String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        // 主类，用来传入指定的图片 .watermark水印 水印位置：BOTTOM_RIGHT右下角
        // ImageIO.read引入水印图片的位置
        // 0.25f为透明度 压缩为80%
        // toFile输出到哪个地方
        Thumbnails.of(new File("abc.jpg")).size(200, 200)
                .watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "watermark.jpg")), 0.25f)
                .outputQuality(0.8f).toFile("abcnew.jpg");
    }

    /*
     * storePath是文件的路径还是目录的路径
     * 如果storePath是文件路径则删除该文件
     * 如果storePath是目录路径则删除该目录下的所有文件
     */
    public static void deleteFileOrPath(String storePath) {
        File fileOrPath = new File(PathUtil.getImageBasePath()
                + storePath);
        //是否存在
        if (fileOrPath.exists()) {
            //是目录
            if (fileOrPath.isDirectory()) {
                File files[] = fileOrPath.listFiles();
                for (int i = 0; i < files.length; i++) {
                    files[i].delete();
                }
            }
            fileOrPath.delete();
        }
    }


    //处理详情图，并返回新生成图片的相对值路径
    public static String generateNormalImg(
            ImageHolder thumbnail, String targetAddr) {
        // 获取不重复的随机名
        String realFileName = getRandomFileName();
        // 获取文件的扩展名如png,jpg等
        String extension = getFileExtension(thumbnail.getImageName());
        // 如果目标路径不存在，则自动创建
        makeDirPath(targetAddr);
        // 获取文件存储的相对路径(带文件名)
        String relativeAddr = targetAddr + realFileName + extension;
        logger.debug("current relativeAddr is :" + relativeAddr);
        // 获取文件要保存到的目标路径
        File dest = new File(PathUtil.getImageBasePath() + relativeAddr);
        logger.debug("current complete addr is :"
                + PathUtil.getImageBasePath() + relativeAddr);
        // 调用Thumbnails生成带有水印的图片
        try {
            Thumbnails.of(thumbnail.getImage()).size(337, 640)
                    .watermark(Positions.BOTTOM_RIGHT,
                            ImageIO.read(new File(basePath + "/watermark.jpg")), 0.25f)
                    .outputQuality(0.9f).toFile(dest);
        } catch (IOException e) {
            logger.error(e.toString());
            throw new RuntimeException("创建缩图片失败：" + e.toString());
        }
        // 返回图片相对路径地址
        return relativeAddr;
    }


}

package com.json.osp.util;

import com.json.osp.dto.ImageHolder;
import jdk.nashorn.internal.ir.annotations.Ignore;
import net.coobird.thumbnailator.Thumbnails;

import net.coobird.thumbnailator.geometry.Positions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.imageio.ImageIO;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
public class ImageUtil {
    // 通过线程去逆推到类加载器得到资源路径
    public static String basePath = Thread.currentThread() // 当前线程
            .getContextClassLoader() // 类加载器
            .getResource("")
            .getPath();
    private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final Random r = new Random();

    private static Logger logger = LoggerFactory.getLogger(ImageUtil.class);


    @Ignore
    public static void main(String[] args) throws IOException {
        String imgUrl = basePath + "/json.jpg";
        Thumbnails.of(new File(imgUrl))
                .size(200, 200) // 指定图片大小
                // 三个参数：水印位置， 水印图片， 透明度
                .watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "/watermark.jpg")), 0.25f)
                .outputQuality(0.8f) // 压缩图片 （80%）
                .toFile("D:/jsonNew.jpg"); // 输出路径
    }

    /**
     * 将CommonsMultipartFile转换成File类
     * Spring 对上传上来的文件会用一个MultipartFile的接口去处理，CommonsMultipartFile实现了MultipartFile
     * 将CommonsMultipartFile可以转换成File，方便我们进行处理
     * @param cFile
     * @return
     */
    public static File transferCommonsMultipartFileToFile(CommonsMultipartFile cFile) {
        File newFile = new File(cFile.getOriginalFilename());
        try {
            // 将CommonsMultipartFile文件流写入File中
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

    /**
     * 处理详情图，并返回新生成图片的相对值路径
     *
     * @param thumbnail
     * @param targetAddr
     * @return
     */
    public static String generateNormalImg(ImageHolder thumbnail, String targetAddr) {
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
        File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
        logger.debug("current complete addr is :" + PathUtil.getImgBasePath() + relativeAddr);
        // 调用Thumbnails生成带有水印的图片
        try {
            System.out.println("水印路径：" + basePath + "/watermark.png");
            Thumbnails.of(thumbnail.getImage())
                    .size(200, 200)
                    .toFile(dest);
        } catch (IOException e) {
            logger.error(e.toString());
            throw new RuntimeException("创建缩图片失败：" + e.toString());
        }
        // 返回图片相对路径地址
        return relativeAddr;
    }

    /**
     * 处理缩略图，并返回新生成图片的相对值路径
     *
     * @param thumbnail
     * @param relativeAddr 表示图片存放目录的相对路径(带文件名)
     * @return
     */
    public static void generateUserImgThumbnail(ImageHolder thumbnail, String relativeAddr) throws FileNotFoundException {
//        // 获取不重复的随机名
////        String realFileName = getRandomFileName();
//        // 获取文件的扩展名如png,jpg等
//        String extension = getFileExtension(thumbnail.getImageName());
//        // 如果目标路径不存在，则自动创建
//        makeDirPath(targetAddr);
//        // 获取文件存储的相对路径(带文件名)
//        String relativeAddr = targetAddr + realFileName + extension;
        logger.debug("current relativeAddr is :" + relativeAddr);
        // 获取文件要保存到的目标路径
        File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
        logger.debug("current complete addr is :" + PathUtil.getImgBasePath() + relativeAddr);
        logger.debug("basePath is :" + basePath);
        // 用户没有上传头像
        if(thumbnail == null) {
            File shopImg = new File(basePath + "defaultUserImg.png");
            InputStream is = new FileInputStream(shopImg);
            // 调用Thumbnails生成用户头像
            try {
                Thumbnails.of(is)
                        .size(200, 200)
                        .toFile(dest);
            } catch (IOException e) {
                logger.error(e.toString());
                throw new RuntimeException("创建头像失败：" + e.toString());
            }
        }
        else { // 用户上传了头像
            // 调用Thumbnails生成用户头像
            try {
                Thumbnails.of(thumbnail.getImage())
                        .size(200, 200)
                        .toFile(dest);
            } catch (IOException e) {
                logger.error(e.toString());
                throw new RuntimeException("创建头像失败：" + e.toString());
            }
        }

    }

    /**
     * 创建目标路径所涉及的目录
     * @param targetAddr
     */
    private static void makeDirPath(String targetAddr) {
        // 获取绝对路径
        String realFileParentPath = PathUtil.getImgBasePath() + targetAddr;
        File dirPath = new File(realFileParentPath);
        if (!dirPath.exists()) {
            dirPath.mkdirs(); // 递归创建不存在文件夹
        }
    }


    /**
     * 获取文件的扩展名
     * @param fileName
     * @return
     */
    public static String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf('.'));
    }


    /**
     * 生成随机文件名:当前年月日小时分钟秒钟+五位随机数
     * @return
     */
    public static String getRandomFileName() {
        // 获取随机的五位数
        int ranNum = r.nextInt(89999) + 10000;
        String nowTimeStr = sDateFormat.format(new Date());
        return nowTimeStr + ranNum;
    }

    /**
     * storePath是文件路径还是目录路径
     * 是文件路径则删除该文件就行
     * 是目录路径则删除该目录下所有文件
     * @param storePath
     */
    public static void deleteFileOrPath(String storePath) {
        File fileOrPath = new File(PathUtil.getImgBasePath() + storePath);
        if(fileOrPath.exists()) {
            // 这里存在漏洞，若目录下有目录呢？之后补
            if(fileOrPath.isDirectory()) {
                for(File f : fileOrPath.listFiles())
                    f.delete();
            }
            fileOrPath.delete();
        }
    }
}

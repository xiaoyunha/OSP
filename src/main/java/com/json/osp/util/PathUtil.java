package com.json.osp.util;

import com.json.osp.dto.ImageHolder;

import java.io.File;

public class PathUtil {

    /**
     * 获取项目资源存放根路径，其实就是项目存放图片的根路径
     * @return
     */
    public static String getImgBasePath() {
        // 获取运行系统的名字
        String os = System.getProperty("os.name");
        String basePath = "";
        // 以下的分隔符先不考虑，之后会替换成合适的
        if (os.toLowerCase().startsWith("win")) {
            basePath = "D:/data/ospImage";
        } else {
            basePath = "/home/ospImage";
        }
        // 将所有的分隔符替换成对应系统默认的分隔符
        basePath = basePath.replace("/", File.separator);
        return basePath;
    }

    // 获取店铺图片的存储路径（相对路径）
    public static String getProductImagePath(long productId) {
        String imagePath = "/upload/productImg/" + productId + "/";
        return imagePath.replace("/", File.separator);
    }

    // 获取用户头像存储路径（相对路径，不包含文件名）
    public static String generateUserImgPath(ImageHolder thumbnail) {
        String targetAddr = "/upload/userImg/";
        String relativeImgAdrr = null;
        // 若用户没有上传头像

        // 获取不重复的随机名
        String realFileName = ImageUtil.getRandomFileName();
        String extension = ".png"; // 用户不上传头像，使用默认头像的扩展名
        if (thumbnail != null) {
            // 获取文件的扩展名如png,jpg等
            extension = ImageUtil.getFileExtension(thumbnail.getImageName());
        }
        // 如果目标路径不存在，则自动创建
        makeDirPath(targetAddr);
        // 获取文件存储的相对路径(带文件名)
        String relativeAddr = targetAddr + realFileName + extension;

        return relativeAddr.replace("/", File.separator);
    }

    /**
     * 创建目标路径所涉及的目录
     * @param targetAddr
     */
    private static void makeDirPath(String targetAddr) {
        // 获取绝对路径
        String realFileParentPath = getImgBasePath() + targetAddr;
        File dirPath = new File(realFileParentPath);
        if (!dirPath.exists()) {
            dirPath.mkdirs(); // 递归创建不存在文件夹
        }
    }
}

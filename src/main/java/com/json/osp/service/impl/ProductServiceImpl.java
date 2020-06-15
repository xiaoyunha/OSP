package com.json.osp.service.impl;

import com.json.osp.dao.ProductDao;
import com.json.osp.dao.RecordDao;
import com.json.osp.dao.UserDao;
import com.json.osp.dto.ImageHolder;
import com.json.osp.dto.ProductExecution;
import com.json.osp.entity.*;
import com.json.osp.service.ProductService;
import com.json.osp.util.ImageUtil;
import com.json.osp.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductDao productDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private RecordDao recordDao;
    @Transactional
    @Override
    public ProductExecution addProduct(Product product, List<ImageHolder> productImgHolderList) throws RuntimeException {
        // 空值判断
        if (product != null) {
            // 给商品设置上默认属性
            product.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            product.setLastEditTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            // 默认为上架的状态
            product.setEnableStatus(1);
            productDao.insertProduct(product);
            // 若商品详情图不为空则添加
            if (productImgHolderList != null && productImgHolderList.size() > 0) {
                addProductImgList(product, productImgHolderList);
            }
            return new ProductExecution(1, "添加商品成功");
        } else {
            // 传参为空则返回空值错误信息
            return new ProductExecution(0, "添加失败,product为空");
        }
    }

    @Transactional
    @Override
    public void modifyProduct(Product product) {
        try {
            productDao.updateProduct(product);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Override
    public List<Product> getProductList(Long productId, Long productCategoryId) {
        return productDao.selectProduct(productId, productCategoryId);
    }

    @Override
    @Transactional
    public int buyOne(Product buyOne, UserInfo userInfo) throws RuntimeException {

        BigDecimal totalMoney = buyOne.getPrice().multiply(new BigDecimal(buyOne.getCount()));
        // 余额不足
        if(userInfo.getMoney().compareTo(totalMoney) < 0)
            return 2;
        Product product = productDao.selectProduct(buyOne.getProductId(), null).get(0);
        // 库存不足
        if(product.getCount() < buyOne.getCount())
            return 3;
        // 下架
        if(product.getEnableStatus() != 1)
            return 4;

        //购买过程
        try {
            // 买家扣费
            userInfo.setMoney(userInfo.getMoney().subtract(totalMoney));
            // 减少商品库存
            product.setCount(product.getCount() - buyOne.getCount());
            // 商家收钱
            UserInfo admin = userDao.queryUserInfo(1L);
            admin.setMoney(admin.getMoney().add(totalMoney));
            userDao.updateUserInfo(admin);
            // 更新商品信息
            productDao.updateProduct(product);
            // 更新用户信息
            userDao.updateUserInfo(userInfo);
            // 购买记录
            BoughtRecord boughtRecord = new BoughtRecord();
            boughtRecord.setBoughtTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            boughtRecord.setUnitPrice(buyOne.getPrice());
            boughtRecord.setCount(buyOne.getCount());
            boughtRecord.setUser(userInfo);
            boughtRecord.setProduct(buyOne);
            int effectNum = recordDao.insertBoughtRecord(boughtRecord);
            if(effectNum > 0)
                return 1;
            else
                return 5;

        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }

    }

    @Transactional
    @Override
    public int buyAll(List<Product> shoppingCar, UserInfo userInfo) {
        BigDecimal totalMoney = new BigDecimal("0.00");
        for(Product product : shoppingCar) {
            Product p = productDao.selectProduct(product.getProductId(), null).get(0);
            // 库存不足或者下架
            if(p.getEnableStatus() != 1 || (p.getCount() < product.getCount()))
                return 3;
            totalMoney = totalMoney.add(p.getPrice().multiply(new BigDecimal(product.getCount())));
        }
        // 余额不足
        if(userInfo.getMoney().compareTo(totalMoney) < 0)
            return 2;

        try {
            // 买家扣费
            userInfo.setMoney(userInfo.getMoney().subtract(totalMoney));

            // 减少商品库存
            for(Product product : shoppingCar) {
                Product p = productDao.selectProduct(product.getProductId(), null).get(0);
                p.setCount(p.getCount() -product.getCount());
                productDao.updateProduct(p);
                // 购买记录
                BoughtRecord boughtRecord = new BoughtRecord();
                boughtRecord.setBoughtTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                boughtRecord.setUnitPrice(p.getPrice());
                boughtRecord.setCount(product.getCount());
                boughtRecord.setUser(userInfo);
                boughtRecord.setProduct(product);
                recordDao.insertBoughtRecord(boughtRecord);
            }

            // 商家收钱
            UserInfo admin = userDao.queryUserInfo(1L);
            admin.setMoney(admin.getMoney().add(totalMoney));
            userDao.updateUserInfo(admin);
            // 更新用户信息
            userDao.updateUserInfo(userInfo);
                return 1;
        }catch (Exception e) {
            throw new RuntimeException();
        }



    }

    @Override
    public List<Product> RecommendProduct(UserInfo userInfo) {
         List<BrowseRecord> browseRecordList = recordDao.queryBrowseRecord(userInfo.getUserId(), null);
         Set<Long> productCategorySet = new HashSet<>();
         List<Product> productList = new ArrayList<>();
         for(BrowseRecord browseRecord : browseRecordList){
             productCategorySet.add(browseRecord.getProduct().getProductCategory().getProductCategoryId());
         }
         for(Long productCategoryId : productCategorySet)
            productList.addAll(productDao.selectProduct(null, productCategoryId));

        return productList;
    }

    @Override
    public void addBrowseRecord(UserInfo userInfo, Product product) {
        BrowseRecord browseRecord = new BrowseRecord();
        browseRecord.setBrowseTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        browseRecord.setProduct(product);
        browseRecord.setUser(userInfo);
        recordDao.insertBrowseRecord(browseRecord);
    }

    @Override
    public List<BoughtRecord> getBoughtRecord(UserInfo userInfo) {

        return recordDao.queryBoughtRecord(userInfo.getUserId(), null);
    }

    @Override
    public List<BrowseRecord> getBrowseRecord(UserInfo userInfo) {
        return recordDao.queryBrowseRecord(userInfo.getUserId(), null);
    }



    /**
     * 批量添加图片
     *
     * @param product
     * @param productImgHolderList
     */
    private void addProductImgList(Product product, List<ImageHolder> productImgHolderList) {
        // 获取图片存储路径，这里直接存放到相应店铺的文件夹底下
        String dest = PathUtil.getProductImagePath(product.getProductId());
        List<ProductImg> productImgList = new ArrayList<ProductImg>();
        // 遍历图片一次去处理，并添加进productImg实体类里
        for (ImageHolder productImgHolder : productImgHolderList) {
            String imgAddr = ImageUtil.generateNormalImg(productImgHolder, dest);
            ProductImg productImg = new ProductImg();
            productImg.setImgAddr(imgAddr);
            productImg.setProductId(product.getProductId());
            productImg.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            productImgList.add(productImg);
        }
        // 如果确实是有图片需要添加的，就执行批量添加操作
        if (productImgList.size() > 0) {
            try {
                int effectedNum = productDao.batchInsertProductImg(productImgList);
                if (effectedNum <= 0) {
                    throw new RuntimeException("创建商品详情图片失败");
                }
            } catch (Exception e) {
                throw new RuntimeException("创建商品详情图片失败:" + e.toString());
            }
        }
    }
}

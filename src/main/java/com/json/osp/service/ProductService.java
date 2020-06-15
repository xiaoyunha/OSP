package com.json.osp.service;

import com.json.osp.dto.ImageHolder;
import com.json.osp.dto.ProductExecution;
import com.json.osp.entity.BoughtRecord;
import com.json.osp.entity.BrowseRecord;
import com.json.osp.entity.Product;
import com.json.osp.entity.UserInfo;

import java.util.List;

public interface ProductService {
    /**
     * 添加商品信息以及图片处理
     *
     * @param product
     * @param productImgList
     * @return
     * @throws RuntimeException
     */
    ProductExecution addProduct(Product product, List<ImageHolder> productImgList)
            throws RuntimeException;

    void modifyProduct(Product product);

    /**
     * 获取商品信息
     * @param productId
     * @param productCategoryId
     * @return
     */
    List<Product> getProductList(Long productId, Long productCategoryId);

    /**
     * 购买一件商品
     * @param buyOne
     * @return
     * @throws RuntimeException
     */
    int buyOne(Product buyOne, UserInfo userInfo) throws RuntimeException;

    List<BoughtRecord> getBoughtRecord(UserInfo userInfo);

    List<BrowseRecord> getBrowseRecord(UserInfo userInfo);

    int buyAll(List<Product> shoppingCar, UserInfo userInfo);

    List<Product> RecommendProduct(UserInfo userInfo);

    void addBrowseRecord(UserInfo userInfo, Product product);
}

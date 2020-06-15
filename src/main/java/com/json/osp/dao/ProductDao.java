package com.json.osp.dao;

import com.json.osp.entity.Product;
import com.json.osp.entity.ProductCategory;
import com.json.osp.entity.ProductImg;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductDao {

    /**
     * 插入商品信息
     * @param product
     * @return
     */
    int insertProduct(Product product);

    /**
     * 更新商品信息
     *
     * @param product
     * @return
     */
    int updateProduct(Product product);

    /**
     * 批量插入商品图片
     * @param productImgList
     * @return
     */
    int batchInsertProductImg(List<ProductImg> productImgList);

    /**
     * 按商品类别或者商品Id查找商品
     * @return
     */
    List<Product> selectProduct(@Param("productId")Long productId,
                                @Param("productCategoryId")Long productCategoryId);


}

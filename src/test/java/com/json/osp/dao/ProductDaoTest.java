package com.json.osp.dao;

import com.json.osp.BaseTest;
import com.json.osp.entity.Product;
import com.json.osp.entity.ProductCategory;
import com.json.osp.entity.ProductImg;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProductDaoTest extends BaseTest {
    @Autowired
    private ProductDao productDao;

    @Ignore
    @Test
    public void testInsertProduct() {
        Product product = new Product();
        ProductCategory productCategory = new ProductCategory();
        productCategory.setProductCategoryId(2L);
        product.setPrice(new BigDecimal("100.00"));
        product.setEnableStatus(1);
        product.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        product.setProductCategory(productCategory);
        product.setProductName("lala");
        product.setProductDesc("tesst");
        product.setCount(100);
//        product.setImgAddr("///");
        productDao.insertProduct(product);
        System.out.println(product.getProductId());
    }

    @Ignore
    @Test
    public void testbatchInsertProductImg() {
        List<ProductImg> productImgList = new ArrayList<>();
        for(int i = 0 ; i < 3 ; i ++) {
            ProductImg img = new ProductImg();
            img.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            img.setImgAddr("test" + i);
            img.setPriority(1);
            img.setProductId(1L);
            productImgList.add(img);
        }
        int i = productDao.batchInsertProductImg(productImgList);
        System.out.println(i);
    }

    @Ignore
    @Test
    public void testUpdateProduct() {
        Product product = new Product();
        ProductCategory productCategory = new ProductCategory();
        productCategory.setProductCategoryId(1L);
        product.setPrice(new BigDecimal("10.00"));
        product.setProductId(1L);
//        product.setEnableStatus(1);
        product.setLastEditTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        product.setProductCategory(productCategory);
        product.setProductName("testgai");
        product.setProductDesc("tesst");
        product.setCount(1040);
//        product.setImgAddr("///");
        productDao.updateProduct(product);
        System.out.println(product.getProductId());
    }

    @Ignore
    @Test
    public void testSelectProductByProductCategory() {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setProductCategoryId(1L);
        List<Product> productList = productDao.selectProduct(null, null);
        for(Product product : productList){
            System.out.println(product);
        }
    }
}

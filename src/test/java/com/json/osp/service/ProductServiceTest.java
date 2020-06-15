package com.json.osp.service;

import com.json.osp.BaseTest;
import com.json.osp.dao.ProductDao;
import com.json.osp.dto.ImageHolder;
import com.json.osp.entity.Product;
import com.json.osp.entity.ProductCategory;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProductServiceTest extends BaseTest {
    @Autowired
    private ProductService productService;
    @Ignore
    @Test
    public void testAddProduct() throws FileNotFoundException {
        List<ImageHolder> imageHolders = new ArrayList<>();
//        File shopImg = new File("C:\\Users\\json\\Pictures\\Camera Roll\\json.jpg");
//        InputStream is = new FileInputStream(shopImg);
        for(int i = 0 ; i < 3 ; i ++) {
            File shopImg = new File("C:\\Users\\json\\Pictures\\Camera Roll\\json"+ i + ".jpg");
            InputStream is = new FileInputStream(shopImg);
            ImageHolder imageHolder = new ImageHolder(shopImg.getName(), is);
            imageHolders.add(imageHolder);
        }

        Product product = new Product();
        ProductCategory productCategory = new ProductCategory();
        productCategory.setProductCategoryId(3L);
        product.setPrice(new BigDecimal("10.00"));
//        product.setProductId(1L);
//        product.setEnableStatus(1);
        product.setLastEditTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        product.setProductCategory(productCategory);
        product.setProductName("testgai");
        product.setProductDesc("tesst");
        product.setCount(1040);
        productService.addProduct(product, imageHolders);
        System.out.println(product.getProductId());
    }
}

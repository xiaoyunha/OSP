package com.json.osp.dao;

import com.json.osp.BaseTest;
import com.json.osp.entity.BoughtRecord;
import com.json.osp.entity.BrowseRecord;
import com.json.osp.entity.Product;
import com.json.osp.entity.UserInfo;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RecordDaoTest extends BaseTest {
    @Autowired
    private RecordDao recordDao;

    @Ignore
    @Test
    public void testInsertBoughtRecord() {
        BoughtRecord boughtRecord = new BoughtRecord();
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(27L);
        Product product = new Product();
        product.setProductId(1L);
        boughtRecord.setBoughtTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        boughtRecord.setCount(100);
        boughtRecord.setUnitPrice(new BigDecimal("100"));
        boughtRecord.setUser(userInfo);
        boughtRecord.setProduct(product);

        recordDao.insertBoughtRecord(boughtRecord);
    }

    @Ignore
    @Test
    public void testInsertBrowseRecord() {
        BrowseRecord browseRecord = new BrowseRecord();
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(27L);
        Product product = new Product();
        product.setProductId(1L);
        browseRecord.setBrowseTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        browseRecord.setUser(userInfo);
        browseRecord.setProduct(product);
        for(int i = 0 ; i < 10 ; i ++)
            recordDao.insertBrowseRecord(browseRecord);
    }

    @Ignore
    @Test
    public void testQueryBoughtRecord() {
        Long userId = 27L;
        Long productCategoryId = 1L;
        System.out.println(recordDao.queryBoughtRecord(27L, null));
    }

    @Ignore
    @Test
    public void testQueryBrowseRecord() {
        Long userId = 27L;
        Long productCategoryId = 1L;
        System.out.println(recordDao.queryBrowseRecord(27L, null));
    }
}

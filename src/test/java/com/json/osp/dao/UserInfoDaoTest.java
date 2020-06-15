package com.json.osp.dao;

import com.json.osp.BaseTest;
import com.json.osp.entity.LocalAuth;
import com.json.osp.entity.ProductCategory;
import com.json.osp.entity.UserInfo;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class UserInfoDaoTest extends BaseTest {
    @Autowired
    private UserDao userDao;

    @Ignore
    @Test
    public void testQueryUserInfo() {
        UserInfo userInfo = userDao.queryUserInfo(27L);
        System.out.println(userInfo);
    }

    @Ignore
    @Test
    public void testInsertUserInfo() {
        UserInfo userInfo = new UserInfo();
        userInfo.setGender("男");
        userInfo.setName("test");
        userInfo.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        userInfo.setEmail("test");
        userInfo.setMoney(new BigDecimal("10000.6666"));
        userInfo.setUserType(1);
        userInfo.setEnableStatus(0);
        int effectNum = userDao.insertUserInfo(userInfo);
        System.out.println(userInfo);
    }

    @Ignore
    @Test
    public void testUpdateUserInfo() {
        UserInfo userInfo = new UserInfo();
        ProductCategory productCategory = new ProductCategory();
        productCategory.setProductCategoryId(2L);
        userInfo.setUserId(28L);
        userInfo.setGender("测");
        userInfo.setName("test1");
        userInfo.setLastEditTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        userInfo.setEmail("test1");
        userInfo.setMoney(new BigDecimal("10480.3"));
        userInfo.setProductCategory(productCategory);
//        userInfo.setUserType(1);
        userInfo.setEnableStatus(1);
        int effectNum = userDao.updateUserInfo(userInfo);
        assertEquals(1, effectNum);
        System.out.println(userInfo);
    }

    @Ignore
    @Test
    public void testUpdateLoacalAuth() {
        LocalAuth localAuth = new LocalAuth();
        localAuth.setUsername("xiugaiceshi");
        localAuth.setPassword("xiugaiceshi");
        localAuth.setLocalAuthId(1L);
        userDao.updateLocalAuth(localAuth);
    }

    @Ignore
    @Test
    public void  testInsertLocalAuth() {
        LocalAuth localAuth = new LocalAuth();
        localAuth.setUserId(1L);
        localAuth.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        localAuth.setUsername("test");
        localAuth.setPassword("test");
        userDao.insertLocalAuth(localAuth);
        System.out.println(localAuth.getLocalAuthId());

    }

    @Ignore
    @Test
    public void testSelectLocalAuth() {
        LocalAuth localAuth = new LocalAuth();
        localAuth.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        localAuth.setUsername("test1");
        localAuth.setPassword("test");
        localAuth = userDao.selectLocalAuth(localAuth);
    }

    @Ignore
    @Test
    public void testSelectLocalAuthUsername() {
        String username = "test1";
        Long user_id = userDao.selectLocalAuthUsername(username);
        System.out.println(user_id);
    }

    @Ignore
    @Test
    public void testSelectAssistantInfo() {
        List<UserInfo> assistantList = userDao.selectAssistantInfo();
        System.out.println(assistantList);
    }
}
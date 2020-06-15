package com.json.osp.service;

import com.json.osp.BaseTest;
import com.json.osp.dto.ImageHolder;
import com.json.osp.entity.LocalAuth;
import com.json.osp.entity.UserInfo;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;

public class UserInfoServiceTest extends BaseTest {
    @Autowired
    private UserInfoService userInfoService;
    @Ignore
    @Test
    public void testAddUser() throws FileNotFoundException {

        UserInfo user = new UserInfo();
        user.setName("tests");
        user.setEmail("sss");
        user.setEnableStatus(0);
        user.setUserType(1);
        user.setMoney(new BigDecimal("1000"));
        LocalAuth auth = new LocalAuth();
        auth.setUsername("yonghdduming");
        auth.setPassword("mima");

        File shopImg = new File("C:\\Users\\json\\Pictures\\Camera Roll\\json.jpg");
        InputStream is = new FileInputStream(shopImg);
        ImageHolder imageHolder = new ImageHolder(shopImg.getName(), is);
        userInfoService.addUser(user, auth, null);
        System.out.println(user.getProfileImg());

    }

    @Ignore
    @Test
    public void testUpdateUser() throws FileNotFoundException {

        UserInfo user = new UserInfo();
        user.setUserId(1L);
        user.setName("xiu");
        user.setEmail("ssd");
        user.setMoney(new BigDecimal("104500"));

        LocalAuth auth = new LocalAuth();
        auth.setLocalAuthId(1L);
        auth.setUsername("yongsdsdhuming");
        auth.setPassword("msdsdsdima");

        File shopImg = new File("C:\\Users\\json\\Pictures\\Camera Roll\\json.jpg");
        InputStream is = new FileInputStream(shopImg);
        ImageHolder imageHolder = new ImageHolder(shopImg.getName(), is);
        userInfoService.modifyUserInfo(user, auth, imageHolder);
        System.out.println(user.getProfileImg());

    }
}

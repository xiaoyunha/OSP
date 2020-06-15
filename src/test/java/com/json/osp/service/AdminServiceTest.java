package com.json.osp.service;

import com.json.osp.BaseTest;
import com.json.osp.dao.UserDao;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AdminServiceTest extends BaseTest {
    @Autowired
    private UserDao userDao;
    @Autowired
    private AdminService adminService;


    @Ignore
    @Test
    public void testDeleteOneAssistant() {
        adminService.deleteOneAssistant(24L);
    }
}

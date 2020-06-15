package com.json.osp.service.impl;

import com.json.osp.dao.ProductDao;
import com.json.osp.dao.RecordDao;
import com.json.osp.dao.UserDao;
import com.json.osp.dto.Assistant;
import com.json.osp.entity.BoughtRecord;
import com.json.osp.entity.LocalAuth;
import com.json.osp.entity.Product;
import com.json.osp.entity.UserInfo;
import com.json.osp.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private RecordDao recordDao;
    @Autowired
    private ProductDao productDao;

    @Override
    public List<Assistant> getAssistantInfo() {
        List<UserInfo> userInfoList = userDao.selectAssistantInfo();
        List<Assistant> assistantList = new ArrayList<>();
        for (UserInfo userInfo : userInfoList) {
            Assistant assistant = new Assistant();
            assistant.setUserInfo(userInfo);
            assistant.setLocalAuth(userDao.selectLocalAuthById(userInfo.getUserId()));
            assistantList.add(assistant);
        }
        return assistantList;
    }

    @Transactional
    @Override
    public void deleteOneAssistant(Long userId) {
        try {
            // 删除tb_local_auth 信息
            userDao.deleteLocalAuth(userId);
            // 删除tb_user_info 信息
            userDao.deleteUser(userId);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Override
    public void modifyAssistant(Assistant assistant) {
        try {
            userDao.updateLocalAuth(assistant.getLocalAuth());
            userDao.updateUserInfo(assistant.getUserInfo());
        }catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Override
    public List<BoughtRecord> getBoughtRecord(long productCategoryId) {

        return recordDao.queryBoughtRecord(null, productCategoryId);
    }

    @Override
    public List<Product> getProductByProductCategoryId(long productCategoryId) {
        return productDao.selectProduct(null, productCategoryId);
    }
}

package com.json.osp.service.impl;

import com.json.osp.dao.UserDao;
import com.json.osp.dto.ImageHolder;
import com.json.osp.entity.LocalAuth;
import com.json.osp.entity.UserInfo;
import com.json.osp.service.UserInfoService;
import com.json.osp.util.ImageUtil;
import com.json.osp.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Autowired
    private UserDao userDao;

    @Override
    public UserInfo getUserInfo(Long userId) {
        return userDao.queryUserInfo(userId);
    }

    @Override
    @Transactional
    public void addUser(UserInfo newUser, LocalAuth newAuth, ImageHolder thumbnail) throws RuntimeException{
        String createTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        // 生成用户图像的相对路径
        String profileImg = PathUtil.generateUserImgPath(thumbnail);
        // 1.添加用户信息
        try {
            newUser.setProfileImg(profileImg);
//            newUser.setEnableStatus(1); // 默认可以使用
            newUser.setCreateTime(createTime);
            userDao.insertUserInfo(newUser);
        } catch (Exception e) {
            throw new RuntimeException("插入用户信息失败");
        }
        // 2.添加账户信息
        try {
            newAuth.setUserId(newUser.getUserId());
            newAuth.setCreateTime(createTime);
            userDao.insertLocalAuth(newAuth);

        } catch (Exception e) {
            throw new RuntimeException("插入本地账号信息失败");
        }
        // 3.添加用户头像
        // to-do
        try {
            ImageUtil.generateUserImgThumbnail(thumbnail, profileImg);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("用户头像保存失败");
        }
    }

    @Transactional
    @Override
    public void modifyUserInfo(UserInfo userInfo, LocalAuth localAuth, ImageHolder thumbnail) throws RuntimeException {
        String lastEditTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String profileImg = null;
        boolean isUpdateUserImg = false;
        // 判断是否修改用户头像
        if(thumbnail != null)
            isUpdateUserImg = true;

        if(isUpdateUserImg)
            profileImg = PathUtil.generateUserImgPath(thumbnail);
        // 1.添加用户信息
        try {
            userInfo.setLastEditTime(lastEditTime);
            if(isUpdateUserImg)
                userInfo.setProfileImg(profileImg);
            userDao.updateUserInfo(userInfo);
        } catch (Exception e) {
            throw new RuntimeException("修改用户信息失败");
        }
        // 2.添加账户信息
        try {
            localAuth.setLastEditTime(lastEditTime);
            userDao.updateLocalAuth(localAuth);

        } catch (Exception e) {
            throw new RuntimeException("修改本地账号信息失败");
        }
        // 3.添加用户头像
        // to-do
        try {
            if(isUpdateUserImg)
                ImageUtil.generateUserImgThumbnail(thumbnail, profileImg);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("用户头像修改失败");
        }
    }

    @Override
    public LocalAuth checkLocalAuth(LocalAuth localAuth) {
        return userDao.selectLocalAuth(localAuth);
    }

    @Override
    public boolean checkUniqueUsername(String username) {
        Long user_id = userDao.selectLocalAuthUsername(username);
        if(user_id != null)
            return true;
        return false;
    }
}

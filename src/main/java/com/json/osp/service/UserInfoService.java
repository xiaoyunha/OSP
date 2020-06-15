package com.json.osp.service;

import com.json.osp.dto.ImageHolder;
import com.json.osp.entity.LocalAuth;
import com.json.osp.entity.UserInfo;

public interface UserInfoService {
    /**
     * 获取用户信息
     * @param userId
     * @return  UserInfo
     */
    UserInfo getUserInfo(Long userId);

    /**
     * 添加新用户
     * @param userInfo
     * @param localAuth
     */
     void addUser(UserInfo userInfo, LocalAuth localAuth, ImageHolder thumbnail) throws RuntimeException;

    /**
     * 修改用户信息
     * @param userInfo
     * @param localAuth
     * @param thumbnail
     * @throws RuntimeException
     */
     void modifyUserInfo(UserInfo userInfo, LocalAuth localAuth, ImageHolder thumbnail) throws RuntimeException;

    /**
     * 验证账户密码是否正确
     * 正确的话在localAuth中设置userId
     * @param localAuth
     * @return
     */
     LocalAuth checkLocalAuth(LocalAuth localAuth);

    /**
     * 检查用户名是否被注册过
     * 注册过：true 没有：false
     * @param username
     * @return
     */
    boolean checkUniqueUsername(String username);
}

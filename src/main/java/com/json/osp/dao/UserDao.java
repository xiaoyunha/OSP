package com.json.osp.dao;

import com.json.osp.entity.LocalAuth;
import com.json.osp.entity.UserInfo;

import java.util.List;

public interface UserDao {
    UserInfo queryUserInfo(Long userId);

    int insertUserInfo(UserInfo userInfo);

    int insertLocalAuth(LocalAuth localAuth);

    int updateUserInfo(UserInfo userInfo);

    int updateLocalAuth(LocalAuth localAuth);

    LocalAuth selectLocalAuth(LocalAuth localAuth);

    LocalAuth selectLocalAuthById(Long userId);

    Long selectLocalAuthUsername(String username);

    List<UserInfo> selectAssistantInfo();


    void deleteLocalAuth(Long userId);

    void deleteUser(Long userId);
}

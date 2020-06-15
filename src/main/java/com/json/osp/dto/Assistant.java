package com.json.osp.dto;

import com.json.osp.entity.LocalAuth;
import com.json.osp.entity.UserInfo;

public class Assistant {
    private UserInfo userInfo;
    private LocalAuth localAuth;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public LocalAuth getLocalAuth() {
        return localAuth;
    }

    public void setLocalAuth(LocalAuth localAuth) {
        this.localAuth = localAuth;
    }

    @Override
    public String toString() {
        return "Assistant{" +
                "userInfo=" + userInfo +
                ", localAuth=" + localAuth +
                '}';
    }
}

package com.json.osp.entity;

import java.util.Date;

public class BrowseRecord {
    private Long recordId;
    private Product product;
    private UserInfo user;
    private String browseTime;

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public String getBrowseTime() {
        return browseTime;
    }

    public void setBrowseTime(String browseTime) {
        this.browseTime = browseTime;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "BrowseRecord{" +
                "recordId=" + recordId +
                ", product=" + product +
                ", user=" + user +
                ", browseTime=" + browseTime +
                '}';
    }
}

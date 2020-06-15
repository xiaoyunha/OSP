package com.json.osp.entity;

import java.math.BigDecimal;
import java.util.Date;

public class BoughtRecord {
    private Long boughtId;
    private Product product;
    private UserInfo user;
    private String boughtTime;
    private BigDecimal unitPrice; // 单价
    private Integer count; // 购买数量

    public Long getBoughtId() {
        return boughtId;
    }

    public void setBoughtId(Long boughtId) {
        this.boughtId = boughtId;
    }

    public String getBoughtTime() {
        return boughtTime;
    }

    public void setBoughtTime(String boughtTime) {
        this.boughtTime = boughtTime;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
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
        return "BoughtRecord{" +
                "boughtId=" + boughtId +
                ", product=" + product +
                ", user=" + user +
                ", boughtTime=" + boughtTime +
                ", unitPrice=" + unitPrice +
                ", count=" + count +
                '}';
    }
}

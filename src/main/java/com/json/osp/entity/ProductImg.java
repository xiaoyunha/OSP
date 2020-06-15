package com.json.osp.entity;

import java.util.Date;

public class ProductImg {
    // 主键ID
    private Long productImgId;
    // 图片地址
    private String imgAddr;
    // 权重，越大越排前显示
    private Integer priority;
    // 创建时间
    private String createTime;
    // 标明是属于哪个商品的图片
    private Long productId;

    public Long getProductImgId() {
        return productImgId;
    }

    public void setProductImgId(Long productImgId) {
        this.productImgId = productImgId;
    }

    public String getImgAddr() {
        return imgAddr;
    }

    public void setImgAddr(String imgAddr) {
        this.imgAddr = imgAddr;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    @Override
    public String toString() {
        return "ProductImg{" +
                "productImgId=" + productImgId +
                ", imgAddr='" + imgAddr + '\'' +
                ", priority=" + priority +
                ", createTime=" + createTime +
                ", productId=" + productId +
                '}';
    }
}

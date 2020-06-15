package com.json.osp.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class Product {
    // 主键ID
    private Long productId;
    // 商品名
    private String productName;
    // 商品简介
    private String productDesc;
    // 商品详细介绍
    private String productIntroduce;
    // 价格
    private BigDecimal price;
    // 数量
    private Integer count;
    // 创建时间
    private String createTime;
    // 最近一次的更新时间
    private String lastEditTime;
    // 0.下架 1.上架
    private Integer enableStatus;
    // 图片详情图列表，跟商品是多对一的关系
    private List<ProductImg> productImgList;
    // 商品类别，一件商品仅属于一个商品类别
    private ProductCategory productCategory;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getLastEditTime() {
        return lastEditTime;
    }

    public void setLastEditTime(String lastEditTime) {
        this.lastEditTime = lastEditTime;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getProductIntroduce() {
        return productIntroduce;
    }

    public void setProductIntroduce(String productIntroduce) {
        this.productIntroduce = productIntroduce;
    }


    public Integer getEnableStatus() {
        return enableStatus;
    }

    public void setEnableStatus(Integer enableStatus) {
        this.enableStatus = enableStatus;
    }

    public List<ProductImg> getProductImgList() {
        return productImgList;
    }

    public void setProductImgList(List<ProductImg> productImgList) {
        this.productImgList = productImgList;
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", productDesc='" + productDesc + '\'' +
                ", productIntroduce='" + productIntroduce + '\'' +
                ", price=" + price +
                ", count=" + count +
                ", createTime=" + createTime +
                ", lastEditTime=" + lastEditTime +
                ", enableStatus=" + enableStatus +
                ", productImgList=" + productImgList +
                ", productCategory=" + productCategory +
                '}';
    }
}

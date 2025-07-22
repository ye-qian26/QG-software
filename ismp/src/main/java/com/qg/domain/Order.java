package com.qg.domain;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.sql.Date;

@TableName("`order`")
public class Order {
    private Long id;
    private Long softwareId;
    private Double price;
    private Date time;
    private Long userId;
    private Long developerId;
    @TableLogic
    private int isDeleted;

    public Order() {
    }

    public Order(Long id, Long softwareId, Double price, Date time, Long userId, Long developerId, int isDeleted) {
        this.id = id;
        this.softwareId = softwareId;
        this.price = price;
        this.time = time;
        this.userId = userId;
        this.developerId = developerId;
        this.isDeleted = isDeleted;
    }

    /**
     * 获取
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取
     * @return softwareId
     */
    public Long getSoftwareId() {
        return softwareId;
    }

    /**
     * 设置
     * @param softwareId
     */
    public void setSoftwareId(Long softwareId) {
        this.softwareId = softwareId;
    }

    /**
     * 获取
     * @return price
     */
    public Double getPrice() {
        return price;
    }

    /**
     * 设置
     * @param price
     */
    public void setPrice(Double price) {
        this.price = price;
    }

    /**
     * 获取
     * @return time
     */
    public Date getTime() {
        return time;
    }

    /**
     * 设置
     * @param time
     */
    public void setTime(Date time) {
        this.time = time;
    }

    /**
     * 获取
     * @return userId
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 设置
     * @param userId
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * 获取
     * @return developerId
     */
    public Long getDeveloperId() {
        return developerId;
    }

    /**
     * 设置
     * @param developerId
     */
    public void setDeveloperId(Long developerId) {
        this.developerId = developerId;
    }

    /**
     * 获取
     * @return isDeleted
     */
    public int getIsDeleted() {
        return isDeleted;
    }

    /**
     * 设置
     * @param isDeleted
     */
    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String toString() {
        return "Order{id = " + id + ", softwareId = " + softwareId + ", price = " + price + ", time = " + time + ", userId = " + userId + ", developerId = " + developerId + ", isDeleted = " + isDeleted + "}";
    }
}

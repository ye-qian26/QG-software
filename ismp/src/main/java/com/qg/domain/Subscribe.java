package com.qg.domain;


import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Subscribe {


    private Long id;
    private Long userId;
    private Long developerId;

    @TableLogic
    private Integer isDeleted;


    public Subscribe() {
    }

    public Subscribe(Long id, Long userId, Long developerId) {
        this.id = id;
        this.userId = userId;
        this.developerId = developerId;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
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

    public String toString() {
        return "Subscribe{id = " + id + ", userId = " + userId + ", developerId = " + developerId + "}";
    }
}

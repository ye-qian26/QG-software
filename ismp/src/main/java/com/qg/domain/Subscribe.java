package com.qg.domain;


import com.fasterxml.jackson.annotation.JsonProperty;

public class Subscribe {

    private Long id;
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("developer_id")
    private Long developerId;

    public Subscribe() {
    }

    public Subscribe(long id, long userId, long developerId) {
        this.id = id;
        this.userId = userId;
        this.developerId = developerId;
    }

    /**
     * 获取
     * @return id
     */
    public long getId() {
        return id;
    }

    /**
     * 设置
     * @param id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * 获取
     * @return userId
     */
    public long getUserId() {
        return userId;
    }

    /**
     * 设置
     * @param userId
     */
    public void setUserId(long userId) {
        this.userId = userId;
    }

    /**
     * 获取
     * @return developerId
     */
    public long getDeveloperId() {
        return developerId;
    }

    /**
     * 设置
     * @param developerId
     */
    public void setDeveloperId(long developerId) {
        this.developerId = developerId;
    }

    public String toString() {
        return "Subscribe{id = " + id + ", userId = " + userId + ", developerId = " + developerId + "}";
    }

}

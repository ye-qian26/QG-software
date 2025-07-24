package com.qg.vo;

import com.baomidou.mybatisplus.annotation.TableLogic;

public class AdminManageUserVO {

    private Long id;    // 用户id
    private Long banId;
    private String name;
    private String avatar;
    private Integer role;
    private Integer status;
    @TableLogic
    private Integer isDeleted;
    private String startTime;
    private String endTime;


    public AdminManageUserVO() {
    }

    public AdminManageUserVO(Long id, Long banId, String name, String avatar, Integer role, Integer status, Integer isDeleted, String startTime, String endTime) {
        this.id = id;
        this.banId = banId;
        this.name = name;
        this.avatar = avatar;
        this.role = role;
        this.status = status;
        this.isDeleted = isDeleted;
        this.startTime = startTime;
        this.endTime = endTime;
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
     * @return banId
     */
    public Long getBanId() {
        return banId;
    }

    /**
     * 设置
     * @param banId
     */
    public void setBanId(Long banId) {
        this.banId = banId;
    }

    /**
     * 获取
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * 设置
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取
     * @return avatar
     */
    public String getAvatar() {
        return avatar;
    }

    /**
     * 设置
     * @param avatar
     */
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    /**
     * 获取
     * @return role
     */
    public Integer getRole() {
        return role;
    }

    /**
     * 设置
     * @param role
     */
    public void setRole(Integer role) {
        this.role = role;
    }

    /**
     * 获取
     * @return status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置
     * @param status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取
     * @return isDeleted
     */
    public Integer getIsDeleted() {
        return isDeleted;
    }

    /**
     * 设置
     * @param isDeleted
     */
    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    /**
     * 获取
     * @return startTime
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * 设置
     * @param startTime
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * 获取
     * @return endTime
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * 设置
     * @param endTime
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String toString() {
        return "AdminManageUserVO{id = " + id + ", banId = " + banId + ", name = " + name + ", avatar = " + avatar + ", role = " + role + ", status = " + status + ", isDeleted = " + isDeleted + ", startTime = " + startTime + ", endTime = " + endTime + "}";
    }
}

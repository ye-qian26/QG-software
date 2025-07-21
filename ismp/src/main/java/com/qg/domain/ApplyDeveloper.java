package com.qg.domain;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;



@TableName("apply_developer")
public class ApplyDeveloper {
    private Long id;
    private Long userId;
    private String applyTime;
    private String reason;
    private String material;
    private Integer status;
    @TableLogic
    private int isDeleted;

    public ApplyDeveloper() {
    }

    public ApplyDeveloper(Long id, Long userId, String applyTime, String reason, String material, Integer status, int isDeleted) {
        this.id = id;
        this.userId = userId;
        this.applyTime = applyTime;
        this.reason = reason;
        this.material = material;
        this.status = status;
        this.isDeleted = isDeleted;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
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
     * @return applyTime
     */
    public String getApplyTime() {
        return applyTime;
    }

    /**
     * 设置
     * @param applyTime
     */
    public void setApplyTime(String applyTime) {
        this.applyTime = applyTime;
    }

    /**
     * 获取
     * @return reason
     */
    public String getReason() {
        return reason;
    }

    /**
     * 设置
     * @param reason
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * 获取
     * @return material
     */
    public String getMaterial() {
        return material;
    }

    /**
     * 设置
     * @param material
     */
    public void setMaterial(String material) {
        this.material = material;
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

    public String toString() {
        return "ApplyDeveloper{id = " + id + ", userId = " + userId + ", applyTime = " + applyTime + ", reason = " + reason + ", material = " + material + ", status = " + status + "}";
    }
}

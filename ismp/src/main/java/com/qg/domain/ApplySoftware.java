package com.qg.domain;


import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("apply_software")
public class ApplySoftware {
    private Long id;
    private Long userId;
    private String reason;
    private String material;
    private Integer status;
    private Long softwareId;
    private String applyTime;
    private String applyReason;

    @TableLogic
    private Integer isDeleted;


    public ApplySoftware() {
    }

    public ApplySoftware(Long id, Long userId, String reason, String material, Integer status, Long softwareId, String applyTime, String applyReason, Integer isDeleted) {
        this.id = id;
        this.userId = userId;
        this.reason = reason;
        this.material = material;
        this.status = status;
        this.softwareId = softwareId;
        this.applyTime = applyTime;
        this.applyReason = applyReason;
        this.isDeleted = isDeleted;
    }


    public void setUserId(Long userId) {
        this.userId = userId;
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
    public void setUserId(long userId) {
        this.userId = userId;
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
     * @return applyReason
     */
    public String getApplyReason() {
        return applyReason;
    }

    /**
     * 设置
     * @param applyReason
     */
    public void setApplyReason(String applyReason) {
        this.applyReason = applyReason;
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

    public String toString() {
        return "ApplySoftware{id = " + id + ", userId = " + userId + ", reason = " + reason + ", material = " + material + ", status = " + status + ", softwareId = " + softwareId + ", applyTime = " + applyTime + ", applyReason = " + applyReason + ", isDeleted = " + isDeleted + "}";
    }
}

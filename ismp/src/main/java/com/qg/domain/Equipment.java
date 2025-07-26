package com.qg.domain;


import com.baomidou.mybatisplus.annotation.TableLogic;

public class Equipment {
    private Long id;
    private Long userId;
    private Long softwareId;
    private Integer status;
    private String code1;
    private String code2;
    private String code3;
    private String name;
    @TableLogic
    private int isDeleted;

    public Equipment() {
    }
    public Equipment(Long userId, Long softwareId, Integer status){
        this.userId = userId;
        this.softwareId = softwareId;
        this.status = status;
    }

    public Equipment(Long userId, Long softwareId, Integer status,String name) {
        this.userId = userId;
        this.softwareId = softwareId;
        this.status = status;
        this.name = name;
    }


    public Equipment(Long id, Long userId, Long softwareId, Integer status, String code1, String code2, String code3,String name) {
        this.id = id;
        this.userId = userId;
        this.softwareId = softwareId;
        this.status = status;
        this.code1 = code1;
        this.code2 = code2;
        this.code3 = code3;
        this.name = name;
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
     * @return code1
     */
    public String getCode1() {
        return code1;
    }

    /**
     * 设置
     * @param code1
     */
    public void setCode1(String code1) {
        this.code1 = code1;
    }

    /**
     * 获取
     * @return code2
     */
    public String getCode2() {
        return code2;
    }

    /**
     * 设置
     * @param code2
     */
    public void setCode2(String code2) {
        this.code2 = code2;
    }

    /**
     * 获取
     * @return code3
     */
    public String getCode3() {
        return code3;
    }

    /**
     * 设置
     * @param code3
     */
    public void setCode3(String code3) {
        this.code3 = code3;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return "Equipment{id = " + id + ", userId = " + userId + ", softwareId = " + softwareId + ", status = " + status + ", code1 = " + code1 + ", code2 = " + code2 + ", code3 = " + code3 + "name="+ name +"}";
    }
}

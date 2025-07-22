package com.qg.dto;

public class UserDto {
    private int userId;
    private String name;

    public UserDto() {
    }

    public UserDto(int userId, String name) {
        this.userId = userId;
        this.name = name;
    }

    /**
     * 获取
     * @return userId
     */
    public int getUserId() {
        return userId;
    }

    /**
     * 设置
     * @param userId
     */
    public void setUserId(int userId) {
        this.userId = userId;
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

    public String toString() {
        return "UserDto{userId = " + userId + ", name = " + name + "}";
    }
}

package com.qg.dto;

public class UserDto {
    private int id;
    private String name;
    private Integer role;
    private String avatar;


    public UserDto() {
    }

    public UserDto(int id, String name, Integer role, String avatar) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.avatar = avatar;
    }

    /**
     * 获取
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * 设置
     * @param id
     */
    public void setId(int id) {
        this.id = id;
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

    public String toString() {
        return "UserDto{id = " + id + ", name = " + name + ", role = " + role + ", avatar = " + avatar + "}";
    }
}

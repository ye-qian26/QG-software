package com.qg.dto;

public class UserDto {
    private int id;
    private String name;
    private Integer role;

    public UserDto() {
    }

    public UserDto(int id, String name, Integer role) {
        this.id = id;
        this.name = name;
        this.role = role;
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

    public String toString() {
        return "UserDto{id = " + id + ", name = " + name + ", role = " + role + "}";
    }
}

package com.qg.dto;

public class UserDto {
    private int id;
    private String name;

    public UserDto() {
    }

    public UserDto(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * 获取
     * @return userId
     */
    public int getId() {
        return id;
    }


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

    public String toString() {
        return "UserDto{userId = " + id + ", name = " + name + "}";
    }
}

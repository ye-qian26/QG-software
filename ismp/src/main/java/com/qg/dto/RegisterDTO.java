package com.qg.dto;

import com.qg.domain.User;

public class RegisterDTO {
    private User user;
    private String code;


    public RegisterDTO() {
    }

    public RegisterDTO(User user, String code) {
        this.user = user;
        this.code = code;
    }

    /**
     * 获取
     * @return user
     */
    public User getUser() {
        return user;
    }

    /**
     * 设置
     * @param user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * 获取
     * @return code
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置
     * @param code
     */
    public void setCode(String code) {
        this.code = code;
    }

    public String toString() {
        return "RegisterDTO{user = " + user + ", code = " + code + "}";
    }
}

package com.qg.vo;

public class FanVO {

    private Long id;
    private String name;
    private String avatar;
    private String role;


    public FanVO() {
    }


    public FanVO(Long id, String name, String avatar, Integer role) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        setRole(role);
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
    public String getRole() {
        return role;
    }

    /**
     * 设置
     * @param role
     */
    public void setRole(Integer role) {
        if (role == 1) {
            this.role = "管理员";
        } else if (role == 2) {
            this.role = "开发商";
        } else if (role == 3) {
            this.role = "用户";
        }
    }

    public String toString() {
        return "FanVO{id = " + id + ", name = " + name + ", avatar = " + avatar + ", role = " + role + "}";
    }
}

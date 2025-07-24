package com.qg.vo;

/**
 * 查看我的关注
 */
public class SubscribeVO {

    private Long id;
    private String name;
    private String avatar;
    private Long followersCount;
    private Long softwareCount;


    public SubscribeVO() {
    }

    public SubscribeVO(Long id, String name, String avatar, Long followersCount, Long softwareCount) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.followersCount = followersCount;
        this.softwareCount = softwareCount;
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
     * @return followersCount
     */
    public Long getFollowersCount() {
        return followersCount;
    }

    /**
     * 设置
     * @param followersCount
     */
    public void setFollowersCount(Long followersCount) {
        this.followersCount = followersCount;
    }

    /**
     * 获取
     * @return softwareCount
     */
    public Long getSoftwareCount() {
        return softwareCount;
    }

    /**
     * 设置
     * @param softwareCount
     */
    public void setSoftwareCount(Long softwareCount) {
        this.softwareCount = softwareCount;
    }

    public String toString() {
        return "SubscribeVO{id = " + id + ", name = " + name + ", avatar = " + avatar + ", followersCount = " + followersCount + ", softwareCount = " + softwareCount + "}";
    }
}

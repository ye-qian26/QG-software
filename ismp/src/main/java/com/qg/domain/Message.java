package com.qg.domain;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.annotation.TableLogic;



public class Message {
    private Long id;
    private Long receiverId;
    private Long posterId;
    private String content;
    private String time;
    private Integer isRead;


    public Message() {
    }

    public Message(Long id, Long receiverId, Long posterId, String content, String time, Integer isRead) {
        this.id = id;
        this.receiverId = receiverId;
        this.posterId = posterId;
        this.content = content;
        this.time = time;
        this.isRead = isRead;
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
     * @return receiverId
     */
    public Long getReceiverId() {
        return receiverId;
    }

    /**
     * 设置
     * @param receiverId
     */
    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    /**
     * 获取
     * @return posterId
     */
    public Long getPosterId() {
        return posterId;
    }

    /**
     * 设置
     * @param posterId
     */
    public void setPosterId(Long posterId) {
        this.posterId = posterId;
    }

    /**
     * 获取
     * @return content
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置
     * @param content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 获取
     * @return time
     */
    public String getTime() {
        return time;
    }

    /**
     * 设置
     * @param time
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * 获取
     * @return isRead
     */
    public Integer getIsRead() {
        return isRead;
    }

    /**
     * 设置
     * @param isRead
     */
    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }

    public String toString() {
        return "Message{id = " + id + ", receiverId = " + receiverId + ", posterId = " + posterId + ", content = " + content + ", time = " + time + ", isRead = " + isRead + "}";
    }
}

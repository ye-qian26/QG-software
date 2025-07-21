package com.qg.domain;

/**
 * 常量类
 */
public class Constants {
    /**
     * 用户标识
     */
    public static final Integer USER_ROLE_ADMIN = 1;
    public static final Integer USER_ROLE_DEVELOPER = 2;
    public static final Integer USER_ROLE_USER = 3;

    /**
     * 设备标识
     * 0：已预约
     * 1：已购买
     */
    public static final Integer EQUIPMENT_STATUS_ORDER = 0;
    public static final Integer EQUIPMENT_STATUS_BOUGHT = 1;

    /**
     * 软件标识
     * 0：未发行，待审核
     * 1：已发布，可购买
     * 2：已过审，未发行，可预约
     */
    public static final Integer SOFTWARE_STATUS_UNREVIEWED = 0;
    public static final Integer SOFTWARE_STATUS_SALE = 1;
    public static final Integer SOFTWARE_STATUS_ORDER = 2;

    /**
     * 消息标识
     * 0：未读
     * 1：已读
     */
    public static final Integer MESSAGE_NO_READ = 0;
    public static final Integer MESSAGE_READ = 1;

    /**
     * 逻辑删除
     * 0：未删除
     * 1：已删除
     */
    public static final Integer SOFTWARE_DELETE_NO = 0;
    public static final Integer REVIEW_DELETE_NO = 0;
    public static final Integer ORDER_DELETE_NO = 0;
    public static final Integer MESSAGE_DELETE_NO = 0;
    public static final Integer BAN_DELETE_NO = 0;
    public static final Integer SOFTWARE_DELETE_YES = 1;
    public static final Integer REVIEW_DELETE_YES = 1;
    public static final Integer ORDER_DELETE_YES = 1;
    public static final Integer MESSAGE_DELETE_YES = 1;
    public static final Integer BAN_DELETE_YES = 1;

}

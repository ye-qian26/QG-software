package com.qg.utils;

/**
 * 常量类
 */
public class Constants {
    /**
     * 用户标识
     */
    public static final String USER_ROLE_ADMIN = "1";
    public static final String USER_ROLE_DEVELOPER = "2";
    public static final String USER_ROLE_USER = "3";

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
     * 1：已过审，未发行，可预约
     * 2：已发布，可购买
     */
    public static final Integer SOFTWARE_STATUS_UNREVIEWED = 0;
    public static final Integer SOFTWARE_STATUS_ORDER = 1;
    public static final Integer SOFTWARE_STATUS_SALE = 2;

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
    public static final Integer IS_NOT_DELETED = 0;
    public static final Integer IS_DELETED = 1;

    /**
     * 是否处理
     * 0：未处理
     * 1：已处理
     */
    public static final Integer IS_NOT_HANDLED = 0;
    public static final Integer IS_HANDLED = 1;

    /**
     * token验证
     * tokenkey的头
     * token有效期
      */
    public static final String LOGIN_USER_KEY = "login:user:";
    public static final long LOGIN_USER_TTL = 30;
}

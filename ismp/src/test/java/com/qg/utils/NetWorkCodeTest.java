package com.qg.utils;

import org.junit.jupiter.api.Test;

/**
 * @Description: 测试mac码  // 类说明
 * @ClassName: NetWorkCodeTest    // 类名
 * @Author: weisn          // 创建者
 * @Date: 2025/7/26 16:14   // 时间
 * @Version: 1.0     // 版本
 */
public class NetWorkCodeTest {

    @Test
    public void testGetNetWorkCode() {
        try {
            System.out.println(NetWorkCode.getNetWorkCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

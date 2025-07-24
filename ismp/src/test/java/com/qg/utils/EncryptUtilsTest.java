package com.qg.utils;


import org.junit.jupiter.api.Test;
import org.springframework.data.util.Pair;

import static com.qg.utils.EncryptUtils.*;

/**
 * @Description: 测试传输加密功能  // 类说明，在创建类时要填写
 * @ClassName: EncryptUtils    // 类名，会自动填充
 * @Author: weisn          // 创建者
 * @Date: 2025/7/23 20:16   // 时间
 * @Version: 1.0     // 版本
 */
public class EncryptUtilsTest {

    @Test
    public void test() throws Exception {
        System.out.println("================准备各类密钥===================");
        String aesKey = EncryptUtils.createAesKey(192);
        System.out.println("AES key: " + aesKey);
        Pair<String, String> rsaKeyPair = createPemRsaKeyPair(1024);
        String rsaPublicKey = rsaKeyPair.getFirst();
        String rsaPrivateKey = rsaKeyPair.getSecond();


        System.out.println("RSA public key: \n" + rsaPublicKey);
        System.out.println();
        System.out.println("RSA private key: \n" + rsaPrivateKey);

        System.out.println("\n================准备待传输（加密）数据===================");
        String data = "hello word! hello lrd!";
        System.out.println("待加密数据： " + data);

        System.out.println("\n================使用AES key对数据进行加密===================");
        String encryptedText = aesEncrypt(data, aesKey);
        System.out.println("AES 加密后的数据: " + encryptedText);

        System.out.println("\n================使用RSA公钥对AES key进行加密===================");
        String encryptAesKey = rsaEncrypt(aesKey, rsaPublicKey);
        System.out.println("加密后的AES key: " + encryptAesKey);

        System.out.println("\n================使用RSA私钥对AES key进行解密===================");
        String decryptAesKey = rsaDecrypt(encryptAesKey, rsaPrivateKey);
        System.out.println("解密后的AES key: " + decryptAesKey);

        System.out.println("\n================使用解密后的AES key进行数据解密===================");
        String decryptedText = aesDecrypt(encryptedText, decryptAesKey);
        System.out.println("解密后的数据: " + decryptedText);

    }
}

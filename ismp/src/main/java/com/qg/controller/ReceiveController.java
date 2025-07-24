package com.qg.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qg.service.ReceiveService;
import com.qg.utils.EncryptUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/receives")
public class ReceiveController {

    @Autowired
    private ReceiveService receiveService;


    /*private static final String PRIVATE_KEY = "-----BEGIN PRIVATE KEY-----\n...你的私钥...\n-----END PRIVATE KEY-----";
    private static final String PUBLIC_KEY  = "-----BEGIN PUBLIC KEY-----\n...你的公钥...\n-----END PUBLIC KEY-----";*/


    @PostMapping("/mac")
    public String receive(@RequestBody String jsonData) {
        /*try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(jsonData);

            String encData = root.get("data").asText();
            String encKey  = root.get("key").asText();
            String encIv   = root.get("iv").asText();

            // 1. 用私钥解密AES密钥和IV
            String aesKey = EncryptUtils.rsaDecrypt(encKey, PRIVATE_KEY);
            String aesIv  = EncryptUtils.rsaDecrypt(encIv, PRIVATE_KEY);

            // 2. AES解密data
            String plainText = EncryptUtils.aesDecrypt(encData, aesKey, aesIv);
            System.out.println("收到解密后的明文: " + plainText);

            // 3. 业务处理
            String result = receiveService.Permissions(plainText); // 这里返回字符串，如 "true"/"false"

            // 4. 加密返回
            // 随机生成AES密钥和IV
            String respAesKey = EncryptUtils.createAesKey(16);
            String respAesIv  = EncryptUtils.createAesKey(16);
            String respCipher = EncryptUtils.aesEncrypt(result, respAesKey, respAesIv);

            String respEncKey = EncryptUtils.rsaEncrypt(respAesKey, PUBLIC_KEY);
            String respEncIv  = EncryptUtils.rsaEncrypt(respAesIv, PUBLIC_KEY);

            // 构造JSON返回
            ObjectMapper respMapper = new ObjectMapper();
            JsonNode respRoot = respMapper.createObjectNode()
                    .put("data", respCipher)
                    .put("key", respEncKey)
                    .put("iv", respEncIv);

            return respRoot.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\":\"解密失败\"}";
        }*/

        return receiveService.Permissions(jsonData);

    }

}

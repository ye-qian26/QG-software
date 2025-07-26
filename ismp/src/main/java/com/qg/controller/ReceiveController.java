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

    private static final String PRIVATE_KEY = "-----BEGIN PRIVATE KEY-----\n" +
            "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDP2zDU5vsU5ABO\n" +
            "4QSYPCi2b2C+aex+vUMoKiRB6I3mLRKmJfA8GlXgaafNSinKPMUsjTqd7ibnlUvF\n" +
            "FPutG8e1DszS3s9w61Y6Pz81j8VNDZGpo5w1JYU9z6boGCEM1tBSntmQg+4hRkWT\n" +
            "K3H+f/gICsxDYdgKrCoiDgsWEPfdcDm8mfdUo5x6KuYWY74Rlt3wCj4+/n+fsqr8\n" +
            "hT/eSA8qrIVWJmW329WeeECOjOZmBWUIeDtcztpyKw+931YH8WqKEQFQug/PmvxR\n" +
            "o6hAX0tWmqGwl1qKsZgbHvlJw4tpICx1xnnazXhsWENro0RKNTnuw17D8/z62BpD\n" +
            "TUlUnMBjAgMBAAECggEAIV/lkFniOPF/Jys0TKlkNB7gHtLtOVB0Zw+VMMwBdNMJ\n" +
            "6omu8EeeQGFWozzkRufJFSU8Iz2pG4g6WVPMMXiDcYwF0VobXts6xjF/mb8TBBNI\n" +
            "rz9pBpOWuF0ORLgTHnejjZCaCH1WkBszcrUIIcA0eD4bnDoqsVQBk+RDXXCuC+z+\n" +
            "ClZBslzwI8P+tf/LHL0EuSy0TAQLKD8aNg8ls5c9jRX6RNDVGz8tSw84PPWVrjHh\n" +
            "+wgQqUmHXy7r5oveZJWBDdUI4FReUkq3VFWcI93wO0L9lt/uB7bAr7X4xAJecpvl\n" +
            "XynDQgAmpDD89g+W8LvdwGtUS3OfkP2JXvWRnn734QKBgQDuQbluev/KDeiRaLwy\n" +
            "Pye/RU80fdm6N44L+tCsX2GqXl9e0JZAVfBeHdj1h5dQpsiw1uBruHXEjxtWHDod\n" +
            "0ZG9Kodw5ZFr2K8BuyCpwL4rgcrC8LuLUfFqQQ3EVBaUXtxVvQUYQd9CQpAoeslc\n" +
            "Jxxky2Ki/MsyURloOS7wJNIJcwKBgQDfVeRN9k6KkPEjRNzrGbw6LUMwRZ+x0ORD\n" +
            "aLlsVaREiBka37HRQWlAhCMkSw6HkcFJDokmJIBiCh/gbDQAB2ANC7O7J0dTfzRW\n" +
            "qrq92S133kUiET8RkmdGrdkMCn8xjHVSWnuAf9B96+aucDFwDEIOX85Jtfxq5Pry\n" +
            "QKNoGRRxUQKBgQCkxBsChlkiWF//Pxj2/qn0InBV8rqBhy3apwdsTUHzWfRGU6W0\n" +
            "tDnYPHfvxGcmRVEAynjExfnbhsP21upx+J363dBwiLH6jQzp+4jpG/vnhoi8K1Ln\n" +
            "VDHJs1db8HL1BG0HdgJx9gv5fKLI+7EI45tFMUnzQTMchu01IGaPeEcwdQKBgBMD\n" +
            "8ShiLFXH+YATQOwTiN87M1lM1EgDjSvgIsmCkTX0kI6lNTKn6xo/Je7VJSuN0XMy\n" +
            "9aJ1wSiXzBcDn3nRJvB1apwov/3v+rjWWIgJkcF+/0vjRAdu+7Qc4ToROrsxRlv4\n" +
            "sENEnq/aAg4gJ1Vu9cDbj2jfQdqoHGW78y3N1NvBAoGAD526rMBxuXKZml2ieyQx\n" +
            "Nrmu76gr5zkNstgxg6PzkVRO/wYQ4webwKGUzirE7HopM9xCzXhAo0Gxhg1+Cmts\n" +
            "Z9wEcpS/GBPUDuJL+olwj2jzEy/HyyT5De6UJvglxXOnzLI5YYY3iAct2P6qmzXu\n" +
            "0ipw7TXxoPAxfJw4gl1Z2QU=\n" +
            "-----END PRIVATE KEY-----\n";

    @PostMapping("/mac")
    public String receive(@RequestBody String jsonData) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(jsonData);

            String encData = root.get("data").asText();
            String encKey  = root.get("key").asText();
            String encIv   = root.get("iv").asText();

            // 1. 用私钥解密AES密钥和IV (全部byte[])
            byte[] aesKey = EncryptUtils.rsaDecryptToBytes(encKey, PRIVATE_KEY);
            byte[] aesIv  = EncryptUtils.rsaDecryptToBytes(encIv, PRIVATE_KEY);

            // 2. AES解密data
            String plainText = EncryptUtils.aesDecrypt(encData, aesKey, aesIv);
            System.out.println("收到解密后的明文: " + plainText);

            String[] parts = plainText.split("\\|");
            if (parts.length != 3) {
                return "false";
            }
            String email = parts[0];
            String mac = parts[1];
            String name = parts[2];

            String serviceData = "mac=" + mac + ";email=" + email + ";name=" + name;


            String message = receiveService.Permissions(serviceData);

            System.out.println(email + "\n" + mac + "\n" + name );

            return message;
        } catch (Exception e) {
            e.printStackTrace();
            return "false";
        }
    }
}
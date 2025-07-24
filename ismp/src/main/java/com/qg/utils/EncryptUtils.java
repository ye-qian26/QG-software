package com.qg.utils;


import lombok.SneakyThrows;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.data.util.Pair;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import static org.bouncycastle.jce.provider.BouncyCastleProvider.PROVIDER_NAME;

public class EncryptUtils {


    //注册加密库
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    private static final String AES_ALGORITHM = "AES";
    private static final String RSA_ALGORITHM = "RSA";
    private static final String AES_ALGORITHM_MODEL = "AES/ECB/PKCS5Padding";

    /**
     * @Author lrt
     * @Description //TODO 生成Aes密钥
     * @Date 20:46 2025/7/23
     * @Param
     * @param keySize
     * @return java.lang.String
     **/
    @SneakyThrows
    public static String createAesKey(int keySize) throws Exception {
        KeyGenerator keygen = KeyGenerator.getInstance(AES_ALGORITHM);
        keygen.init(keySize);
        SecretKey secretKey = keygen.generateKey();
        byte[] keyBytes = secretKey.getEncoded();
        return Base64.getEncoder().encodeToString(keyBytes);
    }

    /**
     * @Author lrt
     * @Description //TODO Aes加密
     * @Date 20:46 2025/7/23
     * @Param
     * @param content
     * @param key
     * @return java.lang.String
     **/
    @SneakyThrows
    public static String aesEncrypt(String content, String key) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM_MODEL, PROVIDER_NAME);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), AES_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

        byte[] result = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(result);
    }


    /**
     * @Author lrt
     * @Description //TODO Aes解密
     * @Date 20:46 2025/7/23
     * @Param
     * @param content
     * @param key
     * @return java.lang.String
     **/
    @SneakyThrows
    public static String aesDecrypt(String content, String key) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM_MODEL, PROVIDER_NAME);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), AES_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

        byte[] result = Base64.getDecoder().decode(content.getBytes(StandardCharsets.UTF_8));
        byte[] decrypted = cipher.doFinal(result);
        return new String(decrypted, StandardCharsets.UTF_8);
    }


    /**
     * @Author lrt
     * @Description //TODO 生成rsa密钥
     * @Date 20:45 2025/7/23
     * @Param
     * @param keySize
     * @return org.springframework.data.util.Pair<java.lang.String,java.lang.String>
     **/
    @SneakyThrows
    public static Pair<String, String> createPemRsaKeyPair(int keySize) throws Exception {
        String pemPublicKeyFormat = "-----BEGIN PUBLIC KEY-----\n%s\n-----END PUBLIC KEY-----";
        String pemPrivateKeyFormat = "-----BEGIN PRIVATE KEY-----\n%s\n-----END PRIVATE KEY-----";

        KeyPairGenerator generator = KeyPairGenerator.getInstance(RSA_ALGORITHM, PROVIDER_NAME);
        generator.initialize(keySize);
        KeyPair kp = generator.generateKeyPair();
        String publicKey = Base64.getEncoder().encodeToString(kp.getPublic().getEncoded()).replaceAll("(.{64})", "$1\n");
        String privateKey = Base64.getEncoder().encodeToString(kp.getPrivate().getEncoded()).replaceAll("(.{64})", "$1\n");

        return Pair.of(
                String.format(pemPublicKeyFormat, publicKey),
                String.format(pemPrivateKeyFormat, privateKey));
    }


    /**
     * @Author lrt
     * @Description //TODO rsa加密
     * @Date 20:44 2025/7/23
     * @Param
     * @param content
     * @param pemPublicKey
     * @return java.lang.String
     **/
    @SneakyThrows
    public static String rsaEncrypt(String content, String pemPublicKey) throws Exception {
        byte[] publicKeyBytes = parsePemKey(pemPublicKey);
        KeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM, PROVIDER_NAME);
        PublicKey key = keyFactory.generatePublic(keySpec);

        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] encryptedBytes = cipher.doFinal(content.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }


    /**
     * @Author lrt
     * @Description //TODO 使用私钥解密
     * @Date 20:48 2025/7/23
     * @Param
 * @param encryptedContent
 * @param pemPrivateKey
     * @return java.lang.String
     **/

    @SneakyThrows
    public static String rsaDecrypt(String encryptedContent, String pemPrivateKey) throws Exception {
        byte[] privateKeyBytes = parsePemKey(pemPrivateKey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM, PROVIDER_NAME);
        PrivateKey key = keyFactory.generatePrivate(keySpec);

        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);

        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedContent);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }


    @SneakyThrows
    private static byte[] parsePemKey(String pemKey) throws IOException {
        PemReader pemReader =new PemReader(new StringReader(pemKey));
        PemObject pemObject = pemReader.readPemObject();
        pemReader.close();
        return pemObject.getContent();
    }

    @SneakyThrows
    public static String aesEncrypt(String content, String key, String iv) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", PROVIDER_NAME);
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), AES_ALGORITHM);
        IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        byte[] result = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(result);
    }

    @SneakyThrows
    public static String aesDecrypt(String content, String key, String iv) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", PROVIDER_NAME);
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), AES_ALGORITHM);
        IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        byte[] result = Base64.getDecoder().decode(content);
        byte[] decrypted = cipher.doFinal(result);
        return new String(decrypted, StandardCharsets.UTF_8);
    }



}

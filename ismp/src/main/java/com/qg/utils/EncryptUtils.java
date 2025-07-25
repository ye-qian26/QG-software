package com.qg.utils;

import lombok.SneakyThrows;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.StringReader;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import static org.bouncycastle.jce.provider.BouncyCastleProvider.PROVIDER_NAME;

public class EncryptUtils {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    private static final String AES_ALGORITHM = "AES";
    private static final String RSA_ALGORITHM = "RSA";

    @SneakyThrows
    public static byte[] rsaDecryptToBytes(String encryptedContent, String pemPrivateKey) throws Exception {
        byte[] privateKeyBytes = parsePemKey(pemPrivateKey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM, PROVIDER_NAME);
        PrivateKey key = keyFactory.generatePrivate(keySpec);

        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding", PROVIDER_NAME);
        cipher.init(Cipher.DECRYPT_MODE, key);

        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedContent);
        return cipher.doFinal(encryptedBytes); // 返回原始byte[]
    }

    @SneakyThrows
    private static byte[] parsePemKey(String pemKey) throws IOException {
        PemReader pemReader = new PemReader(new StringReader(pemKey));
        PemObject pemObject = pemReader.readPemObject();
        pemReader.close();
        return pemObject.getContent();
    }

    @SneakyThrows
    public static String aesDecrypt(String content, byte[] key, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", PROVIDER_NAME);
        SecretKeySpec keySpec = new SecretKeySpec(key, AES_ALGORITHM);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        byte[] result = Base64.getDecoder().decode(content);
        byte[] decrypted = cipher.doFinal(result);
        return new String(decrypted, java.nio.charset.StandardCharsets.UTF_8);
    }
}
package com.aeye.mifss.common.utils;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Security;

public class AesUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(AesUtils.class);
    /**
     * 密钥算法 AES
     */
    private static final String KEY_ALGORITHM = "AES";
    /**
     * 加解密算法/工作模式/填充方式
     * java支持PKCS5Padding、不支持PKCS7Padding
     * Bouncy Castle支持PKCS7Padding填充方式
     * transformation的格式为algorithm/mode/padding，其中algorithm为必输项，如: AES/DES/CBC/PKCS5Padding
     * 缺省的mode为ECB，缺省的padding为PKCS5Padding;
     */
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS7Padding";

    static {
        // 是PKCS7Padding填充方式，则需要添加Bouncy Castle支持
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * AES 加密操作
     *
     * @param content 待加密内容
     * @param key     加密密钥
     * @param iv      偏移量
     * @return 返回加密字符串
     */
    public static String encrypt(String content, String key, String iv) {
        return encrypt(content, key.getBytes(StandardCharsets.UTF_8), iv);
    }

    public static String encrypt(String content, byte[] keyBytes, String iv) {
        try {
            byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);
            // 实例化 ECB模式，PKCS5填充
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            IvParameterSpec ivParameterSpec = null;
            if (StringUtils.isNotEmpty(iv)) {
                // 实例化  CBC模式，PKCS7填充
                cipher = Cipher.getInstance(CIPHER_ALGORITHM, "BC");
                ivParameterSpec = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));
            }
            // 初始化为加密模式的密码器
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(keyBytes, KEY_ALGORITHM), ivParameterSpec);
            byte[] encryptBytes = cipher.doFinal(contentBytes);
            // byte数组转换为16进制字符串返回
            return Hex.encodeHexString(encryptBytes);
        } catch (Exception ex) {
            LOGGER.error("encrypt error:{}", ex.getMessage());
        }
        return null;
    }

    /**
     * AES 解密操作
     *
     * @param content 待解密内容
     * @param key     加密密钥
     * @param iv      偏移量
     * @return 返回解密字符串
     */
    public static String decrypt(String content, String key, String iv) {
        try {
            byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
            // 实例化 ECB模式，PKCS5填充
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            IvParameterSpec ivParameterSpec = null;
            if (StringUtils.isNotEmpty(iv)) {
                // 实例化 CBC模式，PKCS7填充
                cipher = Cipher.getInstance(CIPHER_ALGORITHM, "BC");
                ivParameterSpec = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));
            }
            // 使用密钥初始化，设置为解密模式
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(keyBytes, KEY_ALGORITHM), ivParameterSpec);
            // 16进制字符串转换为byte数组
            byte[] decryptBytes = cipher.doFinal(Hex.decodeHex(content.toCharArray()));
            return new String(decryptBytes, StandardCharsets.UTF_8);
        } catch (Exception ex) {
            LOGGER.error("encrypt error:{}", ex.getMessage());
        }
        return null;
    }

    /**
     * AES解密
     * @param encryptBytes 待解密的byte[]
     * @param decryptKey  解密密钥
     * @return 解密后的String
     * @throws Exception
     */
    public static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey){
        try{
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128);

            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(), "AES"));
            byte[] decryptBytes = cipher.doFinal(encryptBytes);

            return new String(decryptBytes);
        }catch (Exception ex){
            LOGGER.error(ex.getMessage(), ex);
        }
        return new String(encryptBytes);
    }
}

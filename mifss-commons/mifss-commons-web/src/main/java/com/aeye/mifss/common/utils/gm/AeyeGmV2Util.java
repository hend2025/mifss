package com.aeye.mifss.common.utils.gm;

import cn.hsa.hsaf.core.framework.web.exception.BusinessException;
import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSONObject;
import org.bouncycastle.asn1.gm.GMNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jcajce.provider.asymmetric.util.ECUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPrivateKeySpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.util.BigIntegers;
import org.bouncycastle.util.encoders.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * SM2签名算法：SM3withSM2
 * SM2曲线规则：sm2p256v1
 * SM4加解密算法：SM4/ECB/PKCS7Padding
 * RSA加解密算法：RSA/ECB/PKCS1Padding
 * AES加解密算法：AES/ECB/PKCS5Padding
 */
public class AeyeGmV2Util {

    private static String sm2SpecStr = "sm2p256v1";
    private static X9ECParameters x9ECParameters = GMNamedCurves.getByName(sm2SpecStr);
    private static ECParameterSpec ecParameterSpec = new ECParameterSpec(x9ECParameters.getCurve(), x9ECParameters.getG(), x9ECParameters.getN());

    /**
     * SM4密钥生成算法
     */
    public static final String SM4_ALGORITHM_NAME = "SM4";
    /**
     * SM4加解密算法
     */
    public static final String SM4_ALGORITHM_NAME_ECB_PADDING = "SM4/ECB/PKCS7Padding";

    /**
     * RSA密钥生成算法
     */
    public static final String RSA_ALGORITHM_NAME = "RSA";
    /**
     * RSA加解密算法
     */
    public static final String RSA_ALGORITHM_NAME_ECB_PADDING = "RSA/ECB/PKCS1Padding";

    /**
     * AES密钥生成算法
     */
    public static final String AES_ALGORITHM_NAME = "AES";
    /**
     * AES加解密算法
     */
    public static final String AES_ALGORITHM_NAME_ECB_PADDING = "AES/ECB/PKCS5Padding";

    public static String encryptSm4(String keyBase64, String data) throws Exception{
        return encryptSm4(Base64.decode(keyBase64), data.getBytes());
    }

    public static String encryptSm4(byte[] key, byte[] data) throws Exception{
        Cipher cipher = Cipher.getInstance(SM4_ALGORITHM_NAME_ECB_PADDING, BouncyCastleProvider.PROVIDER_NAME);
        Key sm4Key = new SecretKeySpec(key, SM4_ALGORITHM_NAME);
        // ENCRYPT_MODE 加密模式
        cipher.init(Cipher.ENCRYPT_MODE, sm4Key);
        byte[] encData = cipher.doFinal(data);
        return Base64.toBase64String(encData);
    }

    public static byte[] decryptSm4Byte(byte[] key, byte[] data) throws Exception{
        Cipher cipher = Cipher.getInstance(SM4_ALGORITHM_NAME_ECB_PADDING, BouncyCastleProvider.PROVIDER_NAME);
        Key sm4Key = new SecretKeySpec(key, SM4_ALGORITHM_NAME);
        // DECRYPT_MODE 解密模式
        cipher.init(Cipher.DECRYPT_MODE, sm4Key);
        return cipher.doFinal(data);
    }

    public static String decryptSm4Str(String keyBase64, String dataBase64) throws Exception{
        return new String(decryptSm4Byte(Base64.decode(keyBase64), Base64.decode(dataBase64)));
    }

    public static String generateSm4KeyBase64() throws Exception{
        KeyGenerator kg = KeyGenerator.getInstance(SM4_ALGORITHM_NAME, BouncyCastleProvider.PROVIDER_NAME);
        kg.init(128, SecureRandom.getInstance("SHA1PRNG"));
        return Base64.toBase64String(kg.generateKey().getEncoded());
    }

    public static GenKeyDto generateSm2Key() throws Exception{
        KeyPairGenerator keyPairGenerator = null;
        ECGenParameterSpec sm2Spec = new ECGenParameterSpec(sm2SpecStr);
        keyPairGenerator = KeyPairGenerator.getInstance("EC", new BouncyCastleProvider());

        keyPairGenerator.initialize(sm2Spec, SecureRandom.getInstance("SHA1PRNG"));
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        GenKeyDto dto = new GenKeyDto();
        dto.setPublicKey(((BCECPublicKey)publicKey).getQ().getEncoded(false));
        dto.setPrivateKey(((BCECPrivateKey)privateKey).getD().toByteArray());
        if(dto.getPrivateKey().length == 33 && dto.getPrivateKey()[0] == 0){
            byte[] newByte = new byte[dto.getPrivateKey().length -1];
            System.arraycopy(dto.getPrivateKey(), 1, newByte, 0, dto.getPrivateKey().length - 1);
            dto.setPrivateKey(newByte);
        }
        return dto;
    }

    public static GenKeyDto generateRsaKey(int keySize) throws Exception{
        KeyPairGenerator kpGen = KeyPairGenerator.getInstance(RSA_ALGORITHM_NAME);
        kpGen.initialize(keySize);
        KeyPair kp = kpGen.generateKeyPair();
        GenKeyDto dto = new GenKeyDto();
        dto.setPrivateKey(kp.getPrivate().getEncoded());
        dto.setPublicKey(kp.getPublic().getEncoded());
        return dto;
    }

    public static GenKeyDto generateRsaKey() throws Exception{
        return generateRsaKey(2048);
    }

    public static String signSm2Base64(String priKeySm2Base64, byte[] data)throws Exception{
        ECPrivateKeySpec ecPrivateKeySpec = new ECPrivateKeySpec(BigIntegers.fromUnsignedByteArray(Base64.decode(priKeySm2Base64)), ecParameterSpec);
        // 算法algorithm=EC
        BCECPrivateKey ecPrivateKey = new BCECPrivateKey("EC", ecPrivateKeySpec, BouncyCastleProvider.CONFIGURATION);
        // 算法实例SM3withSM2，BC提供
        Signature signer = Signature.getInstance("SM3withSM2",BouncyCastleProvider.PROVIDER_NAME);
        signer.initSign(ecPrivateKey, SecureRandom.getInstance("SHA1PRNG"));
        signer.update(data, 0, data.length);
        return Base64.toBase64String(signer.sign());
    }

    public static boolean verifySignSm2(String pubKeySm2Base64, String signBase64, byte[] data) throws Exception{
        ECCurve curve = x9ECParameters.getCurve();
        ECPublicKeySpec ecPublicKeySpec = new ECPublicKeySpec(curve.decodePoint(Base64.decode(pubKeySm2Base64)), ecParameterSpec);
        BCECPublicKey ecPublicKey = new BCECPublicKey("EC", ecPublicKeySpec, BouncyCastleProvider.CONFIGURATION);

        Signature sig = Signature.getInstance("SM3withSM2", BouncyCastleProvider.PROVIDER_NAME);
        sig.initVerify(ecPublicKey);
        sig.update(data);
        return sig.verify(Base64.decode(signBase64));
    }

    public static byte[] decryptSm2(byte[] priKey, byte[] data) throws Exception{
        ECPrivateKeySpec ecPrivateKeySpec = new ECPrivateKeySpec(BigIntegers.fromUnsignedByteArray(priKey), ecParameterSpec);
        BCECPrivateKey bcecPrivateKey = new BCECPrivateKey("EC", ecPrivateKeySpec, BouncyCastleProvider.CONFIGURATION);
        ECParameterSpec ecParameterSpec = bcecPrivateKey.getParameters();
        ECDomainParameters ecDomainParameters = new ECDomainParameters(ecParameterSpec.getCurve(),
                ecParameterSpec.getG(), ecParameterSpec.getN());
        ECPrivateKeyParameters ecPrivateKeyParameters = new ECPrivateKeyParameters(bcecPrivateKey.getD(),
                ecDomainParameters);
        SM2Engine sm2Engine = new SM2Engine(SM2Engine.Mode.C1C3C2);
        sm2Engine.init(false, ecPrivateKeyParameters);
        byte[] arrayOfBytes = sm2Engine.processBlock(data, 0, data.length);
        return arrayOfBytes;
    }

    public static byte[] encryptSm2(byte[] pubKey, byte[] data) throws Exception{
        ECCurve curve = x9ECParameters.getCurve();
        ECPublicKeySpec ecPublicKeySpec = new ECPublicKeySpec(curve.decodePoint(pubKey), ecParameterSpec);
        BCECPublicKey ecPublicKey = new BCECPublicKey("EC", ecPublicKeySpec, BouncyCastleProvider.CONFIGURATION);

        CipherParameters pubKeyParameters = new ParametersWithRandom(ECUtil.generatePublicKeyParameter(ecPublicKey), SecureRandom.getInstance("SHA1PRNG"));
        SM2Engine sm2Engine = new SM2Engine(SM2Engine.Mode.C1C3C2);
        sm2Engine.init(true, pubKeyParameters);

        byte[] arrayBytes = sm2Engine.processBlock(data, 0, data.length);
        return arrayBytes;
    }

    public static byte[] encryptRsa(byte[] pubKey, byte[] message) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM_NAME_ECB_PADDING);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(pubKey);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM_NAME);
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(message);
    }

    public static byte[] decryptRsa(byte[] priKey, byte[] input) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM_NAME_ECB_PADDING);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(priKey);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM_NAME);
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(input);
    }

    public static String generateAesKeyBase64() throws Exception{
        KeyGenerator kg = KeyGenerator.getInstance(AES_ALGORITHM_NAME);
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        //设置种子
//        random.setSeed(password.getBytes());
        kg.init(128, random);
        SecretKey secretKey = kg.generateKey();
        return Base64.toBase64String(secretKey.getEncoded());
    }
    public static byte[] decryptAes(byte[] key, byte[] data) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(key, AES_ALGORITHM_NAME);
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM_NAME_ECB_PADDING);
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        return cipher.doFinal(data);
    }

    public static byte[] encryptAes(byte[] key, byte[] data) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(key, AES_ALGORITHM_NAME);
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM_NAME_ECB_PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] bytes = cipher.doFinal(data);
        return bytes;
    }

    public static void main(String[] args) throws Exception{
        // 加密文件生成步骤
        // 步骤一
        GenKeyDto sm2Key = generateSm2Key();
        System.out.println("getPublicKey   " + sm2Key.getPublicKey().length);
        System.out.println("getPrivateKey   " + sm2Key.getPrivateKey().length);
        System.out.println("sm2私钥Base64：" + Base64.toBase64String(sm2Key.getPrivateKey()));
        System.out.println("sm2公钥Base64：" + Base64.toBase64String(sm2Key.getPublicKey()));
        // 步骤二
        String sm4Key = generateSm4KeyBase64();
        System.out.println("国密SM4:" + sm4Key);
        byte[] sm4KeyByte = Base64.decode(sm4Key);
        // 步骤三
        byte[] data3 = addBytes(sm2Key.getPublicKey(), sm4KeyByte);
        byte[] aeyeKey = Base64.decode(encryptSm4("20230316_&*#$zhy".getBytes(), data3));
        // 步骤四 写入到秘钥文件
        FileUtil.writeBytes(aeyeKey, "D:\\IOT\\aeye_sm.key");

        // 解密秘钥文件步骤
        // 步骤一：读取aeye_sm.key秘钥文件
        byte[] aeyeSM = FileUtil.readBytes("D:\\IOT\\aeye_sm.key");
        // 解密得到原始SM2+SM4的秘钥字节
        byte[] aeyeSMKey = decryptSm4Byte("20230316_&*#$zhy".getBytes(), aeyeSM);
        // 分割截取最后的SM4秘钥字节，最后16位字节
        byte[] newSm4Byte = new byte[16];
        // 分割截取前面的SM2公钥字节，前面65个字节
        byte[] newSm2Byte = new byte[65];
        System.arraycopy(aeyeSMKey, 0, newSm2Byte, 0, 65);
        System.arraycopy(aeyeSMKey, 65, newSm4Byte, 0, 16);

        // 设备端使用加密秘钥加密1400报文
        // 步骤一：模拟1400报文字符串
        String st = "我是测试字符串";
        // 步骤二：使用sm4秘钥加密1400报文
        String sm4EncData = encryptSm4(newSm4Byte, st.getBytes());
        // 步骤三：使用sm2公钥加密sm4秘钥
        String sm2EncData = Base64.toBase64String(encryptSm2(newSm2Byte, newSm4Byte));
        // 步骤四：拼接加密完整报文，参考：《加密设备与1400平台协同标准》文档
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("encData", sm4EncData);
        jsonObject.put("encKey", sm2EncData);

        // 模拟服务器解密步骤
        // 步骤一：拿到报文解密SM4秘钥
        String encKey = jsonObject.getString("encKey");
        // 步骤二：解密1400原始报文，使用配对的SM2私钥，解密encKey得到SM4秘钥，注意：encKey和encData从设备端发过来都是Base64编码的，这里都要解一下
        byte[] decData = decryptSm4Byte(decryptSm2(sm2Key.getPrivateKey(), Base64.decode(encKey)),
                Base64.decode(jsonObject.getString("encData")));
        System.out.println("最终原始报文：" + new String(decData));
    }

    /**
     *
     * @param channelId - 渠道ID
     * @param data
     * @return
     * @throws Exception
     */
    public static JSONObject buildEncData(String priKeySm2Base64, String keySm4Base64, String channelId, String data) throws Exception{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("channelId", channelId);
        jsonObject.put("timestamp", System.currentTimeMillis());
        jsonObject.put("data", data);
        //对实际的data数据进行 加密
        String msg = encryptSm4(keySm4Base64, data);
        //插入加密数据
        jsonObject.put("encData", msg);
        //最终请求的报文进行签名
        String signBase64 = signSm2Base64(priKeySm2Base64, jsonObject.toJSONString().getBytes());
        //插入签名报文
        jsonObject.put("signData", signBase64);
        //移除原始数据
        jsonObject.remove("data");
        return jsonObject;
    }

    public static JSONObject buildDecData(String pubKeySm2Base64, String keySm4Base64, JSONObject encData) throws Exception{
        String data = decryptSm4Str(keySm4Base64, encData.getString("encData"));
        encData.put("data", data);
        Object signData = encData.remove("signData");
        boolean boo = verifySignSm2(pubKeySm2Base64, signData.toString(), encData.toJSONString().getBytes());
        if(!boo){
            throw new BusinessException("国密SM2验签失败");
        }
        encData.remove("encData");
        return encData;
    }

    public static byte[] addBytes(byte[] data1, byte[] data2) {
        byte[] data3 = new byte[data1.length + data2.length];
        System.arraycopy(data1, 0, data3, 0, data1.length);
        System.arraycopy(data2, 0, data3, data1.length, data2.length);
        return data3;

    }

    static{
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null){
            Security.addProvider(new BouncyCastleProvider());
        }
    }
}

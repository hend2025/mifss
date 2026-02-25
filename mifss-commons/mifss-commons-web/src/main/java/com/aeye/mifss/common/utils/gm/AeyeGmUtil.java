package com.aeye.mifss.common.utils.gm;

import cn.hsa.ims.common.utils.AeyeSpringContextUtils;
import cn.hutool.core.codec.Base64;
import com.aeye.mifss.common.config.AeyeGmConfig;
import com.alibaba.fastjson.JSONObject;

public class AeyeGmUtil {

    /**
     * 公共渠道ID - 加密|解密
     */
    public static String channelId = "!@#$1qaz@WSXm,./";
    public static String appId = channelId;
    /**
     * 公共渠道凭证 - 加密|解密|签名
     */
    public static String channelSecret = "c782422043414268a396b0ca1dfcccad";
    public static String sm4key = channelSecret;
    /**
     * 公共私钥 - 签名
     */
    public static String privateKey;
    /**
     * 公共公钥 - 验签
     */
    public static String pubKey;
    /**
     * 加密算法类型
     */
    public static String encType = "SM4";
    /**
     * 签名算法类型
     */
    public static String signType = "SM2";

    static {
        AeyeGmConfig gmConfig = AeyeSpringContextUtils.getBean(AeyeGmConfig.class);
        privateKey = gmConfig.getPrivateKey();
        pubKey = gmConfig.getPubKey();
    }

    public static String encMsg(String data) throws Exception{
        //对实际的data数据进行 加密
        return HseEncAndDecUtil.sm4Encrypt(channelId, channelSecret, data);
    }

    public static String decMsg(String encData) throws Exception{
        //对实际的data数据进行 加密
        return HseEncAndDecUtil.sm4Decrypt(channelId, channelSecret, encData);
    }

    public static String sign(String data){
        return sign(data, channelSecret, privateKey);
    }

    public static String sign(String data, String channelSecret, String privateKey){
        return HseEncAndDecUtil.signature(data, channelSecret, privateKey);
    }

    public static boolean verifySign(String decryptData, String encryptData, String sign){
        return verifySign(decryptData, encryptData, sign, channelSecret, pubKey);
    }

    public static boolean verifySign(String decryptData, String encryptData, String sign, String channelSecret, String pubKey){
        return HseEncAndDecUtil.verify(decryptData, encryptData, sign, channelSecret, pubKey);
    }

    public static JSONObject buildEncData(JSONObject data) throws Exception{
        return buildEncData(channelId, channelSecret, privateKey, data);
    }

    /**
     *
     * @param channelId - 渠道ID
     * @param channelSecret - 渠道凭证
     * @param privateKey - 私钥
     * @param data
     * @return
     * @throws Exception
     */
    public static JSONObject buildEncData(String channelId, String channelSecret, String privateKey, JSONObject data) throws Exception{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("channelId", channelId);
        jsonObject.put("timestamp", System.currentTimeMillis());
        jsonObject.put("data", data);

        //对实际的data数据进行 加密
        String msg = HseEncAndDecUtil.sm4Encrypt(channelId, channelSecret, data.toJSONString());
        //插入加密数据
        jsonObject.put("encData", msg);
        //最终请求的报文进行签名
        String asig = HseEncAndDecUtil.signature(jsonObject.toJSONString(), channelSecret, privateKey);
        jsonObject.remove("data");
        //插入签名报文
        jsonObject.put("signData", asig);
        return jsonObject;
    }

    public static void main(String[] args) throws Exception{
        JSONObject data = new JSONObject();
        data.put("seq", 118230803);
        JSONObject pic1 = new JSONObject();
        pic1.put("p1", "dfsdf");
        pic1.put("p2", "dfsdf");
        pic1.put("p3", "dfsdf");
        pic1.put("p4", "dfsdf");
        pic1.put("p5", "dfsdf");
        data.put("pic", pic1);

        JSONObject buildEncData = buildEncData(data);
        System.out.println(verifySign(data.toJSONString(), buildEncData.toJSONString(), buildEncData.getString("signData")));

        JSONObject obj = new JSONObject();
        obj.put("encDataBase64", Base64.encode(buildEncData.toJSONString().getBytes()));

        System.out.println(obj);
    }
}

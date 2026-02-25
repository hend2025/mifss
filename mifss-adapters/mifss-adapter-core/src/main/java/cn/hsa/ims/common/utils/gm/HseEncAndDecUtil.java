package cn.hsa.ims.common.utils.gm;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.util.StringUtils;

import java.util.Base64;
import java.util.Iterator;
import java.util.Map;

public class HseEncAndDecUtil {


    /**
     * sm2签名
     * @param message 未加密报文
     * @param chnlSecret 渠道密钥
     * @param prvKey 渠道私钥
     * @return 签名串 String
     * @throws Exception
     */
    public static String signature(String message,String chnlSecret,String prvKey){
        byte[] messageByte;
        try {
            JSONObject jsonObject = JSON.parseObject(message);
            removeEmpty(jsonObject);
            messageByte = SignUtil.getSignText(jsonObject, chnlSecret).getBytes("UTF-8");
        }catch (Exception e){
            messageByte = message.getBytes();
        }

        byte[] chnlSecretByte = chnlSecret.getBytes();
        byte[] prvkey = Base64.getDecoder().decode(prvKey);
        String sign = Base64.getEncoder().encodeToString(EasyGmUtils.signSm3WithSm2(messageByte, chnlSecretByte, prvkey));
        return sign;

    }

    /**
     * sm2验签
     * @param msg sm4解密后报文
     * @param source 原始响应报文
     * @param signatureStr 签名串
     * @param chnlSecret 渠道密钥
     * @param pubKey 平台公钥
     * @return 验证是否通过 boolean
     * @throws Exception
     */
    public static boolean verify(String msg,String source, String signatureStr,String chnlSecret,String pubKey){
        byte[] msgByte;
        try {
            JSONObject jsonObject = JSON.parseObject(msg);
            JSONObject jsonObjects = JSON.parseObject(source);
            jsonObjects.remove("signData");
            jsonObjects.remove("encData");
            jsonObjects.put("data",jsonObject);
            removeEmpty(jsonObject);
            String str = SignUtil.getSignText(jsonObjects, chnlSecret);
            msgByte = str.getBytes("UTF-8");
        }catch (Exception e){
            msgByte = msg.getBytes();
        }
        byte[] signatureByte = Base64.getDecoder().decode(signatureStr),
                chnlSecretByte = chnlSecret.getBytes(),
                pubKeyByte = Base64.getDecoder().decode(pubKey);
        return EasyGmUtils.verifySm3WithSm2(msgByte, chnlSecretByte, signatureByte, pubKeyByte);
    }


    /**
     * sm4加密
     * @param chnlId 渠道id
     * @param chnlSecret 渠道密钥
     * @param message 待加密报文
     * @return 加密后的报文内容 String
     * @throws Exception
     */
    public static String sm4Encrypt(String chnlId,String chnlSecret,String message) throws Exception {
        //用appId加密appSecret获取新秘钥
        byte[] appSecretEncData = EasyGmUtils.sm4Encrypt(chnlId.substring(0, 16).getBytes("UTF-8"), chnlSecret.getBytes("UTF-8"));
        //新秘钥串
        byte[] secKey = Hex.toHexString(appSecretEncData).toUpperCase().substring(0, 16).getBytes("UTF-8");
        //加密数据
        String encryptDataStr = Hex.toHexString(EasyGmUtils.sm4Encrypt(secKey, message.getBytes("UTF-8"))).toUpperCase();
        return encryptDataStr;
    }

    /**
     * sm4解密
     * @param chnlId 渠道id
     * @param chnlSecret 渠道密钥
     * @param message 待解密报文
     * @return 解密后的报文 String
     * @throws Exception
     */
    public static String sm4Decrypt (String chnlId,String chnlSecret,String message) throws Exception{
        //生产解密key
        byte[] appSecretEncDataDecode = EasyGmUtils.sm4Encrypt(chnlId.substring(0, 16).getBytes("UTF-8"), chnlSecret.getBytes("UTF-8"));
        byte[] secKeyDecode = Hex.toHexString(appSecretEncDataDecode).toUpperCase().substring(0, 16).getBytes("UTF-8");

        String dencryptDataStr = new String(EasyGmUtils.sm4Decrypt(secKeyDecode, Hex.decode(message)));

        /*System.out.println("解密数据：" + dencryptDataStr);*/
        return dencryptDataStr;
    }


    private static void removeEmpty(JSONObject jsonObject){
        Iterator<Map.Entry<String, Object>> it = jsonObject.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry<String, Object> entry = it.next();
            Object value = entry.getValue();
            if (value instanceof JSONArray) {
                JSONArray jsonArray = (JSONArray) value;
                // 数组长度为0时将其处理,防止Gson转换异常
                if (jsonArray.size() == 0) {
                    it.remove();
                } else {
                    jsonArray.remove(null);
                    jsonArray.remove("");
                    for (int i=0;i<jsonArray.size();i++) {
                        if(jsonArray.get(i) instanceof JSONObject){
                            JSONObject asJsonObject = (JSONObject) jsonArray.get(i);
                            removeEmpty(asJsonObject);
                        }
                    }
                }
            }
            if (value instanceof JSONObject) {
                JSONObject asJsonObject = (JSONObject) value;
                removeEmpty(asJsonObject);
            }
            if (value == null){
                it.remove();
            }
            if (value instanceof String && StringUtils.isEmpty(value)){
                it.remove();
            }
        }
    }

    public static void main(String[] args) throws Exception{

    }
}

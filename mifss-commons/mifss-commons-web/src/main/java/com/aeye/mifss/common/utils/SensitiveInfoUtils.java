package com.aeye.mifss.common.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import com.aeye.mifss.common.utils.gm.AeyeGmUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

public class SensitiveInfoUtils {

    public static final int INT_2 = 2;

    /**
     * [中文姓名] 只显示第一个汉字，其他隐藏为2个星号<例子：李**>
     */
    public static String chineseName(final String fullName) {
        if (StrUtil.isBlank(fullName)) {
            return "";
        }
        final String name = StringUtils.left(fullName, 1);
        if(fullName.length() > INT_2){
            return name.concat(StringUtils
                    .removeStart(StringUtils.leftPad(StringUtils.right(fullName, 1), StringUtils.length(fullName), "*"),
                            "*"));
        }else{
            return StringUtils.rightPad(name, StringUtils.length(fullName), "*");
        }
    }

    /**
     * [中文姓名] 只显示第一个汉字，其他隐藏为2个星号<例子：李**>
     */
    public static String chineseName(final String familyName, final String givenName) {
        if (StringUtils.isBlank(familyName) || StringUtils.isBlank(givenName)) {
            return "";
        }
        return chineseName(familyName + givenName);
    }

    /**
     * [身份证号] 显示最后四位，其他隐藏。共计18位或者15位。<例子：*************5762>
     */
    public static String idCardNum(final String id) {
        if (StringUtils.isBlank(id)) {
            return "";
        }

        return StringUtils.left(id, 3).concat(StringUtils
                .removeStart(StringUtils.leftPad(StringUtils.right(id, 4), StringUtils.length(id), "*"),
                        "***"));
    }

    /**
     * [固定电话] 后四位，其他隐藏<例子：****1234>
     */
    public static String fixedPhone(final String num) {
        if (StringUtils.isBlank(num)) {
            return "";
        }
        return StringUtils.leftPad(StringUtils.right(num, 4), StringUtils.length(num), "*");
    }

    /**
     * [手机号码] 前三位，后四位，其他隐藏<例子:138******1234>
     */
    public static String mobilePhone(final String num) {
        if (StringUtils.isBlank(num)) {
            return "";
        }
        return StringUtils.left(num, INT_2).concat(StringUtils
                .removeStart(StringUtils.leftPad(StringUtils.right(num, INT_2), StringUtils.length(num), "*"),
                        "***"));

    }

    /**
     * [地址] 只显示到地区，不显示详细地址；我们要对个人信息增强保护<例子：北京市海淀区****>
     *
     * @param sensitiveSize 敏感信息长度
     */
    public static String address(final String address, final int sensitiveSize) {
        if (StringUtils.isBlank(address)) {
            return "";
        }
        final int length = StringUtils.length(address);
        return StringUtils.rightPad(StringUtils.left(address, length - sensitiveSize), length, "*");
    }

    /**
     * [电子邮箱] 邮箱前缀仅显示第一个字母，前缀其他隐藏，用星号代替，@及后面的地址显示<例子:g**@163.com>
     */
    public static String email(final String email) {
        if (StringUtils.isBlank(email)) {
            return "";
        }
        final int index = StringUtils.indexOf(email, "@");
        if (index <= 1) {
            return email;
        } else {
            return StringUtils.rightPad(StringUtils.left(email, 1), index, "*")
                    .concat(StringUtils.mid(email, index, StringUtils.length(email)));
        }
    }

    /**
     * [银行卡号] 前六位，后四位，其他用星号隐藏每位1个星号<例子:6222600**********1234>
     */
    public static String bankCard(final String cardNum) {
        if (StringUtils.isBlank(cardNum)) {
            return "";
        }
        return StringUtils.left(cardNum, 6).concat(StringUtils.removeStart(
                StringUtils.leftPad(StringUtils.right(cardNum, 4), StringUtils.length(cardNum), "*"),
                "******"));
    }

    /**
     * [公司开户银行联号] 公司开户银行联行号,显示前两位，其他用星号隐藏，每位1个星号<例子:12********>
     */
    public static String cnapsCode(final String code) {
        if (StringUtils.isBlank(code)) {
            return "";
        }
        return StringUtils.rightPad(StringUtils.left(code, INT_2), StringUtils.length(code), "*");
    }

    /**
     * 字段国密算法加密处理
     */
    public static String gmEncrypt(final String code) throws IOException {
        if (StringUtils.isBlank(code)) {
            return "";
        }
        try {
            return AeyeGmUtil.encMsg(code);
        }catch (Exception ex){
            throw new IOException(ex);
        }
    }

    /**
     * base64
     */
    public static String base64Code(final String code) throws IOException {
        if (StringUtils.isBlank(code)) {
            return "";
        }
        try {
            return Base64.encode(code);
        }catch (Exception ex){
            throw new IOException(ex);
        }
    }

    /**
     * base64
     */
    public static String simpleEncrypt(String code) throws IOException {
        if (StringUtils.isBlank(code)) {
            return "";
        }
        try {
            code = code.replaceAll("0", "%")
                    .replaceAll("1", "!")
                    .replaceAll("2", "@")
                    .replaceAll("3", "#")
                    .replaceAll("4", "+")
                    .replaceAll("5", "-")
                    .replaceAll("6", "&")
                    .replaceAll("7", "*")
                    .replaceAll("8", "=")
                    .replaceAll("9", "^");
            return code;
        }catch (Exception ex){
            throw new IOException(ex);
        }
    }
}

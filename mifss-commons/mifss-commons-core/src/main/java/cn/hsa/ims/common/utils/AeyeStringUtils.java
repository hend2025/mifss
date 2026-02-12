package cn.hsa.ims.common.utils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author shenxingping
 * @Date 2021/1/16
 */
public class AeyeStringUtils extends org.apache.commons.lang3.StringUtils {
    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("^(0|86|17951)?(13[0-9]|15[0-9]|17[0-9]|18[0-9]|14[57])[0-9]{8}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
    private static final Pattern IE_PATTERN = Pattern.compile("MSIE\\s+(\\d+(\\.(\\d+)?)?)\\s*;", Pattern.CASE_INSENSITIVE);
    private static final Pattern BR_PATTERN = Pattern.compile("<br[^>]*/?>", Pattern.CASE_INSENSITIVE);
    private static final Pattern HTML_TAG_PATTERN = Pattern.compile("<(\\S*?) [^>]*>.*?</\\1>|<.*? />", Pattern.CASE_INSENSITIVE);

    private static final Pattern FORMAT_PATTERN = Pattern.compile(".*?(\\{(\\w+)?}).*?", Pattern.MULTILINE);


    /**
     * 微信号正则
     * 1、微信号是微信的唯一凭证，只能设置1次；
     * 2、可使用6-20个字母、数字、下划线和减号；
     * 3、必须以字母开头（字母不区分大小写）；
     * 4、不支持设置中文。
     */
    private static final Pattern WECHAT_ACCOUNT_PATTERN = Pattern.compile("^[a-zA-Z][a-zA-Z0-9-_]{5,}");

    /**
     * /\s*,?(\d+)\s*,?\s*($|\d)/
     */
    private final static int DECODE_SETP = 4;
    /**
     * unicode 转换成 中文
     *
     * @param src
     * @return
     */
    public static String decodeUnicode(String src) {
        char aChar;
        int len = src.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len; ) {
            aChar = src.charAt(x++);
            if (aChar == '\\') {
                aChar = src.charAt(x++);
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < DECODE_SETP; i++) {
                        aChar = src.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException(
                                        "Malformed   \\uxxxx   encoding.");
                        }
                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't'){
                        aChar = '\t';
                    }
                    else if (aChar == 'r'){
                        aChar = '\r';}
                    else if (aChar == 'n'){
                        aChar = '\n';}
                    else if (aChar == 'f'){
                        aChar = '\f';
                    }
                    outBuffer.append(aChar);
                }
            } else {
                outBuffer.append(aChar);
            }
        }
        return outBuffer.toString();

    }

    public static String htmlEncode(String source) {
        if (source == null) {
            return "";
        }
        String html = "";
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < source.length(); i++) {
            char c = source.charAt(i);
            switch (c) {
                case '<':
                    buffer.append("&lt;");
                    break;
                case '>':
                    buffer.append("&gt;");
                    break;
                case '&':
                    buffer.append("&amp;");
                    break;
                case '"':
                    buffer.append("&quot;");
                    break;
                case 10:
                case 13:
                    break;
                default:
                    buffer.append(c);
            }
        }
        html = buffer.toString();
        return html;
    }

    /**
     * 是否是手机号码
     *
     * @param str
     * @return
     */
    public static boolean isMobile(String str) {
        if (!isEmpty(str)) {
            return PHONE_NUMBER_PATTERN.matcher(str).find();
        }
        return false;
    }

    /**
     * 是否是邮箱
     *
     * @param str
     * @return
     */
    public static boolean isEmail(String str) {
        if (!isEmpty(str)) {
            return EMAIL_PATTERN.matcher(str).find();
        }
        return false;
    }

    public static float getIeVersion(String userAgent) {
        if (!isEmpty(userAgent)) {
            Matcher matcher = IE_PATTERN.matcher(userAgent);
            if (matcher.find()) {
                return Float.valueOf(matcher.group(1));
            }
        }
        return -1;
    }


    /**
     * @param text
     * @return
     */
    public static String replaceBr(String text, String replacement) {
        if (!isEmpty(text)) {
            Matcher matcher = BR_PATTERN.matcher(text);
            return matcher.replaceAll(isEmpty(replacement) ? "\n" : replacement);
        }
        return text;
    }

    public static String replaceBr(String text) {
        return replaceBr(text, "\n");
    }


    /**
     * 获取文件后缀
     *
     * @param fileName
     * @return
     */
    public static String getFileSuffix(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
     * 将 String 列表转化成字符串，格式是：1,2,3,4
     *
     * @param list
     * @return
     */
    public static String listToString(List<String> list) {
        if (list == null || list.size() == 0) {
            return "";
        }
        String temp = list.toString();
        return temp.substring(1, temp.length() - 1);
    }

    /**
     * 将 String 列表转化成字符串，格式是：'1','2','3','4'
     *
     * @param list
     * @return
     */
    public static String listToStringWithSingleQuote(List<String> list) {
        if (list == null || list.size() == 0) {
            return "";
        }
        List<String> newList = new ArrayList<String>();
        for (String temp : list) {
            newList.add("'" + temp + "'");
        }
        return listToString(newList);
    }

    /**
     * 将 String 数组转化成字符串，格式是：'1','2','3','4'
     *
     * @param array
     * @return
     */
    public static String arrayToStringWithSingleQuote(String[] array) {
        if (array == null || array.length == 0) {
            return "";
        }
        return listToStringWithSingleQuote(Arrays.asList(array));
    }

    /**
     * 判断字符c是否为中文
     *
     * @param c
     * @return
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION    //GENERAL_PUNCTUATION 判断中文的“号
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION    //CJK_SYMBOLS_AND_PUNCTUATION 判断中文的。号
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS)    //HALFWIDTH_AND_FULLWIDTH_FORMS 判断中文的，号
        {
            return true;
        }
        return false;
    }

    /**
     * 去除字符串 data 首尾的符号
     *
     * @param data
     * @return
     */
    public static String removeStrStartOrEndSymbol(String data, String symbol) {
        if (isAnyBlank(symbol, data)) {
            return "";
        }
        data = data.trim();
        if (data.startsWith(symbol)) {
            data = data.substring(data.indexOf(symbol) + 1, data.length());
        }
        if (data.endsWith(symbol)) {
            data = data.substring(0, data.lastIndexOf(symbol));
        }
        return data;
    }

    public static String joinStringArray(String[] array, String split) {
        if (array == null || array.length == 0) {
            return "";
        }
        if (split == null) {
            split = "";
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            builder.append(array[i]);
            if (i < array.length - 1) {
                builder.append(split);
            }
        }
        return builder.toString();
    }

    public static String joinStringList(List<String> array, String split) {
        if (array == null || array.size() == 0) {
            return "";
        }
        if (split == null) {
            split = "";
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < array.size(); i++) {
            builder.append(array.get(i));
            if (i < array.size() - 1) {
                builder.append(split);
            }
        }
        return builder.toString();
    }


    /**
     * 字符串格式化
     * Created by chengshengru on 2017/05/17.
     *
     * @param template 字符串模板
     * @param args     对应的产生
     * @return
     */
    public static String format(String template, Object... args) {
        if (AeyeStringUtils.isNotBlank(template)) {
            if (args != null && args.length > 0) {
                StringBuilder builder = new StringBuilder();
                Matcher matcher = FORMAT_PATTERN.matcher(template);
                int index = 0, groupIndex = -1, start = 0, end = 0, length = 0;
                String subStr = null, groupStr = null;
                boolean isMatch = false;
                while (matcher.find()) {
                    isMatch = true;
                    start = matcher.start();
                    end = matcher.end();
                    //获取当前匹配成功的字符串
                    subStr = template.substring(start, end);
                    length += subStr.length();
                    if (index < args.length) {
                        groupStr = matcher.group(1);
                        //获取表达是字符串在子串中的位置
                        groupIndex = subStr.indexOf(groupStr);
                        if (groupIndex != -1) {
                            builder.append(subStr.substring(0, groupIndex));
                            builder.append(String.valueOf(args[index]));
                            //处理子串尾部的数据
                            if (groupIndex + groupStr.length() < subStr.length()) {
                                builder.append(subStr.substring(groupIndex + groupStr.length()));
                            }
                        }
                    } else {
                        //如果参数不够,则不做任何处理
                        builder.append(subStr);
                    }
                    index++;
                }
                if (isMatch) {
                    //处理尾部没有匹配的数据
                    if (length != template.length()) {
                        builder.append(template.substring(length));
                    }
                    return builder.toString();
                }
            }
        }
        return template;
    }


    /**
     * 1、微信号是微信的唯一凭证，只能设置1次；
     * 2、可使用6-20个字母、数字、下划线和减号；
     * 3、必须以字母开头（字母不区分大小写）；
     * 4、不支持设置中文。
     *
     * @param account
     * @return
     */
    public static boolean isWechatAccount(String account) {
        if (isNotBlank(account)) {
            return WECHAT_ACCOUNT_PATTERN.matcher(account).find();
        }
        return false;
    }

    private static String join(String delimiter,List<Object> list){
        List<String> strings = new ArrayList<>(list.size());
        list.forEach(item->{
            strings.add(String.valueOf(item));
        });
        return String.join(delimiter,strings);
    }

    /**
     * 下划线转驼峰
     *
     * @param str
     * @param spec 是否包含数字
     * @return
     */
    public static String underlineToCamelhump(String str, boolean spec) {
        String reg = "_[a-z]";
        if (spec) {
            reg = "_[a-z0-9]";
        }
        Matcher matcher = Pattern.compile(reg).matcher(str);
        StringBuilder builder = new StringBuilder(str);
        for (int i = 0; matcher.find(); ++i) {
            builder.replace(matcher.start() - i, matcher.end() - i, matcher.group().substring(1).toUpperCase());
        }

        if (Character.isUpperCase(builder.charAt(0))) {
            builder.replace(0, 1, String.valueOf(Character.toLowerCase(builder.charAt(0))));
        }
        return builder.toString();
    }

    /**
     * 下划线转驼峰（字母数字）
     *
     * @param str
     * @return
     */
    public static String underlineToCamelhump(String str) {
        return underlineToCamelhump(str, false);
    }

    /**
     * 驼峰转下划线
     *
     * @param str
     * @param spec 是否包含数字
     * @return
     */
    public static String camelhumpToUnderline(String str, boolean spec) {
        int size;
        char[] chars;
        StringBuilder sb = new StringBuilder((size = (chars = str.toCharArray()).length) * 3 / 2 + 1);
        for (int i = 0; i < size; ++i) {
            char c = chars[i];
            if (Character.isUpperCase(c)) {
                sb.append('_').append(Character.toLowerCase(c));
            } else if (spec && Character.isDigit(c) && i > 0 && !Character.isDigit(chars[i - 1])) {
                //不是连续的数字
                sb.append('_').append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.charAt(0) == 95 ? sb.substring(1) : sb.toString();
    }

    public static String camelhumpToUnderline(String str) {
        return camelhumpToUnderline(str, false);
    }


    /**
     * 隐藏文本
     *
     * @param text
     * @param left
     * @param right
     * @param maskCode
     * @return
     */
    public static String maskText(String text, int left, int right, String maskCode) {
        if (!isEmpty(text)) {
            int length = text.length();
            if (length > left + right) {
                StringBuilder builder = new StringBuilder();
                builder.append(text.substring(0, left));
                for (int index = 0; index < length - left - right; index++) {
                    builder.append(maskCode);
                }
                builder.append(text.substring(length - right, length));
                return builder.toString();
            }
        }
        return text;
    }

    public static String maskText(String text, int left, int right) {
        return maskText(text, left, right, "*");
    }

    /**
     * 隐藏手机号码
     *
     * @param mobile
     * @return
     */
    public static String maskMobile(String mobile) {
        return maskText(mobile, 3, 4);
    }


    /**
     * 隐藏银行卡
     *
     * @param bankNumber
     * @return
     */
    public static String maskBankNumber(String bankNumber) {
        return maskText(bankNumber, 4, 4);
    }

    public static String maskName(String name){
        if(isNotBlank(name)){
            name = name.trim();
            return maskText(name,1,0);
        }
        return name;
    }

    public static boolean isEmpty(Object o) {
        return o == null || "".equals(o);
    }

    private static final Set<String> trueValues = new HashSet(4);
    private static final Set<String> falseValues = new HashSet(4);
    static {
        trueValues.add("true");
        trueValues.add("on");
        trueValues.add("yes");
        trueValues.add("1");
        falseValues.add("false");
        falseValues.add("off");
        falseValues.add("no");
        falseValues.add("0");
    }
    public static Boolean convertStrToBoolean(String source) {
        String value = source.trim();
        if ("".equals(value)) {
            return null;
        } else {
            value = value.toLowerCase();
            if (trueValues.contains(value)) {
                return Boolean.TRUE;
            } else if (falseValues.contains(value)) {
                return Boolean.FALSE;
            } else {
                throw new IllegalArgumentException("Invalid boolean value '" + source + "'");
            }
        }
    }
    public static String convertBooleanToStr(Boolean source){
        if(source != null && source){
            return "1";
        }
        return "0";
    }
}

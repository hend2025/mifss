/**
 * Project Name:mbr-parent
 * Date:2017-08-25 16:48
 * Copyright (c) 2017, AEYE All Rights Reserved.
 */
package cn.hsa.ims.common.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;

/**
 * Function: ADD FUNCTION. <br/>
 * Date: 2017-08-25 16:48 <br/>
 *
 * @author duanxuhua
 * @version 1.0
 */
public class Base64Utils {

    private final static String JPG_BASE64_KEY = "/9j";

    public static String urlEncode(byte[] data){
        Base64.Encoder encode = Base64.getUrlEncoder();
        return encode.encodeToString(data);
    }
    public static byte[] urlDecode(String data){
        Base64.Decoder decoder = Base64.getUrlDecoder();
        return decoder.decode(data);
    }

    public static boolean isImage(String imageBase64, String imageType){
        return imageBase64.startsWith(JPG_BASE64_KEY);
    }

    private static final char[] LEGAL_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_".toCharArray();
    /**
     * data[]进行编码
     * @param data
     * @return
     */
    @Deprecated
    public static String encode(byte[] data) {
        int start = 0;
        int len = data.length;
        StringBuffer buf = new StringBuffer(data.length * 3 / 2);

        int end = len - 3;
        int i = start;
        int n = 0;

        while (i <= end) {
            int d = ((((int) data[i]) & 0x0ff) << 16)
                    | ((((int) data[i + 1]) & 0x0ff) << 8)
                    | (((int) data[i + 2]) & 0x0ff);

            buf.append(LEGAL_CHARS[(d >> 18) & 63]);
            buf.append(LEGAL_CHARS[(d >> 12) & 63]);
            buf.append(LEGAL_CHARS[(d >> 6) & 63]);
            buf.append(LEGAL_CHARS[d & 63]);

            i += 3;

            if (n++ >= 14) {
                n = 0;
                buf.append(" ");
            }
        }

        if (i == start + len - 2) {
            int d = ((((int) data[i]) & 0x0ff) << 16)
                    | ((((int) data[i + 1]) & 255) << 8);

            buf.append(LEGAL_CHARS[(d >> 18) & 63]);
            buf.append(LEGAL_CHARS[(d >> 12) & 63]);
            buf.append(LEGAL_CHARS[(d >> 6) & 63]);
            buf.append("=");
        } else if (i == start + len - 1) {
            int d = (((int) data[i]) & 0x0ff) << 16;

            buf.append(LEGAL_CHARS[(d >> 18) & 63]);
            buf.append(LEGAL_CHARS[(d >> 12) & 63]);
            buf.append("==");
        }

        return buf.toString();
    }

    private static int decode(char c) {
        if (c >= 'A' && c <= 'Z') {
            return ((int) c) - 65;
        }
        else if (c >= 'a' && c <= 'z'){
            return ((int) c) - 97 + 26;
        }
        else if (c >= '0' && c <= '9'){
            return ((int) c) - 48 + 26 + 26;
        }
        else {
            switch (c) {
                case '+':
                    return 62;
                case '/':
                    return 63;
                case '=':
                    return 0;
                default:
                    throw new RuntimeException("unexpected code: " + c);
            }
        }
    }

    /**
     * Decodes the given Base64 encoded String to a new byte array. The byte
     * array holding the decoded data is returned.
     */
    @Deprecated
    public static byte[] decode(String s) {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            decode(s, bos);
        } catch (IOException e) {
            throw new RuntimeException();
        }
        byte[] decodedBytes = bos.toByteArray();
        try {
            bos.close();
            bos = null;
        } catch (IOException ex) {
            System.err.println("Error while decoding BASE64: " + ex.toString());
        }
        return decodedBytes;
    }

    private static void decode(String s, OutputStream os) throws IOException {
        int i = 0;

        int len = s.length();

        while (true) {
            while (i < len && s.charAt(i) <= ' ') {
                i++;
            }

            if (i == len) {
                break;
            }

            int tri = (decode(s.charAt(i)) << 18)
                    + (decode(s.charAt(i + 1)) << 12)
                    + (decode(s.charAt(i + 2)) << 6)
                    + (decode(s.charAt(i + 3)));

            os.write((tri >> 16) & 255);
            if (s.charAt(i + 2) == '=') {
                break;
            }
            os.write((tri >> 8) & 255);
            if (s.charAt(i + 3) == '=') {
                break;
            }
            os.write(tri & 255);

            i += 4;
        }
    }

    public static void main(String args[]) throws Exception{
//        DateTimeFormatter formatter = AeyeJdk8DateUtil.DATETIME_MINUTE;
//        Date nextTime = AeyeJdk8DateUtil.convertToDate(LocalDate.parse("201901010030", formatter).atStartOfDay());
//        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(nextTime));
//        String bb = urlEncode("test/[day]/11111111ggddddddddddddddddddddddddddddddddddbbbbbb11111111ggddddddddddddddddddddddddddddddddddbbbbbb.jpg".getBytes());
//        System.out.println(new String(bb));
        byte[] bb = urlDecode("TTAwLzAwLzAwL3dLZ1FsR0wxejUyQUI5UU9BQUJVLVo2X3VOQTU2MC50eHQ=");
        System.out.println(new String(bb));
    }
}

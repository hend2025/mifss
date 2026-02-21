package cn.hsa.ims.common.utils;

public class AeyeContentTypeUtil {
    public static String contentType(String filenameExtension) {

        if(filenameExtension.lastIndexOf(".") > -1 && !filenameExtension.startsWith(".")){
            filenameExtension = filenameExtension.substring(filenameExtension.lastIndexOf("."), filenameExtension.length());
        }
        if (".bmp".equals(filenameExtension.toLowerCase())) {
            return "image/bmp";
        }
        if (".gif".equals(filenameExtension.toLowerCase())) {
            return "image/gif";
        }
        if (".jpeg".equals(filenameExtension.toLowerCase())
                || ".jpg".equals(filenameExtension.toLowerCase()) || ".png".equals(filenameExtension.toLowerCase())) {
            return "image/jpeg";
        }
        if (".html".equals(filenameExtension.toLowerCase())) {
            return "text/html";
        }
        if (".txt".equals(filenameExtension.toLowerCase())) {
            return "text/plain";
        }
        if (".vsd".equals(filenameExtension.toLowerCase())) {
            return "application/vnd.visio";
        }
        if (".pptx".equals(filenameExtension.toLowerCase())
                || ".ppt".equals(filenameExtension.toLowerCase())) {
            return "application/vnd.ms-powerpoint";
        }
        if (".docx".equals(filenameExtension.toLowerCase())
                || ".doc".equals(filenameExtension.toLowerCase())) {
            return "application/msword";
        }
        if (".xml".equals(filenameExtension.toLowerCase())) {
            return "text/xml";
        }
        if (".pdf".equals(filenameExtension.toLowerCase())) {
            return "application/pdf";
        }
        return "";
    }

    public static String imgTypeValid(String dataPrix) {
        if ("data:image/jpeg;".equalsIgnoreCase(dataPrix)) {
            return ".jpeg";
        } else if ("data:image/jpg;".equalsIgnoreCase(dataPrix)) {
            return ".jpg";
        } else if ("data:image/gif;".equalsIgnoreCase(dataPrix)) {
            return ".gif";
        } else if ("data:image/png;".equalsIgnoreCase(dataPrix)) {
            return ".png";
        } else if ("data:image/apng;".equalsIgnoreCase(dataPrix)) {
            return ".apng";
        } else if ("data:image/svg;".equalsIgnoreCase(dataPrix)) {
            return ".svg";
        } else if ("data:image/bmp;".equalsIgnoreCase(dataPrix)) {
            return ".bmp";
        }
        return "";
    }
}

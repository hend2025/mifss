package cn.hsa.ims.common.utils;

import cn.hsa.hsaf.core.framework.web.exception.BusinessException;
import cn.hsa.hsaf.core.fsstore.FSEntity;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.io.IoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class AeyeFileReadWriteUtil {

    private static Logger log = LoggerFactory.getLogger(AeyeFileReadWriteUtil.class);

    public static final String RENAME_SPLIT_UUID_MARK = "@@";

    public static final String DIR_SPLIT_MARK = "/";

    public static final String FILE_SPLIT_MARK = ".";

    public static final String SYS_TMP_DIR = System.getProperty("java.io.tmpdir");

    static {
        try {
            SslUtil.ignoreSsl();
        } catch (Exception ex) {
            log.warn("全局https忽略证书配置异常！msg={}", ex.getMessage());
        }
    }

    /**
     * 文件流直接向response输出增加传输速度
     * 
     * @param resp
     * @param inputStream
     */
    public static void httpWriteFile(HttpServletResponse resp, InputStream inputStream) {
        OutputStream out = null;
        try {
            out = resp.getOutputStream();
            int len = 0;
            byte[] b = new byte[1024];
            while ((len = inputStream.read(b)) != -1) {
                out.write(b, 0, len);
            }
            out.flush();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            IoUtil.close(out);
            IoUtil.close(inputStream);
        }
    }

    public static boolean byteWriteFile(byte[] data, File outFile) {
        OutputStream out = null;
        try {
            out = new FileOutputStream(outFile);
            out.write(data);
            return true;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return false;
        } finally {
            IoUtil.close(out);
        }
    }

    public static void toZip(String sourceDir, String targetFile) {
        FileOutputStream zipStream = null;
        ZipOutputStream out = null;
        try {
            // 处理压缩文件
            zipStream = new FileOutputStream(targetFile);
            out = new ZipOutputStream(new BufferedOutputStream(zipStream));
            File[] sourceFiles = new File(sourceDir).listFiles();
            for (int i = 0; i < sourceFiles.length; i++) {
                BufferedInputStream in = new BufferedInputStream(new FileInputStream(sourceFiles[i]));
                try {
                    // 设置 ZipEntry 对象
                    out.putNextEntry(new ZipEntry(sourceFiles[i].getPath()));
                    int b;
                    while ((b = in.read()) != -1) {
                        // 从源文件读出，往压缩文件中写入
                        out.write(b);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                } finally {
                    IoUtil.close(in);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            if (out != null) {
                try {
                    out.closeEntry();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
            IoUtil.close(zipStream);
        }
    }

    /**
     * @param url 读取网络文件的完整地址 例如: http://127.0.0.1:8080/avatar/100000/abc.jpg
     * @return
     */
    public static byte[] readFile(URL url) throws Exception {

        // 打开链接
        HttpURLConnection conn = (HttpURLConnection) valiUrlSafe(url).openConnection();
        // 设置请求方式为"GET"
        conn.setRequestMethod(HttpMethod.GET.name());
        // 超时响应时间为5秒
        conn.setConnectTimeout(5 * 1000);
        InputStream inStream = null;
        ByteArrayOutputStream outStream = null;
        byte[] data = null;
        try {
            // 通过输入流获取图片数据
            inStream = conn.getInputStream();
            outStream = new ByteArrayOutputStream();
            // 创建一个Buffer字符串
            byte[] buffer = new byte[1024];
            // 每次读取的字符串长度，如果为-1，代表全部读取完毕
            int len = 0;
            // 使用一个输入流从buffer里把数据读取出来
            while ((len = inStream.read(buffer)) != -1) {
                // 用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
                outStream.write(buffer, 0, len);
            }
            // 得到图片的二进制数据，以二进制封装得到数据，具有通用性
            data = outStream.toByteArray();
        } catch (Exception ex) {
            log.debug("文件下载异常！url={};errorMsg={}", url, ex.getMessage());
        } finally {
            IoUtil.close(inStream);
            IoUtil.close(outStream);
        }
        return data;
    }

    public static InputStream readFileStream(URL url) throws Exception {
        // 打开链接
        HttpURLConnection conn = (HttpURLConnection) valiUrlSafe(url).openConnection();
        // 设置请求方式为"GET"
        conn.setRequestMethod(HttpMethod.GET.name());
        // 超时响应时间为5秒
        conn.setConnectTimeout(5 * 1000);
        try {
            // 通过输入流获取图片数据
            return conn.getInputStream();
        } catch (Exception ex) {
            log.debug("文件下载异常！url={};errorMsg={}", url, ex.getMessage());
            IoUtil.close(conn.getInputStream());
        }
        return null;
    }

    public static byte[] readByFile(File file) throws Exception {
        if (!file.exists()) {
            return null;
        }
        BufferedInputStream bis = null;
        byte[] b = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(file));
            // 读取文件内容
            b = new byte[bis.available()];
            bis.read(b);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        } finally {
            IoUtil.close(bis);
        }
        return b;
    }

    public static byte[] readFile(FSEntity entity) {
        BufferedInputStream in = null;
        ByteArrayOutputStream bos = null;
        try {
            bos = new ByteArrayOutputStream((int) entity.getSize());
            in = new BufferedInputStream(entity.getInputstream());

            int bufSize = 1024;
            byte[] buffer = new byte[bufSize];
            int len = 0;
            while (-1 != (len = in.read(buffer, 0, bufSize))) {
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        } catch (Exception ex) {
            String baseKeyPrex = AeyeFSManager.defaultBucket + "/";
            String baseKey = null;
            if (entity.getKeyId().startsWith(baseKeyPrex)) {
                baseKey = Base64Utils.urlEncode(entity.getKeyId().replace(baseKeyPrex, "").getBytes());
            } else {
                baseKey = Base64Utils.urlEncode(entity.getKeyId().getBytes());
            }
            log.error("文件下载失败：errMsg=" + ex.getMessage() + "；baseKey=" + baseKey + "；key=" + entity.getKeyId()
                    + "；name=" + entity.getName(), ex);
        } finally {
            IoUtil.close(bos);
            IoUtil.close(in);
        }
        return new byte[0];
    }

    public static byte[] readFile(InputStream inputStream) {
        BufferedInputStream in = null;
        ByteArrayOutputStream bos = null;
        try {
            bos = new ByteArrayOutputStream();
            in = new BufferedInputStream(inputStream);

            int bufSize = 1024;
            byte[] buffer = new byte[bufSize];
            int len = 0;
            while (-1 != (len = in.read(buffer, 0, bufSize))) {
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        } finally {
            IoUtil.close(bos);
            IoUtil.close(in);
        }
        return new byte[0];
    }

    public static String renameToUUID(String fileName) {
        if (StrUtil.isNotBlank(fileName)) {
            int index = fileName.lastIndexOf(".");
            if (index > -1) {
                String name = fileName.substring(index + 1);
                if (StrUtil.isNotBlank(name)) {
                    return AeyeIdGeneratorUtil.uuid() + "." + name;
                }
            }
        }
        return AeyeIdGeneratorUtil.uuid();
    }

    public static String renameSplitUUID(String fileName) {
        if (StrUtil.isNotBlank(fileName)) {
            int index = fileName.lastIndexOf(FILE_SPLIT_MARK);
            String name = null;
            StringBuilder rename = new StringBuilder();
            if (index > -1) {
                String suffix = fileName.substring(index + 1);
                name = fileName.substring(0, index);
                if (StrUtil.isNotBlank(name)) {
                    rename.append(name).append(RENAME_SPLIT_UUID_MARK);
                }
                rename.append(AeyeIdGeneratorUtil.uuid());
                if (StrUtil.isNotBlank(suffix)) {
                    rename.append(FILE_SPLIT_MARK).append(suffix);
                }
            } else {
                rename.append(fileName).append(RENAME_SPLIT_UUID_MARK).append(AeyeIdGeneratorUtil.uuid());
            }
            return rename.toString();
        }
        return AeyeIdGeneratorUtil.uuid();
    }

    /**
     * 计算字节单位
     * 
     * @param size
     * @return
     */
    public static String showByteSizeUnit(long size) {
        // 如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
        if (size < 1024) {
            return String.valueOf(size) + "B";
        } else {
            size = size / 1024;
        }
        // 如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
        // 因为还没有到达要使用另一个单位的时候
        // 接下去以此类推
        if (size < 1024) {
            return String.valueOf(size) + "KB";
        } else {
            size = size / 1024;
        }
        if (size < 1024) {
            // 因为如果以MB为单位的话，要保留最后1位小数，
            // 因此，把此数乘以100之后再取余
            size = size * 100;
            return String.valueOf((size / 100)) + "."
                    + String.valueOf((size % 100)) + "MB";
        } else {
            // 否则如果要以GB为单位的，先除于1024再作同样的处理
            size = size * 100 / 1024;
            return String.valueOf((size / 100)) + "."
                    + String.valueOf((size % 100)) + "GB";
        }
    }

    public static void main(String args[]) throws Exception {

    }

    public static String getValidDirectoryPath(String input, File parent, boolean allowNull) throws BusinessException {
        try {
            if (input == null || input.trim().length() == 0) {
                if (allowNull) {
                    return null;
                } else {
                    throw new BusinessException("Input directory path required: context=" + input);
                }
            } else {
                File dir = new File(input);
                if (!dir.exists()) {
                    throw new BusinessException("Invalid directory, does not exist: path=" + input);
                } else if (!dir.isDirectory()) {
                    throw new BusinessException("Invalid directory, not a directory: path=" + input);
                } else if (!parent.exists()) {
                    throw new BusinessException("Invalid directory, specified parent does not exist: parent=" + parent);
                } else if (!parent.isDirectory()) {
                    throw new BusinessException(
                            "Invalid directory, specified parent is not a directory:  parent=" + parent);
                } else if (!dir.getCanonicalPath().startsWith(parent.getCanonicalPath())) {
                    throw new BusinessException("Invalid directory, not inside specified parent: path=" + parent);
                } else {
                    String canonicalPath = dir.getCanonicalPath();
                    if (!canonicalPath.equals(input)) {
                        throw new BusinessException(
                                "Invalid directory name does not match the canonical path: path=" + input);
                    } else {
                        return canonicalPath;
                    }
                }
            }
        } catch (Exception e) {
            throw new BusinessException("Failure to validate directory path:" + input);
        }
    }

    public static String getValidFileExt(String input, List<String> allowedExtensions) throws Exception {
        for (String ext : allowedExtensions) {
            if (input.toLowerCase().endsWith(ext.toLowerCase())) {
                return input;
            }
        }
        throw new BusinessException("Invalid file name does not exist: input=" + input);
    }

    /**
     * 检查文件名是否包含恶意符号和空字节
     *
     * @param filename 要检查的文件名
     * @return 如果文件名安全返回true，否则返回false
     */
    public static String validFileSafe(String filename) throws BusinessException {
        if (StrUtil.isBlank(filename)) {
            return filename;
        }

        // 检查是否包含空字节(null byte)
        if (filename.contains("\0") || filename.contains("\\0") || filename.contains("%00")) {
            throw new BusinessException("检查是否包含空字节");
        }

        // 检查是否包含恶意路径遍历符号
        if (filename.contains("..") ||
                filename.contains("../") ||
                filename.contains("..\\") ||
                filename.contains("./") ||
                filename.contains(".\\")) {
            throw new BusinessException("检查是否包含恶意路径遍历符号");
        }

        // 检查是否包含危险字符
        String[] maliciousChars = { ";", "&", "|", "`", "$", "<", ">",
                "\"", "'", "\\", "*", "?", "{", "}",
                "[", "]", "(", ")", "~", "!", "#" };

        for (String maliciousChar : maliciousChars) {
            if (filename.contains(maliciousChar)) {
                throw new BusinessException("检查是否包含危险字符");
            }
        }

        // 检查是否以点或空格结尾（可能导致问题）
        if (filename.endsWith(".") || filename.endsWith(" ")) {
            throw new BusinessException("检查是否以点或空格结尾");
        }

        // 检查是否包含控制字符
        for (int i = 0; i < filename.length(); i++) {
            char c = filename.charAt(i);
            if (Character.isISOControl(c)) {
                throw new BusinessException("检查是否包含控制字符");
            }
        }

        return filename;
    }

    /**
     *
     * @param input
     * @param allowedExtensions
     * @param allowNull
     * @return
     * @throws BusinessException
     */

    public static String getValidFileName(String input, List<String> allowedExtensions, boolean allowNull)
            throws BusinessException {
        if (allowedExtensions != null && !allowedExtensions.isEmpty()) {
            String canonical = "";

            try {
                if (input == null || input.trim().length() == 0) {
                    if (allowNull) {
                        return null;
                    }

                    throw new BusinessException("Input required: input=" + input);
                }

                canonical = (new File(input)).getCanonicalFile().getName();
                File f = new File(canonical);
                String c = f.getCanonicalPath();
                String cpath = c.substring(c.lastIndexOf(File.separator) + 1);
                if (!input.equals(cpath)) {
                    throw new BusinessException(
                            "Invalid directory name does not match the canonical path: input=" + input);
                }
            } catch (IOException e) {
                throw new BusinessException("Invalid file name does not exist: input=" + input);
            }

            for (String ext : allowedExtensions) {
                if (input.toLowerCase().endsWith(ext.toLowerCase())) {
                    return canonical;
                }
            }

            throw new BusinessException(
                    "Invalid file name does not have valid extension ( " + allowedExtensions + "): input=" + input);
        } else {
            throw new BusinessException(
                    "getValidFileName called with an empty or null list of allowed Extensions, therefore no files can be uploaded");
        }
    }

    /**
     * Url 参数校验，禁用不需要的协议。仅仅允许http和https请求。可以防止类似于`file:///, gopher:// , ftp://`
     * 等引起的问题。
     */
    public static URL valiUrlSafe(URL url) throws BusinessException {
        if (url == null) {
            throw new BusinessException("Invalid URL: url=" + url);
        } else {
            String protocol = url.getProtocol();
            if (!"http".equals(protocol) && !"https".equals(protocol)) {
                throw new BusinessException("Invalid URL: url=" + url);
            } else {
                return url;
            }
        }
    }
}
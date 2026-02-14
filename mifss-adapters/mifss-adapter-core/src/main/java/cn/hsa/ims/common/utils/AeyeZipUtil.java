package cn.hsa.ims.common.utils;

import cn.hutool.core.io.IoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class AeyeZipUtil {

    private static Logger log = LoggerFactory.getLogger(AeyeZipUtil.class);

    /**
     * @param sourceFile 要压缩的文件/目录
     * @param zipFile    压缩文件存放地方
     */
    public static void zip(File sourceFile, File zipFile) {
        ZipOutputStream zipOut = null;
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(zipFile);
            zipOut = new ZipOutputStream(fileOutputStream);
            zipFile(zipOut, sourceFile, "");
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        } finally {
            try {
                if (zipOut != null) {
                    zipOut.flush();
                }
            } catch (Exception ex) {
            }
             IoUtil.close(zipOut);
        }
    }

    /**
     * @param fileByte 要压缩的文件字节
     * @param zipFile  压缩文件存放地方
     */
    public static void zip(Map<String, byte[]> fileByte, File zipFile) {
        ZipOutputStream zipOut = null;
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(zipFile);
            zipOut = new ZipOutputStream(fileOutputStream);
            for (String fileName : fileByte.keySet()) {
                zipFile(zipOut, fileByte.get(fileName), fileName, "");
            }
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        } finally {
            try {
                if (zipOut != null) {
                    zipOut.flush();
                }
            } catch (Exception ex) {
            }
            IoUtil.close(zipOut);
        }
    }

    /**
     *
     * @param sourceZip 待解压文件路径
     * @param destDir   解压到的路径
     */
    public static void unZip(String sourceZip, String destDir) {
        // 保证文件夹路径最后是"/"或者"\"
        char lastChar = destDir.charAt(destDir.length() - 1);
        if (lastChar != '/' && lastChar != '\\') {
            destDir += File.separator;
        }
        File destDirectory = new File(destDir);
        if (!destDirectory.exists()) {
            destDirectory.mkdirs();
        }

        /*
         * ant下的zip工具默认压缩编码为UTF-8编码，
         * 而winRAR软件压缩是用的windows默认的GBK或者GB2312编码
         * 所以解压缩时要制定编码格式
         */
        ZipFile zipFile = null;
        try {
            // 指定GBK编码
            zipFile = new ZipFile(sourceZip, Charset.forName("GBK"));
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String entryName = entry.getName();
                File entryDestination = new File(destDir, entryName);
                if (entry.isDirectory()) {
                    entryDestination.mkdirs();
                } else {
                    entryDestination.getParentFile().mkdirs();
                    InputStream in = null;
                    OutputStream out = null;
                    try {
                        in = zipFile.getInputStream(entry);
                        out = new FileOutputStream(entryDestination);
                        IoUtil.copy(in, out);
                    } finally {
                         IoUtil.close(in);
                         IoUtil.close(out);
                    }
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            if (zipFile != null) {
                try {
                    zipFile.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    public static void main(String[] args) {
        zip(new File("D:\\4.jpg"), new File("D:\\gg.zip"));
    }

    /**
     *
     * @param output   ZipOutputStream对象
     * @param fileByte 要压缩的文件字节
     * @param fileName 压缩的文件名
     * @param basePath zip包里面的条目根目录
     */
    private static void zipFile(ZipOutputStream output, byte[] fileByte, String fileName, String basePath) {
        // 压缩文件
        try {
            basePath = (basePath.length() == 0 ? "" : basePath + "/") + fileName;
            output.putNextEntry(new ZipEntry(basePath));
            output.write(fileByte);
        } catch (Exception e) {
        } finally {
            try {
                output.closeEntry();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    /**
     *
     * @param output   ZipOutputStream对象
     * @param file     要压缩的文件或文件夹
     * @param basePath zip包里面的条目根目录
     */
    private static void zipFile(ZipOutputStream output, File file, String basePath) {
        FileInputStream input = null;
        // 文件为目录
        if (file.isDirectory()) {
            // 得到当前目录里面的文件列表
            File list[] = file.listFiles();
            basePath = basePath + (basePath.length() == 0 ? "" : "/")
                    + file.getName();
            // 循环递归压缩每个文件
            for (File f : list) {
                zipFile(output, f, basePath);
            }
        } else {
            // 压缩文件
            try {
                basePath = (basePath.length() == 0 ? "" : basePath + "/")
                        + file.getName();
                output.putNextEntry(new ZipEntry(basePath));
                input = new FileInputStream(file);
                int readLen = 0;
                byte[] buffer = new byte[1024 * 8];
                while ((readLen = input.read(buffer, 0, 1024 * 8)) != -1) {
                    output.write(buffer, 0, readLen);
                }
            } catch (Exception e) {
            } finally {
                try {
                    output.closeEntry();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
                 IoUtil.close(input);
            }
        }
    }
}

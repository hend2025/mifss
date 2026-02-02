//package com.aeye.mifss.common.utils;
//
//import cn.hsa.hsaf.core.framework.web.exception.AppException;
//import cn.hsa.hsaf.core.framework.web.exception.BusinessException;
//import cn.hsa.hsaf.core.fsstore.FSAccessControlList;
//import cn.hsa.hsaf.core.fsstore.FSEntity;
//import cn.hsa.hsaf.core.fsstore.FSManager;
//import cn.hsa.ims.common.dto.AeyeFSEntity;
//import cn.hsa.ims.common.exception.HSAFExceptionCode;
//import cn.hutool.core.util.StrUtil;
//import java.io.ByteArrayInputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.InputStream;
//import java.net.URL;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Stream;
//import org.apache.tomcat.util.http.fileupload.IOUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.core.env.Environment;
//
//public class AeyeFSManager {
//    private static final Logger log = LoggerFactory.getLogger(AeyeFSManager.class);
//    public static final String FSSTORE_TYPE_FDFS = "fdfs";
//    public static String defaultBucket;
//    public static String fssStoreUrl;
//    public static String fsstoreType;
//    public static String defaultOssDir;
//    public static String allowFormat;
//    public static List<String> allowFormats = new ArrayList();
//    private static int maxFileNameLength = 100;
//    private static int maxZipLength = 10000;
//    private static long maxZipSize = 1073741824L;
//    private static String DATETIME_DATE_KEY = "[date]";
//    private static String DATETIME_MONTH_KEY = "[month]";
//    private static String DATETIME_DAY_KEY = "[day]";
//
//    public static AeyeFSEntity putObject(File uploadFile) {
//        return putObject("", uploadFile);
//    }
//
//    public static AeyeFSEntity putObject(String fileName, File uploadFile) {
//        AeyeFSEntity aeyeFSEntity = null;
//        InputStream inputStream = null;
//
//        try {
//            if (AeyeStringUtils.isBlank(fileName)) {
//                fileName = uploadFile.getName();
//            }
//
//            InputStream var6 = new FileInputStream(uploadFile);
//            AeyeFSEntity entity = new AeyeFSEntity();
//            entity.setName(AeyeFileReadWriteUtil.renameSplitUUID(fileName));
//            entity.setUpdatedate(new Date());
//            entity.setInputstream(var6);
//            entity.setSize(uploadFile.length());
//            aeyeFSEntity = putObject(entity);
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//        }
//
//        return aeyeFSEntity;
//    }
//
//    public static AeyeFSEntity putObject(URL url) {
//        AeyeFSEntity aeyeFSEntity = null;
//
//        try {
//            aeyeFSEntity = putObject(url.getFile(), AeyeFileReadWriteUtil.readFile(url));
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//        }
//
//        return aeyeFSEntity;
//    }
//
//    public static AeyeFSEntity putObject(byte[] uploadFile) {
//        return putObject("", uploadFile);
//    }
//
//    public static AeyeFSEntity putObject(String fileName, byte[] uploadFile) {
//        return putObject(defaultBucket, fileName, uploadFile);
//    }
//
//    public static AeyeFSEntity putObject(String bucket, String fileName, byte[] uploadFile) {
//        AeyeFSEntity aeyeFSEntity = null;
//
//        try {
//            AeyeFSEntity entity = new AeyeFSEntity();
//            entity.setName(AeyeFileReadWriteUtil.renameSplitUUID(fileName));
//            entity.setUpdatedate(new Date());
//            entity.setInputstream(new ByteArrayInputStream(uploadFile));
//            entity.setSize((long)uploadFile.length);
//            aeyeFSEntity = putObject(bucket, entity);
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//        }
//
//        return aeyeFSEntity;
//    }
//
//    public static AeyeFSEntity putObject(AeyeFSEntity aeyeFSEntity) throws Exception {
//        return putObject(defaultBucket, aeyeFSEntity);
//    }
//
//    public static AeyeFSEntity putObject(String bucket, AeyeFSEntity fsEntity) throws Exception {
//        try {
//            if (!checkFormats(fsEntity.getName())) {
//                throw new BusinessException(HSAFExceptionCode.HDF_MODULE_OSS_05.getCode(), HSAFExceptionCode.HDF_MODULE_OSS_05.getMsg() + "，文件名：" + fsEntity.getName());
//            }
//
//            if (fsEntity.getInputstream() == null) {
//                throw new AppException(HSAFExceptionCode.HDF_MODULE_OSS_03.getCode(), HSAFExceptionCode.HDF_MODULE_OSS_03.getMsg());
//            }
//
//            String fileName = originalFileName(fsEntity.getName());
//            if (StrUtil.isNotBlank(fileName) && fileName.length() > maxFileNameLength) {
//                throw new AppException(HSAFExceptionCode.HDF_MODULE_OSS_04.getCode(), HSAFExceptionCode.HDF_MODULE_OSS_04.getMsg() + "【" + maxFileNameLength + "】，文件名：" + fsEntity.getName());
//            }
//
//            FSManager fsManager = (FSManager)AeyeSpringContextUtils.getBean(FSManager.class);
//            fsEntity.setName(defaultOssDir + fsEntity.getName());
//            LocalDateTime nowTime = LocalDateTime.now();
//            if (fsEntity.getName().contains(DATETIME_DAY_KEY)) {
//                fsEntity.setName(fsEntity.getName().replace(DATETIME_DAY_KEY, AeyeJdk8DateUtil.DAY.format(nowTime)));
//            }
//
//            if (fsEntity.getName().contains(DATETIME_DATE_KEY)) {
//                fsEntity.setName(fsEntity.getName().replace(DATETIME_DATE_KEY, AeyeJdk8DateUtil.DATE.format(nowTime)));
//            }
//
//            if (fsEntity.getName().contains(DATETIME_MONTH_KEY)) {
//                fsEntity.setName(fsEntity.getName().replace(DATETIME_MONTH_KEY, AeyeJdk8DateUtil.MONTH.format(nowTime)));
//            }
//
//            fsEntity.setAcl(FSAccessControlList.Private);
//            fsManager.putObject(AeyeStringUtils.isNotBlank(bucket) ? bucket : defaultBucket, fsEntity);
//            fsEntity.setKeyId(Base64Utils.urlEncode(fsEntity.getKeyId().getBytes()));
//        } finally {
//            if (null != fsEntity) {
//                IOUtils.closeQuietly(fsEntity.getInputstream());
//            }
//
//        }
//
//        return fsEntity;
//    }
//
//    public static boolean exist(String key) throws Exception {
//        return exist(defaultBucket, key);
//    }
//
//    public static boolean exist(String bucket, String key) throws Exception {
//        AeyeFSEntity entity = null;
//
//        boolean var3;
//        try {
//            entity = getObjectOnlyInfo(bucket, key);
//            if (entity == null) {
//                var3 = false;
//                return var3;
//            }
//
//            var3 = true;
//        } catch (Exception ex) {
//            throw ex;
//        } finally {
//            if (entity != null) {
//                IOUtils.closeQuietly(entity.getInputstream());
//            }
//
//        }
//
//        return var3;
//    }
//
//    public static boolean deleteObject(String keyId) {
//        return deleteObject(defaultBucket, keyId);
//    }
//
//    public static boolean deleteObject(String bucket, String keyId) {
//        if (AeyeStringUtils.isBlank(keyId)) {
//            return false;
//        } else {
//            FSManager fsManager = (FSManager)AeyeSpringContextUtils.getBean(FSManager.class);
//            return fsManager.deleteObject(AeyeStringUtils.isNotBlank(bucket) ? bucket : defaultBucket, new String(Base64Utils.urlDecode(keyId)));
//        }
//    }
//
//    public static byte[] getObjectData(String keyId) {
//        return getObjectData(defaultBucket, keyId);
//    }
//
//    public static byte[] getObjectData(String bucket, String keyId) {
//        AeyeFSEntity fsEntity = getObjectEntity(bucket, keyId);
//        return fsEntity == null ? null : fsEntity.getData();
//    }
//
//    public static AeyeFSEntity getObjectEntity(String keyId) {
//        AeyeFSEntity fsEntity = getObjectEntity(defaultBucket, keyId);
//        return fsEntity;
//    }
//
//    public static AeyeFSEntity getObjectEntity(String bucket, String keyId) {
//        AeyeFSEntity fsEntity = getObjectEntity(bucket, keyId, true);
//        return fsEntity;
//    }
//
//    public static AeyeFSEntity getObjectOnlyInfo(String keyId) {
//        AeyeFSEntity fsEntity = getObjectOnlyInfo(defaultBucket, keyId);
//        return fsEntity;
//    }
//
//    public static AeyeFSEntity getObjectOnlyInfo(String bucket, String keyId) {
//        AeyeFSEntity fsEntity = getObjectEntity(bucket, keyId, false);
//        return fsEntity;
//    }
//
//    public static String originalFileName(String fileName) {
//        int index = -1;
//        if ((index = fileName.indexOf("@@")) > 0) {
//            String prefix = fileName.substring(0, index);
//            String suffix = "";
//            int suffixIndex = fileName.lastIndexOf(".");
//            if (suffixIndex > -1) {
//                suffix = fileName.substring(suffixIndex, fileName.length());
//            }
//
//            fileName = prefix + suffix;
//        }
//
//        return renameFileName(fileName);
//    }
//
//    public static String renameFileName(String fileName) {
//        int index = -1;
//        if ((index = fileName.lastIndexOf("/")) > 0) {
//            fileName = fileName.substring(index + 1, fileName.length());
//        }
//
//        return fileName;
//    }
//
//    public static AeyeFSEntity getObjectEntity(String bucket, String keyId, boolean isLoadData) {
//        FSEntity fsEntity = null;
//        AeyeFSEntity aeyeFSEntity = null;
//
//        try {
//            if ("fdfs".equals(fsstoreType)) {
//                fsEntity = new AeyeFSEntity();
//                fsEntity.setName(new String(Base64Utils.urlDecode(keyId)));
//                fsEntity.setInputstream(AeyeFileReadWriteUtil.readFileStream(new URL(fssStoreUrl + "/" + fsEntity.getName())));
//            } else {
//                FSManager fsManager = (FSManager)AeyeSpringContextUtils.getBean(FSManager.class);
//                fsEntity = fsManager.getObject(AeyeStringUtils.isNotBlank(bucket) ? bucket : defaultBucket, new String(Base64Utils.urlDecode(keyId)));
//            }
//
//            if (fsEntity != null) {
//                aeyeFSEntity = new AeyeFSEntity();
//                int index = -1;
//                if ((index = fsEntity.getName().indexOf("@@")) > 0) {
//                    String prefix = fsEntity.getName().substring(0, index);
//                    String suffix = "";
//                    int suffixIndex = fsEntity.getName().lastIndexOf(".");
//                    if (suffixIndex > -1) {
//                        suffix = fsEntity.getName().substring(suffixIndex, fsEntity.getName().length());
//                    }
//
//                    fsEntity.setName(prefix + suffix);
//                }
//
//                index = -1;
//                if ((index = fsEntity.getName().lastIndexOf("/")) > 0) {
//                    fsEntity.setName(fsEntity.getName().substring(index + 1, fsEntity.getName().length()));
//                }
//
//                if (isLoadData) {
//                    aeyeFSEntity.setData(AeyeFileReadWriteUtil.readFile(fsEntity));
//                    fsEntity.setSize((long)aeyeFSEntity.getData().length);
//                }
//
//                fsEntity.setKeyId(keyId);
//            }
//        } catch (Exception ex) {
//            log.error("文件下载异常:bucket=[{}]-key=[{}]-错误信息={}", new Object[]{bucket, keyId, ex.getMessage()});
//        } finally {
//            if (isLoadData && fsEntity != null) {
//                IOUtils.closeQuietly(fsEntity.getInputstream());
//                fsEntity.setInputstream((InputStream)null);
//            }
//
//            if (null != fsEntity && aeyeFSEntity != null) {
//                aeyeFSEntity.setSize(fsEntity.getSize());
//                aeyeFSEntity.setInputstream(fsEntity.getInputstream());
//                aeyeFSEntity.setName(fsEntity.getName());
//                aeyeFSEntity.setContentType(fsEntity.getContentType());
//                aeyeFSEntity.setAcl(fsEntity.getAcl());
//                aeyeFSEntity.setKeyId(fsEntity.getKeyId());
//                aeyeFSEntity.setUpdatedate(fsEntity.getUpdatedate());
//            }
//
//        }
//
//        return aeyeFSEntity;
//    }
//
//    public static byte[] packZipFiles(String zipFileName, Map<String, String> fileMapping) throws Exception {
//        if (fileMapping.size() > maxZipLength) {
//            throw new BusinessException(HSAFExceptionCode.HDF_MODULE_OSS_02.getCode(), HSAFExceptionCode.HDF_MODULE_OSS_02.getMsg() + "，当前=" + fileMapping.size() + "；最大限制=" + maxZipLength);
//        } else {
//            StringBuilder logMsg = new StringBuilder();
//            String zipFileNameLocal = AeyeFileReadWriteUtil.getValidFileExt(AeyeFileReadWriteUtil.SYS_TMP_DIR + (AeyeFileReadWriteUtil.SYS_TMP_DIR.endsWith(File.separator) ? "" : File.separator) + AeyeFileReadWriteUtil.renameSplitUUID(zipFileName), Collections.singletonList(".zip"));
//            File zipFile = new File(zipFileNameLocal);
//
//            byte[] var17;
//            try {
//                buildLogMsg("文件服务器批量下载开始...", logMsg);
//                Map<String, byte[]> dataList = new HashMap(AeyeMapUtil.intMapSize(fileMapping.size()));
//                long zipSize = 0L;
//
//                for(String keyId : fileMapping.keySet()) {
//                    byte[] data = getObjectData(keyId);
//                    if (data != null) {
//                        zipSize += (long)data.length;
//                        if (zipSize > maxZipSize) {
//                            throw new BusinessException(HSAFExceptionCode.HDF_MODULE_OSS_02.getCode(), HSAFExceptionCode.HDF_MODULE_OSS_02.getMsg() + "，当前=" + zipSize + "；最大限制=" + maxZipSize);
//                        }
//
//                        dataList.put(fileMapping.get(keyId), data);
//                    } else {
//                        buildLogMsg(String.format("文件服务器下载失败：%s", keyId), logMsg);
//                    }
//                }
//
//                buildLogMsg(String.format("批量下载总数量：%s", dataList.size()), logMsg);
//                buildLogMsg(String.format("临时zip存放目录：%s", zipFile.getPath()), logMsg);
//                dataList.put("logMsg.log", logMsg.toString().getBytes());
//                AeyeZipUtil.zip(dataList, zipFile);
//                if (!zipFile.exists()) {
//                    return null;
//                }
//
//                byte[] data = AeyeFileReadWriteUtil.readByFile(zipFile);
//                var17 = data;
//            } catch (Exception ex) {
//                log.error(ex.getMessage(), ex);
//                return null;
//            } finally {
//                zipFile.deleteOnExit();
//            }
//
//            return var17;
//        }
//    }
//
//    private static boolean checkFormats(String fileFullName) {
//        String suffix = fileFullName.substring(fileFullName.lastIndexOf(".") + 1).toLowerCase();
//        if (suffix.length() == fileFullName.length()) {
//            return true;
//        } else {
//            Stream var10000 = allowFormats.stream();
//            suffix.getClass();
//            return var10000.anyMatch(suffix::contains);
//        }
//    }
//
//    private static void buildLogMsg(String msg, StringBuilder buffer) {
//        buffer.append(AeyeJdk8DateUtil.DATETIME_MINUTE_CN.format(AeyeJdk8DateUtil.convertToLocalDateTime(new Date(System.currentTimeMillis())))).append(":").append(msg).append("\r\n");
//    }
//
//    static {
//        Environment env = (Environment)AeyeSpringContextUtils.getBean(Environment.class);
//        defaultBucket = env.getProperty("fsstore.bucket");
//        fsstoreType = env.getProperty("fsstore.type");
//        fssStoreUrl = env.getProperty("fsstore.url");
//        if (StrUtil.isNotBlank(fssStoreUrl)) {
//            if (!fssStoreUrl.endsWith("/")) {
//                fssStoreUrl = fssStoreUrl + "/" + defaultBucket;
//            } else {
//                fssStoreUrl = fssStoreUrl + defaultBucket;
//            }
//        }
//
//        defaultOssDir = env.getProperty("fsstore.defaultOssDir");
//        allowFormat = env.getProperty("spring.application.multipart.allowFormat");
//        if (AeyeStringUtils.isNotBlank(defaultOssDir)) {
//            defaultOssDir = defaultOssDir.endsWith("/") ? defaultOssDir : defaultOssDir + "/";
//        } else {
//            defaultOssDir = "ZHY/[month]/";
//        }
//
//        if (AeyeStringUtils.isBlank(allowFormat)) {
//            allowFormat = "doc,docx,xls,xlsx,ppt,pptx,pdf,jpg,gif,jpeg,png,txt,log,wmv,mp4,zip,rar,apk";
//        }
//
//        allowFormats.addAll(Arrays.asList(allowFormat.split(",")));
//    }
//
//}

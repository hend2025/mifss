package cn.hsa.ims.common.utils;

import cn.hsa.hsaf.core.framework.web.exception.AppException;
import cn.hsa.hsaf.core.framework.web.exception.BusinessException;
import cn.hsa.hsaf.core.fsstore.FSAccessControlList;
import cn.hsa.hsaf.core.fsstore.FSEntity;
import cn.hsa.hsaf.core.fsstore.FSManager;
import cn.hsa.ims.common.dto.AeyeFSEntity;
import cn.hsa.ims.common.exception.HSAFExceptionCode;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.core.env.Environment;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
public class AeyeFSManager {

    public static final String FSSTORE_TYPE_FDFS = "fdfs";
    public static String defaultBucket;
    public static String fssStoreUrl;
    public static String fsstoreType;
    public static String defaultOssDir;
    public static String allowFormat;
    public static List<String> allowFormats =new ArrayList<>();

    private static int maxFileNameLength = 100;
    private static int maxZipLength = 10000;
    private static long maxZipSize = 1073741824L;
    /**
     * 年月日
     */
    private static String DATETIME_DATE_KEY = "[date]";
    /**
     * 年月
     */
    private static String DATETIME_MONTH_KEY = "[month]";
    /**
     * 单数字天
     */
    private static String DATETIME_DAY_KEY = "[day]";

    static{
        Environment env = AeyeSpringContextUtils.getBean(Environment.class);
        defaultBucket = env.getProperty("fsstore.bucket");
        fsstoreType = env.getProperty("fsstore.type");
        fssStoreUrl = env.getProperty("fsstore.url");
        if(StrUtil.isNotBlank(fssStoreUrl)){
            if(!fssStoreUrl.endsWith(AeyeFileReadWriteUtil.DIR_SPLIT_MARK)){
                fssStoreUrl = fssStoreUrl + AeyeFileReadWriteUtil.DIR_SPLIT_MARK + defaultBucket;
            }else{
                fssStoreUrl = fssStoreUrl + defaultBucket;
            }
        }
        defaultOssDir = env.getProperty("fsstore.defaultOssDir");
        allowFormat = env.getProperty("spring.application.multipart.allowFormat");
        if(AeyeStringUtils.isNotBlank(defaultOssDir)){
            // 检查必须'/'结束
            defaultOssDir = defaultOssDir.endsWith(AeyeFileReadWriteUtil.DIR_SPLIT_MARK) ? defaultOssDir : defaultOssDir + AeyeFileReadWriteUtil.DIR_SPLIT_MARK;
        }else{
            // 默认以ZHY为目录名按月存储,必须'/'结束
            defaultOssDir = "ZHY/[month]/";
        }
        if(AeyeStringUtils.isBlank(allowFormat)){
            allowFormat = "doc,docx,xls,xlsx,ppt,pptx,pdf,jpg,gif,jpeg,png,txt,log,wmv,mp4,zip,rar,apk";
        }
        allowFormats.addAll(Arrays.asList(allowFormat.split(",")));
    }

    public static AeyeFSEntity putObject(File uploadFile){
        return putObject("", uploadFile);
    }

    public static AeyeFSEntity putObject(String fileName, File uploadFile){
        AeyeFSEntity aeyeFSEntity = null;
        InputStream inputStream = null;
        try {
            if(AeyeStringUtils.isBlank(fileName)){
                fileName = uploadFile.getName();
            }
            inputStream = new FileInputStream(uploadFile);
            AeyeFSEntity entity = new AeyeFSEntity();
            entity.setName(AeyeFileReadWriteUtil.renameSplitUUID(fileName));
            entity.setUpdatedate(new Date());
            entity.setInputstream(inputStream);
            entity.setSize(uploadFile.length());
            aeyeFSEntity = putObject(entity);
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return aeyeFSEntity;
    }

    public static AeyeFSEntity putObject(URL url){
        AeyeFSEntity aeyeFSEntity = null;
        try {
            aeyeFSEntity = putObject(url.getFile(), AeyeFileReadWriteUtil.readFile(url));
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return aeyeFSEntity;
    }

    public static AeyeFSEntity putObject(byte[] uploadFile){
        return putObject("", uploadFile);
    }

    public static AeyeFSEntity putObject(String fileName, byte[] uploadFile){
        return putObject(defaultBucket, fileName, uploadFile);
    }

    public static AeyeFSEntity putObject(String bucket, String fileName, byte[] uploadFile){
        AeyeFSEntity aeyeFSEntity = null;
        try {
            AeyeFSEntity entity = new AeyeFSEntity();
            entity.setName(AeyeFileReadWriteUtil.renameSplitUUID(fileName));
            entity.setUpdatedate(new Date());
            entity.setInputstream(new ByteArrayInputStream(uploadFile));
            entity.setSize(uploadFile.length);
            aeyeFSEntity = putObject(bucket, entity);
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return aeyeFSEntity;
    }

    public static AeyeFSEntity putObject(AeyeFSEntity aeyeFSEntity)throws Exception{
        return putObject(defaultBucket, aeyeFSEntity);
    }

    public static AeyeFSEntity putObject(String bucket, AeyeFSEntity fsEntity) throws Exception{

        try {
            // 格式校验
            if(!checkFormats(fsEntity.getName())){
                throw new BusinessException(HSAFExceptionCode.HDF_MODULE_OSS_05.getCode(), HSAFExceptionCode.HDF_MODULE_OSS_05.getMsg() + "，文件名："+fsEntity.getName());
            }

            if(fsEntity.getInputstream() == null){
                throw new AppException(HSAFExceptionCode.HDF_MODULE_OSS_03.getCode(), HSAFExceptionCode.HDF_MODULE_OSS_03.getMsg());
            }
            // 长度校验
            String fileName = originalFileName(fsEntity.getName());
            if(StrUtil.isNotBlank(fileName) && fileName.length() > maxFileNameLength){
                throw new AppException(HSAFExceptionCode.HDF_MODULE_OSS_04.getCode(), HSAFExceptionCode.HDF_MODULE_OSS_04.getMsg() + "【"+maxFileNameLength+"】，文件名："+fsEntity.getName());
            }
            FSManager fsManager = AeyeSpringContextUtils.getBean(FSManager.class);
            // 全局前缀
            fsEntity.setName(defaultOssDir + fsEntity.getName());
            LocalDateTime nowTime = LocalDateTime.now();
            // 占位符表达式检查
            if(fsEntity.getName().contains(DATETIME_DAY_KEY)){
                fsEntity.setName(fsEntity.getName().replace(DATETIME_DAY_KEY, AeyeJdk8DateUtil.DAY.format(nowTime)));
            }
            if(fsEntity.getName().contains(DATETIME_DATE_KEY)){
                fsEntity.setName(fsEntity.getName().replace(DATETIME_DATE_KEY, AeyeJdk8DateUtil.DATE.format(nowTime)));
            }
            if(fsEntity.getName().contains(DATETIME_MONTH_KEY)){
                fsEntity.setName(fsEntity.getName().replace(DATETIME_MONTH_KEY, AeyeJdk8DateUtil.MONTH.format(nowTime)));
            }
            fsEntity.setAcl(FSAccessControlList.Private);
            fsManager.putObject(AeyeStringUtils.isNotBlank(bucket) ? bucket : defaultBucket, fsEntity);
            fsEntity.setKeyId(Base64Utils.urlEncode(fsEntity.getKeyId().getBytes()));
        }finally {
            //关闭流
            if(null != fsEntity){
                IOUtils.closeQuietly(fsEntity.getInputstream());
            }
        }
        return fsEntity;
    }

    public static boolean exist(String key) throws  Exception{
        return exist(defaultBucket, key);
    }

    public static boolean exist(String bucket, String key) throws Exception{
        AeyeFSEntity entity = null;
        try{
            entity = getObjectOnlyInfo(bucket, key);
            if(entity != null){
                return true;
            }else{
                return false;
            }
        }catch (Exception ex){
            throw ex;
        }finally {
            if(entity != null){
                IOUtils.closeQuietly(entity.getInputstream());
            }
        }
    }

    /**
     * 删除文件
     * @param keyId
     * @return
     */
    public static boolean deleteObject(String keyId){
        return deleteObject(defaultBucket, keyId);
    }

    /**
     * 删除文件
     * @param bucket
     * @param keyId
     * @return
     */
    public static boolean deleteObject(String bucket, String keyId){
        if(AeyeStringUtils.isBlank(keyId)){
            return false;
        }
        FSManager fsManager = AeyeSpringContextUtils.getBean(FSManager.class);
        return fsManager.deleteObject(AeyeStringUtils.isNotBlank(bucket) ? bucket : defaultBucket, new String(Base64Utils.urlDecode(keyId)));
    }

    /**
     * 返回文件二进制数组
     * @param keyId
     * @return
     */
    public static byte[] getObjectData(String keyId){
        return getObjectData(defaultBucket, keyId);
    }

    /**
     * 返回文件二进制数组
     * @param bucket
     * @param keyId
     * @return
     */
    public static byte[] getObjectData(String bucket, String keyId){
        AeyeFSEntity fsEntity = getObjectEntity(bucket, keyId);
        return fsEntity == null ? null : fsEntity.getData();
    }

    /**
     * 返回对象信息及文件二进制数组
     * @param keyId
     * @return
     */
    public static AeyeFSEntity getObjectEntity(String keyId){
        AeyeFSEntity fsEntity = getObjectEntity(defaultBucket, keyId);
        return fsEntity;
    }

    /**
     * 返回对象信息及文件二进制数组
     * @param keyId
     * @return
     */
    public static AeyeFSEntity getObjectEntity(String bucket, String keyId){
        AeyeFSEntity fsEntity = getObjectEntity(bucket, keyId, true);
        return fsEntity;
    }

    /**
     * 返回对象信息
     * @param keyId
     * @return
     */
    public static AeyeFSEntity getObjectOnlyInfo(String keyId){
        AeyeFSEntity fsEntity = getObjectOnlyInfo(defaultBucket, keyId);
        return fsEntity;
    }

    /**
     * 返回对象信息
     * @param keyId
     * @return
     */
    public static AeyeFSEntity getObjectOnlyInfo(String bucket, String keyId){
        AeyeFSEntity fsEntity = getObjectEntity(bucket, keyId, false);
        return fsEntity;
    }

    /**
     * 获取原始文件名-去除文件目录-去除文件名的uuid
     * @return
     */
    public static String originalFileName(String fileName){
        int index = -1;
        if((index = fileName.indexOf(AeyeFileReadWriteUtil.RENAME_SPLIT_UUID_MARK)) > 0){
            // 判断分割符，取出原始文件名
            String prefix = fileName.substring(0, index);
            String suffix = "";
            int suffixIndex = fileName.lastIndexOf(".");
            if(suffixIndex > -1){
                suffix = fileName.substring(suffixIndex, fileName.length());
            }
            fileName = prefix + suffix;
        }

        return renameFileName(fileName);
    }

    /**
     * 获取重命名后的文件名-去除文件目录
     * @param fileName
     * @return
     */
    public static String renameFileName(String fileName){
        int index = -1;
        if((index = fileName.lastIndexOf(AeyeFileReadWriteUtil.DIR_SPLIT_MARK)) > 0){
            // 判断是否带路径/之类的，取最后的文件名
            fileName = fileName.substring(index + 1, fileName.length());
        }
        return fileName;
    }

    public static AeyeFSEntity getObjectEntity(String bucket, String keyId, boolean isLoadData){
        FSEntity fsEntity = null;
        AeyeFSEntity aeyeFSEntity = null;
        try {
            if(FSSTORE_TYPE_FDFS.equals(fsstoreType)){
                fsEntity = new AeyeFSEntity();
                fsEntity.setName(new String(Base64Utils.urlDecode(keyId)));
                fsEntity.setInputstream(AeyeFileReadWriteUtil.readFileStream(new URL(fssStoreUrl + AeyeFileReadWriteUtil.DIR_SPLIT_MARK + fsEntity.getName())));
            }else{
                FSManager fsManager = AeyeSpringContextUtils.getBean(FSManager.class);

                fsEntity = fsManager.getObject(AeyeStringUtils.isNotBlank(bucket) ? bucket : defaultBucket, new String(Base64Utils.urlDecode(keyId)));
            }

            if(fsEntity != null){
                aeyeFSEntity = new AeyeFSEntity();
                int index = -1;
                if((index = fsEntity.getName().indexOf(AeyeFileReadWriteUtil.RENAME_SPLIT_UUID_MARK)) > 0){
                    // 判断分割符，取出原始文件名
                    String prefix = fsEntity.getName().substring(0, index);
                    String suffix = "";
                    int suffixIndex = fsEntity.getName().lastIndexOf(".");
                    if(suffixIndex > -1){
                        suffix = fsEntity.getName().substring(suffixIndex, fsEntity.getName().length());
                    }
                    fsEntity.setName(prefix + suffix);
                }
                index = -1;
                if((index = fsEntity.getName().lastIndexOf(AeyeFileReadWriteUtil.DIR_SPLIT_MARK)) > 0){
                    // 判断是否带路径/之类的，取最后的文件名
                    fsEntity.setName(fsEntity.getName().substring(index + 1, fsEntity.getName().length()));
                }
                if(isLoadData) {
                    aeyeFSEntity.setData(AeyeFileReadWriteUtil.readFile(fsEntity));
                    fsEntity.setSize(aeyeFSEntity.getData().length);
                }
                fsEntity.setKeyId(keyId);
            }
        }catch (Exception ex){
            log.error("文件下载异常:bucket=[{}]-key=[{}]-错误信息={}", bucket, keyId, ex.getMessage());
        }finally {
            // 关闭流-isLoadData已经加载数据了，这里马上关闭流
            if(isLoadData && fsEntity != null){
                IOUtils.closeQuietly(fsEntity.getInputstream());
                fsEntity.setInputstream(null);
            }
            if(null != fsEntity && aeyeFSEntity != null){
                aeyeFSEntity.setSize(fsEntity.getSize());
                aeyeFSEntity.setInputstream(fsEntity.getInputstream());
                aeyeFSEntity.setName(fsEntity.getName());
                aeyeFSEntity.setContentType(fsEntity.getContentType());
                aeyeFSEntity.setAcl(fsEntity.getAcl());
                aeyeFSEntity.setKeyId(fsEntity.getKeyId());
                aeyeFSEntity.setUpdatedate(fsEntity.getUpdatedate());
            }
        }
        return aeyeFSEntity;
    }

    /**
     * 打包下载一批文件，参数key=原始文件名，value=映射文件名
     * @param zipFileName
     * @param fileMapping
     * @return
     */
    public static byte[] packZipFiles(String zipFileName, Map<String, String> fileMapping) throws Exception{
        if(fileMapping.size() > maxZipLength){
            throw new BusinessException(HSAFExceptionCode.HDF_MODULE_OSS_02.getCode(), HSAFExceptionCode.HDF_MODULE_OSS_02.getMsg() + "，当前="+fileMapping.size()+"；最大限制="+maxZipLength);
        }
        StringBuilder logMsg = new StringBuilder();
        String zipFileNameLocal = AeyeFileReadWriteUtil.getValidFileExt(AeyeFileReadWriteUtil.SYS_TMP_DIR +
                (AeyeFileReadWriteUtil.SYS_TMP_DIR.endsWith(File.separator)?"":File.separator) +
                AeyeFileReadWriteUtil.renameSplitUUID(zipFileName), Collections.singletonList(".zip"));

        File zipFile = new File(zipFileNameLocal);

        try{
            buildLogMsg("文件服务器批量下载开始...", logMsg);
            Map<String, byte[]> dataList = new HashMap<>(AeyeMapUtil.intMapSize(fileMapping.size()));
            long zipSize = 0L;
            for(String keyId : fileMapping.keySet()){
                byte[] data = AeyeFSManager.getObjectData(keyId);
                if(data != null){
                    zipSize += data.length;
                    if(zipSize > maxZipSize){
                        throw new BusinessException(HSAFExceptionCode.HDF_MODULE_OSS_02.getCode(), HSAFExceptionCode.HDF_MODULE_OSS_02.getMsg() + "，当前="+zipSize+"；最大限制="+maxZipSize);
                    }
                    dataList.put(fileMapping.get(keyId), data);
                }else{
                    buildLogMsg(String.format("文件服务器下载失败：%s", keyId), logMsg);
                }
            }
            buildLogMsg(String.format("批量下载总数量：%s", dataList.size()), logMsg);
            buildLogMsg(String.format("临时zip存放目录：%s", zipFile.getPath()), logMsg);

            // 压缩日志文件到zip包
            dataList.put("logMsg.log", logMsg.toString().getBytes());

            // 压缩图片文件到zip包
            AeyeZipUtil.zip(dataList, zipFile);
            if(zipFile.exists()){
                byte[] data = AeyeFileReadWriteUtil.readByFile(zipFile);
                return data;
            }
        }catch (Exception ex){
            log.error(ex.getMessage(), ex);
        }finally {
            zipFile.deleteOnExit();
        }
        return null;
    }

    private static boolean checkFormats(String fileFullName){
        String suffix = fileFullName.substring(fileFullName.lastIndexOf(".") + 1).toLowerCase();
        if(suffix.length() == fileFullName.length()){
            return true;
        }
        return allowFormats.stream().anyMatch(suffix::contains);
    }

    private static void buildLogMsg(String msg, StringBuilder buffer){
        buffer.append(AeyeJdk8DateUtil.DATETIME_MINUTE_CN.format(AeyeJdk8DateUtil.convertToLocalDateTime(new Date(System.currentTimeMillis())))).append(":").append(msg).append("\r\n");
    }
}

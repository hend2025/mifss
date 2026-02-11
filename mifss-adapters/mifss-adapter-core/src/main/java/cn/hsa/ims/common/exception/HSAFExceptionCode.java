package cn.hsa.ims.common.exception;

public class HSAFExceptionCode {
    @Deprecated
    public static final String SYS = "19";
    //两位系统编号+两位模块编号+两位异常编号

    /**
     * 文件服务异常
     */
    public static final ExceptionItem HDF_MODULE_OSS_01 = ExceptionItem.build(getIntCode("0101"), "文件存储服务异常");
    public static final ExceptionItem HDF_MODULE_OSS_02 = ExceptionItem.build(getIntCode("0102"), "ZIP打包文件大小超过最大限制");
    public static final ExceptionItem HDF_MODULE_OSS_03 = ExceptionItem.build(getIntCode("0103"), "文件输入流为空");
    public static final ExceptionItem HDF_MODULE_OSS_04 = ExceptionItem.build(getIntCode("0104"), "文件名超出最大长度限制");
    public static final ExceptionItem HDF_MODULE_OSS_05 = ExceptionItem.build(getIntCode("0105"), "不允许上传的文件格式，参考：spring.servlet.multipart.allowFormat");
    /**
     * redis通用功能
     */
    public static final ExceptionItem HDF_MODULE_REDIS_01 = ExceptionItem.build(getIntCode("0201"), "redis缓存服务异常");

    /**
     * 网关通用功能
     */
    public static final ExceptionItem HDF_MODULE_GATEWAY_01 = ExceptionItem.build(getIntCode("0301"), "网关调用异常");

    public static final ExceptionItem HDF_MODULE_COMM_01 = ExceptionItem.build(getIntCode("0001"), "功能或者方法不支持，暂未开放");
    
    protected static int getIntCode(String code){
        return Integer.parseInt(SYS + code);
    }
}
